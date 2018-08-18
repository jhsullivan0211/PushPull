package com.jhsullivan.pushpull.user_interface;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.jhsullivan.pushpull.game_logic.Vector2D;

/**
 * A View which tracks and processes user input anywhere within its bounds.
 *
 * Created by Joshua Sullivan on 5/15/2018.
 */

public class InputController extends View implements GestureDetector.OnGestureListener {


    private GestureDetectorCompat gestureDetector;
    private static final int DISTANCE_THRESHOLD = 12;
    private boolean enabled = true;
    private Context context;

    /**
     * Basic constructor to use when creating this View in code.
     *
     * @param context   The context (usually Activity) which contains this View.
     */
    public InputController(Context context) {
        super(context);
        init(context);
    }

    /**
     * Constructor called when inflating this View from XML.
     *
     * @param context   The context (usually Activity) which contains this View.
     * @param attrs     The AttributeSet of this View.
     */
    public InputController(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * Constructor to perform inflation from XML and apply a class-specific base style from a
     * theme attribute.
     *
     * @param context       The context (usually Activity) which contains this View.
     * @param attrs         The AttributeSet of this View.
     * @param defStyleAttr  The style attribute of this View.
     */
    public InputController(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    /**
     * Basic initialization, common to all the View constructors.  Sets the context variable,
     * as well as the listeners for touch/gestures.
     *
     * @param context   The context (usually Activity) that contains the View.
     */
    private void init(Context context) {
        this.context = context;
        this.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    return true;
                }
                performClick();
                return InputController.super.onTouchEvent(motionEvent);
            }
        });
        gestureDetector = new GestureDetectorCompat(context, this);
    }

    /**
     * Has no affect.  All click events will be in response to gestures instead.
     * @return  Returns the result of View.performClick();
     */
    @Override
    public boolean performClick() {
        return super.performClick();
    }


    /**
     * Unused, but required to implement GestureDetector.OnGestureListener.
     *
     */
    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    /**
     * Unused, but required to implement GestureDetector.OnGestureListener.
     *
     */
    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    /**
     * Unused, but required to implement GestureDetector.OnGestureListener.
     */
    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    /**
     * Unused, but required to implement GestureDetector.OnGestureListener.
    */
    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    /**
     * Unused, but required to implement GestureDetector.OnGestureListener.
     */
    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    /**
     * Process the fling gesture (finger presses down, moves across screen, and lifts), determining
     * whether the fling was sufficient to count as an intended user input, and returning whether
     * it is or not.  To count as intended input, distance traveled by the finger must exceed
     * a certain threshold distance.  If the movement is sufficient, sends a message to the
     * containing Activity to apply movement in the direction of the finger movement.
     *
     * @param e1            The location on the screen where the user first touches.
     * @param e2            The location on the screen where the finger leaves the screen after
     *                      dragging across it.
     * @param velocityX     The velocity of the movement in the X direction, unused here.
     * @param velocityY     The velocity of the movement in the Y direction, unused here.
     * @return              Returns whether the movment was sufficient to count as an input.
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

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

    /**
     * Sends a message to the contianing Activity that the user has executed a finger movement in
     * the specified direction.
     *
     * @param direction     The direction of the user's finger gesture.
     */
    private void applyMovement(Vector2D.Direction direction) {
        if (context instanceof PlayActivity) {
            ((PlayActivity) context).handleInput(direction);
        }
    }

    /**
     * Sets whether input should be processed or not.
     *
     * @param enabled   Whether or not input should be processed.
     */
    public void setInputEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
