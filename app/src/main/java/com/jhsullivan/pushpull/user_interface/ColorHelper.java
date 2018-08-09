package com.jhsullivan.pushpull.user_interface;

import android.graphics.Color;

/**
 * A utility class for getting access to colors from anywhere, without accessing the resource
 * file.  This may be deprecated in favor of the XML resources later.  All methods simply get
 * the int for a particular color.
 *
 * Created by Joshua Sullivan on 5/14/2018.
 */

public class ColorHelper {

    public static int getBlockColor() {
        return Color.LTGRAY;
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

    public static int getWallColor() {
        return Color.DKGRAY;
    }
}
