package com.jhsullivan.pushpull.game_objects;


import com.jhsullivan.pushpull.game_logic.Actor;
import com.jhsullivan.pushpull.game_logic.Vector2D;


/***
 * This interface defines Actors that take up space in the level and can move around.
 * Provides methods for updating movement.
 */
public interface GameObject extends Actor {

    /***
     * Sets the location of the GameObject.
     *
     * @param location  The value for which to set the GameObject's location attribute.
     */
    public void setLocation(Vector2D location);


    /***
     * Sets the "move" boolean, which defines whether the GameObject can move.
     *
     * @param move  The value for which to set the GameObject's move attribute.
     */
    public void setMove(boolean move);


    /***
     * @return Returns whether the GameObject can move or not.
     */
    public boolean canMove();




}
