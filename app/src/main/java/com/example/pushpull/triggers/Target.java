package com.example.pushpull.triggers;

import android.graphics.Color;

import com.example.pushpull.game_logic.Level;
import com.example.pushpull.game_objects.GameObject;
import com.example.pushpull.myLibrary.Vector2D;

public class Target extends Trigger {

    private boolean filled;

    public Target(Vector2D location, Level level) {
        super(location, level);
        this.color = Color.rgb(255,20,147);
        filled = level.isPositionFilled(location);
    }

    @Override
    public void act(GameObject filler) {

    }

    @Override
    public void undo(GameObject leaver) {

    }


}
