package com.example.pushpull.game_objects;




    import android.graphics.Canvas;


    import com.example.pushpull.game_logic.Vector2D;
    import com.example.pushpull.user_interface.ColorHelper;
    import com.example.pushpull.user_interface.DrawingHelper;
    import com.example.pushpull.user_interface.LevelView;

    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.List;

/**
 * A concrete implementation of GameObject which is one of a group of game objects that all move
 * together as a single unit, and can also be pushed onto targets to win.
 */
public class BlockCluster implements GameObject{


    private int color = ColorHelper.getClusterColor();
    private Vector2D location;
    private List<BlockCluster> clusters = new ArrayList<>();
    private boolean move = true;
    private Character clusterID;

    /**
     *  Basic constructor which takes in a clusterID to assign to this block.
     *
     * @param clusterID     An ID which all of the BlockCluster objects that move with this
     *                      block share.
     */
    public BlockCluster(Character clusterID) {
        this.clusterID = clusterID;
    }

    /**
     * Assigns this block's cluster group to be the supplied list of BlockClusters.  NOTE: does not
     * copy the supplied list, but rather moves the clusters pointer to the supplied List.
     *
     * @param template      The list of BlockClusters to set the cluster point to.
     */
    public void makeCluster(List<BlockCluster> template) {
        clusters = template;
    }

    /**
     *
     * @return  Returns this game object's location.
     */
    @Override
    public Vector2D getLocation() {
        return this.location;
    }

    /**
     * Draws the game object onto the specified Canvas using information from the specified
     * LevelView.
     *
     * @param levelView     The LevelView from which to get drawing information.
     * @param canvas        The Canvas on which to draw.
     */
    @Override
    public void draw(LevelView levelView, Canvas canvas) {
        DrawingHelper drawingHelper = new DrawingHelper(levelView, canvas);
        drawingHelper.drawSquareBody(color, location);

        List<Vector2D.Direction> borderDirections = new ArrayList<>();
        for (Vector2D.Direction direction : Vector2D.Direction.values()) {
            if (!hasAdjacentCluster(direction)) {
                borderDirections.add(direction);
            }
        }

        drawingHelper.drawBorders(borderDirections, location);
    }


    /**
     * Sets the location of this game object.
     *
     * @param location  The value for which to set the GameObject's location attribute.
     */
    @Override
    public void setLocation(Vector2D location) {
        this.location = location;
    }


    /**
     * @return  Returns an unmodifiable version of this block cluster's cluster group.
     */
    public List<BlockCluster> getCluster() {
        return Collections.unmodifiableList(clusters);
    }

    /**
     * Sets whether this game object can move.
     *
     * @param move  The value for which to set the GameObject's move attribute.
     */
    @Override
    public void setMove(boolean move) {
        this.move = move;
    }

    /**
     * @return  Returns whether this game object can move.
     */
    @Override
    public boolean canMove() {
        return move;
    }

    /**
     * @return  Returns the cluster ID of this BlockCluster.
     */
    public Character getClusterID() {
        return clusterID;
    }

    /**
     * Returns whether there is another BlockCluster with the same cluster ID as this one in the
     * specified direction.
     *
     * @param direction     The direction in which to look for another BlockCluster.
     * @return              Returns whether or not a BlockCluster with the same cluster ID is found
     *                      in the specified direction.
     *
     */
    private boolean hasAdjacentCluster(Vector2D.Direction direction) {
        Vector2D pointInDirection = getLocation().getPointInDirection(direction);
        for (BlockCluster cluster : clusters) {
            if (cluster.getLocation().equals(pointInDirection)) {
                return true;
            }
        }

        return false;
    }





}
