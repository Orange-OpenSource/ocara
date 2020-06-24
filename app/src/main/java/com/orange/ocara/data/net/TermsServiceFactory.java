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

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.annotation.Nullable;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * a Factory for {@link TermsService}
 */
class TermsServiceFactory {

    static TermsService makeTermsService(OkHttpClient okHttpClient) {

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(BuildConfig.OCARA_SERVEUR)
                .addConverterFactory(htmlConverterFactory())
                .client(okHttpClient)
                .build();

        return retrofit.create(TermsService.class);
    }

    /**
     *
     * @return a {@link Converter.Factory} that deals with converting response into {@link String}
     */
    private static Converter.Factory htmlConverterFactory() {
        return new Converter.Factory() {
            @Nullable
            @Override
            public Converter<ResponseBody, String> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
                return value -> value.string();
            }
        };
    }
}
