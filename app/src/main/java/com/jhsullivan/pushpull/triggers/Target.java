package com.jhsullivan.pushpull.triggers;

import android.graphics.Canvas;

import com.jhsullivan.pushpull.game_logic.Level;
import com.jhsullivan.pushpull.game_objects.GameObject;
import com.jhsullivan.pushpull.game_objects.Player;
import com.jhsullivan.pushpull.game_logic.Vector2D;
import com.jhsullivan.pushpull.user_interface.DrawingHelper;

/**
 * Represents the target that must be covered with a block to win.
 */
public class Target implements Trigger {

    private Level level;
    private Vector2D location;

    /**
     * Constructor for Target.
     *
     * @param location  The location of the target.
     * @param level     The level
     */
    public Target(Vector2D location, Level level) {
        this.location = location;
        this.level = level;
    }

    /**
     *
     * @return  Returns whether a GameObject that is not a Player is on this trigger.
     */
    @Override
    public boolean isFilled() {
        return (level.isPositionFilled(location) &&
                !(level.getObjectAt(location) instanceof Player));
    }


    /**
     *
     * @return  Returns the location of this Target.
     */
    @Override
    public Vector2D getLocation() {
        return location;
    }

    /**
     * Does nothing.  Necessary implementation of Trigger interface.
     * @param filler    N/A
     */
    @Override
    public void act(GameObject filler) {

    }

    /**
     * Does nothing.  Necessary implementation of Trigger interface.
     * @param leaver    N/A
     */
    @Override
    public void undo(GameObject leaver) {

    }

    /**
     * Draws this Target onto the specified Canvas using information from the specified
     * LevelView.
     *
     * @param levelView     The LevelView from which to get drawing specifications.
     * @param canvas        The Canvas on which to draw the target.
     */
    @Override
    public void draw(DrawingHelper drawingHelper, Canvas canvas) {
        drawingHelper.drawIcon("target", location, canvas);

    }

    /**
     *
     * @return  Returns the color of this object.
     */
    @Override
    public int getColor() {
        return 0;
    }




}
