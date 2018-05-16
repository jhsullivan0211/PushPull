package com.example.pushpull.game_logic;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.widget.TextView;

import com.example.pushpull.R;
import com.example.pushpull.myLibrary.Vector2D;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;


public class LevelManager {


    private Level currentLevel;
    private int levelIndex = 0;
    private String[] filePaths = new String[0];
    private Context context;
    private AssetManager assetManager;





    public LevelManager(Context context) throws IOException {

        this.context = context;
        try {
            assetManager = context.getAssets();
            filePaths = assetManager.list("Levels");
        }
        catch (IOException e) {
            //TODO: handle IOException
            e.printStackTrace();
        }

    }

    private Level createLevelFromIndex(int index) throws LevelLoadException {

        Level result;
        try {
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
            throw new LevelLoadException("Error when attempting to open file.");
        }
    }


    public int getLevelCount() {
       return filePaths.length;
    }

    public boolean checkVictory() throws LevelLoadException {
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

    public void advanceLevel() throws LevelLoadException{
        setLevel(levelIndex + 1);
    }

    public void setLevel(int levelIndex) throws LevelLoadException{
        this.levelIndex = levelIndex;
        this.currentLevel = createLevelFromIndex(levelIndex);
        setMessage();

    }

    private void setMessage() {
        TextView message = ((Activity) context).findViewById(R.id.message);
        String text = "";
        switch(levelIndex) {
            case 0:
                text = "Swipe to move.";
                break;
            case 1:
                text = "You must cover all of the pink circles to win.";
                break;
            case 2:
                text = "When a square is blue, it can pull but not push.";
                break;
            case 3:
                text = "Move over the red/blue circles to change color.";
                break;
            case 9:
                text = "Blocks can be larger and different shapes, but still can be moved.";
                break;
            case 11:
                text = "A yellow square moves all connected blocks.";
        }
        message.setText(text);
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

    public void reset() throws LevelLoadException {
        currentLevel = new Level(currentLevel.getLayout());
    }
}
