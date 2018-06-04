package com.example.pushpull.user_interface;

import android.graphics.Color;

/**
 * Created by joshu on 5/14/2018.
 */

public class ColorHelper {

    public static int getBlockColor() {
        return Color.LTGRAY;
       // return 0xFF506D2F;
    }

    public static int getClusterColor() {
        return getBlockColor();
    }

    public static int getGrabAllColor() {
        return 0xFFFFEC5C;
    }

    public static int getPushColor() {
        return 0xFFF70025;

    }

    public static int getPullColor() {
        return 0xFF375E97;
    }

    public static int getBackgroundColor() {
        return 0xFFFDFDFD;
    }

    public static int getBorderColor() {
        return 0xFF00171B;
    }

    public static int getTargetColor() {
        return 0xFFFA6775;
    }

    public static int getWallColor() {
        return Color.DKGRAY;
    }
}
