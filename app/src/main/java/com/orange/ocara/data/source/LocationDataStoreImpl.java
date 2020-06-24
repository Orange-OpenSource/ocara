/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2020 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.data.source;

public class LocationDataStoreImpl implements LocationSource.LocationDataStore {

    private final LocationSource.LocationCache locationCache;

    /**
     * instantiates.
     *
     * @param locationCache a repository to local database
     */
    public LocationDataStoreImpl(LocationSource.LocationCache locationCache) {
        this.locationCache = locationCache;
    }

    @Override
    public boolean exists(String noImmo) {

        return locationCache.exists(noImmo);
    }

    @Override
    public void init() {

        locationCache.init();
    }
}
