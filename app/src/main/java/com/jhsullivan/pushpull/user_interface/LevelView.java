package com.jhsullivan.pushpull.user_interface;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.jhsullivan.pushpull.R;
import com.jhsullivan.pushpull.game_logic.Level;
import com.jhsullivan.pushpull.game_objects.GameObject;
import com.jhsullivan.pushpull.game_logic.Vector2D;
import com.jhsullivan.pushpull.game_objects.Player;
import com.jhsullivan.pushpull.triggers.Target;
import com.jhsullivan.pushpull.triggers.Trigger;


public class LevelView extends View {
    /**
     * A View which draws all of the visible objects in a given level, as well as a background
     * image.  Automatically resizes and moves its drawing area to occupy the center of the
     * screen in such a way that it remains a fully visible square.  This class communicates
     * with the Level class to get the information about object positions/types.  Levels can
     * be changed dynamically, and the LevelView will reflect those changes.
     */

    private static final int backgroundColor = ColorHelper.getBackgroundColor();
    private static final int marginDivisorFactor = 100;

    private Level level;
    private Paint paint = new Paint(backgroundColor);
    private int gridLength;
    private int actorUnit;
    private int size;
    private int radius;
    private int marginX;
    private int marginY;

    public Drawable targetIcon;
    public int radiusFactor = 205;


    /**
     * Basic constructor to use when creating this View from code.
     *
     * @param context   The context (usually an Activity) that contains this View.
     */
    public LevelView(Context context) {

        super(context);
        init(context);
    }

    /**
     * Constructor that is called when inflating the View from XML.
     *
     * @param context   The context of the View.
     * @param attrs     The attributes of the XML tag that is inflating the View.
     */
    public LevelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    /**
     * Perform inflation from XML and apply a class-specific base style from a theme attribute.
     *
     * @param context       The ocntext of the View.
     * @param attrs         The attributes of the XML tag that is inflating the View.
     * @param defStyleAttr  An attribute in the current theme that contains a reference to a style
     *                      resource that supplies default values for the view. Can be 0 to not look
     *                      for defaults.
     */
    public LevelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * The initialization method called by each constructor.  Initializes some variables and
     * reformats the View according to the screen size.
     *
     * @param context   The context of this View, usually an Activity.
     */
    private void init(final Context context) {
        targetIcon = getResources().getDrawable(R.drawable.check);
        final Activity activity = (Activity) context;
        this.setWillNotDraw(false);
    }

    /**
     * Called when the size of the View changes.
     *
     * @param w     The new width of the View.
     * @param h     The new height of the View.
     * @param oldw  The old width of the View.
     * @param oldh  The old height of the View.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        size = this.getSize();
        radius = size / radiusFactor;
        actorUnit = size / gridLength;
        marginX = (this.getWidth() - size) / 2;
        marginY = (this.getHeight() - size) / 2;
    }

    /**
     * Sets the level displayed by this View.
     *
     * @param level The new level, to be set to.
     */
    public void setLevel(Level level) {
        this.level = level;
        this.gridLength = level.getGridLength();
        invalidate();
    }

    /**
     * The method called to draw the View.  Draws the background, the grid (small circles that
     * mark each position on the board), and each visible game entity.
     *
     * @param canvas    The canvas on which to draw.
     */
    @Override
    public void onDraw(Canvas canvas) {

        if (level == null || size == 0) {
            return;
        }

        paint.setColor(backgroundColor);
        canvas.drawRect(marginX, marginY, marginX + size, marginY + size, paint);
        drawGrid(canvas);

        for (Trigger trigger : level.getTriggers()) {
            trigger.draw(this, canvas);
        }

        for (GameObject gameObject : level.getGameObjects()) {
            gameObject.draw(this, canvas);
        }

        for (Target target : level.getTargets()) {
            if (target.isFilled()) {
                target.drawSuccessIcon(this, canvas);
            }
        }
    }

    /**
     * Draws a grid of small circles that demarcate discrete positions of the board.
     *
     * @param canvas    The canvas on which to draw.
     */
    private void drawGrid(Canvas canvas) {
        Paint gridPaint = new Paint();
        gridPaint.setARGB(25, 0, 59, 70);

        for (int i = 0; i < 10; i += 1) {
            for (int j = 0; j < 10; j += 1) {
                int x = (i * getActorUnit() + getActorUnit() / 2) + marginX;
                int y = (j * getActorUnit() + getActorUnit() / 2) + marginY;

                canvas.drawCircle(x, y, radius, gridPaint);
            }
        }
    }

    /**
     *
     * @return  Returns the size of the visible part of this view.
     */
    public int getSize() {
        return Math.min(this.getHeight(), this.getWidth());
    }

    /**
     *
     * @return  Returns an actor unit, which is just the width of the max size of a game entity.
     */
    public int getActorUnit() {
        return actorUnit;
    }

    /**
     * Given a Vector2D game board position, returns the screen location corresponding to that
     * position.
     *
     * @param inputVector   A Vector2D location on the game board.
     * @return  Returns a Vector2D converted to screen distance.
     */
    public Vector2D getScreenVector(Vector2D inputVector) {
        int x = inputVector.getX() * getActorUnit() + marginX;
        int y = inputVector.getY() * getActorUnit() + marginY;
        return new Vector2D(x, y);
    }


}
