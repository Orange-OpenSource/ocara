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



import com.orange.ocara.data.source.ImageSource;
import com.orange.ocara.utils.exceptions.ConnectorException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * default implementation of {@link ImageSource.ImageRemote}
 */
public class ImageRemoteImpl implements ImageSource.ImageRemote {

    private String baseUrl= "";
    @Override
    public InputStream get(String filename) {

        //Old baseUrl = BuildConfig.OCARA_SERVEUR + "images/" + filename
        try {
            URL url = new URL(baseUrl + "images/" + filename);

            Request request = new Request.Builder().url(url).build();
            Response response = ApiClient.getOkHttpClient().newCall(request).execute();

            return response.body().byteStream();
        } catch (IOException e) {
            Timber.e(e, "Picture Save Error : SAVE picture error - Do nothing");
            throw ConnectorException.from(e);
        }
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
