package com.jhsullivan.pushpull.game_objects;

import android.graphics.Canvas;

import com.jhsullivan.pushpull.game_logic.Level;
import com.jhsullivan.pushpull.game_logic.Vector2D;
import com.jhsullivan.pushpull.user_interface.ColorHelper;
import com.jhsullivan.pushpull.user_interface.DrawingHelper;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A game object that can be moved directly.  Depending on its current state, can also move
 * Blocks and BlockClusters in the level.  Currently, there are three types of player state:
 * PUSH, PULL, and GRABALL (stored in an enum Type).  PUSH causes the player object to push a
 * single block when moving, if possible.  PULL causes the player object to pull a single block
 * when moved.  GRABALL moves all blocks attached to it, including player blocks, or not at all
 * if any of those attached blocks cannot move.
 */
public class Player implements GameObject{

    public enum Type {PUSH, PULL, GRABALL};

    private Type type;
    private int color = ColorHelper.getPushColor();
    private Vector2D location;
    private boolean move = true;

    private Player.Type previousType;
    private Vector2D undoLocation;

    /**
     * Constructor for the Player object.
     *
     * @param type  The type of player, one of the enum values of Type.
     */
    public Player(Type type) {
        changeType(type);

    }

    /**
     * Sets the type of the player to the specified type.
     *
     * @param type  The type to which to change the player.
     */
    public void changeType(Type type) {
        this.type = type;
    }

    /**
     *
     * @return  Returns the type of this player.
     */
    public Type getType() {
        return this.type;
    }

    /**
     *
     * @return  Returns the player's location.
     */
    @Override
    public Vector2D getLocation() {
        return this.location;
    }

    /**
     * Moves the player in a specified direction, along with any blocks that the player's type
     * should move (e.g. pushing a block in the way if in the PUSH state).
     *
     * @param direction   The direction in which to move this player.
     * @param level       The level in which to query for other blocks to move if in the way.
     * @return            Returns whether this move was successful.
     */
    public boolean move(Vector2D.Direction direction, Level level) {

        if (!move) {
            return true;
        }
        Vector2D movePoint = location.getPointInDirection(direction);
        GameObject other;
        Set<GameObject> movers = new HashSet<>();
        movers.add(this);

        switch (type) {
            case PUSH:
                other = level.getObjectAt(movePoint);
                addIfValid(other, movers);
                return level.moveGroup(movers, direction);
            case PULL:
                Vector2D.Direction opposite = Vector2D.getOppositeDirection(direction);
                Vector2D pullPoint = location.getPointInDirection(opposite);
                other = level.getObjectAt(pullPoint);
                addIfValid(other, movers);
                return level.moveGroup(movers, direction);

            case GRABALL:
                movers.addAll(getContiguousObjects(this, level));
                return level.moveGroup(movers, direction);
        }

        throw new IllegalArgumentException("Player type is unknown.");
    }

    /**
     * Returns all game objects that are attached (i.e. not separated by empty space, only
     * blocks/players/cluster) to the specified GameObject in the specified Level.
     *
     * @param target    The GameObject from which to get all attached.
     * @param level     The Level to query for attached objects.
     * @return          Returns a collection of game objects attached to the supplied GameObject.
     */
    public Collection<GameObject> getContiguousObjects(GameObject target, Level level) {

        Set<GameObject> attached = new HashSet<>();
        Set<GameObject> next = new HashSet<>();
        Set<GameObject> frontier = new HashSet<>();
        next.add(target);

        do {
            for (GameObject obj : next) {
                attached.add(obj);
                for (GameObject adj : level.getAdjacentObjects(obj)) {
                    if (!attached.contains(adj) && !(adj instanceof Wall) && adj.canMove()) {
                        frontier.add(adj);
                    }
                }
            }
            next = frontier;
            frontier = new HashSet<>();
        } while (next.size() > 0);

        return attached;
    }

    /**
     * A simple conversion of IDs to type, used for player and transformer spawning.
     * @param id        The ID to convert.
     * @return          The type that the given ID corresponds to.
     */
    public static Type idToType(Character id) {
        if (id == 'p' || id == 'P') {
            return Type.PUSH;
        }
        if (id == 'q' || id == 'Q') {
            return Type.PULL;
        }
        if (id == 'r' || id == 'R') {
            return Type.GRABALL;
        }
        return null;
    }


    /**
     * Adds a GameObject (and all objects in its cluster, if it is a BlockCluster) to the
     * specified list if it is not null, a wall, or a player.
     *
     * @param obj       The GameObject to add, if it is neither null, Wall, or Player.
     * @param list      The List to which the GameObject should be added, if applicable.
     */
    private static void addIfValid(GameObject obj, Collection<GameObject> list) {
        if (obj == null) {
            return;
        }

        if (obj instanceof Wall) {
            return;
        }

        if (obj instanceof Player) {
            return;
        }

        if (obj instanceof BlockCluster) {
            BlockCluster bc = (BlockCluster) obj;
            list.addAll(bc.getCluster());
            return;
        }

        list.add(obj);
    }

    public Player.Type getPreviousType() {
        return this.previousType;
    }

    public void setPreviousType(Player.Type previousType) {
        this.previousType = previousType;
    }

    /**
     * Draws the game object onto the specified Canvas using the specified DrawingHelper.
     *
     * @param drawingHelper     The DrawingHelper to use to help draw the object.
     * @param canvas            The Canvas on which to draw.
     */
    @Override
    public void draw(DrawingHelper drawingHelper, Canvas canvas) {
        if (type == Type.PUSH) {
            color = ColorHelper.getPushColor();
        }
        if (type == Type.PULL) {
            color = ColorHelper.getPullColor();
        }
        if (type == Type.GRABALL) {
            color = ColorHelper.getGrabAllColor();
        }

        drawingHelper.drawSquareBody(color, location, canvas);
    }

    /**
     * Sets the location of the Player.
     *
     * @param location  The value for which to set the GameObject's location attribute.
     */
    @Override
    public void setLocation(Vector2D location) {
        this.location = location;
    }

    /**
     * Sets whether the player can move.
     *
     * @param move  The value for which to set the GameObject's move attribute.
     */
    @Override
    public void setMove(boolean move) {
        this.move = move;
    }

    /**
     * Returns whether the player can move or not.  Movement is turned off upon a successful move.
     * @return Returns the value of the move attribute.
     *
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
     * @return  Returns the color of the object.
     */
    @Override
    public int getColor() {
        return this.color;
    }

}
