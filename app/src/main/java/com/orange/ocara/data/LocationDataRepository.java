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

package com.orange.ocara.data;

import com.orange.ocara.business.repository.LocationRepository;
import com.orange.ocara.data.source.LocationSource;

import lombok.RequiredArgsConstructor;

/** default implementation of {@link LocationRepository} */
@RequiredArgsConstructor
public class LocationDataRepository implements LocationRepository {

    private final LocationSource.LocationDataStore dataStore;

    @Override
    public boolean exists(String noImmo) {
        return dataStore.exists(noImmo);
    }

    @Override
    public void init() {
        dataStore.init();
    }
}
