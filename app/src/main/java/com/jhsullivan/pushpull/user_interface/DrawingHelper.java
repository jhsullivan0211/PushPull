package com.jhsullivan.pushpull.user_interface;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.jhsullivan.pushpull.game_logic.Vector2D;

import java.util.Collection;


/**
 * Contains useful methods for drawing onto a Canvas of a LevelView.
 *
 * Created by Joshua Sullivan on 5/13/2018.
 */

public class DrawingHelper {
    private enum Shape {CIRCLE, SQUARE};
    private int borderWidth;
    private int borderWidthFactor = 205;
    private LevelView levelView;
    private Canvas canvas;



    /**
     * Basic constructor for the DrawingHelper class.  Assigns parameters to member variables.
     *
     *
     * @param levelView     The LevelView in which to get drawing specifications.
     * @param canvas        The Canvas on which to draw.
     */
    public DrawingHelper(LevelView levelView, Canvas canvas) {
        this.levelView = levelView;
        this.canvas = canvas;
        this.borderWidth = levelView.getSize() / borderWidthFactor;
    }

    /**
     * Draws a filled-in square that is the specified color, at the specified location.
     *
     * @param color     The color of the square's fill.
     * @param location  The location to draw the square.
     */
    public void drawSquareBody(int color, Vector2D location) {
       drawBody(Shape.SQUARE, location, color);
    }

    /**
     * Draws a filled-in circle that is the specified color, at the specified location.
     *
     * @param color         The color of the circle's fill.
     * @param location      The location to draw the circle.
     */
    public void drawCircleBody(int color, Vector2D location) {
        drawBody(Shape.CIRCLE, location, color);
    }

    /**
     * Draws a body of specified shape and color at the specified location.  This method
     * is not publicly accessed; instead, use drawSquareBody or drawCircleBody.
     *
     * @param shape     The shape of the body to draw.
     * @param location  The location to draw the shape.
     * @param color     The color of the shape to draw.
     */
    private void drawBody(Shape shape, Vector2D location, int color) {
        Vector2D screenLocation = levelView.getScreenVector(location);
        int x = screenLocation.getX();
        int y = screenLocation.getY();
        int width = levelView.getActorUnit();
        int circleOffset = levelView.getActorUnit() / 10;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        if (shape == Shape.SQUARE) {
            canvas.drawRect(x, y, x + width, y + width, paint);
        }
        else if (shape == Shape.CIRCLE) {
            canvas.drawCircle(x + width / 2, y + width / 2,
                    width / 2 - circleOffset, paint);
        }
    }

    /**
     * Draws all four borders at the specified location, forming a square.
     *
     * @param location  The location at which to draw the borders.
     */
    public void drawAllBorders(Vector2D location) {
        for (Vector2D.Direction direction : Vector2D.Direction.values()) {
            drawBorder(direction, location);
        }
    }

    /**
     * Draws all of the borders in the directions found in the specified list, at the
     * specified location.
     *
     * @param directions    A Collection of Directions in which to draw borders.
     * @param location      The location in which to draw the borders.
     */
    public void drawBorders(Collection<Vector2D.Direction> directions, Vector2D location) {
        for (Vector2D.Direction direction : directions) {
            drawBorder(direction, location);
        }
    }

    /**
     * Draws a border in the specified direction, relative to the center of the square.
     *
     * @param direction     The direction defining which edge should have a border.
     * @param location      The location at which to draw the border.
     */
    public void drawBorder(Vector2D.Direction direction, Vector2D location) {

        Vector2D screenLocation = levelView.getScreenVector(location);
        int x = screenLocation.getX();
        int y = screenLocation.getY();
        int width = levelView.getActorUnit();
        Paint paint = new Paint();
        paint.setColor(ColorHelper.getBorderColor());
        paint.setAlpha(85);
        paint.setStrokeWidth(borderWidth);

        switch (direction) {
           case UP:
                canvas.drawLine(x, y, x + width, y, paint);
                break;
            case DOWN:
                canvas.drawLine(x, y + width, x + width, y + width, paint);
                break;
            case RIGHT:
                canvas.drawLine(x + width, y, x + width, y + width, paint);
                break;
            case LEFT:
                canvas.drawLine(x, y, x, y + width, paint);
                break;
            default:
                break;

        }
    }

    /**
     * Draws the specified Drawable at the given location.
     *
     * @param icon      The icon to draw.
     * @param location  The location to draw the icon.
     */
    public void drawIcon(Drawable icon, Vector2D location) {
        Vector2D screenLocation = levelView.getScreenVector(location);
        int x = screenLocation.getX();
        int y = screenLocation.getY();
        int width = levelView.getActorUnit();
        icon.setBounds(x, y, x + width, y + width);
        icon.draw(canvas);
    }
}
