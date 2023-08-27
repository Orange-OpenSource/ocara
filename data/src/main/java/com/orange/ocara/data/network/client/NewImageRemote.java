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

package com.orange.ocara.data.network.client;



import com.orange.ocara.utils.BuildConfig;
import com.orange.ocara.utils.exceptions.ConnectorException;

import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;

import okhttp3.Callback;
import okhttp3.Request;
import timber.log.Timber;

public class NewImageRemote{
    @Inject
    NewImageRemote(){}
    public void get(String filename, Callback callback) {

        //Old baseUrl = BuildConfig.OCARA_SERVEUR + "images/" + filename
        try {
            URL url = new URL(BuildConfig.OCARA_SERVEUR + "images/" + filename);

            Request request = new Request.Builder().url(url).build();
            ApiClient.getOkHttpClient().newCall(request).enqueue(callback);
        } catch (IOException e) {
            Timber.e(e, "Picture Save Error : SAVE picture error - Do nothing");
            throw ConnectorException.from(e);
        }
    }
}
