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

package com.orange.ocara.data.net;

import com.orange.ocara.BuildConfig;
import com.orange.ocara.data.common.ConnectorException;
import com.orange.ocara.data.net.client.ApiClient;
import com.orange.ocara.data.source.ImageSource.ImageRemote;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * default implementation of {@link ImageRemote}
 */
public class ImageRemoteImpl implements ImageRemote {

    @Override
    public InputStream get(String filename) {

        try {
            URL url = new URL(BuildConfig.OCARA_SERVEUR + "images/" + filename);

            Request request = new Request.Builder().url(url).build();
            Response response = ApiClient.getOkHttpClient().newCall(request).execute();

            return response.body().byteStream();
        } catch (IOException e) {
            Timber.e(e, "Picture Save Error : SAVE picture error - Do nothing");
            throw ConnectorException.from(e);
        }
    }
}
