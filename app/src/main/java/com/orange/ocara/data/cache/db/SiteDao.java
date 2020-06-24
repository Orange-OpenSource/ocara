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

package com.orange.ocara.data.cache.db;

import com.orange.ocara.business.model.SiteModel;
import com.orange.ocara.data.cache.model.SiteEntity;

/**
 * description of a DAO for {@link SiteEntity}s
 */
public interface SiteDao {

    /**
     *  Checks if a {@link SiteModel} matches a {@link String} in database
     *
     * @param noImmo a {@link String}
     * @return true if at least one entity matches the given argument
     */
    boolean exists(String noImmo);

    /**
     * Checks if a {@link SiteModel} exists in database
     *
     * @param location a {@link SiteModel}
     * @return true if at least one entity matches the given argument
     */
    boolean exists(SiteModel location);

    /**
     * Persists an item
     *
     * @param location a {@link SiteModel} to persist
     * @return the persisted location
     */
    SiteModel save(SiteModel location);

    /**
     * Persists a bunch of items
     *
     * @param items a bunch of {@link SiteModel}s
     * @return the persisted elements
     */
    Iterable<SiteModel> save(Iterable<SiteModel> items);

    int count();
}
