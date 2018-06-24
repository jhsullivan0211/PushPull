package com.example.pushpull.user_interface;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jhsullivan.pushpull.R;
import com.example.pushpull.game_logic.Level;
import com.example.pushpull.game_objects.GameObject;
import com.example.pushpull.game_logic.Vector2D;
import com.example.pushpull.triggers.Target;
import com.example.pushpull.triggers.Trigger;


public class LevelView extends View {





    private static final int backgroundColor = ColorHelper.getBackgroundColor();
    private static final int marginDivisorFactor = 100;

    private Level level;
    private Paint paint = new Paint(backgroundColor);
    private int gridLength;

    public Drawable targetIcon;

    public LevelView(Context context) {
        super(context);
        init(context);
    }

    public LevelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public LevelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {


        targetIcon = getResources().getDrawable(R.drawable.check);


    }

    public void setLevel(Level level) {
        this.level = level;
        this.gridLength = level.getGridLength();
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {

        int size = getSize();

        this.setMeasuredDimension(size, size);

        if (level == null) {
            return;
        }
        paint.setColor(backgroundColor);
        canvas.drawRect(0, 0, size, size, paint);

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



    public void drawGrid(Canvas canvas) {
        Paint gridPaint = new Paint();
        gridPaint.setARGB(15, 0, 59, 70);


        for (int i = 0; i < 10; i += 1) {
            for (int j = 0; j < 10; j += 1) {
                int x = i * getActorUnit() + getActorUnit() / 2;
                int y = j * getActorUnit() + getActorUnit() / 2;

                canvas.drawCircle(x, y, 5, gridPaint);
            }
        }
    }

    public int getSize() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return this.getHeight();
        }
        else {
            return this.getWidth();
        }
    }

    private int getMargin() {
        return getSize() / marginDivisorFactor;
    }

    public int getActorUnit() {
        return ((getSize() - (2 * getMargin())) / gridLength);
    }
    //TODO: include an object height?


    public Vector2D getScreenVector(Vector2D inputVector) {
        int x = inputVector.getX() * getActorUnit() + getMargin();
        int y = inputVector.getY() * getActorUnit() + getMargin();
        return new Vector2D(x, y);
    }


}
