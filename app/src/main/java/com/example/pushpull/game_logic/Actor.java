package com.example.pushpull.game_logic;

import android.graphics.Canvas;

import com.example.pushpull.myLibrary.Vector2D;
import com.example.pushpull.user_interface.LevelView;

import java.io.Serializable;

/**
 *  This interface defines visible game entities, which reside in the Level class and
 *  are viewed using the LevelView class.  Each Actor defines how it is drawn, and has
 *  getters for location and color
 *
 * Created by Joshua Sullivan on 5/13/2018.
 */

public interface Actor extends Serializable{

    public void draw(LevelView levelView, Canvas canvas);
    public Vector2D getLocation();


}
