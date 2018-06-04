package com.example.pushpull.game_logic;


import android.content.Context;
import android.content.res.AssetManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/***
 * Class which handles switching between levels and other level-related events.
 */

public class LevelManager {


    private Level currentLevel;
    private int levelIndex = 0;
    private String[] filePaths = new String[0];
    private Context context;
    private AssetManager assetManager;



    public LevelManager(Context context) throws IOException {
        this.context = context;
        assetManager = context.getAssets();
        filePaths = assetManager.list("Levels");
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

    public boolean checkVictory() {
        return currentLevel.isComplete();
    }



    public void setLevel(int levelIndex) throws LevelLoadException{
        this.levelIndex = levelIndex;
        this.currentLevel = createLevelFromIndex(levelIndex);
        this.currentLevel.update();

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
