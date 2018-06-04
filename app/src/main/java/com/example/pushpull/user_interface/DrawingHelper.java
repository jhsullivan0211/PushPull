package com.example.pushpull.user_interface;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.example.pushpull.game_logic.Actor;
import com.example.pushpull.game_logic.Level;
import com.example.pushpull.game_objects.GameObject;
import com.example.pushpull.myLibrary.Vector2D;

import java.util.Collection;

/**
 * Created by joshu on 5/13/2018.
 */

public class DrawingHelper {
    private enum Shape {CIRCLE, SQUARE};
    private static final int borderWidth = 5;
    private static final int circleOffset = 5;

    private LevelView levelView;
    private Canvas canvas;
    private Actor actor;


    public DrawingHelper(LevelView levelView, Canvas canvas, Actor actor) {
        this.levelView = levelView;
        this.canvas = canvas;
        this.actor = actor;
    }

    public void drawSquareBody(int color) {
       drawBody(Shape.SQUARE, color);
    }

    public void drawCircleBody(int color) {
        drawBody(Shape.CIRCLE, color);
    }

    private void drawBody(Shape shape, int color) {
        Vector2D screenLocation = levelView.getScreenVector(actor.getLocation());
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

    public void drawAllBorders() {
        for (Vector2D.Direction direction : Vector2D.Direction.values()) {
            drawBorder(direction);
        }
    }

    public void drawBorders(Collection<Vector2D.Direction> directions) {
        for (Vector2D.Direction direction : directions) {
            drawBorder(direction);
        }
    }

    public void drawBorder(Vector2D.Direction direction) {

        Vector2D screenLocation = levelView.getScreenVector(actor.getLocation());
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

    public void drawIcon(Drawable icon) {
        Vector2D screenLocation = levelView.getScreenVector(actor.getLocation());
        int x = screenLocation.getX();
        int y = screenLocation.getY();
        int width = levelView.getActorUnit();
        icon.setBounds(x, y, x + width, y + width);
        icon.draw(canvas);
    }
}
