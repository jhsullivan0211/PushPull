package com.jhsullivan.pushpull.user_interface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.jhsullivan.pushpull.R;

/**
 * The startup activity - the first activity seen when the app loads.
 */
public class StartupActivity extends AppCompatActivity {


    private int currentLevelIndex;
    private boolean soundOn = true;

    /**
     * Method called when this Activity is created.  Accesses app data to get the current level,
     * and adds onClickListeners to the buttons.
     *
     * @param savedInstanceState    The saved state of the activity, not used here since no state
     *                              information needs to be preserved.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        if (getIntent().getBooleanExtra(ActivityUtility.exitID, false)) {
            finish();
        }

        SharedPreferences preferences = getSharedPreferences(ActivityUtility.dataFileName, 0);
        currentLevelIndex = preferences.getInt(ActivityUtility.currentLevelID, 0);


        ImageButton playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });

        ImageButton soundButton = findViewById(R.id.soundButton);
        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSound();
            }
        });
    }

    /**
     * The method called when this Activity is resumed.  Loads app data to get the current level.
     */
    @Override
    protected  void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences(ActivityUtility.dataFileName, 0);
        currentLevelIndex = preferences.getInt(ActivityUtility.currentLevelID, 0);
    }

    /**
     * Starts up the main game part of the app.
     */
    private void startGame() {
        Intent starterIntent = new Intent(this, PlayActivity.class);
        starterIntent.putExtra(ActivityUtility.currentLevelID, currentLevelIndex);
        starterIntent.putExtra(ActivityUtility.soundID, soundOn);
        startActivity(starterIntent);
    }

    /**
     * Toggles sound on or off.
     */
    private void toggleSound() {
        ImageButton soundButton = findViewById(R.id.soundButton);
        if (soundOn) {
            soundButton.setImageResource(R.drawable.sound_off_icon_main_screen);
        }
        else {
            soundButton.setImageResource(R.drawable.sound_on_icon_main_screen);
        }
        soundOn = !soundOn;
    }



}
