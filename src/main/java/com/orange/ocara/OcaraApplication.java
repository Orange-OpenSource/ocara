/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara;

import com.orange.ocara.model.ModelManager;
import com.orange.ocara.model.RuleSetLoader;
import com.orange.ocara.tools.Logger;
import com.orange.ocara.tools.injection.DaggerApplication;

import javax.inject.Inject;

import timber.log.Timber;

public class OcaraApplication extends DaggerApplication {

    @Inject
    ModelManager modelManager;
    @Inject
    RuleSetLoader ruleSetLoader;


    @Override
    public void onCreate() {
        Logger.initialize();

        super.onCreate();
        initialize();
    }

    @Override
    public void onTerminate() {
        terminate();
        super.onTerminate();

        Logger.terminate();
    }

    /**
     * Called on application initialization.
     */
    protected void initialize() {
        Timber.i("Initialize starting");

        ruleSetLoader.initialize();
        modelManager.initialize();


        Timber.i("Initialize done");
    }

    /**
     * Called on application end.
     */
    protected void terminate() {
        Timber.i("Terminate starting");

        ruleSetLoader.terminate();
        modelManager.terminate();

        Timber.i("Terminate done");
    }

    @Override
    protected Object[] getAppModules() {
        return new Object[]{new OcaraApplicationModule(getApplicationContext())};
    }
}
