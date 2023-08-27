/*
 *
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 *
 */

package com.orange.ocara.mobile.di

import android.content.Context
import com.orange.ocara.data.cache.database.OcaraDB
import com.orange.ocara.data.network.client.ApiClientFactory
import com.orange.ocara.data.network.client.ImageRemoteImpl
import com.orange.ocara.data.network.client.RemoteWebServiceFactory
import com.orange.ocara.data.network.remoteContracts.ImageRemoteApi
import com.orange.ocara.data.network.remoteContracts.RulesetService
import com.orange.ocara.data.network.remoteContracts.RulesetWebServiceRx
import com.orange.ocara.data.source.ImageSource
import com.orange.ocara.data.source.ImageSource.ImageRemote
import com.orange.ocara.data.source.NetworkInfoSource.NetworkInfoCache
import com.orange.ocara.domain.cache.ImageCacheImpl
import com.orange.ocara.domain.cache.NetworkInfoCacheImpl
import com.orange.ocara.domain.cache.file.AssetImageStorageImpl
import com.orange.ocara.domain.cache.file.LocalFileStorage
import com.orange.ocara.domain.cache.file.StorageType
import com.orange.ocara.domain.cache.prefs.DefaultRulesetPreferences
import com.orange.ocara.domain.cache.prefs.DefaultRulesetPreferencesImpl
import com.orange.ocara.domain.cache.prefs.NetworkPreferences
import com.orange.ocara.domain.cache.prefs.NetworkPreferencesImpl
import com.orange.ocara.domain.interactors.ExportAuditTask
import com.orange.ocara.domain.interactors.GetProfileTypeFromRuleSetAsMap
import com.orange.ocara.domain.workers.DemoData
import com.orange.ocara.mobile.BuildConfig
import com.orange.ocara.mobile.workers.DependenciesProvider
import com.orange.ocara.mobile.workers.Mediator
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    public fun provideOcaraDB(@ApplicationContext context: Context): OcaraDB {
        return OcaraDB.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {

        return ApiClientFactory.makeOkHttpClient(context, BuildConfig.DEBUG)
    }

    @Provides
    @Singleton
    fun provideRuleSetWebService(okHttpClient: OkHttpClient): RulesetWebServiceRx {
        return RemoteWebServiceFactory
                .makeInstanceWithRx(okHttpClient, BuildConfig.OCARA_SERVEUR)
                .create(RulesetWebServiceRx::class.java)
    }

    @Provides
    @Singleton
    fun provideRuleSetService(okHttpClient: OkHttpClient): RulesetService {
        return RemoteWebServiceFactory
                .makeInstanceWithRx(okHttpClient, BuildConfig.OCARA_SERVEUR)
                .create(RulesetService::class.java)
    }

    @Provides
    @Singleton
    fun provideImageRemoteApi(okHttpClient: OkHttpClient): ImageRemoteApi {
        return RemoteWebServiceFactory
                .makeInstanceWithRx(okHttpClient, BuildConfig.OCARA_SERVEUR)
                .create(ImageRemoteApi::class.java)
    }

    @Provides
    @Singleton
    fun defaultRulesetPreferences(@ApplicationContext context: Context?): DefaultRulesetPreferences {
        return DefaultRulesetPreferencesImpl(context)
    }

    @Singleton
    @Provides
    fun imageCache(@ApplicationContext rootContext: Context?): ImageSource.ImageCache {
        return ImageCacheImpl(LocalFileStorage(rootContext, StorageType.EXTERNAL_CACHE), AssetImageStorageImpl(rootContext, "images"))
    }

    @Provides
    @Singleton
    fun imageRemote(): ImageRemote {
        return ImageRemoteImpl()
    }


    @Provides
    @Singleton
    fun networkPreferences(@ApplicationContext context: Context?): NetworkPreferences {
        return NetworkPreferencesImpl(context)
    }

    @Provides
    @Singleton
    fun networkInfoCache(@ApplicationContext context: Context?, networkPreferences: NetworkPreferences?): NetworkInfoCache {
        return NetworkInfoCacheImpl(context, networkPreferences)
    }

    @Provides
    @Singleton
    fun provideWorkerDemoData(): DemoData {
        return DemoData()
    }


    @Provides
    @Singleton
    fun provideSomeShit(a: GetProfileTypeFromRuleSetAsMap): Mediator{
        return DependenciesProvider(a)
    }

//    @Singleton
//    @Binds
//    abstract fun provideMediator(mediatorImp: DependenciesProvider): Mediator

}