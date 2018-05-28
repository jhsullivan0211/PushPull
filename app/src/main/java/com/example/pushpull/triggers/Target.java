package com.example.pushpull.triggers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.example.pushpull.R;
import com.example.pushpull.game_logic.Level;
import com.example.pushpull.game_objects.GameObject;
import com.example.pushpull.game_objects.Player;
import com.example.pushpull.myLibrary.Vector2D;
import com.example.pushpull.user_interface.ColorHelper;
import com.example.pushpull.user_interface.DrawingHelper;
import com.example.pushpull.user_interface.LevelView;

public class Target implements Trigger {

    private Level level;
    private Vector2D location;
    private int color = ColorHelper.getTargetColor();
    private Drawable checkIcon;

    public Target(Vector2D location, Level level) {
        this.location = location;
        this.level = level;
    }

    @Override
    public boolean isFilled() {
        return (level.isPositionFilled(location) &&
                !(level.getObjectAt(location) instanceof Player));
    }


    @Override
    public Vector2D getLocation() {
        return location;
    }



    @Override
    public void act(GameObject filler) {

    }

    @Override
    public void undo(GameObject leaver) {

    }

    @Override
    public int getColor(){
        return color;
    }

    @Override
    public void draw(LevelView levelView, Canvas canvas) {
        DrawingHelper drawingHelper = new DrawingHelper(levelView, canvas, this);
        drawingHelper.drawCircleBody();

    }

    public void drawIcon(LevelView levelView, Canvas canvas) {
        Drawable icon = levelView.targetIcon;
        DrawingHelper drawingHelper = new DrawingHelper(levelView, canvas, this);
        drawingHelper.drawIcon(icon);

    }




}
