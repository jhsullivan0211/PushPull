package com.jhsullivan.pushpull.game_objects;


import android.graphics.Canvas;
import com.jhsullivan.pushpull.game_logic.Level;
import com.jhsullivan.pushpull.game_logic.Vector2D;
import com.jhsullivan.pushpull.user_interface.ColorHelper;
import com.jhsullivan.pushpull.user_interface.DrawingHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A GameObject which does not move at all, only takes up space.
 */
public class Wall implements GameObject{


    private int color = ColorHelper.getWallColor();
    private Vector2D location;
    private List<Wall> walls = new ArrayList<>();



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
     * Returns all walls that are contiguous to the specified wall in the specified level.
     *
     * @param target    The GameObject from which to get all attached.
     * @param level     The Level to query for attached objects.
     * @return          Returns a collection of game objects attached to the supplied GameObject.
     */
    public Collection<Wall> getContiguousWalls(Level level) {

        Set<Wall> attached = new HashSet<>();
        Set<Wall> next = new HashSet<>();
        Set<Wall> frontier = new HashSet<>();
        next.add(this);

        do {
            for (Wall obj : next) {
                attached.add(obj);
                for (GameObject adj : level.getAdjacentObjects(obj)) {
                    if ((adj instanceof Wall) && !attached.contains(adj) ) {
                        frontier.add((Wall) adj);
                    }
                }
            }
            next = frontier;
            frontier = new HashSet<>();
        } while (next.size() > 0);

        return attached;
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





}
