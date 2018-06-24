package com.example.pushpull.game_objects;


import android.graphics.Canvas;

import com.example.pushpull.game_logic.Vector2D;
import com.example.pushpull.user_interface.ColorHelper;
import com.example.pushpull.user_interface.DrawingHelper;
import com.example.pushpull.user_interface.LevelView;

/**
 * A concrete implementation of GameObject, which is to be moved around by the player and put
 * onto the targets to win.
 */
public class Block implements GameObject{


    private int color = ColorHelper.getBlockColor();
    private Vector2D location;
    private boolean move = true;


    /**
     * Draws the block onto the specified LevelView.
     *
     * @param levelView     The LevelView required for drawing information.
     * @param canvas        The canvas on which to draw.
     */
    @Override
    public void draw(LevelView levelView, Canvas canvas) {
        DrawingHelper drawingHelper = new DrawingHelper(levelView, canvas);
        drawingHelper.drawSquareBody(color, location);
        drawingHelper.drawAllBorders(location);
    }

    /**
     *
     * @return      Returns the block's location.
     */
    @Override
    public Vector2D getLocation() {
        return this.location;
    }

    /**
     * Sets the location to the specified Vector2D.
     *
     * @param location  The value for which to set the GameObject's location attribute.
     */
    @Override
    public void setLocation(Vector2D location) {
        this.location = location;
    }

    /**
     *
     * @param move  Sets whether the block can move or not.
     */
    @Override
    public void setMove(boolean move) {
        this.move = move;
    }

    /**
     *
     * @return  Returns whether the block can move or not.
     */
    @Override
    public boolean canMove() {
        return move;
    }




}
