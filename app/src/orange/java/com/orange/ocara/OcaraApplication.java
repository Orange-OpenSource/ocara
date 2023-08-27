/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.facebook.stetho.Stetho;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.orange.ocara.conflicts.net.client.ApiClient;
import com.orange.ocara.tools.OcaraLogger;

import org.androidannotations.annotations.EApplication;

import timber.log.Timber;

/**
 * Bootstrap of the application
 */
@EApplication
public class OcaraApplication extends Application {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();

        OcaraLogger.initialize();

        AppCenter.start(this, "a37103e7-6037-4080-86e4-22f935f5aac0", Analytics.class);

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }

        initialize();

        // Specific customization for Orange
//        OcaraConfiguration.findAll().setAboutXmlId(R.layout.activity_about_orange);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * Called on application initialization.
     */
    protected void initialize() {
        Timber.i("Initialize starting");

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        ApiClient.init(this);

        Timber.i("Initialize done");
    }
}
