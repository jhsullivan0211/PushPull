package com.jhsullivan.pushpull.game_objects;


import android.graphics.Canvas;
import com.jhsullivan.pushpull.game_logic.Vector2D;
import com.jhsullivan.pushpull.user_interface.ColorHelper;
import com.jhsullivan.pushpull.user_interface.DrawingHelper;

/**
 * A concrete implementation of GameObject, which is to be moved around by the player and put
 * onto the targets to win.
 */
public class Block implements GameObject {


    private int color = ColorHelper.getBlockColor();
    private Vector2D location;
    private boolean move = true;
    private Vector2D undoLocation;


    /**
     * Draws the block onto the specified LevelView using the specified DrawingHelper.
     *
     * @param drawingHelper    The DrawingHelper to use to help draw the object.
     * @param canvas        The canvas on which to draw.
     */
    @Override
    public void draw(DrawingHelper drawingHelper, Canvas canvas) {
        drawingHelper.drawSquareBody(color, location, canvas);
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

    /**
     *
     * @return  Returns the previous location of this game object, where it will go if the undo
     * action is performed.
     */
    @Override
    public Vector2D getUndoLocation() {
        return this.undoLocation;
    }


    /**
     * Sets the value of the undoLocation field.
     *
     * @param undoLocation The location to which to set the undo location.
     */
    @Override
    public void setUndoLocation(Vector2D undoLocation) {
        this.undoLocation = undoLocation;
    }

    /**
     *
     * @return  Returns the color of this object.
     */
    @Override
    public int getColor() {
        return this.color;
    }


}
