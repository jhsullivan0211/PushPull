package com.example.pushpull.user_interface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pushpull.R;
import com.example.pushpull.game_logic.Level;
import com.example.pushpull.game_logic.LevelLoadException;
import com.example.pushpull.game_logic.LevelManager;
import com.example.pushpull.game_objects.GameObject;
import com.example.pushpull.game_objects.Player;
import com.example.pushpull.myLibrary.Vector2D;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    LevelView levelView;
    LevelManager levelManager;
    InputController inputController;

    public static final String currentLevelID = "PUSHPULL.CURRENT_LEVEL";
    private static final String gameStateID = "PUSHPULL.GAME_STATE";
    private static final String undoPositionID = "PUSHPULL.UNDO_LOC_STATE";
    private static final String undoTypeID = "PUSHPULL.UNDO_TYPE_STATE";
    public static final String levelCountID = "PUSHPULL.LEVEL_COUNT";
    public static final String maxLevelID = "PUSHPULL.MAX_LEVEL";
    public static final String dataFileName = "PUSHPULL_DATA";

    public static final String[] winMessages = {"Success!", "Brilliant!", "Marvelous!",
                                                "Excellent!", "Well done!", "Fantastic!",
                                                "Superb!", "Nice!", "Great job!"};

    public List<String> winMessageList = Arrays.asList(winMessages);

    private int winMessageIndex = 0;


    static final int LEVEL_SELECT_RID = 278;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        levelView = findViewById(R.id.levelView);
        inputController = findViewById(R.id.inputController);
        Intent intent = getIntent();


        try {
            levelManager = new LevelManager(this);

            if (savedInstanceState != null) {
                int levelIndex = savedInstanceState.getInt(currentLevelID);
                loadLevel(levelIndex);

                Level level = levelManager.getCurrentLevel();
                Serializable currentSerial = savedInstanceState.getSerializable(gameStateID);
                Serializable undoLocSerial = savedInstanceState.getSerializable(undoPositionID);
                Serializable undoTypeSerial = savedInstanceState.getSerializable(undoTypeID);

                HashMap<Vector2D, GameObject> currentGameState;
                HashMap<Vector2D, GameObject> undoLocationState;
                HashMap<Player, Player.Type> undoTypeState;

                currentGameState = (HashMap<Vector2D, GameObject>) currentSerial;
                undoLocationState = (HashMap<Vector2D, GameObject>) undoLocSerial;
                undoTypeState = (HashMap<Player, Player.Type>) undoTypeSerial;

                level.loadState(currentGameState, undoLocationState, undoTypeState);



            }
            else {
                int currentLevelIndex = intent.getIntExtra(currentLevelID, -1);
                if (currentLevelIndex == -1) {
                    //TODO: throw error popup
                    Log.d("ERROR", "onCreate: Error");
                }
                loadLevel(currentLevelIndex);
                openLevelSelect();
            }

        }
        catch (IOException e) {
            //TODO: popup error here
            e.printStackTrace();
            return;
        }

       enableButtons();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        Level level = levelManager.getCurrentLevel();
        savedInstanceState.putSerializable(gameStateID, level.getCurrentState());
        savedInstanceState.putSerializable(undoPositionID, level.getUndoLocationState());
        savedInstanceState.putSerializable(undoTypeID, level.getUndoTypeState());
        savedInstanceState.putInt(currentLevelID, levelManager.getLevelIndex());
    }

    public void resetLevel() {

        try {
            levelManager.reset();
        }
        catch (LevelLoadException e) {
            //TODO: popup error here
            e.printStackTrace();
        }

        levelView.setLevel(levelManager.getCurrentLevel());
    }


    public void undo() {
        levelManager.revertState();
        levelView.setLevel(levelManager.getCurrentLevel());


    }

    private void loadLevel(int index) {
        try {

            SharedPreferences preferences = getSharedPreferences(dataFileName, 0);
            SharedPreferences.Editor editor = preferences.edit();
            int maxLevel;
            if (!preferences.contains(maxLevelID)) {
                editor.putInt(maxLevelID, 0);
                editor.apply();
                maxLevel = 0;
            }
            else {
                maxLevel = preferences.getInt(maxLevelID, -1);
                if (maxLevel == -1) {
                    //TODO: throw error, popup
                    Log.d("Error", "loadLevel: Error!");
                }
            }

            if (index > maxLevel) {
                editor.putInt(maxLevelID, index);
                editor.apply();
            }

            levelManager.setLevel(index);
            levelView.setLevel(levelManager.getCurrentLevel());

            TextView message = findViewById(R.id.message);
            message.setText(levelManager.getCurrentLevel().getMessage());
        }
        catch (LevelLoadException e) {
            e.printStackTrace();
            //TODO: popup error indicator
        }

    }


    public void activateWinEvent() {
        final TextView winMessage = findViewById(R.id.winMessage);
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

    private void enableButtons() {
        ImageButton skipButton = findViewById(R.id.undo);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undo();
            }
        });

        ImageButton resetButton = findViewById(R.id.reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetLevel();
            }
        });

        ImageButton levelSelect = findViewById(R.id.levelSelect);
        levelSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLevelSelect();
            }
        });
    }

    private void disableButtons() {

        ImageButton skipButton = findViewById(R.id.undo);
        skipButton.setOnClickListener(null);

        ImageButton resetButton = findViewById(R.id.reset);
        resetButton.setOnClickListener(null);

        ImageButton levelSelect = findViewById(R.id.levelSelect);
        levelSelect.setOnClickListener(null);
    }



    public void openLevelSelect() {

        Intent levelSelectIntent = new Intent(this, LevelSelectActivity.class);
        levelSelectIntent.putExtra(levelCountID, levelManager.getLevelCount());
        levelSelectIntent.putExtra(currentLevelID, levelManager.getLevelIndex());
        levelSelectIntent.putExtra(StartupActivity.startupID, false);
        startActivityForResult(levelSelectIntent, LEVEL_SELECT_RID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LEVEL_SELECT_RID) {
            if (resultCode == RESULT_OK) {
                int levelIndex = data.getIntExtra(LevelSelectActivity.chosenIndexID, -1);
                    loadLevel(levelIndex);
            }
        }
    }

    public void handleInput(Vector2D.Direction direction) {

        levelManager.getCurrentLevel().processInput(direction);
        if (levelManager.checkVictory()) {
            activateWinEvent();
        }
        levelView.invalidate();

    }



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
