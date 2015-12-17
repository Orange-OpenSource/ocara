/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara;

import com.activeandroid.ActiveAndroid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestOcaraApplication extends OcaraApplication {

    @Override
    public void onCreate() {
        ActiveAndroid.initialize(this, true);

        super.onCreate();
    }

    @Override
    public void onTerminate() {
        ActiveAndroid.dispose();

        super.onTerminate();
    }

    @Override
    protected Object[] getAppModules() {
        List<Object> modules = new ArrayList<Object>();
        modules.addAll(Arrays.asList(super.getAppModules()));
        modules.add(new OcaraApplicationModuleMock());
        return modules.toArray();
    }

}
