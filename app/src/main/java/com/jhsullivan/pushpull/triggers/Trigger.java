package com.jhsullivan.pushpull.triggers;


import com.jhsullivan.pushpull.game_logic.Actor;
import com.jhsullivan.pushpull.game_objects.GameObject;


/***
 * This interface defines Actors that do not take up space, can do an action
 * when they are covered by a game object, and can do a different action when
 * that game object leaves the trigger.
 *
 * Created by Joshua Sullivan on 5/13/2018.
 */
public interface Trigger extends Actor {

    public boolean isFilled();
    public void act(GameObject filler);
    public void undo(GameObject leaver);


}