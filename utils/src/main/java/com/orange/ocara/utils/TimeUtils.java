/*
 *
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 *
 */

package com.orange.ocara.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeUtils {
    public static String getTimeInFormatForDatebase() {
        return SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).
                format(Calendar.getInstance().getTime());
    }

    // converts milliseconds time in 00:00 format
    public static String getTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        if (milliseconds % 1000 >= 500) {
            seconds++;
        }
        long minutes = seconds / 60;
        seconds %= 60;
        String secondsStr;
        if (seconds < 10) {
            secondsStr = "0" + seconds;
        } else {
            secondsStr = seconds + "";
        }
        return minutes + ":" + secondsStr;
    }
}
