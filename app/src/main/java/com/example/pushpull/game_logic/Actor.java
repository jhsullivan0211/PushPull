package com.example.pushpull.game_logic;

import android.graphics.Canvas;

import com.example.pushpull.myLibrary.Vector2D;
import com.example.pushpull.user_interface.LevelView;

import java.io.Serializable;

/**
 * Created by joshu on 5/13/2018.
 */

public interface Actor extends Serializable{

    public void draw(LevelView levelView, Canvas canvas);
    public Vector2D getLocation();
    public int getColor();

}
