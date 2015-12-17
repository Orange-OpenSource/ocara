/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.tools.injection;

import android.app.IntentService;

public abstract class DaggerIntentService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DaggerIntentService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerHelper.inject(this, getApplicationContext());
    }

}
