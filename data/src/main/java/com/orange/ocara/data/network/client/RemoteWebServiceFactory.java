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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orange.ocara.utils.LowercaseEnumTypeAdapterFactory;

import java.lang.reflect.Modifier;

import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteWebServiceFactory {

    private static final String DATE_FORMAT_WS = "yyyyMMddHHmmss";

    public static Retrofit makeInstance(OkHttpClient okHttpClient, String baseUrl) {
        return new Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(gsonConverterFactory())
                .client(okHttpClient)
                .build();
    }

    public static Retrofit makeInstanceWithRx(OkHttpClient okHttpClient, String baseUrl) {
        return new Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(gsonConverterFactory())
                .client(okHttpClient)
                .build();
    }


    /**
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
