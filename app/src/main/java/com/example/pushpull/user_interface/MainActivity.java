package com.example.pushpull.user_interface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pushpull.R;
import com.example.pushpull.game_logic.LevelManager;

public class MainActivity extends AppCompatActivity {



    LevelView levelView;
    LevelManager levelManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        levelView = findViewById(R.id.levelView);
        levelManager = new LevelManager(this);
        levelView.setLevel(levelManager.getCurrentLevel());
        levelView.setLevelManager(levelManager);

        Button skipButton = findViewById(R.id.skipButton);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipLevel();
            }
        });

        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetLevel();
            }
        });



    }

    public void resetLevel() {
        levelManager.reset();
        levelView.setLevel(levelManager.getCurrentLevel());
    }


    public void skipLevel() {
        levelManager.advanceLevel();
        levelView.setLevel(levelManager.getCurrentLevel());
    }




}
