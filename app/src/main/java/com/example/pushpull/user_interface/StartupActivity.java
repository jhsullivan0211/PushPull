package com.example.pushpull.user_interface;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.pushpull.R;
import com.example.pushpull.game_logic.LevelManager;
import com.example.pushpull.game_objects.Player;

import java.io.IOException;

public class StartupActivity extends AppCompatActivity {

    private LevelManager levelManager;
    private int currentLevelIndex;
    public static final String startupID = "PUSHPULL.STARTUP";
    public static final String soundID = "PUSHPULL.SOUND";
    private boolean soundOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        try {
            levelManager = new LevelManager(this);
            //TODO: open file, find current level, set currentLevel to that.
            currentLevelIndex = 0;
        }
        catch (IOException e) {
            e.printStackTrace();
            //TODO: popup error
        }

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

    private void startGame() {
        Intent starterIntent = new Intent(this, MainActivity.class);
        starterIntent.putExtra(MainActivity.currentLevelID, currentLevelIndex);
        starterIntent.putExtra(soundID, soundOn);
        startActivity(starterIntent);
    }

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
