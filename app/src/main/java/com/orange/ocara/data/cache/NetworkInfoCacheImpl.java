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

package com.orange.ocara.data.cache;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.orange.ocara.data.cache.prefs.NetworkPreferences;
import com.orange.ocara.data.source.NetworkInfoSource;


/**
 * default implementation of {@link com.orange.ocara.data.source.NetworkInfoSource.NetworkInfoCache}
 */
public class NetworkInfoCacheImpl implements NetworkInfoSource.NetworkInfoCache {

    private final Context context;

    private final NetworkPreferences prefs;

    private static final Long EXPIRATION_TIME = (long) (10 * 1000);

    NetworkInfoCacheImpl(Context context, NetworkPreferences prefs) {
        this.context = context;
        this.prefs = prefs;
    }

    @Override
    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        long lastUpdateTime = prefs.getLastCacheTime();
        return currentTime - lastUpdateTime > EXPIRATION_TIME;
    }

    @Override
    public boolean isCached() {
        return false;
    }

    @Override
    public boolean isNetworkAvailable() {
        if (isExpired()) {
            reset();
        }
        return prefs.isNetworkAvailable();
    }

    @Override
    public void reset() {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;

        prefs.setNetworkAvailable(activeNetworkInfo != null && activeNetworkInfo.isConnected());
        prefs.setLastCacheTime(System.currentTimeMillis());
    }
}
