package com.jhsullivan.pushpull.game_logic;

/**
 * A custom Exception which is used to indicate a problem loading a level.
 *
 * Created by Joshua Sullivan on 5/9/2018.
 */

public class LevelLoadException extends Exception {
    public LevelLoadException(String message) {
        super(message);
    }
}
