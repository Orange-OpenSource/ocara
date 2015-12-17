/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.tools.injection;

import dagger.ObjectGraph;

public final class DaggerHelper {

    /**
     * Private constructor.
     */
    private DaggerHelper() {
    }

    /**
     * To inject on object into a DaggerInjector instance object.
     *
     * @param object Object to inject
     * @param into   DaggerInjector object type
     */
    public static void inject(Object object, Object into) {

        if (into instanceof DaggerInjector) {
            ((DaggerInjector) into).inject(object);
        }
    }

    /**
     * To get the ObjectGraph from a DaggerInjector object
     *
     * @param from DaggerInjector object
     * @return ObjectGraph if found, null otherwise
     */
    public static ObjectGraph getObjectGraph(Object from) {
        if (from instanceof DaggerInjector) {
            return ((DaggerInjector) from).getObjectGraph();
        }

        return null;
    }
}
