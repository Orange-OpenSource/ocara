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

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

/**
 * Factory that creates instances of {@link OkHttpClient}
 */
public class ApiClientFactory {

    private static final long HTTP_TIMEOUT = 1000;
    private static final int MAX_SIZE_CACHE = 10 * 1024 * 1024;
    private static final int FOUR_WEEKS = 2419200;
    private static final int ONE_MINUTE = 60;

    /**
     * <p>By default, secure connections (using protocols like TLS and HTTPS) from all apps trust the
     * pre-installed system CAs, and apps targeting Android 6.0 (API level 23) and lower also trust
     * the user-added CA store by default. An app can customize its own connections using
     * base-config (for app-wide customization) or domain-config (for per-domain customization).</p>

     * see Android Developer's article about <a href="https://developer.android.com/training/articles/security-config">Network Security Configuration</a>
     *
     *
     * @param context a {@link Context}
     * @param isDebug a boolean
     */
    static OkHttpClient makeOkHttpClient(final Context context, boolean isDebug) {
        Context applicationContext = context.getApplicationContext();

        return new OkHttpClient
                .Builder()
                .cache(cache(context, MAX_SIZE_CACHE))
                .connectTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(responseCacheInterceptor())
                .addInterceptor(offlineResponseCacheInterceptor(applicationContext))
                .addInterceptor(httpLoggingInterceptor(isDebug))
                .build();
    }

    /**
     * Interceptor to cache data and maintain it for a minute.
     *
     * If the same network request is sent within a minute,
     * the response is retrieved from cache.
     */
    private static class ResponseCacheInterceptor implements Interceptor {
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + ONE_MINUTE)
                    .build();
        }
    }

    private static Interceptor responseCacheInterceptor() {
        return new ResponseCacheInterceptor();
    }

    /**
     * Interceptor to cache data and maintain it for four weeks.
     *
     * If the device is offline, stale (at most four weeks old)
     * response is fetched from the cache.
     */
    private static class OfflineResponseCacheInterceptor implements Interceptor {

        private final Context context;

        OfflineResponseCacheInterceptor(Context context) {
            this.context = context;
        }

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            if (!isNetworkAvailable()) {
                request = request.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control",
                                "public, only-if-cached, max-stale=" + FOUR_WEEKS)
                        .build();
            }
            return chain.proceed(request);
        }

        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
            boolean result = activeNetworkInfo != null && activeNetworkInfo.isConnected();
            Timber.i("NetworkIsAvailable=%b", result);
            return result;
        }

    }

    private static Interceptor offlineResponseCacheInterceptor(Context context) {

        return new OfflineResponseCacheInterceptor(context);
    }

    private static Interceptor httpLoggingInterceptor(boolean isDebug) {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
            Timber.tag("Retrofit");
            Timber.d(message);
        });
        loggingInterceptor.setLevel(isDebug ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        return loggingInterceptor;
    }

    private static Cache cache(final Context context, int cacheSize) {

        return new Cache(new File(context.getCacheDir(), context.getPackageName()), cacheSize);
    }
}
