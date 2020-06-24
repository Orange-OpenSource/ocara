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

package com.orange.ocara.data.cache;

import android.content.Context;
import android.util.Patterns;

import com.orange.ocara.BuildConfig;
import com.orange.ocara.data.cache.db.AuditCloneDaoImpl;
import com.orange.ocara.data.cache.db.AuditQueryDaoImpl;
import com.orange.ocara.data.cache.db.DemoRulesetDaoImpl;
import com.orange.ocara.data.cache.db.DemoSiteDaoImpl;
import com.orange.ocara.data.cache.db.EquipmentDaoImpl;
import com.orange.ocara.data.cache.db.IllustrationDaoImpl;
import com.orange.ocara.data.cache.db.ModelManager;
import com.orange.ocara.data.cache.db.ModelManagerImpl;
import com.orange.ocara.data.cache.db.OnboardingItemInMemoryDaoImpl;
import com.orange.ocara.data.cache.db.ProfileTypeDaoImpl;
import com.orange.ocara.data.cache.db.QuestionDaoImpl;
import com.orange.ocara.data.cache.db.RuleCategoryDaoImpl;
import com.orange.ocara.data.cache.db.RuleDaoImpl;
import com.orange.ocara.data.cache.db.RulesetDaoImpl;
import com.orange.ocara.data.cache.db.SiteDaoImpl;
import com.orange.ocara.data.cache.db.TermsDaoImpl;
import com.orange.ocara.data.cache.file.AssetImageStorageImpl;
import com.orange.ocara.data.cache.file.LocalFileStorage;
import com.orange.ocara.data.cache.file.StorageType;
import com.orange.ocara.data.cache.prefs.DefaultRulesetPreferencesImpl;
import com.orange.ocara.data.cache.prefs.NetworkPreferencesImpl;
import com.orange.ocara.data.cache.prefs.OnboardingStatusPreferencesImpl;
import com.orange.ocara.data.cache.prefs.TermsOfUsePreferencesImpl;
import com.orange.ocara.data.source.AuditSource;
import com.orange.ocara.data.source.EquipmentSource;
import com.orange.ocara.data.source.IllustrationSource;
import com.orange.ocara.data.source.ImageSource;
import com.orange.ocara.data.source.LocationSource;
import com.orange.ocara.data.source.NetworkInfoSource;
import com.orange.ocara.data.source.OnboardingSource;
import com.orange.ocara.data.source.ProfileTypeSource;
import com.orange.ocara.data.source.QuestionSource;
import com.orange.ocara.data.source.RuleCategorySource;
import com.orange.ocara.data.source.RuleSource;
import com.orange.ocara.data.source.RulesetSource;
import com.orange.ocara.data.source.TermsSource;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import timber.log.Timber;

/**
 * a factory for creating connectors from the caching layer
 */
@EBean(scope = EBean.Scope.Singleton)
public class DataCacheConfig {

    private RulesetSource.RulesetCache rulesetCache;

    private TermsSource.TermsCache termsCache;

    private AuditSource.AuditCache auditCache;

    private OnboardingSource.OnboardingCache onboardingCache;

    private LocationSource.LocationCache locationCache;

    private ImageSource.ImageCache imageCache;

    private QuestionSource.QuestionCache questionCache;

    private RuleSource.RuleCache ruleCache;

    private IllustrationSource.IllustrationCache illustrationCache;

    private ProfileTypeSource.ProfileTypeCache profileTypeCache;

    private EquipmentSource.EquipmentCache equipmentCache;

    private RuleCategorySource.RuleCategoryCache ruleCategoryCache;

    private NetworkInfoSource.NetworkInfoCache networkInfoCache;

    @Bean(ModelManagerImpl.class)
    ModelManager modelManager;

    @RootContext
    void setContext(Context rootContext) {

        rulesetCache = new RulesetCacheImpl(
                new RulesetDaoImpl(rootContext),
                new DefaultRulesetPreferencesImpl(rootContext),
                new DemoRulesetDaoImpl(rootContext));

        onboardingCache = new OnboardingCacheImpl(new OnboardingItemInMemoryDaoImpl(rootContext), new OnboardingStatusPreferencesImpl(rootContext));

        auditCache = new AuditCacheImpl(new AuditQueryDaoImpl(), new AuditCloneDaoImpl(modelManager));

        termsCache = new TermsCacheImpl(new TermsDaoImpl(rootContext), new TermsOfUsePreferencesImpl(rootContext));

        locationCache = new LocationCacheImpl(
                new SiteDaoImpl(),
                new DemoSiteDaoImpl(rootContext));

        imageCache = new ImageCacheImpl(new LocalFileStorage(rootContext, StorageType.EXTERNAL_CACHE), new AssetImageStorageImpl(rootContext, "images"));

        ruleCache = new RuleCacheImpl(new RuleDaoImpl());

        questionCache = new QuestionCacheImpl(new QuestionDaoImpl());

        illustrationCache = new IllustrationCacheImpl(new IllustrationDaoImpl());

        equipmentCache = new EquipmentCacheImpl(new EquipmentDaoImpl());

        profileTypeCache = new ProfileTypeCacheImpl(new ProfileTypeDaoImpl());

        ruleCategoryCache = new RuleCategoryCacheImpl(new RuleCategoryDaoImpl());

        // Caching network info is only relevant if networking is correctly configured. Else, this connector is stubbed, in order to provide static data.
        networkInfoCache = isConfigValid() ? new NetworkInfoCacheImpl(rootContext, new NetworkPreferencesImpl(rootContext)) : networkInfoCacheStub();
        networkInfoCache.reset();
    }

    /** @return true if the parameters are correct */
    private static boolean isConfigValid() {
        String url = BuildConfig.OCARA_SERVEUR;
        boolean result = Patterns.WEB_URL.matcher(url).matches();
        Timber.i("Message=Checking remote is available;Param=%s;Result=%b", url, result);
        return result;
    }

    public RulesetSource.RulesetCache rulesetCache() {
        return rulesetCache;
    }

    public TermsSource.TermsCache termsCache() {
        return termsCache;
    }

    public AuditSource.AuditCache auditCache() {
        return auditCache;
    }

    public OnboardingSource.OnboardingCache onboardingCache() {
        return onboardingCache;
    }

    public LocationSource.LocationCache locationCache() {
        return locationCache;
    }

    public ImageSource.ImageCache imageCache() {
        return imageCache;
    }

    public QuestionSource.QuestionCache questionCache() {
        return questionCache;
    }

    public RuleSource.RuleCache ruleCache() {
        return ruleCache;
    }

    public IllustrationSource.IllustrationCache illustrationCache() {
        return illustrationCache;
    }

    public ProfileTypeSource.ProfileTypeCache profileTypeCache() {
        return profileTypeCache;
    }

    public EquipmentSource.EquipmentCache equipmentCache() {
        return equipmentCache;
    }

    public RuleCategorySource.RuleCategoryCache ruleCategoryCache() {
        return ruleCategoryCache;
    }

    public NetworkInfoSource.NetworkInfoCache networkInfoCache() {
        return networkInfoCache;
    }

    /** @return a stub for {@link com.orange.ocara.data.source.NetworkInfoSource.NetworkInfoCache} */
    private NetworkInfoSource.NetworkInfoCache networkInfoCacheStub() {
        return new NetworkInfoSource.NetworkInfoCache() {
            @Override
            public boolean isExpired() {
                return false;
            }

            @Override
            public boolean isCached() {
                return false;
            }

            @Override
            public boolean isNetworkAvailable() {
                return false;
            }

            @Override
            public void reset() {

            }
        };
    }
}
