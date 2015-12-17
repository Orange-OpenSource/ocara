/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.tools.injection;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import dagger.ObjectGraph;

public abstract class DaggerActivity extends AppCompatActivity implements DaggerInjector {

    private ObjectGraph mActivityGraph = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ObjectGraph og = DaggerHelper.getObjectGraph(getApplicationContext());

        if (og != null) {
            mActivityGraph = og.plus(getActivityModules());
            mActivityGraph.inject(this);
        }
    }

    @Override
    protected void onDestroy() {
        // Eagerly clear the reference to the activity graph to allow it to be garbage
        // collected as soon as possible.
        mActivityGraph = null;
        super.onDestroy();
    }

    @Override
    public void inject(Object object) {
        if (mActivityGraph != null) {
            mActivityGraph.inject(object);
        }
    }

    @Override
    public ObjectGraph getObjectGraph() {
        return mActivityGraph;
    }

    /**
     * @return All Activity modules to inject.
     */
    protected abstract Object[] getActivityModules();
}
