package com.example.pushpull.game_objects;


import android.graphics.Color;
import com.example.pushpull.game_logic.Level;
import com.example.pushpull.myLibrary.Vector2D;

public class Block implements GameObject{


    private int color = Color.LTGRAY;
    private Vector2D location;
    private Level level;
    private boolean move = true;

    public Block(Level level) {
        this.level = level;
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
