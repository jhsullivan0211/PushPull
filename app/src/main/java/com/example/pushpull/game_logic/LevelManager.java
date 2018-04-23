package com.example.pushpull.game_logic;


import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;


public class LevelManager {


    private Level currentLevel;
    private int levelIndex = 0;
    File[] levels;
    public LevelManager(Context context) {

        try {
            File levelFiles = new File(context.getAssets().open("res\\levels"));
            levels = levelFiles.listFiles();
            Arrays.sort(levels);
            currentLevel = Level.createLevelFromFile(levels[levelIndex]);
        }
        catch (IOException e) {
            e.printStackTrace();
        }





    }

    public void advanceLevel() {
        levelIndex += 1;
        if (levelIndex >= levels.length) {
            levelIndex = 0;
        }
        currentLevel = Level.createLevelFromFile(levels[levelIndex]);
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void reset() {
        currentLevel = new Level(currentLevel.getLayout());
    }
}
