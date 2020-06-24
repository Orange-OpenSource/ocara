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

package com.orange.ocara.tools;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.orange.ocara.BuildConfig;

import timber.log.Timber;

/**
 * This class handles the logs
 */
public final class OcaraLogger {

    private OcaraLogger() {
    }

    /**
     * Call this to initialize the Logger, usually in the application class
     */
    public static void initialize() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    /**
     * A tree which logs important information for crash reporting.
     */
    private static class CrashReportingTree extends Timber.Tree {

        private final String KEY_PRIORITY = "priority";
        private final String KEY_TAG = "tag";
        private final String KEY_MESSAGE = "message";

        @Override
        protected void log(final int priority, final String tag, final String message, final Throwable t) {

            if (priority != Log.VERBOSE && priority != Log.DEBUG && priority != Log.INFO) {
                Crashlytics.setInt(KEY_PRIORITY, priority);
                Crashlytics.setString(KEY_TAG, tag);
                Crashlytics.setString(KEY_MESSAGE, message);

                if (t == null) {
                    Crashlytics.logException(new Exception(message));
                } else {
                    Crashlytics.logException(t);
                }
            }
        }
    }
}
