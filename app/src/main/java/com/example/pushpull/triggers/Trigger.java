package com.example.pushpull.triggers;

import android.graphics.Color;

import com.example.pushpull.game_logic.Level;
import com.example.pushpull.game_objects.GameObject;
import com.example.pushpull.myLibrary.Vector2D;

public abstract class Trigger {

    protected Level level;
    protected Vector2D location;
    protected int color;

    public Trigger(Vector2D location, Level level) {
        this.location = location;
        this.level = level;
    }

    public boolean isFilled() {
        return level.isPositionFilled(location);
    }

    public Vector2D getLocation() {
        return location;
    }

    public int getColor() {
        return color;
    }

    public abstract void act(GameObject filler);

    public abstract void undo(GameObject leaver);
}