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
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Player implements GameObject{


    public enum Type {PUSH, PULL, GRABALL};

    private Type type;
    private int color;
    private Vector2D location;
    private boolean move = true;

    public Player(Type type) {
        changeType(type);
    }

    public void changeType(Type type) {
        this.type = type;
        if (type == Type.PUSH) {
            this.color = ColorHelper.getPushColor();
        }
        if (type == Type.PULL) {
            this.color = ColorHelper.getPullColor();
        }
        if (type == Type.GRABALL) {
            this.color = ColorHelper.getGrabAllColor();
        }
    }

    public Type getType() {
        return this.type;
    }

    @Override
    public Vector2D getLocation() {
        return this.location;
    }

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
                movers.addAll(getAllAttached(this, level));
                return level.moveGroup(movers, direction);
        }

        throw new IllegalArgumentException("Player type is unknown.");
    }

    public Collection<GameObject> getAllAttached(GameObject target, Level level) {

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

    @Override
    public void draw(LevelView levelView, Canvas canvas) {
        DrawingHelper drawingHelper = new DrawingHelper(levelView, canvas, this);
        drawingHelper.drawSquareBody();
        drawingHelper.drawAllBorders();
    }


    @Override
    public int getColor() {
        return this.color;
    }

    @Override
    public void setLocation(Vector2D location) {
        this.location = location;
    }


    @Override
    public void setMove(boolean move) {
        this.move = move;
    }

    @Override
    public boolean canMove() {
        return move;
    }


}
