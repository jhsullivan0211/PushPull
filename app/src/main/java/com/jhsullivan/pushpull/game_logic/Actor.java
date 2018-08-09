package com.jhsullivan.pushpull.game_logic;

import android.graphics.Canvas;

import com.jhsullivan.pushpull.user_interface.LevelView;

import java.io.Serializable;

/**
 *  This interface defines visible game entities.  Each Actor defines how it is drawn, and has
 *  a getter for location.
 *
 * Created by Joshua Sullivan on 5/13/2018.
 */

public interface Actor extends Serializable{

    public void draw(LevelView levelView, Canvas canvas);
    public Vector2D getLocation();


}
