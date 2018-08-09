package com.jhsullivan.pushpull.game_logic;


import android.content.Context;
import android.content.res.AssetManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/***
 * Handles switching between levels and other level-related events.
 *
 * This class opens program asset files which contain level information, and creates levels from
 * those files, in addition to switching between levels and tracking which level is current.
 * The levels are stored in the form of .csv files in the assets section of the app.  The .csv
 * information is passed into the Level class to create the level.  Because files will be accessed,
 * there is possibility of throwing either an IOException in response to a file problem or a
 * LevelLoadException in response to a formatting issue of the level.  The class throws these
 * errors when the constructor or loading methods are called, and allows the user to handle them.
 */

public class LevelManager {


    private Level currentLevel;
    private int levelIndex = 0;
    private String[] filePaths = new String[0];
    private Context context;
    private AssetManager assetManager;


    /**
     * Basic constructor which gets the file paths of the level assets.
     *
     * @param context           The context, typically an Activity, from which to get assets.
     * @throws IOException
     */
    public LevelManager(Context context) throws IOException {
        this.context = context;
        assetManager = context.getAssets();
        filePaths = assetManager.list("Levels");
    }

    /**
     * Given an index, accesses the appropriate level file from the assets and returns a level
     * using the layout information from the .csv asset file.  Throws a LevelLoadExcpetion if
     * the level file is improperly formatted (determined during construction of the Level class).
     *
     * @param index     The index of the level.  NOTE: this index starts at 0, not 1, so the index
     *                  will not match the level numbers in the file name.
     *
     * @return          A Level created from the file at the given index in the directory.
     * @throws LevelLoadException
     */
    private Level createLevelFromIndex(int index) throws LevelLoadException {

        Level result;
        try {
            if (index >= filePaths.length) {
                throw new LevelLoadException("Level index exceeds number of levels.");
            }
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

    /**
     *
     * @return  Returns the total number of levels.
     */
    public int getLevelCount() {
       return filePaths.length;
    }

    /**
     *
     * @return      Returns whether the current level has been beaten.
     */
    public boolean checkVictory() {
        return currentLevel.isComplete();
    }

    /**
     * Sets the current level to the specified index, loading a new level accordingly.  Throws a
     * LevelLoadException if it fails to create a level from the index.
     *
     * @param levelIndex            The index of the level to load.
     * @throws LevelLoadException
     */
    public void setLevel(int levelIndex) throws LevelLoadException{
        this.levelIndex = levelIndex;
        this.currentLevel = createLevelFromIndex(levelIndex);
        this.currentLevel.update();

    }

    /**
     *
     * @return      Returns the current Level.
     */
    public Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     *
     * @return      Returns the index of the current Level.
     */
    public int getLevelIndex() {
        return levelIndex;
    }

    /**
     * Issues a command to the level to revert to its saved "undo" state.
     */
    public void revertState() {
        currentLevel.revertState();
    }

    /**
     * Reloads the current level, throwing a LevelLoadException upon failure.
     *
     * @throws LevelLoadException
     */
    public void reset() throws LevelLoadException {
        currentLevel = new Level(currentLevel.getLayout());
    }
}
