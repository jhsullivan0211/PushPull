package com.jhsullivan.pushpull.triggers;


import android.graphics.Canvas;
import com.jhsullivan.pushpull.game_logic.Level;
import com.jhsullivan.pushpull.game_objects.GameObject;
import com.jhsullivan.pushpull.game_objects.Player;
import com.jhsullivan.pushpull.game_logic.Vector2D;
import com.jhsullivan.pushpull.user_interface.ColorHelper;
import com.jhsullivan.pushpull.user_interface.DrawingHelper;


/**
 * Transformer trigger, which transforms any player that moves onto it into the type of this
 * transformer.  Type is held using the Player.Type enum from the Player class.
 */
public class Transformer implements Trigger {

    private boolean filled;
    private Player.Type type;
    private Level level;
    private Vector2D location;
    private int color;


    /**
     * Basic constructor for the Transformer class.  The type and location are immutable,
     * and so are provided here, as is the level.
     *
     * @param type          The Type of this Transformer.
     * @param location      The location of this Transformer.
     * @param level         The Level which contains this Transformer, to be used when checking
     *                      for players.
     */
    //TODO: evaluate whether providing the Level is a good idea.
    public Transformer(Player.Type type, Vector2D location, Level level) {
        this.location = location;
        this.level = level;
        filled = level.isPositionFilled(location);
        this.type = type;
        switch (type) {
            case PULL:
                color = ColorHelper.getPullColor();
                break;
            case PUSH:
                color = ColorHelper.getPushColor();
                break;
            case GRABALL:
                color = ColorHelper.getGrabAllColor();
                break;
        }

    }

    /**
     *
     * @return      Returns the type of this Transformer.
     */
    public Player.Type getType() {
        return type;
    }


    /**
     *
     * @return  Returns whether this trigger is filled or not.
     */
    @Override
    public boolean isFilled() {
        return level.isPositionFilled(location);
    }


    /**
     * The action to perform when this trigger is filled.  Here, this means changing the player
     * to the type of this Transformer.
     *
     * @param filler
     */
    @Override
    public void act(GameObject filler) {
        if (filler instanceof Player) {
            Player player = (Player) filler;
            player.changeType(type);
        }
    }

    /**
     * The action to perform when the trigger is left.
     * @param leaver        The GameObject that is leaving the trigger.
     */
    @Override
    public void undo(GameObject leaver) {
        filled = false;
    }

    /**
     *
     * @return  Returns the location of this trigger.
     */
    @Override
    public Vector2D getLocation() {
        return location;
    }

    /**
     * Draws this Transformer on the specified Canvas using the specified DrawingHelper.
     *
     * @param drawingHelper The DrawingHelper that draws the object.
     * @param canvas        The Canvas on which to draw this Transformer.
     */
    @Override
    public void draw(DrawingHelper drawingHelper, Canvas canvas) {

        drawingHelper.drawCircleBody(color, location, canvas);

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