package com.example.pushpull.user_interface;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.pushpull.game_logic.Vector2D;

/**
 * Created by joshu on 5/15/2018.
 */

public class InputController extends View implements GestureDetector.OnGestureListener {


    private GestureDetectorCompat gestureDetector;
    private static final int DISTANCE_THRESHOLD = 5;
    private boolean enabled = true;
    private Context context;

    public InputController(Context context) {
        super(context);
        init(context);
    }

    public InputController(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InputController(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        this.context = context;
        this.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    return true;
                }
                //TODO: figure this performClick thing out
                performClick();
                return InputController.super.onTouchEvent(motionEvent);
            }
        });
        gestureDetector = new GestureDetectorCompat(context, this);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }


    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
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
    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {



        if (!enabled) {
            return false;
        }
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
        if (context instanceof PlayActivity) {
            ((PlayActivity) context).handleInput(direction);
        }


    }



    public void setInputEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
