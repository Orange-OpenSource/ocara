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

package com.orange.ocara.data.cache.prefs;

/**
 * Behaviour of a service that stores and reads network information
 */
public interface NetworkPreferences {

    /**
     * @return the last time data was cached
     */
    Long getLastCacheTime();

    /**
     * Store the last time data was cached
     */
    void setLastCacheTime(long time);

    /**
     *
     * @return true if the device is connected to the network
     */
    boolean isNetworkAvailable();

    /**
     *
     * @param value a boolean
     */
    void setNetworkAvailable(boolean value);
}
