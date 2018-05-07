package com.example.pushpull.user_interface;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.pushpull.game_logic.Level;
import com.example.pushpull.game_logic.LevelManager;
import com.example.pushpull.game_objects.GameObject;
import com.example.pushpull.myLibrary.Vector2D;
import com.example.pushpull.triggers.Trigger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class LevelView extends View implements GestureDetector.OnGestureListener {

    private Level level;

    private enum Shape {CIRCLE, SQUARE};


    private Paint paint = new Paint(Color.BLACK);
    private Context context;
    private LevelManager levelManager;

    private int margin;
    private int width;
    private int size;

    private static final int borderWidth = 5;
    private static final int circleOffset = 3;

    private static final int marginDivisorFactor = 100;


    private static final int VELOCITY_THRESHOLD = 200;
    private static final int DISTANCE_THRESHOLD = 10;
    private int gridLength;

    private GestureDetectorCompat gestureDetector;



    public LevelView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public LevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public LevelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }


    private void init() {
        this.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    return true;
                }
                return LevelView.super.onTouchEvent(motionEvent);
            }
        });
        gestureDetector = new GestureDetectorCompat(context, this);


    }

    public void setLevel(Level level) {
        this.level = level;
        this.gridLength = level.getGridLength();
        invalidate();
    }

    public void setLevelManager(LevelManager levelManager) {
        this.levelManager = levelManager;
    }




    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public void onDraw(Canvas canvas) {

        size = Math.min(this.getWidth(), this.getHeight());
        margin = size / marginDivisorFactor;
        width = ((size - (2 * margin)) / gridLength);

        this.setMeasuredDimension(size, size);

        if (level == null) {
            return;
        }
        paint.setColor(Color.CYAN);
        canvas.drawRect(0, 0, size, size, paint);




        for (Trigger trigger : level.getTriggers()) {
            drawFill(canvas, trigger.getLocation(), trigger.getColor(), Shape.CIRCLE);
        }


        for (GameObject gameObject : level.getGameObjects()) {
            drawFill(canvas, gameObject.getLocation(), gameObject.getColor(), Shape.SQUARE);
            drawBorders(canvas, gameObject);

        }

    }

    public Vector2D getScreenVector(Vector2D inputVector) {
        int x = inputVector.getX() * width + margin;
        int y = inputVector.getY() * width + margin;
        return new Vector2D(x, y);
    }

    public void drawFill(Canvas canvas, Vector2D location, int color, Shape shape) {

        Vector2D screenLocation = getScreenVector(location);
        int x = screenLocation.getX();
        int y = screenLocation.getY();



        if (shape == Shape.SQUARE) {


            paint.setColor(color);
            canvas.drawRect(x, y, x + width, y + width, paint);
            paint.setColor(Color.BLACK);

        }

        if (shape == Shape.CIRCLE) {
            paint.setColor(color);
            canvas.drawCircle(x + width / 2, y + width / 2,
                            width / 2 - circleOffset, paint);
        }
    }



    public void drawBorders(Canvas canvas, Collection<GameObject> group) {



        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(borderWidth);

        for (GameObject obj : group) {

            Vector2D screenLocation = getScreenVector(obj.getLocation());
            int x = screenLocation.getX();
            int y = screenLocation.getY();

            if (level.getObjectBorders(obj) != null)  {
                for (Vector2D.Direction direction : level.getObjectBorders(obj)) {
                    switch(direction) {
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
            }



        }
    }

    public void drawBorders(Canvas canvas, GameObject obj) {
        List<GameObject> single = new ArrayList<>();
        single.add(obj);
        drawBorders(canvas, single);
    }




    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        String msg = "";

        float x = e2.getX() - e1.getX();
        float y = e2.getY() - e1.getY();

        if (Math.abs(x) > DISTANCE_THRESHOLD || Math.abs(y) > DISTANCE_THRESHOLD) {
            Vector2D.Direction direction = null;
            if (Math.abs(x) > Math.abs(y)) {
                direction = (x > 0) ? Vector2D.Direction.RIGHT : Vector2D.Direction.LEFT;
            }
            if (Math.abs(y) > Math.abs(x)) {
                direction = (y > 0) ? Vector2D.Direction.DOWN : Vector2D.Direction.UP;
            }
            if (direction != null) {
                applyMovement(direction);
            }
        }
        return true;
    }

    private void applyMovement(Vector2D.Direction direction) {
        level.processInput(direction);

        if (levelManager.checkVictory()) {
            setLevel(levelManager.getCurrentLevel());
        }
        invalidate();
    }
}
