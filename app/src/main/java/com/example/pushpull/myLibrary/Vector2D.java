package com.example.pushpull.myLibrary;


import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;




public class Vector2D implements Serializable{

    public enum Direction {LEFT, RIGHT, UP, DOWN};


    private final int x;
    private final int y;

    public Vector2D() {
        x = 0;
        y = 0;
    }

    public Vector2D (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object other) {
        if ((other instanceof Vector2D)
                && (((Vector2D) other).getX() == this.getX()
                && ((Vector2D) other).getY() == this.getY())) {
            return true;
        }
        else return false;
    }

    @Override
    public int hashCode() {
        return 17 * this.getX() + 31 * this.getY();
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }

    public Vector2D getPointInDirection(Direction direction, int shiftDistance) {
        if (direction == Direction.LEFT) {
            return new Vector2D(this.getX() - shiftDistance, this.getY());
        }

        if (direction == Direction.UP) {
            return new Vector2D(this.getX(), this.getY() - shiftDistance);
        }

        if (direction == Direction.RIGHT) {
            return new Vector2D(this.getX() + shiftDistance, this.getY());
        }

        if (direction == Direction.DOWN) {
            return new Vector2D(this.getX(), this.getY() + shiftDistance);
        }

        return null;
    }

    public Vector2D getPointInDirection(Direction direction) {
        return getPointInDirection(direction, 1);
    }

    public static Vector2D add(Vector2D first, Vector2D second) {
        int resultX = first.getX() + second.getX();
        int resultY = first.getY() + second.getY();
        return new Vector2D(resultX, resultY);
    }

    public static Vector2D subtract(Vector2D first, Vector2D second) {
        int resultX = first.getX() - second.getX();
        int resultY = first.getY() - second.getY();
        return new Vector2D(resultX, resultY);
    }

    public Vector2D moveToZero(int amount) {
        int count = Math.abs(amount);
        int x = this.getX();
        int y = this.getY();


        while (count > 0) {
            if (x > 0) {
                x -= Math.signum(amount);
            }
            else if (x < 0) {
                x += Math.signum(amount);
            }

            if (y > 0) {
                y -= Math.signum(amount);
            }
            else if (y < 0) {
                y += 1;
            }
            count -= 1;
        }
        return new Vector2D(x, y);
    }

    public Vector2D scale(int scalar) {
        int x = this.getX() * scalar;
        int y = this.getY() * scalar;
        return new Vector2D(x, y);
    }


    public static Direction relativeDirection(Vector2D origin, Vector2D target) {

        if (origin.getY() == target.getY()) {
            if (origin.getX() > target.getX()) {
                return Direction.LEFT;
            }
            else if (origin.getX() < target.getX()) {
                return Direction.RIGHT;
            }
            else return null;
        }
        else if (origin.getX() == target.getX()) {
            if (origin.getY() > target.getY()) {
                return Direction.UP;
            }
            else if (origin.getY() < target.getY()) {
                return Direction.DOWN;
            }
            else return null;
        }
        else return null;
    }

    public Collection<Vector2D> getAdjacentPoints() {
        Collection<Vector2D> points = new LinkedHashSet<>();
        points.add(new Vector2D(this.x + 1, this.y));
        points.add(new Vector2D(this.x - 1, this.y));
        points.add(new Vector2D(this.x, this.y + 1));
        points.add(new Vector2D(this.x, this.y - 1));

        return points;
    }

    public Collection<Vector2D> getDiagonalPoints() {
        Collection<Vector2D> points = new LinkedHashSet<>();
        points.add(new Vector2D(this.x - 1, this.y - 1));
        points.add(new Vector2D(this.x + 1, this.y - 1));
        points.add(new Vector2D(this.x + 1, this.y + 1));
        points.add(new Vector2D(this.x - 1, this.y - 1));
        return points;
    }

    public Collection<Vector2D> getSurroundingPoints() {
        Collection<Vector2D> points = new LinkedHashSet<>();
        points.addAll(getAdjacentPoints());
        points.addAll(getDiagonalPoints());
        return points;
    }
    public static Direction getOppositeDirection(Direction direction) {

        if (direction == Direction.UP) {
            return Direction.DOWN;
        }

        if (direction == Direction.DOWN) {
            return Direction.UP;
        }

        if (direction == Direction.LEFT) {
            return Direction.RIGHT;
        }

        if (direction == Direction.RIGHT) {
            return Direction.LEFT;
        }

        return null;
    }

    public static Direction getRandomDirection(Random rng) {
        int directionIndex = rng.nextInt(Direction.values().length);
        return Direction.values()[directionIndex];
    }

    public static Direction getClockwiseDirection(Direction direction, int numberOfRotations) {

        Direction current = direction;
        for (int i = 0; i < numberOfRotations; i += 1) {
            switch (current) {
                case LEFT:
                    current = Direction.UP;
                    break;
                case UP:
                    current = Direction.RIGHT;
                    break;
                case RIGHT:
                    current = Direction.DOWN;
                    break;
                case DOWN:
                    current = Direction.LEFT;
                    break;
            }
        }
        return current;
    }

    public static Vector2D getDirectionVector(Direction direction) {
        switch (direction) {
            case UP:
                return new Vector2D(0, -1);
            case LEFT:
                return new Vector2D(-1, 0);
            case DOWN:
                return new Vector2D(0, 1);
            case RIGHT:
                return new Vector2D(1, 0);
            default:
                return Vector2D.zero();
        }

    }

    public boolean isWithin(Vector2D topLeft, Vector2D bottomRight) {
        return (this.getX() >= topLeft.getX()
                && this.getX() <= bottomRight.getX()
                && this.getY() >= topLeft.getY()
                && this.getY() <= bottomRight.getY());

    }

    public static Vector2D getRandomPoint(Vector2D topLeft, Vector2D bottomRight, Random rng) {
        int x = topLeft.getX() + rng.nextInt(bottomRight.getX() - topLeft.getX());
        int y = topLeft.getY() + rng.nextInt(bottomRight.getY() - topLeft.getY());

        return new Vector2D(x, y);
    }

    public static Vector2D getRandomPoint(Vector2D point, int distanceToEdge, Random rng) {
        Vector2D topLeft = new Vector2D(point.getX() - distanceToEdge, point.getY() - distanceToEdge);
        Vector2D bottomRight = new Vector2D(point.getX() + distanceToEdge, point.getY() + distanceToEdge);

        return getRandomPoint(topLeft, bottomRight, rng);
    }


    public Vector2D pullToRectangle(Vector2D upperLeft, Vector2D bottomRight) {
        int x;
        int y;

        if (upperLeft.getX() >= bottomRight.getX() || upperLeft.getY() >= bottomRight.getY()) {
            throw new IllegalArgumentException("upperLeft point is below or to the right of bottomRight.");
        }

        if (getX() < upperLeft.getX()) {
            x = upperLeft.getX();
        }

        else if (getX() > bottomRight.getX()) {
            x = bottomRight.getX();
        }
        else {
            x = getX();
        }

        if (getY() < upperLeft.getY()) {
            y = upperLeft.getY();
        }

        else if (getY() > bottomRight.getY()) {
            y = bottomRight.getY();
        }
        else {
            y = getY();
        }

        return new Vector2D(x, y);
    }

    public Vector2D getPointFromDirections(List<Direction> directions) {
        if (directions.size() == 0) {
            return this;
        }
        Vector2D current = this;
        for (Direction direction : directions) {
            current = current.getPointInDirection(direction);
        }
        return current;
    }



    public static Vector2D zero() {
        return new Vector2D(0, 0);
    }





}

