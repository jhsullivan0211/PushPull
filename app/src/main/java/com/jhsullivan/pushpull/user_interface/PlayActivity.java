package com.jhsullivan.pushpull.user_interface;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.jhsullivan.pushpull.R;
import com.jhsullivan.pushpull.game_logic.Level;
import com.jhsullivan.pushpull.game_logic.LevelLoadException;
import com.jhsullivan.pushpull.game_logic.LevelManager;
import com.jhsullivan.pushpull.game_objects.GameObject;
import com.jhsullivan.pushpull.game_objects.Player;
import com.jhsullivan.pushpull.game_logic.Vector2D;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**  This activity contains the main game, handling input, display, and navigation.
 * <p>
 * The PlayActivity is the main activity interacted with by the user.  It contains a
 * LevelManager (which in turn contains a level), through which game data can be accessed and
 * manipulated.  It also contains widgets for displaying game data and controlling input, as well
 * as buttons for undoing an action, restarting a level, navigating to the level select activity,
 * and toggling sound on/off.
 * <p>
 * User input is applied to the level via swiping in the direction of movement.  The user attempts
 * to cover target circles with blocks that can be pushed or pulled by player blocks.  When the
 * user successfully covers all of the targets, a victory screen is shown, and the activity
 * loads the next level.
 *<p>
 * This app saves its instance state to keep track of the current game state, and also tracks
 * the current level and maximum level in a permanent file created via SharedPreferences.
 *
 *
 */
public class PlayActivity extends AppCompatActivity {

    LevelView levelView;
    LevelManager levelManager;
    InputController inputController;
    private int clickAnimSpeed = 100;

    public static final String[] winMessages = {"Success!", "Brilliant!", "Marvelous!",
                                                "Excellent!", "Well done!", "Fantastic!",
                                                "Superb!", "Nice!", "Great job!"};

    public List<String> winMessageList = Arrays.asList(winMessages);
    private int winMessageIndex = winMessages.length;
    private boolean soundOn = true;
    static final int LEVEL_SELECT_RID = 278;
    private Animation clickAnimation = new ScaleAnimation(1f, 1.1f,
            1f, 1.1f, 100, 100);
    public static Resources resourceAccess;




    /**
     * On creation of the activity, access to the widgets is gained, and any
     * previous state information is loaded.
     *
     * @param savedInstanceState  The state to load, if available.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resourceAccess = getResources();
        setContentView(R.layout.activity_play);
        levelView = findViewById(R.id.levelView);
        inputController = findViewById(R.id.inputController);
        Intent intent = getIntent();
        clickAnimation.setDuration(clickAnimSpeed);

        int buttonWidth = findViewById(R.id.soundButton).getWidth();
        int buttonHeight = findViewById(R.id.soundButton).getHeight();

        clickAnimation = new ScaleAnimation(1f, 1.1f,
                1f, 1.1f, buttonWidth * 2, buttonHeight * 2);


        int pivot = 100;

        try {
            levelManager = new LevelManager(this);

            if (savedInstanceState != null) {
                unpackBundle(savedInstanceState);
            }
            else {
                int currentLevelIndex = intent.getIntExtra(ActivityUtility.currentLevelID, -1);


                if (currentLevelIndex == -1) {
                    ActivityUtility.showAlert("Level Error", "An error occurred " +
                            "while attempting to read the current user level.  The application " +
                            "will close.", this);
                }
                loadLevel(currentLevelIndex);
                openLevelSelect();
            }

        }
        catch (IOException e) {
            ActivityUtility.showAlert("File Error", "There was an error when " +
                    "reading app data. The application will close.", this);
        }

       enableButtons();
    }

    /**
     * Unpacks a saved instance state, settings variables and creating a level according to the
     * saved serializeable state.
     *
      * @param savedInstanceState  The state containing the information needed to restore the
     *                            activity.
     */

    private void unpackBundle(Bundle savedInstanceState) {
        int levelIndex = savedInstanceState.getInt(ActivityUtility.currentLevelID);
        loadLevel(levelIndex);

        Level level = levelManager.getCurrentLevel();
        Serializable currentSerial = savedInstanceState.getSerializable(ActivityUtility.gameStateID);
        Serializable undoLocSerial = savedInstanceState.getSerializable(ActivityUtility.undoPositionID);
        Serializable undoTypeSerial = savedInstanceState.getSerializable(ActivityUtility.undoTypeID);

        HashMap<Vector2D, GameObject> currentGameState;
        HashMap<Vector2D, GameObject> undoLocationState;
        HashMap<Player, Player.Type> undoTypeState;

        currentGameState = (HashMap<Vector2D, GameObject>) currentSerial;
        undoLocationState = (HashMap<Vector2D, GameObject>) undoLocSerial;
        undoTypeState = (HashMap<Player, Player.Type>) undoTypeSerial;

        level.loadState(currentGameState, undoLocationState, undoTypeState);
    }

    /**
     * Saves the current game state, which includes the position and type of each piece,
     * the previous position of each piece for the undo button, and the previous type of each
     * player for the undo button.  Also saves the current level, for reloading.
     *
     * @param savedInstanceState  The instance state to save.
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        Level level = levelManager.getCurrentLevel();
        savedInstanceState.putSerializable(ActivityUtility.gameStateID, level.getCurrentState());
        savedInstanceState.putSerializable(ActivityUtility.undoPositionID, level.getUndoLocationState());
        savedInstanceState.putSerializable(ActivityUtility.undoTypeID, level.getUndoTypeState());
        savedInstanceState.putInt(ActivityUtility.currentLevelID, levelManager.getLevelIndex());
    }

    /**
     * Resets the current level, called in response to the restart button being pressed.
     */
    public void resetLevel() {

        try {
            levelManager.reset();
        }
        catch (LevelLoadException e) {
            ActivityUtility.showAlert("Level Load Error", "There was an error "+
                            "reading the level file. The application will close.", this);
        }

        levelView.setLevel(levelManager.getCurrentLevel());
    }

    /**
     * Reverts to the state immediately before the last move, called in response to the undo button.
     */
    public void undo() {
        levelManager.revertState();
        levelView.setLevel(levelManager.getCurrentLevel());

    }

    /**
     * Loads a given level by index.  Called in response to winning a level or after returning
     * from level select with a selected level.  If the given index exceeds the max level
     * achieved so far (stored in app data), updates the max level.  Also updates the stored
     * current level.  Catches a LoadLevelException, and shows an error if it occurs.
     * <p>
     * NOTE: the level index starts at 0, so doesn't match the level
     * number seen on the buttons in level select.

     * @param index  The level index to load.
     */
    private void loadLevel(int index) {
        try {

            if (index < 0) {
                throw new LevelLoadException("Cannot load a negative index level.");
            }

            if (index >= levelManager.getLevelCount()) {
                activateGameOverEvent();
                return;
            }

            SharedPreferences preferences = getSharedPreferences(ActivityUtility.dataFileName, 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(ActivityUtility.currentLevelID, index);
            int maxLevel;

            if (preferences.contains(ActivityUtility.maxLevelID)) {
                maxLevel = preferences.getInt(ActivityUtility.maxLevelID, -1);
            }
            else {
                editor.putInt(ActivityUtility.maxLevelID, index);
                maxLevel = index;
            }

            if (index > maxLevel) {
                editor.putInt(ActivityUtility.maxLevelID, index);
            }

            editor.apply();

            levelManager.setLevel(index);
            levelView.setLevel(levelManager.getCurrentLevel());

            TextView message = findViewById(R.id.message);
            message.setText(levelManager.getCurrentLevel().getMessage());
        }
        catch (LevelLoadException e) {
            ActivityUtility.showAlert("Level Load Error", "There was an error " +
                    "reading the level file. The application will close.", this);
        }
    }

    /**
     * Activates the win event, in response to the player covering all of the targets with blocks.
     * Shows a victory message and advances to the next level after the user clicks it.
     */
    public void activateWinEvent() {
        final ConstraintLayout winLayout = findViewById(R.id.winLayout);
        winLayout.setAlpha(0);
        winLayout.setVisibility(View.VISIBLE);
        winLayout.animate().alpha(1.0f);
        disableButtons();
        shuffleWinMessage();
        inputController.setInputEnabled(false);

        winLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                winLayout.setVisibility(View.GONE);
                int index = levelManager.getLevelIndex() + 1;
                loadLevel(index);
                inputController.setInputEnabled(true);
                enableButtons();
            }
        });
    }

    public void activateGameOverEvent() {
        Intent endIntent = new Intent(this, VictoryActivity.class);
        startActivity(endIntent);
    }

    /**
     * Enables the use of the buttons on the activity.
     */
    private void enableButtons() {
        final ImageButton skipButton = findViewById(R.id.undo);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipButton.startAnimation(clickAnimation);
                undo();
            }
        });

        final ImageButton resetButton = findViewById(R.id.reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetButton.startAnimation(clickAnimation);
                resetLevel();
            }
        });

        final ImageButton levelSelect = findViewById(R.id.levelSelect);
        levelSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                levelSelect.startAnimation(clickAnimation);
                openLevelSelect();
            }
        });

        final ImageButton soundButton = findViewById(R.id.soundButton);
        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundButton.startAnimation(clickAnimation);
                toggleSound();
            }
        });

    }

    /**
     * Disables use of the buttons on the activity.
     */
    private void disableButtons() {

        ImageButton skipButton = findViewById(R.id.undo);
        skipButton.setOnClickListener(null);

        ImageButton resetButton = findViewById(R.id.reset);
        resetButton.setOnClickListener(null);

        ImageButton levelSelect = findViewById(R.id.levelSelect);
        levelSelect.setOnClickListener(null);
    }

    /**
     * Called in response to the sound button being pressed, this toggles between sound
     * being on and sound being off, updating the button graphic accordingly.
     */
    public void toggleSound() {
        ImageButton soundButton = findViewById(R.id.soundButton);
        if (soundOn) {
            soundButton.setImageResource(R.drawable.sound_off_icon);
        }
        else {
            soundButton.setImageResource(R.drawable.sound_on_icon);
        }
        soundOn = !soundOn;
    }


    /**
     * Opens the LevelSelectActivity activity, passing it any relevant data that it needs.
     *
     */
    public void openLevelSelect() {

        Intent levelSelectIntent = new Intent(this, LevelSelectActivity.class);
        levelSelectIntent.putExtra(ActivityUtility.levelCountID, levelManager.getLevelCount());
        levelSelectIntent.putExtra(ActivityUtility.currentLevelID, levelManager.getLevelIndex());
        startActivityForResult(levelSelectIntent, LEVEL_SELECT_RID);
    }

    /**
     * Called when a started activity returns a result.  Currently, only responds to the
     * LevelSelectActivity, where it gets the selected index and loads that level.  See
     * the overidden activity for parameter
     *
     * @param requestCode   The id for the activity returning
     * @param resultCode    The result code for the activity (RESULT_OK for success,
     *                      RESULT_CANCELLED for failure)
     * @param data          An Intent that carries the resulting data from the closed activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LEVEL_SELECT_RID) {
            if (resultCode == RESULT_OK) {
                int levelIndex = data.getIntExtra(ActivityUtility.chosenIndexID, -1);
                    loadLevel(levelIndex);
            }
        }
    }

    /**
     * Applies input in a given direction to the level and checks for victory.  Called by
     * the InputController class.
     *
     * @param direction  The direction in which to apply movement.
     */
    public void handleInput(Vector2D.Direction direction) {

        levelManager.getCurrentLevel().processInput(direction);
        if (levelManager.checkVictory()) {
            activateWinEvent();
        }
        levelView.invalidate();

    }

    /**
     * Changes to the next win message.  Win messages are cycled through so that each message
     * is shown once, and then the collection is shuffled and it starts over.
     */

    public void shuffleWinMessage() {
        TextView winMessage = findViewById(R.id.winMessage);
        if (winMessageIndex == winMessages.length) {
            Collections.shuffle(winMessageList);
            winMessageIndex = 0;
        }
        winMessage.setText(winMessageList.get(winMessageIndex));
        winMessageIndex += 1;
    }





}
