package com.example.pushpull.user_interface;

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
    private int currentLevel;
    public static final String startupID = "PUSHPULL.STARTUP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);


        try {
            levelManager = new LevelManager(this);
            //TODO: open file, find current level, set currentLevel to that.
            currentLevel = 0;
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
    }

    private void startGame() {
        Intent levelSelectIntent = new Intent(this, LevelSelectActivity.class);
        levelSelectIntent.putExtra(MainActivity.levelCountID, levelManager.getLevelCount());
        levelSelectIntent.putExtra(MainActivity.currentLevelID, currentLevel);
        levelSelectIntent.putExtra(startupID, true);
        startActivityForResult(levelSelectIntent, MainActivity.LEVEL_SELECT_RID);

    }
}
