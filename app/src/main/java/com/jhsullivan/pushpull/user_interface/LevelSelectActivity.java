package com.jhsullivan.pushpull.user_interface;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.jhsullivan.pushpull.R;

import java.util.ArrayList;
import java.util.List;

/**
 * An Activity which displays buttons allowing the user to select the current level.
 */
public class LevelSelectActivity extends AppCompatActivity {

    private int levelCount;
    private int currentLevelIndex;
    private Intent myIntent;
    private int maxLevel;
    private int columns;
    private float buttonHeightRatio = 50f/70f;
    private int gridWidth;
    private MediaPlayer buttonSelectSound;

    private View unlockLayout;
    private View lockLayout;


    /**
     * Called when this activity is created.  Accesses and caches necessary data, and dynamically
     * creates and positions the level buttons.
     *
     * @param savedInstanceState    The saved state of the activity, not used here since no state
     *                              information needs to be preserved.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        myIntent = getIntent();
        levelCount = myIntent.getIntExtra(ActivityUtility.levelCountID, 0);
        currentLevelIndex = myIntent.getIntExtra(ActivityUtility.currentLevelID, -1);
        SharedPreferences preferences = getSharedPreferences(ActivityUtility.dataFileName, 0);
        maxLevel = preferences.getInt(ActivityUtility.maxLevelID, -1);

        if (levelCount == 0 || maxLevel == -1) {
            ActivityUtility.showAlert("Level Error", "An error occurred while " +
                    "accessing app files.  The application will close.", this);
        }

        final List<Integer> levels = new ArrayList<>();
        for (int i = 0; i < levelCount; i++) {
            levels.add(i);
        }

        final GridView gridView = findViewById(R.id.gridView);
        gridView.post(new Runnable() {
            @Override
            public void run() {
                columns = gridView.getNumColumns();
                gridWidth = gridView.getWidth();
                ArrayAdapter<Integer> adapter = getLevelSelectAdapter(context, levels);
                gridView.setAdapter(adapter);
            }
        });
    }

    /**
     * Called when the activity starts (i.e. comes into view).
     */
    @Override
    protected void onStart() {
        super.onStart();
        buttonSelectSound = MediaPlayer.create(this, R.raw.action_click);
    }

    /**
     * Called when the activity is no longer in view.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (buttonSelectSound != null) {
            buttonSelectSound.release();
        }

    }

    /**
     * Returns an ArrayAdapter which provides a method to the AdapterView to get the properly
     * formatted level button as a View.  This method chooses the format of the button based
     * on the level index (since the buttons display the level index), the current level (the
     * current level is highlighted), and whether a level is locked or not (the locked levels
     * display a lock instead of a level index).
     *
     * @param context   The context of this adapter, usually the Activity calling the method.
     * @param levels    A List of integers to be used as the level integers.
     * @return          Returns the adapter with the customized getView method to provide
     *                  the level buttons for the AdapterView.
     */
    private ArrayAdapter<Integer> getLevelSelectAdapter(final Context context,
                                                        List<Integer> levels) {

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, 0, levels) {
            @Override @NonNull
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {

                final int levelIndex = position;

                if (levelIndex > maxLevel) {
                    View buttonLayout = LayoutInflater
                                        .from(context).inflate(R.layout.lock_button, new LinearLayout(context));
                    ImageButton button = buttonLayout.findViewById(R.id.lockButton);
                    formatButton(button);

                    return button;
                }
                else {
                    View buttonLayout = LayoutInflater
                                        .from(context).inflate(R.layout.unlock_button, new LinearLayout(context));

                    Button button = buttonLayout.findViewById(R.id.unlockButton);
                    if (levelIndex == currentLevelIndex) {
                        button = buttonLayout.findViewById(R.id.unlockButtonAccented);
                    }
                    button.setText(String.valueOf(levelIndex + 1));


                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            buttonSelectSound.start();
                            setLevel(levelIndex);
                        }
                    });
                    formatButton(button);
                    return button;
                }
            }
        };
        return adapter;
    }

    /**
     * Changes the width and height of a specified button to fit the screen when packed into a
     * GridView.
     *
     * @param button    The button to format.
     */
    private void formatButton(View button) {
        int width = (int) ((gridWidth / columns) * 0.9);
        int height = (int) (width * buttonHeightRatio * 0.9);
        button.setLayoutParams(new AbsListView.LayoutParams(width, height));


    }

    /**
     * Returns to the previous Activity, passing the specified level index back to that Activity.
     *
     * @param levelIndex  The level index chosen to pass back.
     */
    private void setLevel(int levelIndex) {

        if (levelIndex == currentLevelIndex) {
            setResult(RESULT_CANCELED, myIntent);
        }
        else {
            myIntent.putExtra(ActivityUtility.chosenIndexID, levelIndex);
            setResult(RESULT_OK, myIntent);
        }

        finish();
    }
}
