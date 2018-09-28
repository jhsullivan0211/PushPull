package com.jhsullivan.pushpull.user_interface;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import com.jhsullivan.pushpull.game_logic.Vector2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Contains useful methods for drawing onto a Canvas of a LevelView.
 *
 * Created by Joshua Sullivan on 5/13/2018.
 */

public class DrawingHelper {
    private enum Shape {CIRCLE, SQUARE};

    private LevelView levelView;
    private Paint paint = new Paint();
    private Path path = new Path();
    int borderColor = Color.BLACK;
    private int borderWidth;
    private int borderAlpha = 85;
    private int borderWidthFactor = 325;
    private Paint borderPaint = new Paint();
    private int basicTintAmount = 62;
    private int highTintAmount = 72;

    /**
     * Basic constructor for the DrawingHelper class.  Assigns parameters to member variables.
     *
     *
     * @param levelView     The LevelView in which to get drawing specifications.
     * @param canvas        The Canvas on which to draw.
     */
    public DrawingHelper(LevelView levelView) {
        this.levelView = levelView;
        this.borderWidth = levelView.getSize() / borderWidthFactor;
        paint.setAntiAlias(true);
        borderPaint.setColor(borderColor);
        borderPaint.setAlpha(borderAlpha);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setAntiAlias(true);


    }

    /**
     * Sets the color of the fill for this DrawingHelper.
     * @param color The color to set.
     */
    public void setColor(int color) {
        paint.setColor(color);
    }


    /**
     * Draws a filled-in square that is the specified color, at the specified location.
     *
     * @param color     The color of the square's fill.
     * @param location  The location to draw the square.
     */
    public void drawSquareBody(int color, Vector2D location, Canvas canvas) {
       drawBody(Shape.SQUARE, location, color, canvas);
    }

    /**
     * Draws a filled-in circle that is the specified color, at the specified location.
     *
     * @param color         The color of the circle's fill.
     * @param location      The location to draw the circle.
     */
    public void drawCircleBody(int color, Vector2D location, Canvas canvas) {
        drawBody(Shape.CIRCLE, location, color, canvas);
    }

    /**
     * Draws a body of specified shape and color at the specified location.  This method
     * is not publicly accessed; instead, use drawSquareBody or drawCircleBody.
     *
     * @param shape     The shape of the body to draw.
     * @param location  The location to draw the shape.
     * @param color     The color of the shape to draw.
     */
    private void drawBody(Shape shape, Vector2D location, int color, Canvas canvas) {

        Vector2D screenLocation = levelView.getScreenVector(location);
        int x = screenLocation.getX();
        int y = screenLocation.getY();
        int width = levelView.getActorUnit();
        int circleOffset = levelView.getActorUnit() / 10;

        Shader shader = new LinearGradient(x, y, x + width, y + width, color,
                DrawingHelper.getGradientTint(color, highTintAmount), Shader.TileMode.CLAMP);

        paint.setShader(shader);

        if (shape == Shape.SQUARE) {
            path.reset();
            path.moveTo(x, y);
            path.lineTo(x + width, y);
            path.lineTo(x + width, y + width);
            path.lineTo(x, y + width);
            path.lineTo(x, y);
            path.close();
            canvas.drawPath(path, paint);
            canvas.drawPath(path, borderPaint);
        }
        else if (shape == Shape.CIRCLE) {
            canvas.drawCircle(x + width / 2, y + width / 2,
                    width / 2 - circleOffset, paint);
        }

    }

    /**
     * Draws a polygon which is composed of many different squares at each location in the specified
     * list of locations, where location is a point on the game grid, not a screen point.
     *
     * @param locations  The locations of each square that makes up the polygon.
     * @param color      The color of the shape to draw.
     */
    public void drawPolygon(List<Vector2D> locations, int color, Canvas canvas) {


        List<Vector2D> screenList = new ArrayList<>();
        for (Vector2D location : locations) {
            screenList.add(levelView.getScreenVector(location));
        }

        List<Vector2D> bounds = Vector2D.getBounds(screenList);
        Shader shader = new LinearGradient(bounds.get(0).getX(), bounds.get(0).getY(),
                bounds.get(1).getX(), bounds.get(1).getY(), color,
                DrawingHelper.getGradientTint(color, basicTintAmount), Shader.TileMode.CLAMP);
        paint.setShader(shader);


        path.reset();
        for (Vector2D screenLocation : screenList) {
            int x = screenLocation.getX();
            int y = screenLocation.getY();

            if (path.isEmpty()) {
                path.moveTo(x, y);
            }
            else {
                path.lineTo(x, y);
            }
        }
        path.close();
        canvas.drawPath(path, paint);

        //draw border
        canvas.drawPath(path, borderPaint);
    }

    /**
     * Draws the specified Drawable at the given location.
     *
     * @param icon      The icon to draw.
     * @param location  The location to draw the icon.
     */
    public void drawIcon(String tag, Vector2D location, Canvas canvas) {
        Drawable icon = levelView.getIcon(tag);
        Vector2D screenLocation = levelView.getScreenVector(location);
        int x = screenLocation.getX();
        int y = screenLocation.getY();
        int width = levelView.getActorUnit();
        icon.setBounds(x, y, x + width, y + width);
        icon.draw(canvas);
    }

    /**
     * Given a color, returns a slightly darker version of that color.
     *
     * @param color The original color
     * @return      A slightly darker version of the original color.
     */
    public static int getGradientTint(int color, int tintAmount) {

        if (color == ColorHelper.getPushColor()) {
            tintAmount += 15;
        }
        int alpha = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        r = r > tintAmount ? r - tintAmount : 0;
        g = g > tintAmount ? g - tintAmount : 0;
        b = b > tintAmount ? b - tintAmount : 0;

        return Color.argb(alpha, r, g, b);

    }

    /**
     * Given the upper-left corner of a square, returns all four points in the square in clockwise
     * order, starting from the upper-left, where each side is 1 unit long.
     *
     * @param origin    The upper-left corner.
     * @return          A list of points forming a square, in clockwise order starting from the
     *                  given origin.
     */
    public static List<Vector2D> expandOrigin(Vector2D origin) {
        List<Vector2D> expandedList = new LinkedList<>();
        expandedList.add(origin);
        expandedList.add(origin.getPointInDirection(Vector2D.Direction.RIGHT));
        expandedList.add(origin.getPointFromDirections(Vector2D.Direction.RIGHT, Vector2D.Direction.DOWN));
        expandedList.add(origin.getPointInDirection(Vector2D.Direction.DOWN));
        return expandedList;
    }


    /**
     * Given a list of locations representing squares, returns a list of locations forming
     * the outside path of the polygon.  Starts from the first point in the specified list and
     * proceeds around the polygon in clockwise order.
     *
     * @param origins   The location of the upper-left points of squares forming the polygon.
     * @return          A list of points around the polygon, in clockwise order.
     */
    public static List<Vector2D> getPath(List<Vector2D> origins) {

        /*The below algorithm is a bit difficult to understand, but here's how it works.  It starts
        with a random point in the origins list.  It converts this single point (the location of
        the block) into four points in a path, i.e. an order that the drawing function will use
        when drawing the shape.  It then looks for an adjacent location point, converts that
        adjacent point into another path, and then merges that adjacent point with the main path,
        without disrupting the path.  It then does the same for all adjacent points, in breadth-
        first traversal through all the given points.  The result is a path going around the
        squares, in clockwise order. */

        List<Vector2D> path = DrawingHelper.expandOrigin(origins.get(0));
        List<Vector2D> visited = new ArrayList<>();
        visited.add(origins.get(0));
        List<Vector2D> lookQueue = new ArrayList<>();
        lookQueue.add(origins.get(0));

        while (!visited.containsAll(origins) && lookQueue.size() > 0) {
            List<Vector2D> nextQueue = new ArrayList<>();
            for (Vector2D point : lookQueue) {
                for (Vector2D.Direction direction : Vector2D.Direction.values()) {
                    Vector2D adjacent = point.getPointInDirection(direction);
                    if (origins.contains(adjacent) && !visited.contains(adjacent)) {
                        List<Vector2D> toMerge = DrawingHelper.expandOrigin(adjacent);
                        List<Vector2D> intersections = getSide(point, direction);
                        mergePaths(path, toMerge, intersections);
                        visited.add(adjacent);
                        nextQueue.add(adjacent);
                    }
                }
            }
            lookQueue = nextQueue;
        }

       // path.add(path.get(0));
        return path;
    }


    /**
     * Given a point and a direction, returns the points of the side of the square formed with the
     * given point as its upper left point, in clockwise order.
     *
     * @param point     The point representing the upper left of the square.
     * @param direction The direction corresponding to the side of the square.
     * @return  Returns a list of the two points of the side, in clockwise order around the square.
     */
    private static List<Vector2D> getSide(Vector2D point, Vector2D.Direction direction) {
        List<Vector2D> result = new ArrayList<>();
        switch (direction) {

            case UP:
                result.add(point);
                result.add(point.getPointInDirection(Vector2D.Direction.RIGHT));
                break;
            case RIGHT:
                result.add(point.getPointInDirection(Vector2D.Direction.RIGHT));
                result.add(point.getPointFromDirections(Vector2D.Direction.RIGHT, Vector2D.Direction.DOWN));
                break;
            case DOWN:
                result.add(point.getPointFromDirections(Vector2D.Direction.RIGHT, Vector2D.Direction.DOWN));
                result.add(point.getPointInDirection(Vector2D.Direction.DOWN));
                break;
            case LEFT:
                result.add(point.getPointInDirection(Vector2D.Direction.DOWN));
                result.add(point);
                break;
            default:
                break;
        }
        return result;
    }



    /**
     * Given a main path and a path to be absorbed (where path here is a list of Vector2Ds marking
     * the perimeter around a shape in counterclockwise order), merges the two paths into the main
     * path, resulting in a path that circumscribes both of the specified paths in clockwise order.
     *
     * @param mainPath      The main path, which will absorb the other.
     * @param absorbed      The path to be absorbed.
     * @param intersections The intersection points of the two paths, in clockwise order.
     */

    public static void mergePaths(List<Vector2D> mainPath,
                                  List<Vector2D> absorbed,
                                  List<Vector2D> intersections) {

        int index = mainPath.indexOf(intersections.get(0)) + 1;

        //merge two paths
        Vector2D current = getNextFromRing(intersections.get(0), absorbed);
        while (!current.equals(intersections.get(1))) {
            mainPath.add(index, current);
            index += 1;
            current = getNextFromRing(current, absorbed);

        }

        //remove "kinks", i.e. inner points formed when the absorbed forms a pinched off area
        //in the center of 4-square sections.
        current = mainPath.get(0);
        index = 0;
        for (int i = 0; i < mainPath.size(); i++) {
            Vector2D twoAhead = getNextFromRing(getNextFromRing(current, mainPath), mainPath);
            if (current.equals(twoAhead)) {
                mainPath.remove(index);
                mainPath.remove(index);
            }
            current = getNextFromRing(current, mainPath);
            index += 1;
        }

    }

    /**
     * Given a list of Vector2Ds and a Vector2D in that list, returns the next item in the list,
     * or the first item if the current item is the last item.  Essentially, makes the list act
     * as a sort of ring data structure.
     *
     * @param link  The current item in the ring, for which to get the next item.
     * @param ring  The list which contains the items.
     * @return      The next item in the list, in ring-form (i.e. if at the end, gets the first)
     */
    private static Vector2D getNextFromRing(Vector2D link, List<Vector2D> ring) {
        int index = ring.indexOf(link);

        if (index >= ring.size() - 1) {
            return ring.get(0);
        }
        else {
            return ring.get(index + 1);
        }
    }

    /**
     * Given two lists of points, returns a list of intersection points, in the order that they
     * occur in the first list.
     *
     * @param first     The first list of Vector2D points.
     * @param second    The second list of Vector2D points.
     * @return
     */
    public static List<Vector2D> getIntersections(List<Vector2D> first, List<Vector2D> second) {
        List<Vector2D> result = new ArrayList<>();
        for (Vector2D point : first) {
            for (Vector2D other : second) {
                if (point.equals(other)) {
                    result.add(point);
                }
            }
        }
        return result;
    }

}
