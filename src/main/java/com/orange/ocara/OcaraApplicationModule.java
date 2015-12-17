/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara;

import android.content.Context;


import com.orange.ocara.model.ModelModule;
import com.orange.ocara.ui.UiModule;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        complete = true,
        library = true,
        injects = {OcaraApplication.class},
        includes = {UiModule.class, ModelModule.class}
)
public final class OcaraApplicationModule {

    private final Context mApplicationContext;

    /**
     * Constructor.
     *
     * @param applicationContext Application Context
     */
    public OcaraApplicationModule(Context applicationContext) {
        this.mApplicationContext = applicationContext;
    }

    @Provides
    @Singleton
    Context providesApplicationContext() {
        return mApplicationContext;
    }

    @Provides
    @Singleton
    Executor providesExecutor() {
        return Executors.newCachedThreadPool();
    }

    @Provides
    @Singleton
    Picasso providesPicasso() {
        Picasso picasso = Picasso.with(mApplicationContext);
        return picasso;
    }
}

