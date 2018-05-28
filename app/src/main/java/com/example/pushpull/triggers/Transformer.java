package com.example.pushpull.triggers;


import android.graphics.Canvas;
import android.graphics.Color;

import com.example.pushpull.game_logic.Level;
import com.example.pushpull.game_objects.GameObject;
import com.example.pushpull.game_objects.Player;
import com.example.pushpull.myLibrary.Vector2D;
import com.example.pushpull.user_interface.ColorHelper;
import com.example.pushpull.user_interface.DrawingHelper;
import com.example.pushpull.user_interface.LevelView;

public class Transformer implements Trigger {

    private boolean filled;
    private Player.Type type;
    private Level level;
    private Vector2D location;
    private int color;

    public Transformer(Player.Type type, Vector2D location, Level level) {
        this.location = location;
        this.level = level;
        filled = level.isPositionFilled(location);
        this.type = type;

        switch(type) {
            case PUSH:
                color = ColorHelper.getPushColor();
                break;
            case PULL:
                color = ColorHelper.getPullColor();
                break;
            case GRABALL:
                color = ColorHelper.getGrabAllColor();

        }
    }


    @Override
    public boolean isFilled() {
        return level.isPositionFilled(location);
    }

    public Player.Type getType() {
        return type;
    }

    @Override
    public void act(GameObject filler) {
        if (!filled && filler instanceof Player) {
            Player player = (Player) filler;
            player.changeType(type);
        }
    }

    @Override
    public void undo(GameObject leaver) {
        filled = false;
    }

    @Override
    public Vector2D getLocation() {
        return location;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void draw(LevelView levelView, Canvas canvas) {
        DrawingHelper drawingHelper = new DrawingHelper(levelView, canvas, this);
        drawingHelper.drawCircleBody();
    }




}