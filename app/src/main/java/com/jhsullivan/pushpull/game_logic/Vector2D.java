package com.jhsullivan.pushpull.game_logic;


import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;


/**
 * A simple implementation of an immutable two-dimensional integer vector.
 * <p>
 * This class contains just two integers for data; its main utility is the various methods
 * it provides.  This class does not provide much in the way of vector math; rather, its intended
 * use is for navigating grid-like environment, and getting other commonly sought vectors like
 * adjacent points, points in a certain direction, relative points, etc.  These methods often
 * use the Direction enum, and when they do the directions use the following schema:
 * LEFT represents the -x direction, UP represents the -y direction, RIGHT represents the +x
 * direction, and DOWN represents the +y direction.
 *
 */
public class Vector2D implements Serializable{

    public enum Direction {LEFT, RIGHT, UP, DOWN}

    private final int x;
    private final int y;


    /**
     * The main constructor for a 2D vector at (x, y)
     * @param x  The x-coordinate
     * @param y  The y-coordinate
     */
    public Vector2D (int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return  Returns the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * @return  Returns the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @return  Returns the zero vector, i.e. (0, 0).
     */
    public static Vector2D zero() {
        return new Vector2D(0, 0);
    }

    /**
     * Compares whether this Vector2D equals another, defined by whether
     * their x and y values are equivalent to each other.
     *
     * @param other  The object to compare this to.
     * @return      Returns whether this Vector2D is equal to another.
     */
    @Override
    public boolean equals(Object other) {
        if ((other instanceof Vector2D)
                && (((Vector2D) other).getX() == this.getX()
                && ((Vector2D) other).getY() == this.getY())) {
            return true;
        }
        else return false;
    }

    /**
     * A simple, fast hashcode based on the x and y values.
     *
     * @return  Returns a hashcode for this Vector2D.
     */
    @Override
    public int hashCode() {
        return 17 * this.getX() + 31 * this.getY();
    }

    /**
     * @return  Returns a string representing the Vector2D.
     */
    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }

    /**
     * Gets a Vector2D in the specified direction, where LEFT represents the -x direction,
     * UP represents the -y direction, RIGHT represents the +x direciton, and DOWN represents
     * the +y direction.
     *
     * @param direction         The direction in which to move to get the returned point.
     * @param shiftDistance     The magnitude of movement in the specifiec direction.
     * @return                  Returns the point in the given direction the given distance away.
     */
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

    /**
     * Overload of the {@link #getPointInDirection(Direction direction, int shiftDistance)} method,
     * with a default shiftDistance value of 1.
     * @param direction     The direction in which to move to get the returned point.
     * @return              Returns the point direction in the specified direction.
     */
    public Vector2D getPointInDirection(Direction direction) {
        return getPointInDirection(direction, 1);
    }

    /**
     * Returns the result of adding two Vector2D points.
     *
     * @param first     The first point to add.
     * @param second    The second point to add.
     * @return          Returns the result of adding the specified points.
     */
    public static Vector2D add(Vector2D first, Vector2D second) {
        int resultX = first.getX() + second.getX();
        int resultY = first.getY() + second.getY();
        return new Vector2D(resultX, resultY);
    }

    /**
     * Returns the result of subtracting the second Vector2D from the first.
     *
     * @param first     The first point.
     * @param second    The second point, to be subtracted from the first.
     * @return          The difference between the first and second point.
     */
    public static Vector2D subtract(Vector2D first, Vector2D second) {
        int resultX = first.getX() - second.getX();
        int resultY = first.getY() - second.getY();
        return new Vector2D(resultX, resultY);
    }

    /**
     * Multiplies both x and y by the specified scalar.
     *
     * @param scalar    The amount to multiply x and y by.
     * @return          The vector result of multiplication.
     */
    public Vector2D scale(int scalar) {
        int x = this.getX() * scalar;
        int y = this.getY() * scalar;
        return new Vector2D(x, y);
    }

    /**
     * Given an origin and a target, returns the direction of the target from the origin, if
     * the target can be found directly in that direction.  Returns null otherwise.
     *
     * @param origin    The origin point.
     * @param target    The target point.
     * @return          The direction from the origin to the target, if applicable.
     *                  Returns null otherwise.
     */
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

    /**
     * Returns a collection of points adjacent to this one.  Here, adjacent refers
     * to those directly UP, LEFT, RIGHT, or DOWN of the user; diagonals are not included.
     *
     * @return  A collection of the adjacent points.
     */
    public Collection<Vector2D> getAdjacentPoints() {
        Collection<Vector2D> points = new LinkedHashSet<>();
        points.add(new Vector2D(this.x + 1, this.y));
        points.add(new Vector2D(this.x - 1, this.y));
        points.add(new Vector2D(this.x, this.y + 1));
        points.add(new Vector2D(this.x, this.y - 1));

        return points;
    }

    /**
     * @return  Returns a collection of points diagonally adjacent to this one.
     */
    public Collection<Vector2D> getDiagonalPoints() {
        Collection<Vector2D> points = new LinkedHashSet<>();
        points.add(new Vector2D(this.x - 1, this.y - 1));
        points.add(new Vector2D(this.x + 1, this.y - 1));
        points.add(new Vector2D(this.x + 1, this.y + 1));
        points.add(new Vector2D(this.x - 1, this.y - 1));
        return points;
    }

    /**
     * Returns a collection that includes both adjacent and diagonal points around this one.
     *
     * @return  A collection of surrounding points, both adjacent and diagonal.
     */
    public Collection<Vector2D> getSurroundingPoints() {
        Collection<Vector2D> points = new LinkedHashSet<>();
        points.addAll(getAdjacentPoints());
        points.addAll(getDiagonalPoints());
        return points;
    }

    /**
     * Returns the opposite direction of a specified direction.
     *
     * @param direction     The direction whose opposite should be returned.
     * @return              The opposite direction of the specified one.
     */
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

    /**
     * Returns a random direction, using a specified Random to generate it.
     *
     * @param rng   The Random used to randomly select.
     * @return      A random direction.
     */
    public static Direction getRandomDirection(Random rng) {
        int directionIndex = rng.nextInt(Direction.values().length);
        return Direction.values()[directionIndex];
    }

    /**
     * Applies the specified number of closkwise "rotations" to a direction, returning the result.
     * E.g. a single rotation from LEFT would return UP.
     *
     * @param direction             The starting direction.
     * @param numberOfRotations     The number of clockwise turns to do.
     * @return                      The resulting direction after turning clockwise the specified
     *                              number of time.
     */
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

    /**
     * Returns a unit vector in the specified direction.
     *
     * @param direction  The direction for which to return a unit vector.
     * @return           A unit vector (i.e. max magnitute of 1) in the specified direction.
     */
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

    /**
     * Given the top left and bottom right points of a rectangle, returns whether this
     * Vector2D lies within (including borders) the rectangle.
     *
     * @param topLeft       The top left point of the rectangle.
     * @param bottomRight   The bottom right point of the rectangle.
     * @return              Returns whether the specified point lies within the specified rectangle.
     */
    public boolean isWithin(Vector2D topLeft, Vector2D bottomRight) {
        return (this.getX() >= topLeft.getX()
                && this.getX() <= bottomRight.getX()
                && this.getY() >= topLeft.getY()
                && this.getY() <= bottomRight.getY());

    }

    /**
     * Returns a random point within the rectangle specified by the top left and bottom right
     * points, including borders.  Uses a specified Random for calculation.
     *
     * @param topLeft       The top left point of the rectangle.
     * @param bottomRight   The bottom right point of the rectangle.
     * @param rng           The Random to use to get the random point.
     * @return              A random Vector2D within the given rectangle.
     */
    public static Vector2D getRandomPoint(Vector2D topLeft, Vector2D bottomRight, Random rng) {
        int x = topLeft.getX() + rng.nextInt(bottomRight.getX() - topLeft.getX());
        int y = topLeft.getY() + rng.nextInt(bottomRight.getY() - topLeft.getY());

        return new Vector2D(x, y);
    }

    /**
     * Returns a random point in the square defined by a specified distance to each edge from the
     * provided mid point.  Uses a specified Random for calculation.
     *
     * @param point             The mid point of the square.
     * @param distanceToEdge    The distance to each edge of the square.
     * @param rng               A Random used to calculate the random point.
     * @return                  A random Vector2D within the given rectangle.
     */
    public static Vector2D getRandomPoint(Vector2D point, int distanceToEdge, Random rng) {
        Vector2D topLeft = new Vector2D(point.getX() - distanceToEdge, point.getY() - distanceToEdge);
        Vector2D bottomRight = new Vector2D(point.getX() + distanceToEdge, point.getY() + distanceToEdge);

        return getRandomPoint(topLeft, bottomRight, rng);
    }

    /**
     * Given a list of directions, moves a specified distance in each direction in the list,
     * returning the point reached after iterating through the list.
     *
     * @param directions        The list of directions to move in.
     * @param distancePerMove   The distance to move in each direction.
     * @return                  The point reached after traveling in each direction in the list.
     */
    public Vector2D getPointFromDirections(List<Direction> directions, int distancePerMove) {
        if (directions.size() == 0) {
            return this;
        }
        Vector2D current = this;
        for (Direction direction : directions) {
            current = current.getPointInDirection(direction, distancePerMove);
        }
        return current;
    }

    /**
     * An overloaded version of {@code getPointFromDirections(List<Direction> directions,
     * int distancePerMove)} with a default move distance of 1.
     *
     * @param directions    A list of directions to move in.
     * @return              The point reached after moving in each direction in the list.
     */
    public Vector2D getPointFromDirections(List<Direction> directions) {
        return getPointFromDirections(directions, 1);
    }

}

