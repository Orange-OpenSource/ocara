
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
package com.orange.ocara;

import android.content.Context;

import com.orange.ocara.data.network.client.ApiClientFactory;
import com.orange.ocara.data.network.client.ImageRemoteImpl;
import com.orange.ocara.data.network.client.RemoteWebServiceFactory;
import com.orange.ocara.data.network.remoteContracts.ImageRemoteApi;
import com.orange.ocara.data.network.remoteContracts.RulesetService;
import com.orange.ocara.data.network.client.RulesetServiceFactory;
import com.orange.ocara.data.network.remoteContracts.RulesetWebServiceRx;
import com.orange.ocara.data.source.ImageSource;
import com.orange.ocara.data.source.NetworkInfoSource;
import com.orange.ocara.data.cache.database.DAOs.EquipmentSubEquipmentDAO;
import com.orange.ocara.data.cache.database.DAOs.QuestionEquipmentDAO;
import com.orange.ocara.data.cache.database.DAOs.RulesetDAO;

import com.orange.ocara.data.cache.database.OcaraDB;
import com.orange.ocara.domain.BuildConfig;
import com.orange.ocara.domain.cache.ImageCacheImpl;
import com.orange.ocara.domain.cache.NetworkInfoCacheImpl;
import com.orange.ocara.domain.cache.file.AssetImageStorageImpl;
import com.orange.ocara.domain.cache.file.LocalFileStorage;
import com.orange.ocara.domain.cache.file.StorageType;
import com.orange.ocara.domain.cache.prefs.DefaultRulesetPreferences;
import com.orange.ocara.domain.cache.prefs.DefaultRulesetPreferencesImpl;
import com.orange.ocara.domain.cache.prefs.NetworkPreferences;
import com.orange.ocara.domain.cache.prefs.NetworkPreferencesImpl;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;

import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;


@InstallIn(SingletonComponent.class)
@Module
public class HiltModule {
    @Provides
    @Singleton
    RulesetService rulesetService(@ApplicationContext Context context) {
        OkHttpClient client = ApiClientFactory.makeOkHttpClient(context, BuildConfig.DEBUG);
        return RulesetServiceFactory.makeRulesetService(client , BuildConfig.OCARA_SERVEUR);
    }
    @Provides
    @Singleton
    ImageRemoteApi imageRemoteApi(@ApplicationContext Context context) {
        OkHttpClient client = ApiClientFactory.makeOkHttpClient(context, BuildConfig.DEBUG);
        return RemoteWebServiceFactory.makeInstanceWithRx(client , BuildConfig.OCARA_SERVEUR)
                .create(ImageRemoteApi.class);
    }

    @Provides
    @Singleton
    RulesetWebServiceRx provideRuleSetWebService(@ApplicationContext Context context){
        OkHttpClient client = ApiClientFactory.makeOkHttpClient(context, BuildConfig.DEBUG);
        return RemoteWebServiceFactory.makeInstanceWithRx(client,BuildConfig.OCARA_SERVEUR)
                .create(RulesetWebServiceRx.class);
    }

    @Singleton
    @Provides
    OcaraDB ocaraDB(@ApplicationContext Context context) {
        return OcaraDB.getInstance(context);
    }

    @Provides
    @Singleton
    RulesetDAO rulesetDAO(OcaraDB ocaraDB) {
        return ocaraDB.rulesetDAO();
    }

    @Provides
    @Singleton
    DefaultRulesetPreferences defaultRulesetPreferences(@ApplicationContext Context context) {
        return new DefaultRulesetPreferencesImpl(context);
    }

    @Singleton
    @Provides
    ImageSource.ImageCache imageCache(@ApplicationContext Context rootContext) {
        return new ImageCacheImpl(new LocalFileStorage(rootContext, StorageType.EXTERNAL_CACHE), new AssetImageStorageImpl(rootContext, "images"));
    }

    @Provides
    @Singleton
    ImageSource.ImageRemote imageRemote() {
        return new ImageRemoteImpl();
    }


    @Provides
    @Singleton
    NetworkPreferences networkPreferences(@ApplicationContext Context context) {
        return new NetworkPreferencesImpl(context);
    }

    @Provides
    @Singleton
    NetworkInfoSource.NetworkInfoCache networkInfoCache(@ApplicationContext Context context, NetworkPreferences networkPreferences) {
        return new NetworkInfoCacheImpl(context, networkPreferences);
    }

    @Provides
    @Singleton
    EquipmentSubEquipmentDAO equipmentSubEquipmentDAO(OcaraDB ocaraDB) {
        return ocaraDB.equipmentSubEquipmentDAO();
    }

    @Provides
    @Singleton
    QuestionEquipmentDAO questionEquipmentDAO(OcaraDB ocaraDB) {
        return ocaraDB.questionEquipmentDAO();
    }
}

