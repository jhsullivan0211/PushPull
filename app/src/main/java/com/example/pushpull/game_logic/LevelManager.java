package com.example.pushpull.game_logic;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



public class LevelManager {


    private Level currentLevel;
    private int levelIndex = 0;
    private String[] filePaths;
    private Context context;
    private AssetManager assetManager;

    private final int columnNumber = 10;
    private final int rowNumber = 10;


    public LevelManager(Context context) {


        this.context = context;
        currentLevel = createLevelFromIndex(0);


    }

    public Level createLevelFromIndex(int index) {

        Level result;
        try {

            AssetManager assetManager = context.getAssets();
            filePaths = assetManager.list("Levels");
            InputStream singleLevel = assetManager.open("Levels/" + filePaths[levelIndex]);
            BufferedReader br = new BufferedReader(new InputStreamReader(singleLevel));
            String[][] layout = new String[rowNumber][columnNumber];
            int rows = 0;
            String line;
            while ((line = br.readLine()) != null) {

                String[] splitLine = line.split(",");

                if (splitLine.length != columnNumber) {
                    throw new RuntimeException("Level must be 10x10.");
                }

                for (int column = 0; column < splitLine.length; column += 1) {
                    layout[rows][column] = splitLine[column];
                }
                rows += 1;
            }
            result = new Level(layout);
            br.close();
            return result;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean checkVictory() {
        if (currentLevel.isComplete()) {
            advanceLevel();
            return true;
        }
        else {
            return false;
        }
    }

    public void advanceLevel() {
        levelIndex += 1;
        if (levelIndex >= filePaths.length) {
            levelIndex = 0;
        }
        currentLevel = createLevelFromIndex(levelIndex);

    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void reset() {
        currentLevel = new Level(currentLevel.getLayout());

    }
}
