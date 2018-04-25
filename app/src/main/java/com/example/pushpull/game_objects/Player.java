package com.example.pushpull.game_objects;

import android.graphics.Color;

import com.example.pushpull.game_logic.Level;
import com.example.pushpull.myLibrary.Vector2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Player implements GameObject{

    public enum Type {PUSH, PULL, GRABALL};

    private Level level;
    private Type type;;
    private int color;
    private Vector2D location;

    private boolean move = true;


    public Player(Level level, Type type) {
        this.level = level;
        changeType(type);
    }


    public void changeType(Type type) {
        this.type = type;
        if (type == Type.PUSH) {
            this.color = Color.RED;
        }
        if (type == Type.PULL) {
            this.color = Color.BLUE;
        }
        if (type == Type.GRABALL) {
            this.color = Color.YELLOW;
        }

    }

    public Type getType() {
        return this.type;
    }


    @Override
    public Vector2D getLocation() {
        return this.location;
    }


    public boolean move(Vector2D.Direction direction) {

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

                if (level.moveGroup(movers, direction)) {
                    return true;
                }
                else {
                    return level.moveObject(this, direction);
                }

            case GRABALL:
                movers.addAll(level.getAllAttached(this));
                return level.moveGroup(movers, direction);
        }

        throw new IllegalArgumentException("Player type is unknown.");
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
    public int getColor() {
        return this.color;
    }

    @Override
    public void setLocation(Vector2D location) {
        this.location = location;
    }

    public static int getTypeInteger(Type type) {
        if (type == Type.GRABALL) {
            return 1;
        }
        if (type == Type.PULL) {
            return 2;
        }
        if (type == Type.PUSH) {
            return 3;
        }

        throw new IllegalArgumentException("Unknown type used as parameter.");
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
