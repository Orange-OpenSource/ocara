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

package com.orange.ocara.data;

import com.orange.ocara.business.repository.AuditRepository;
import com.orange.ocara.business.repository.ExplanationRepository;
import com.orange.ocara.business.repository.HealthRepository;
import com.orange.ocara.business.repository.LocationRepository;
import com.orange.ocara.business.repository.OnboardingRepository;
import com.orange.ocara.business.repository.QuestionRepository;
import com.orange.ocara.business.repository.RuleRepository;
import com.orange.ocara.business.repository.RulesetRepository;
import com.orange.ocara.business.repository.TermsRepository;
import com.orange.ocara.data.cache.DataCacheConfig;
import com.orange.ocara.data.net.NetConfig;
import com.orange.ocara.data.source.AuditDataStoreImpl;
import com.orange.ocara.data.source.EquipmentDataStoreImpl;
import com.orange.ocara.data.source.EquipmentSource;
import com.orange.ocara.data.source.IllustrationDataStoreImpl;
import com.orange.ocara.data.source.IllustrationSource;
import com.orange.ocara.data.source.ImageDataStoreImpl;
import com.orange.ocara.data.source.ImageSource;
import com.orange.ocara.data.source.LocationDataStoreImpl;
import com.orange.ocara.data.source.OnboardingDataStoreImpl;
import com.orange.ocara.data.source.ProfileTypeDataStoreImpl;
import com.orange.ocara.data.source.ProfileTypeSource;
import com.orange.ocara.data.source.QuestionDataStoreImpl;
import com.orange.ocara.data.source.QuestionSource;
import com.orange.ocara.data.source.RuleDataStoreImpl;
import com.orange.ocara.data.source.RulesetDataStoreImpl;
import com.orange.ocara.data.source.RulesetSource;
import com.orange.ocara.data.source.TermsDataStoreImpl;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

/**
 * Factory for the repositories
 * <p>
 * Should only retrieve implementations of interfaces defined in the following package :
 * - com.orange.ocara.business.repository
 * <p>
 * These implementations should be stored in the following package :
 * - com.orange.ocara.data.repository
 */
@EBean(scope = EBean.Scope.Singleton)
public class DataConfig {

    @Bean(DataCacheConfig.class)
    DataCacheConfig dataCacheConfig;

    @Bean(NetConfig.class)
    NetConfig netConfig;

    private LocationRepository locationRepository;

    private RulesetRepository rulesetRepository;

    private TermsRepository termsRepository;

    private OnboardingRepository onboardingRepository;

    private AuditRepository auditRepository;

    private ExplanationRepository explanationRepository;

    private QuestionRepository questionRepository;

    private RuleRepository ruleRepository;

    private HealthRepository healthRepository;

    @AfterInject
    void afterInject() {

        healthRepository = new HealthDataRepository(netConfig.isConnected());

        ImageSource.ImageDataStore imageDataStore = new ImageDataStoreImpl(
                dataCacheConfig.imageCache(),
                netConfig.imageRemote());

        RulesetSource.RulesetDataStore rulesetDataStore = new RulesetDataStoreImpl(
                dataCacheConfig.rulesetCache(),
                netConfig.rulesetRemote(),
                dataCacheConfig.imageCache(),
                netConfig.imageRemote());


        ProfileTypeSource.ProfileTypeDataStore profileTypeDataStore = new ProfileTypeDataStoreImpl(dataCacheConfig.rulesetCache(), dataCacheConfig.profileTypeCache(), dataCacheConfig.imageCache());

        EquipmentSource.EquipmentDataStore equipmentDataStore = new EquipmentDataStoreImpl(dataCacheConfig.rulesetCache(), dataCacheConfig.equipmentCache(), dataCacheConfig.imageCache());

        IllustrationSource.IllustrationDataStore illustrationDataStore = new IllustrationDataStoreImpl(dataCacheConfig.rulesetCache(),  dataCacheConfig.illustrationCache(), dataCacheConfig.imageCache());

        rulesetRepository = new RulesetDataRepository(rulesetDataStore, equipmentDataStore, illustrationDataStore, profileTypeDataStore, dataCacheConfig.networkInfoCache());

        termsRepository = new TermsDataRepository(
                new TermsDataStoreImpl(
                        dataCacheConfig.termsCache(),
                        netConfig.termsRemote()));


        locationRepository = new LocationDataRepository(new LocationDataStoreImpl(dataCacheConfig.locationCache()));

        explanationRepository = new ExplanationDataRepository(illustrationDataStore, imageDataStore);

        onboardingRepository = new OnboardingItemDataRepository(new OnboardingDataStoreImpl(dataCacheConfig.onboardingCache()));

        auditRepository = new AuditDataRepository(new AuditDataStoreImpl(dataCacheConfig.auditCache()));

        QuestionSource.QuestionDataStore questionDataStore = new QuestionDataStoreImpl(dataCacheConfig.questionCache());
        ruleRepository = new RuleDataRepository(new RuleDataStoreImpl(dataCacheConfig.ruleCache()), questionDataStore);

        questionRepository = new QuestionDataRepository(questionDataStore);
    }

    /**
     * @return a {@link RulesetRepository}
     */
    public RulesetRepository rulesetRepository() {
        return rulesetRepository;
    }

    /**
     * @return a {@link TermsRepository}
     */
    public TermsRepository termsRepository() {
        return termsRepository;
    }

    /**
     * @return an instance of {@link LocationRepository}
     */
    public LocationRepository locationRepository() {
        return locationRepository;
    }

    /**
     * @return an instance of {@link OnboardingRepository}
     */
    public OnboardingRepository onboardingRepository() {
        return onboardingRepository;
    }

    /**
     * @return an instance of {@link AuditRepository}
     */
    public AuditRepository auditRepository() {
        return auditRepository;
    }

    /**
     * @return an instance of {@link ExplanationRepository}
     */
    public ExplanationRepository explanationRepository() {
        return explanationRepository;
    }

    /**
     * @return an instance of {@link QuestionRepository}
     */
    public QuestionRepository questionRepository() {
        return questionRepository;
    }

    /**
     * @return an instance of {@link RuleRepository}
     */
    public RuleRepository ruleRepository() {
        return ruleRepository;
    }

    /**
     *
     * @return an instance of {@link HealthRepository}
     */
    public HealthRepository healthRepository() {
        return healthRepository;
    }
}
