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

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orange.ocara.data.network.remoteContracts.RuleSetApi;
import com.orange.ocara.data.network.remoteContracts.TermsOfUseApi;
import com.orange.ocara.utils.BuildConfig;


import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * ApiClient handles OkHttp client creation
 * <p>
 * TODO this class should be deprecated. it has been replaced by {@link ApiClientFactory} and a bunch of rest services. please remove this class as soon as possible.
 */
public class ApiClient {

    private static final long HTTP_TIMEOUT = 1000;
    private static final int MAX_SIZE_CACHE = 10 * 1024 * 1024;
    private static final int FOUR_WEEKS = 2419200;
    private static final int ONE_MINUTE = 60;
    private static final String DATE_FORMAT_WS = "yyyyMMddHHmmss";

    @SuppressLint("StaticFieldLeak")
    private static volatile ApiClient sInstance;
    private final RuleSetApi mRuleSetApi;
    private final TermsOfUseApi mTermsApi;
    private final OkHttpClient mOkHttpClient;

    /**
     * <p>By default, secure connections (using protocols like TLS and HTTPS) from all apps trust the
     * pre-installed system CAs, and apps targeting Android 6.0 (API level 23) and lower also trust
     * the user-added CA store by default. An app can customize its own connections using
     * base-config (for app-wide customization) or domain-config (for per-domain customization).</p>
     * <p>
     * see Android Developer's article about <a href="https://developer.android.com/training/articles/security-config">Network Security Configuration</a>
     *
     * @param context a {@link Context}
     */
    private ApiClient(final Context context) {
        Context applicationContext = context.getApplicationContext();

        // the parameter OCARA_SERVEUR is valid, then initialize the APIs
        if (Patterns.WEB_URL.matcher(BuildConfig.OCARA_SERVEUR).matches()) {
            mOkHttpClient = new OkHttpClient
                    .Builder()
                    .cache(cache(context, MAX_SIZE_CACHE))
                    .connectTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
                    .addNetworkInterceptor(responseCacheInterceptor())
                    .addInterceptor(offlineResponseCacheInterceptor(applicationContext))
                    .addInterceptor(httpLoggingInterceptor())
                    .build();

            mRuleSetApi = new Retrofit
                    .Builder()
                    .baseUrl(BuildConfig.OCARA_SERVEUR)
                    .addConverterFactory(gsonConverterFactory())
                    .client(mOkHttpClient)
                    .build()
                    .create(RuleSetApi.class);

            mTermsApi = new Retrofit
                    .Builder()
                    .baseUrl(BuildConfig.OCARA_SERVEUR)
                    .addConverterFactory(htmlConverterFactory())
                    .client(mOkHttpClient)
                    .build()
                    .create(TermsOfUseApi.class);
        } else {
            // the parameter OCARA_SERVEUR is not valid, then the APIs are stubbed
            mOkHttpClient = new OkHttpClient();
            mRuleSetApi = (ref, version) -> null;
            mTermsApi = () -> null;
        }
    }

    /**
     * @return a {@link Converter.Factory} that deals with converting response into JSON
     */
    private Converter.Factory gsonConverterFactory() {
        Gson gson = new GsonBuilder()
                .setDateFormat(DATE_FORMAT_WS)
                .serializeNulls()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .create();

        return GsonConverterFactory.create(gson);
    }

    /**
     * @return a {@link Converter.Factory} that deals with converting response into {@link String}
     */
    private Converter.Factory htmlConverterFactory() {
        return new Converter.Factory() {
            @Nullable
            @Override
            public Converter<ResponseBody, String> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
                return value -> value.string();
            }
        };
    }

    /**
     * Get the instance of the singleton
     *
     * @return the instance
     */
    public static ApiClient get() {
        if (sInstance == null) {
            throw new IllegalStateException("Must initialize ApiClient before using get()");
        } else {
            return sInstance;
        }
    }

    /**
     * Initializes ApiClient
     *
     * @param context a context
     */
    public static void init(Context context) {
        if (sInstance == null) {
            synchronized (ApiClient.class) {
                if (sInstance == null) {
                    setApiClient(new ApiClient(context));
                }
            }
        }
    }

    private static void setApiClient(ApiClient instance) {
        sInstance = instance;
    }

    public static RuleSetApi getRuleSetApi() {
        return get().mRuleSetApi;
    }

    public static OkHttpClient getOkHttpClient() {
        return get().mOkHttpClient;
    }

    public static TermsOfUseApi getTermsOfUseApi() {
        return get().mTermsApi;
    }

    /**
     * Interceptor to cache data and maintain it for a minute.
     * <p>
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
     * <p>
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

    private static Interceptor httpLoggingInterceptor() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
            Timber.tag("Retrofit");
            Timber.d(message);
        });
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        return loggingInterceptor;
    }

    private static Cache cache(final Context context, int cacheSize) {

        return new Cache(new File(context.getCacheDir(), context.getPackageName()), cacheSize);
    }

}
