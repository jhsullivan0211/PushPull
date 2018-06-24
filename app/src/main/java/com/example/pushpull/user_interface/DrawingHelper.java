package com.example.pushpull.user_interface;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.example.pushpull.game_logic.Actor;
import com.example.pushpull.game_logic.Vector2D;

import java.util.Collection;


/**
 * Contains useful methods for drawing onto a Canvas of a LevelView.
 *
 * Created by Joshua Sullivan on 5/13/2018.
 */

public class DrawingHelper {
    private enum Shape {CIRCLE, SQUARE};
    private static final int borderWidth = 5;
    private static final int circleOffset = 5;

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
    }

    public void drawSquareBody(int color, Vector2D location) {
       drawBody(Shape.SQUARE, location, color);
    }

    public void drawCircleBody(int color, Vector2D location) {
        drawBody(Shape.CIRCLE, location, color);
    }

    private void drawBody(Shape shape, Vector2D location, int color) {
        Vector2D screenLocation = levelView.getScreenVector(location);
        int x = screenLocation.getX();
        int y = screenLocation.getY();
        int width = levelView.getActorUnit();

        Paint paint = new Paint();
        paint.setColor(color);
        if (shape == Shape.SQUARE) {
            canvas.drawRect(x, y, x + width, y + width, paint);
        }
        else if (shape == Shape.CIRCLE) {
            canvas.drawCircle(x + width / 2, y + width / 2,
                    width / 2 - circleOffset, paint);
        }
    }

    public void drawAllBorders(Vector2D location) {
        for (Vector2D.Direction direction : Vector2D.Direction.values()) {
            drawBorder(direction, location);
        }
    }

    public void drawBorders(Collection<Vector2D.Direction> directions, Vector2D location) {
        for (Vector2D.Direction direction : directions) {
            drawBorder(direction, location);
        }
    }

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

    public void drawIcon(Drawable icon, Vector2D location) {
        Vector2D screenLocation = levelView.getScreenVector(location);
        int x = screenLocation.getX();
        int y = screenLocation.getY();
        int width = levelView.getActorUnit();
        icon.setBounds(x, y, x + width, y + width);
        icon.draw(canvas);
    }
}
