/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.tools.injection;

import android.app.Application;

import dagger.ObjectGraph;

public abstract class DaggerApplication extends Application implements DaggerInjector {

    private ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        inject(this);
    }

    /**
     * @return All application scope module to inject.
     */
    protected abstract Object[] getAppModules();

    @Override
    public void inject(Object object) {
        getObjectGraph().inject(object);
    }

    @Override
    public ObjectGraph getObjectGraph() {
        if (mObjectGraph == null) {
            mObjectGraph = ObjectGraph.create(getAppModules());
        }

        return mObjectGraph;
    }

}
