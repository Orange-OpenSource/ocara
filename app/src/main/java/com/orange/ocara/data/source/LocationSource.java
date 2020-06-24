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

import com.orange.ocara.business.model.SiteModel;

/** contract between a data store that exposes sites (aka locations) and repositories that handle them locally */
public interface LocationSource {

    /**
     * description of the repository that deals with local database
     */
    interface LocationCache {
        /**
         * check if a location exists
         *
         * @param noImmo an identifier for locations
         * @return true if the repository contains at least one location that matches the argument
         */
        boolean exists(String noImmo);

        /**
         * persists an item
         *
         * @param location a {@link SiteModel} to persist
         * @return the persisted location
         */
        SiteModel save(SiteModel location);

        /**
         * persists a bunch of items
         *
         * @param items a bunch of {@link SiteModel}s
         * @return the persisted items
         */
        Iterable<SiteModel> save(Iterable<SiteModel> items);

        /**
         *
         * @return the size of the repository
         */
        int count();

        /**
         * initializes the content
         */
        void init();
    }

    /**
     * description of the repository
     */
    interface LocationDataStore {

        /**
         * checks if a location exists
         *
         * @param noImmo an identifier for locations
         * @return true if the repository contains at least one location that matches the argument
         */
        boolean exists(String noImmo);

        /**
         * initializes the store
         */
        void init();
    }
}
