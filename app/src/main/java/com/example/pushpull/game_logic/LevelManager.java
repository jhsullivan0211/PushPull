package com.example.pushpull.game_logic;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.pushpull.myLibrary.Vector2D;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class LevelManager {


    private Level currentLevel;
    private int levelIndex = 0;
    private String[] filePaths;
    private Context context;

    private int levelCount = 0;


    public LevelManager(Context context) {


        this.context = context;
        currentLevel = createLevelFromIndex(0);

        if (filePaths != null) {
            levelCount = filePaths.length;
        }

    }

    private Level createLevelFromIndex(int index) {

        Level result;
        try {

            AssetManager assetManager = context.getAssets();
            filePaths = assetManager.list("Levels");
            InputStream singleLevel = assetManager.open("Levels/" + filePaths[index]);
            BufferedReader br = new BufferedReader(new InputStreamReader(singleLevel));

            StringBuilder layoutBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                layoutBuilder.append(line);
            }
            String layout = layoutBuilder.toString();
            result = new Level(layout);
            br.close();
            return result;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public int getLevelCount() {
        return levelCount;
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

    public HashMap<Vector2D, Character> getGameStateSerial() {
        return currentLevel.getSerialState();
    }

    public void advanceLevel() {
        setLevel(levelIndex + 1);
    }

    public void setLevel(int levelIndex) {
        this.levelIndex = levelIndex;
        this.currentLevel = createLevelFromIndex(levelIndex);
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public int getLevelIndex() {
        return levelIndex;
    }

    public void revertState() {
        currentLevel.revertState();
    }

    public void reset() {
        currentLevel = new Level(currentLevel.getLayout());
    }
}
