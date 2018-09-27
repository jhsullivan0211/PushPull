package com.jhsullivan.pushpull.user_interface;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;

/**
 * A utility class containing static methods for activities, as well as several static String IDs.
 * This class should not be instantiated.
 *
 * Created by Joshua Sullivan on 6/4/2018.
 */

public class ActivityUtility {

    public static final String currentLevelID = "PUSHPULL.CURRENT_LEVEL";
    public static final String gameStateID = "PUSHPULL.GAME_STATE";
    public static final String undoPositionID = "PUSHPULL.UNDO_LOC_STATE";
    public static final String undoTypeID = "PUSHPULL.UNDO_TYPE_STATE";
    public static final String levelCountID = "PUSHPULL.LEVEL_COUNT";
    public static final String maxLevelID = "PUSHPULL.MAX_LEVEL";
    public static final String dataFileName = "PUSHPULL_DATA";
    public static final String chosenIndexID = "PUSHPULL.LEVEL_INDEX_CHOSEN";
    public static final String soundID = "PUSHPULL.SOUND";
    public static final String exitID = "PUSHPULL.EXIT";
    public static final String levelWidthID = "PUSHPULL.LEVELWIDTH";
    public static final String levelSelectSoundID = "PUSHPULL.LEVELSELECTSOUNDID";


    /**
     * Given a title, message, and activity, shows an alert with the given title and message on
     * the activity and then closes the activity.  Used primarily for critical errors.
     *
     * @param title     The title of the alert.
     * @param message   The message of the alert.
     * @param activity  The activity on which to show the alert.
     */
    public static void showAlert(String title, String message, final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        activity.finish();
                    }
                })
                .show();

        AlertDialog dialog = builder.create();

    }

}
