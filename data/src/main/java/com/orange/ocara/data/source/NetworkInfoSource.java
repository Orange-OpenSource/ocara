/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.data.source;

/**
 * Contract for handling network information
 */
public interface NetworkInfoSource {

    /**
     * behaviour of the dedicated local storage
     */
    interface NetworkInfoCache {

        /**
         * @return true if stored data is expired
         */
        boolean isExpired();

        /**
         * @return true if data is stored in local storage
         */
        boolean isCached();

        /**
         * @return true if the device is connected to the internet
         */
        boolean isNetworkAvailable();

        /**
         * clears the local storage
         */
        void reset();
    }
}
