package com.example.pushpull.user_interface;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.pushpull.R;
import com.example.pushpull.game_logic.LevelManager;
import com.example.pushpull.game_objects.Player;

import java.io.IOException;

public class StartupActivity extends AppCompatActivity {


    private int currentLevelIndex;
    public static final String startupID = "PUSHPULL.STARTUP";
    public static final String soundID = "PUSHPULL.SOUND";
    private boolean soundOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        SharedPreferences preferences = getSharedPreferences(MainActivity.dataFileName, 0);
        currentLevelIndex = preferences.getInt(MainActivity.currentLevelID, 0);

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

    @Override
    protected  void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences(MainActivity.dataFileName, 0);
        currentLevelIndex = preferences.getInt(MainActivity.currentLevelID, 0);
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
