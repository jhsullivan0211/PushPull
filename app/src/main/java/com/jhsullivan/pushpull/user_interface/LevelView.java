package com.jhsullivan.pushpull.user_interface;


import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import com.jhsullivan.pushpull.R;
import com.jhsullivan.pushpull.game_logic.Actor;
import com.jhsullivan.pushpull.game_logic.Level;
import com.jhsullivan.pushpull.game_objects.BlockCluster;
import com.jhsullivan.pushpull.game_objects.GameObject;
import com.jhsullivan.pushpull.game_logic.Vector2D;
import com.jhsullivan.pushpull.game_objects.Player;
import com.jhsullivan.pushpull.game_objects.Wall;
import com.jhsullivan.pushpull.triggers.Target;
import com.jhsullivan.pushpull.triggers.Trigger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LevelView extends View {
    /**
     * A View which draws all of the visible objects in a given level, as well as a background
     * image.  Automatically resizes and moves its drawing area to occupy the center of the
     * screen in such a way that it remains a fully visible square.  This class communicates
     * with the Level class to get the information about object positions/types.  Levels can
     * be changed dynamically, and the LevelView will reflect those changes.
     */

    private static final int backgroundColor = ColorHelper.getBackgroundColor();
    private Level level;
    private Paint paint = new Paint(backgroundColor);
    private Paint textPaint = new Paint(Color.BLACK);
    private int gridLength;
    private int actorUnit;
    private int size;
    private float radius;
    private int marginX;
    private int marginY;
    public Drawable coveredTargetIcon;
    public Drawable targetIcon;
    public int radiusFactor = 205;

    private int textSize = 16;
    private int textPixels = 0;
    private Map<List<Integer>, Shader> shaderMap = new HashMap<>();
    private Map<Actor, DrawingHelper> drawingMap = new HashMap<>();


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
     * Constructor that is called when inflating the View from XML.f
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
     * The initialization method called by each constructor.  Initializes some variables, performs
     * some setup actions, and reformats the View according to the screen size.
     *
     * @param context   The context of this View, usually an Activity.
     */
    private void init(final Context context) {
        coveredTargetIcon = getResources().getDrawable(R.drawable.check);
        targetIcon = getResources().getDrawable(R.drawable.target_icon_small);

        if (getResources().getDisplayMetrics().densityDpi <= 240) {
            textSize = 14;
        }

        textPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                textSize, getResources().getDisplayMetrics());



        textPaint.setTextSize(textPixels);

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
        marginY = (this.getHeight() - size) / 2 - (textPixels / 2);

        if (getBottom() - (getTop() + marginY + size) <= textPixels) {
            textPaint.setTextSize(0);
            marginY = (this.getHeight() - size) / 2;
        }
        mapDrawingHelpers();
        invalidate();

    }

    /**
     * Sets the level displayed by this View.
     *
     * @param level The new level, to be set to.
     */

    public void setLevel(Level level) {
        this.level = level;
        this.gridLength = Level.getGridLength();
        //buildShaders();
        mapDrawingHelpers();
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
        super.onDraw(canvas);
        if (level == null || size == 0) {
            return;
        }

        paint.setColor(backgroundColor);
        canvas.drawRect(marginX, marginY, marginX + size, marginY + size, paint);
        drawGrid(canvas);

        for (Trigger trigger : level.getTriggers()) {
            drawActor(trigger, canvas);
        }



        for (GameObject gameObject : level.getGameObjects()) {
            drawActor(gameObject, canvas);
        }



        int count = 0;
        int filled = 0;
        for (Target target : level.getTargets()) {
            if (target.isFilled()) {
                drawingMap.get(target).drawIcon("covered", target.getLocation(), canvas);
                filled += 1;
            }
            count += 1;
        }

        int x = 4 * getActorUnit() + getActorUnit() * 3 / 4 + marginX;
        int y = size + marginY + textPixels;
        canvas.drawText(filled + "/" + count, x, y, textPaint);


    }

    /**
     * Given an actor, gets its drawingHelper, loads the appropriate shader, and calls
     * that actor's draw method.
     *
     * @param actor The actor to draw.
     */
    private void drawActor(Actor actor, Canvas canvas) {

        DrawingHelper drawingHelper = drawingMap.get(actor);
        int color = actor.getColor();
//        //get the shader using the object's location, color, and bounds.
//        int width = 1;
//        int height = 1;
//        if (actor instanceof BlockCluster) {
//            if (!((BlockCluster) actor).getMajor()) {
//                return;
//            }
//            width = level.getClusterBoundsMap().get(actor).getX();
//            height = level.getClusterBoundsMap().get(actor).getY();
//        }
//        Shader shader = shaderMap.get(Arrays.asList(actor.getLocation().getX(),
//                                                    actor.getLocation().getY(),
//                                                    color,
//                                                    width,
//                                                    height));
//        if (shader != null && actor instanceof BlockCluster) {
//            Log.d("TEST", "drawActor: cluster shader!  " + actor.getLocation().getX() + "," +
//                                                                    actor.getLocation().getY() + "," +
//                                                                    color + ", " + width + ", " + height);
//        }
//        drawingHelper.loadShader(shader);


        actor.draw(drawingHelper, canvas);
    }

    /**
     * Draws a grid of small circles that demarcate discrete positions of the board.
     *
     * @param canvas    The canvas on which to draw.
     */
    private void drawGrid(Canvas canvas) {
        Paint gridPaint = new Paint();
        gridPaint.setARGB(25, 0, 59, 70);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int x = (i * getActorUnit() + getActorUnit() / 2) + marginX;
                int y = (j * getActorUnit() + getActorUnit() / 2) + marginY;

                canvas.drawCircle(x, y, radius, gridPaint);

            }
        }
    }

    /**
     * Fills the drawingMap with the mapping between actors and the DrawingHelper that will draw
     * them.
     */
    private void mapDrawingHelpers() {
        drawingMap = new HashMap<>();
        for (GameObject obj : level.getGameObjects()) {
            DrawingHelper drawingHelper = new DrawingHelper(this);
            drawingHelper.setColor(obj.getColor());
            drawingMap.put(obj, drawingHelper);
        }

        for (Trigger trigger : level.getTriggers()) {
            DrawingHelper drawingHelper = new DrawingHelper(this);
            drawingHelper.setColor(trigger.getColor());
            drawingMap.put(trigger, drawingHelper);
        }
    }

//    /**
//     * Creates shaders for each location and each color, that can be accessed quickly using a
//     * list of x, y, color, width, and height.
//     */
//    private void buildShaders() {
//        shaderMap = new HashMap<>();
//        List<Integer> colors = Arrays.asList(ColorHelper.getPushColor(),
//                ColorHelper.getPullColor(),
//                ColorHelper.getGrabAllColor(),
//                ColorHelper.getBlockColor(),
//                ColorHelper.getWallColor());
//        for (int x = 0; x < Level.getGridLength(); x++) {
//            for (int y = 0; y < Level.getGridLength(); y++) {
//                for (Integer color : colors) {
//                    Vector2D screenVectorA = getScreenVector(new Vector2D(x, y));
//                    Vector2D screenVectorB = getScreenVector(new Vector2D(x + 1, y + 1));
//
//                    Shader shader = new LinearGradient(screenVectorA.getX(), screenVectorA.getY(),
//                            screenVectorB.getX(), screenVectorB.getY(), color,
//                            DrawingHelper.getGradientTint(color), Shader.TileMode.CLAMP);
//
//                    shaderMap.put(Arrays.asList(x, y, color, 1, 1), shader);
//
//
//
//                    if (color != ColorHelper.getBlockColor()) {
//                        continue;
//                    }
//                    for (Vector2D bounds : level.getClusterBoundsMap().values()) {
//                        screenVectorA = getScreenVector(new Vector2D(x, y));
//                        screenVectorB = getScreenVector(Vector2D.add(screenVectorA, bounds));
//
//
//                        shader = new LinearGradient(screenVectorA.getX(), screenVectorA.getY(),
//                                    screenVectorB.getX(), screenVectorB.getY(), color,
//                                DrawingHelper.getGradientTint(color), Shader.TileMode.CLAMP);
//
//                        shaderMap.put(Arrays.asList(x, y, color, bounds.getX(), bounds.getY()), shader);
//                    }
//
//
//                }
//
//
//            }
//        }
//    }

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


    public void provideLayout(ConstraintLayout layout) {
        if (layout.getTag() == "small") {
            textSize = 0;
        }
    }

    public Drawable getIcon(String tag) {
        if (tag.equals("target")) {
            return targetIcon;
        }
        if (tag.equals("covered")) {
            return coveredTargetIcon;
        }
        return null;
    }



}
