package com.example.pushpull.game_objects;


import android.graphics.Color;

import com.example.pushpull.myLibrary.Vector2D;

public class Wall implements GameObject{


    private int color = Color.DKGRAY;
    private Vector2D location;
    private boolean move = false;




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
        //pass
    }

    @Override
    public boolean canMove() {
        return move;
    }



}
