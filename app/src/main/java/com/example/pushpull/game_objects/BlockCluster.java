package com.example.pushpull.game_objects;




    import android.graphics.Canvas;
    import android.graphics.Color;


    import com.example.pushpull.game_logic.Level;
    import com.example.pushpull.myLibrary.Vector2D;
    import com.example.pushpull.user_interface.ColorHelper;
    import com.example.pushpull.user_interface.DrawingHelper;
    import com.example.pushpull.user_interface.LevelView;

    import java.util.ArrayList;
    import java.util.Collection;
    import java.util.Collections;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Set;

public class BlockCluster implements GameObject{


    private int color = ColorHelper.getClusterColor();

    private Vector2D location;
    private List<BlockCluster> clusters = new ArrayList<>();
    private boolean move = true;
    private Character clusterID;

    public BlockCluster(Character clusterID) {
        this.clusterID = clusterID;
    }

    public void makeCluster(List<BlockCluster> template) {
        clusters = template;
    }


    @Override
    public Vector2D getLocation() {
        return this.location;
    }

    @Override
    public void draw(LevelView levelView, Canvas canvas) {
        DrawingHelper drawingHelper = new DrawingHelper(levelView, canvas, this);
        drawingHelper.drawSquareBody(color);

        List<Vector2D.Direction> borderDirections = new ArrayList<>();
        for (Vector2D.Direction direction : Vector2D.Direction.values()) {
            if (!hasAdjacentCluster(direction)) {
                borderDirections.add(direction);
            }
        }

        drawingHelper.drawBorders(borderDirections);


    }


    @Override
    public void setLocation(Vector2D location) {
        this.location = location;
    }



    public List<BlockCluster> getCluster() {
        return Collections.unmodifiableList(clusters);
    }

    @Override
    public void setMove(boolean move) {
        this.move = move;
    }

    @Override
    public boolean canMove() {
        return move;
    }

    public Character getClusterID() {
        return clusterID;
    }

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
