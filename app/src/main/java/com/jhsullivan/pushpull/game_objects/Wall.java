package com.jhsullivan.pushpull.game_objects;


import android.graphics.Canvas;
import android.graphics.Color;

import com.jhsullivan.pushpull.game_logic.Vector2D;
import com.jhsullivan.pushpull.user_interface.ColorHelper;
import com.jhsullivan.pushpull.user_interface.DrawingHelper;
import com.jhsullivan.pushpull.user_interface.LevelView;

import java.util.ArrayList;
import java.util.List;

/**
 * A GameObject which does not move at all, only takes up space.
 */
public class Wall implements GameObject{


    private int color = ColorHelper.getWallColor();
    private Vector2D location;
    private List<Wall> walls = new ArrayList<>();
    private List<Vector2D> path;
    private boolean isMajor;


    /**
     * Sets the walls attribute to point at the supplied List.
     *
     * @param template  The List to which to redirect the walls pointer.
     */
    public void groupWalls(List<Wall> template) {
        walls = template;
    }

    /**
     * Checks for an adjacent wall in the specified direction.
     *
     * @param direction     The direction in which to check for another wall.
     * @return              Returns a boolean indicating the presence of another wall in the
     *                      specified direction.
     */
    private boolean hasAdjacentWall(Vector2D.Direction direction) {
        Vector2D pointInDirection = getLocation().getPointInDirection(direction);
        for (Wall wall : walls) {
            if (wall.getLocation().equals(pointInDirection)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return  Returns this wall's location.
     */
    @Override
    public Vector2D getLocation() {
        return this.location;
    }

    /**
     * Draws the wall onto the specified canvas using the specified DrawingHelper.
     *
     * @param drawingHelper The DrawingHelper that helps draw the object.
     * @param canvas        The canvas on which to draw.
     */
    @Override
    public void draw(DrawingHelper drawingHelper, Canvas canvas) {
//        if (isMajor) {
//            drawingHelper.drawPolygon(path, color, canvas);
//        }

        drawingHelper.drawSquareBody(color, location, canvas);
    }

    /**
     * Sets the location of this Well.  Should only be used once, but this is not enforced.
     *
     * @param location  The value for which to set the GameObject's location attribute.
     */
    @Override
    public void setLocation(Vector2D location) {
        this.location = location;
    }

    /**
     * Unused function from interface, since the wall can never move.
     *
     * @param move  N/A
     */
    @Override
    public void setMove(boolean move) {
        //pass
    }

    /**
     *
     * @return  Returns false, since this game object can never move.
     */
    @Override
    public boolean canMove() {
        return false;
    }

    /**
     *
     * @return  Returns the Wall's location, since it never moves.
     */
    @Override
    public Vector2D getUndoLocation() {
        return this.location;
    }

    /**
     * Does nothing, since the wall never moves and doesn't have an undo location.
     *
     * @param undoLocation N/A
     */
    @Override
    public void setUndoLocation(Vector2D undoLocation) {

    }

    /**
     *
     * @return  Returns the color of the object.
     */
    @Override
    public int getColor() {
        return this.color;
    }

    public void setPath(List<Vector2D> path) {
        this.path = path;
    }

    public boolean isMajor() {
        return this.isMajor;
    }

    public void setMajor(boolean major) {
        this.isMajor = major;
    }

}
