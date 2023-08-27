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

package com.orange.ocara.domain.cache.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * default implementation of {@link NetworkPreferences}
 */
public class NetworkPreferencesImpl implements NetworkPreferences {

    private static final String PREF_BUFFER_PACKAGE_NAME = "com.orange.ocara.data.cache.prefs";

    private static final boolean DEFAULT_VALUE = false;

    private static final String PREF_KEY_NETWORK_AVAILABLE = "network_available";

    private static final String PREF_KEY_LAST_CACHE = "network_available_last_cache";

    /**
     * a reader for {@link SharedPreferences}
     */
    private final SharedPreferences reader;

    @Inject
    public NetworkPreferencesImpl(Context context) {
        this.reader = context.getSharedPreferences(PREF_BUFFER_PACKAGE_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void setLastCacheTime(long time) {
        reader
                .edit()
                .putLong(PREF_KEY_LAST_CACHE, time)
                .apply();
    }

    @Override
    public Long getLastCacheTime() {
        return reader.getLong(PREF_KEY_LAST_CACHE, 0);
    }

    @Override
    public boolean isNetworkAvailable() {
        return reader.getBoolean(PREF_KEY_NETWORK_AVAILABLE, DEFAULT_VALUE);
    }

    @Override
    public void setNetworkAvailable(boolean value) {
        reader
                .edit()
                .putBoolean(PREF_KEY_NETWORK_AVAILABLE, value)
                .apply();
    }
}
