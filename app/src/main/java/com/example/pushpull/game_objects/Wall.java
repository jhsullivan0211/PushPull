package com.example.pushpull.game_objects;


import android.graphics.Canvas;
import android.graphics.Color;

import com.example.pushpull.myLibrary.Vector2D;
import com.example.pushpull.user_interface.DrawingHelper;
import com.example.pushpull.user_interface.LevelView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Wall implements GameObject{


    private int color = Color.DKGRAY;
    private Vector2D location;
    private boolean move = false;
    private List<Wall> walls = new ArrayList<>();

    public void groupWalls(List<Wall> template) {
        walls = template;
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
            if (!hasAdjacentWall(direction)) {
                borderDirections.add(direction);
            }
        }
        drawingHelper.drawBorders(borderDirections);
    }



    @Override
    public void setLocation(Vector2D location) {
        this.location = location;
    }

    @Override
    public void setMove(boolean move) {
        //pass
    }

    @Override
    public boolean canMove() {
        return move;
    }

    private boolean hasAdjacentWall(Vector2D.Direction direction) {
        Vector2D pointInDirection = getLocation().getPointInDirection(direction);
        for (Wall wall : walls) {
            if (wall.getLocation().equals(pointInDirection)) {
                return true;
            }
        }

        return false;
    }



}
