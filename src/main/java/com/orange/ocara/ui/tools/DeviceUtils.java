/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.tools;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.Display;

/**
 * Device utility class.
 */
public class DeviceUtils {

    private static Boolean mIsTablet = null;
    private static Boolean mIsLowDef = null;
    private static Boolean mIs7InchesOrHigher = null;
    private static Boolean mIs10InchesOrHigher = null;
    private static Boolean mHas10InchesOrHigherCapabilities = null;


    /**
     * Helps determine if the app is running in a Tablet context.
     *
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {

        if (mIsTablet != null) {
            return mIsTablet;
        }

        mIsTablet = (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        return mIsTablet;
    }


    public static boolean isInLandscapeMode(Activity activity) {
        return getScreenOrientation(activity) == Configuration.ORIENTATION_LANDSCAPE;
    }

    @SuppressWarnings("deprecation")
    public static int getScreenOrientation(Activity activity) {

        if (activity == null) {
            return Configuration.ORIENTATION_PORTRAIT;
        }

        Display getOrient = activity.getWindowManager().getDefaultDisplay();
        int orientation = Configuration.ORIENTATION_UNDEFINED;
        if (getOrient.getWidth() == getOrient.getHeight()) {
            orientation = Configuration.ORIENTATION_SQUARE;
        } else {
            if (getOrient.getWidth() < getOrient.getHeight()) {
                orientation = Configuration.ORIENTATION_PORTRAIT;
            } else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        return orientation;
    }


}
