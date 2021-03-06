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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orange.ocara.BuildConfig;
import com.orange.ocara.tools.LowercaseEnumTypeAdapterFactory;

import java.lang.reflect.Modifier;

import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * a Factory for {@link RulesetService}
 */
class RulesetServiceFactory {

    private static final String DATE_FORMAT_WS = "yyyyMMddHHmmss";

    static RulesetService makeRulesetService(OkHttpClient okHttpClient) {

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(BuildConfig.OCARA_SERVEUR)
                .addConverterFactory(gsonConverterFactory())
                .client(okHttpClient)
                .build();

        return retrofit.create(RulesetService.class);
    }


    /**
     *
     * @return a {@link Converter.Factory} that deals with converting response into JSON
     */
    private static Converter.Factory gsonConverterFactory() {
        Gson gson = new GsonBuilder()
                .setDateFormat(DATE_FORMAT_WS)
                .serializeNulls()
                .registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory())
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .create();

        return GsonConverterFactory.create(gson);
    }
}
