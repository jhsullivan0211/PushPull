package com.jhsullivan.pushpull.game_logic;

import android.graphics.Canvas;

import com.jhsullivan.pushpull.user_interface.DrawingHelper;
import com.jhsullivan.pushpull.user_interface.LevelView;

import java.io.Serializable;

/**
 *  This interface defines visible game entities.  Each Actor defines how it is drawn, and has
 *  a getter for location.
 *
 * Created by Joshua Sullivan on 5/13/2018.
 */

public interface Actor extends Serializable{

    /**
     * Draws the actor.
     *
     * @param drawingHelper The DrawingHelper that will do that actual drawing.
     */
    public void draw(DrawingHelper drawingHelper, Canvas canvas);

    /**
     *
     * @return  Returns the Vector2D location of the Actor.
     */
    public Vector2D getLocation();

    /**
     *
     * @return  Returns the Actor's color.
     */
    public int getColor();


}
