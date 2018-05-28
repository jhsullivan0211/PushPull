package com.example.pushpull.game_objects;


import android.graphics.Canvas;
import android.graphics.Color;
import com.example.pushpull.game_logic.Level;
import com.example.pushpull.myLibrary.Vector2D;
import com.example.pushpull.user_interface.ColorHelper;
import com.example.pushpull.user_interface.DrawingHelper;
import com.example.pushpull.user_interface.LevelView;

public class Block implements GameObject{


    private int color = ColorHelper.getBlockColor();
    private Vector2D location;
    private boolean move = true;


    @Override
    public void draw(LevelView levelView, Canvas canvas) {
        DrawingHelper drawingHelper = new DrawingHelper(levelView, canvas, this);
        drawingHelper.drawSquareBody();
        drawingHelper.drawAllBorders();
    }

    @Override
    public Vector2D getLocation() {
        return this.location;
    }

    @Override
    public int getColor() {
        return this.color;
    }


    @Override
    public void setLocation(Vector2D location) {
        this.location = location;
    }

    @Override
    public void setMove(boolean move) {
        this.move = move;
    }

    @Override
    public boolean canMove() {
        return move;
    }




}
