/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model;

import java.util.List;
import java.util.Set;

public interface SitesLoader {

    /**
     * To initialize the component.
     */
    void initialize();

    /**
     * To terminate the component.
     */
    void terminate();


    /**
     * Returns the set of uninstalled site packages on the device
     *
     * @return the set of uninstalled site packages on the device
     */
    Set<String> getUninstalledSitePackages();

    List<Site> installSitePackage(String packageName);


}
