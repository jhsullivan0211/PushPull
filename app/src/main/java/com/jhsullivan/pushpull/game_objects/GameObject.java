package com.jhsullivan.pushpull.game_objects;


import com.jhsullivan.pushpull.game_logic.Actor;
import com.jhsullivan.pushpull.game_logic.Vector2D;


/**
 * This interface defines Actors that take up space in the level and can move around.
 * Provides methods for updating movement.
 */
public interface GameObject extends Actor {

    /**
     * Sets the location of the GameObject.
     *
     * @param location  The value for which to set the GameObject's location attribute.
     */
    public void setLocation(Vector2D location);


    /**
     * Sets the "move" boolean, which defines whether the GameObject can move.
     *
     * @param move  The value for which to set the GameObject's move attribute.
     */
    public void setMove(boolean move);


    /**
     * @return Returns whether the GameObject can move or not.
     */
    public boolean canMove();


    /**
     * @return  Returns the undo location of the game object, i.e. its position before the most
     * recent move was performed.
     */
    public Vector2D getUndoLocation();

    /**
     * Sets the value of the game object's undo location, i.e. its position before the most recent
     * move was performed.
     *
     * @param undoLocation The location to which to set the undo location.
     */
    public void setUndoLocation(Vector2D undoLocation);


}
