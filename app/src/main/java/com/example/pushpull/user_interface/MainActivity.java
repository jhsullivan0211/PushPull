package com.example.pushpull.user_interface;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.pushpull.R;
import com.example.pushpull.game_logic.LevelManager;
import com.example.pushpull.myLibrary.Vector2D;

import java.util.HashMap;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {



    LevelView levelView;
    LevelManager levelManager;

    private static final String levelNumberID = "PUSHPULL.LEVELNUMBER";
    private static final String gameStateID = "PUSHPULL.GAMESTATE";
    public static final String levelCountID = "PUSHPULL.LEVEL_COUNT";

    static final int LEVEL_SELECT_RID = 278;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        levelView = findViewById(R.id.levelView);
        levelManager = new LevelManager(this);

        if (savedInstanceState != null) {
           int levelIndex = savedInstanceState.getInt(levelNumberID);
            HashMap<Vector2D, Character> gameState = (HashMap)
                            savedInstanceState.getSerializable(gameStateID);

            levelManager.setLevel(levelIndex);
            levelManager.getCurrentLevel().inflateSerialState(gameState);
        }

        levelView.setLevel(levelManager.getCurrentLevel());
        levelView.setLevelManager(levelManager);

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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable(gameStateID, levelManager.getGameStateSerial());
        savedInstanceState.putInt(levelNumberID, levelManager.getLevelIndex());
    }

    public void resetLevel() {
        levelManager.reset();
        levelView.setLevel(levelManager.getCurrentLevel());
    }


    public void undo() {
        levelManager.revertState();
        levelView.setLevel(levelManager.getCurrentLevel());
    }



    public void openLevelSelect() {
        Intent levelSelectIntent = new Intent(this, LevelSelect.class);
        levelSelectIntent.putExtra(levelCountID, levelManager.getLevelCount());
        startActivityForResult(levelSelectIntent, LEVEL_SELECT_RID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LEVEL_SELECT_RID) {
            if (resultCode == RESULT_OK) {
                int levelIndex = data.getIntExtra(LevelSelect.chosenIndexID, -1);

                if (levelIndex != -1) {
                    levelManager.setLevel(levelIndex);
                    levelView.setLevel(levelManager.getCurrentLevel());
                }
                else {
                    //TODO: handle this error
                }
            }
        }
    }




}
