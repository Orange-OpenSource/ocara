/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.tools.injection;

import dagger.ObjectGraph;

public interface DaggerInjector {

    /**
     * To inject an object.
     *
     * @param object Object to inject
     */
    void inject(Object object);

    /**
     * To get the ObjectGraph.
     *
     * @return ObjectGraph
     */
    ObjectGraph getObjectGraph();
}
