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

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.facebook.stetho.Stetho;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.orange.ocara.data.net.client.ApiClient;
import com.orange.ocara.tools.OcaraLogger;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class TestOcaraApplication extends Application {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();

        CrashlyticsCore crashlytics = new CrashlyticsCore
                .Builder()
                .disabled(BuildConfig.DEBUG)
                .build();
        Fabric.with(this, new Crashlytics.Builder().core(crashlytics).build());

        OcaraLogger.initialize();

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }

        initialize();
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
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        ApiClient.init(this);

        Timber.i("Initialize done");
    }
}
