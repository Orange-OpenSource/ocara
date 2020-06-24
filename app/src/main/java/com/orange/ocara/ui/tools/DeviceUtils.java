/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2020 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
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
