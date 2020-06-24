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

import com.activeandroid.ActiveAndroid;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.model.VersionableModel;
import com.orange.ocara.data.cache.db.DemoRulesetDao;
import com.orange.ocara.data.cache.db.RulesetDao;
import com.orange.ocara.data.cache.prefs.DefaultRulesetPreferences;
import com.orange.ocara.data.net.model.ChainEntity;
import com.orange.ocara.data.net.model.ChainWs;
import com.orange.ocara.data.net.model.EquipmentEntity;
import com.orange.ocara.data.net.model.EquipmentWs;
import com.orange.ocara.data.net.model.IllustrationEntity;
import com.orange.ocara.data.net.model.IllustrationWs;
import com.orange.ocara.data.net.model.ImpactValueEntity;
import com.orange.ocara.data.net.model.ImpactValueWs;
import com.orange.ocara.data.net.model.ProfileTypeEntity;
import com.orange.ocara.data.net.model.ProfileTypeWs;
import com.orange.ocara.data.net.model.QuestionEntity;
import com.orange.ocara.data.net.model.QuestionWs;
import com.orange.ocara.data.net.model.QuestionnaireEntity;
import com.orange.ocara.data.net.model.QuestionnaireGroupEntity;
import com.orange.ocara.data.net.model.QuestionnaireGroupWs;
import com.orange.ocara.data.net.model.QuestionnaireWs;
import com.orange.ocara.data.net.model.RuleEntity;
import com.orange.ocara.data.net.model.RuleImpactEntity;
import com.orange.ocara.data.net.model.RuleImpactWs;
import com.orange.ocara.data.net.model.RuleWs;
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.data.net.model.RulesetWs;
import com.orange.ocara.data.net.model.SubjectEntity;
import com.orange.ocara.data.source.RulesetSource;
import com.orange.ocara.tools.ListUtils;

import java.util.List;

import timber.log.Timber;

import static com.orange.ocara.data.RulesetModelFactory.makeRulesetModel;

/**
 * default implementation of {@link com.orange.ocara.data.source.RulesetSource.RulesetCache}
 */
public class RulesetCacheImpl implements RulesetSource.RulesetCache {

    private final RulesetDao rulesetDao;

    private final DefaultRulesetPreferences defaultRulesetPreferences;

    private final DemoRulesetDao demoRulesetDao;

    private static final Long EXPIRATION_TIME = (long) (60 * 10 * 1000);

    RulesetCacheImpl(RulesetDao rulesetDao, DefaultRulesetPreferences defaultRulesetPreferences, DemoRulesetDao rulesetReader) {
        this.rulesetDao = rulesetDao;
        this.defaultRulesetPreferences = defaultRulesetPreferences;
        this.demoRulesetDao = rulesetReader;
    }

    @Override
    public void saveDefaultRuleset(RulesetModel ruleset) {

        RulesetModel current = defaultRulesetPreferences.retrieveRuleset();
        boolean isDifferent = current == null || !current.getVersionName().equals(ruleset.getVersionName());
        if (isDifferent) {
            Timber.i("CacheMessage=Saving new default ruleset;OldRuleset=%s;NewRuleset=%s", current == null ? "<null>" : current.getVersionName(), ruleset.getVersionName());
            defaultRulesetPreferences.saveRuleset(ruleset);
        } else {
            Timber.d("CacheMessage=No need to change default ruleset", ruleset.getReference());
        }
    }

    @Override
    public RulesetModel findDefaultRuleset() {

        RulesetModel output = null;
        if (defaultRulesetPreferences.checkRulesetExists()) {
            output = defaultRulesetPreferences.retrieveRuleset();
        }
        return output;
    }

    @Override
    public boolean checkDefaultRulesetExists() {
        return findDefaultRuleset() != null;
    }

    @Override
    public RulesetEntity findDemoRuleset() {
        RulesetEntity demo = null;
        if (checkDemoRulesetExists()) {
            RulesetWs tmp = demoRulesetDao.get();
            demo = findOne(tmp.getReference(), tmp.getVersion());
            if (demo == null) {
                demo = save(tmp);
            }
        }
        return demo;
    }

    @Override
    public boolean checkDemoRulesetExists() {
        RulesetWs ruleset = demoRulesetDao.get();

        return ruleset != null && exists(ruleset.getReference(), ruleset.getVersion());
    }

    @Override
    public List<RulesetModel> findAll() {
        List<RulesetModel> output = ListUtils.newArrayList();
        List<RulesetEntity> entities = rulesetDao.findAll();

        for (RulesetEntity item : entities) {
            output.add(makeRulesetModel(item));
        }

        return output;
    }

    @Override
    public RulesetEntity save(RulesetEntity ruleset) {

        ActiveAndroid.beginTransaction();
        RulesetEntity output;
        try {
            output = rulesetDao.save(ruleset);
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
        return output;
    }

    @Override
    public RulesetEntity save(RulesetWs input) {

        ActiveAndroid.beginTransaction();
        RulesetEntity output;
        try {
            // creating the RulesetEntity
            output = new RulesetEntity();

            output.setComment(input.getComment());
            output.setDate(input.getDate());
            output.setLanguage(input.getLanguage());
            output.setReference(input.getReference());
            output.setVersion(input.getVersion().toString());
            output.setAuthorName(input.getUserCredentials() != null ? input.getUserCredentials().getUsername() : null);
            output.setType(input.getType());

            List<ProfileTypeWs> inputProfileTypes = input.getProfileTypes();
            if (!inputProfileTypes.isEmpty()
                    && inputProfileTypes.get(0).getRulesCategories() != null
                    && !inputProfileTypes.get(0).getRulesCategories().isEmpty()) {
                output.setRuleCategoryName(inputProfileTypes.get(0).getRulesCategories().get(0).getName());
            }

            // saving the RulesetEntity. It gives an id to the entity
            output.save();

            // creating and saving the components
            List<IllustrationWs> inputIllustrations = input.getIllustrations();
            for (IllustrationWs item : inputIllustrations) {
                IllustrationEntity.toEntity(item, output).save();
            }

            List<ImpactValueWs> inputImpactValues = input.getImpactValues();
            for (ImpactValueWs item : inputImpactValues) {
                ImpactValueEntity.toEntity(item, output).save();
            }

            List<EquipmentWs> inputEquipments = input.getEquipments();
            for (EquipmentWs item : inputEquipments) {
                EquipmentEntity.toEntity(item, output).save();
            }

            for (ProfileTypeWs item : inputProfileTypes) {
                ProfileTypeEntity.toEntity(item, output).save();
            }

            List<QuestionnaireGroupWs> inputQuestionnaireGroups = input.getQuestionnaireGroups();
            for (QuestionnaireGroupWs item : inputQuestionnaireGroups) {
                QuestionnaireGroupEntity.toEntity(item, output).save();
            }

            List<QuestionnaireWs> inputQuestionnaires = input.getQuestionnaires();
            QuestionnaireEntity questionnaireEntity;
            ChainEntity chainEntity;
            List<ChainEntity> outputChains;
            for (QuestionnaireWs item : inputQuestionnaires) {
                questionnaireEntity = QuestionnaireEntity.toEntity(item, output);
                questionnaireEntity.save();

                outputChains = ListUtils.newArrayList();
                for (ChainWs chainWs : item.getChains()) {
                    chainEntity = ChainEntity.toEntity(chainWs, output, questionnaireEntity);
                    chainEntity.save();
                    outputChains.add(chainEntity);
                }
                questionnaireEntity.setChains(outputChains);
                questionnaireEntity.save();
            }

            List<QuestionWs> inputQuestions = input.getQuestions();
            QuestionEntity questionEntity;
            SubjectEntity subjectEntity;
            for (QuestionWs item : inputQuestions) {
                questionEntity = QuestionEntity.toEntity(item, output);
                questionEntity.save();
                subjectEntity = SubjectEntity.toEntity(item.getSubject());
                subjectEntity.setQuestion(questionEntity);
                subjectEntity.save();
            }

            List<RuleWs> inputRules = input.getRules();
            RuleEntity ruleEntity;
            RuleImpactEntity ruleImpactEntity;
            List<RuleImpactEntity> outputRuleImpacts;
            for (RuleWs item : inputRules) {
                ruleEntity = RuleEntity.toEntity(item, output);
                ruleEntity.save();

                outputRuleImpacts = ListUtils.newArrayList();
                for (RuleImpactWs impactWs : item.getRuleImpacts()) {
                    ruleImpactEntity = RuleImpactEntity.toEntity(impactWs, ruleEntity);
                    ruleImpactEntity.save();
                    outputRuleImpacts.add(ruleImpactEntity);
                }
                ruleEntity.setRuleImpacts(outputRuleImpacts);
                ruleEntity.save();
            }

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
        return output;
    }

    @Override
    public RulesetEntity findOne(Long rulesetId) {
        return rulesetDao.findOne(rulesetId);
    }

    @Override
    public RulesetEntity findOne(String reference, Integer version) {
        return rulesetDao.findOne(reference, version);
    }

    @Override
    public RulesetEntity findLast(String reference) {
        return rulesetDao.findLast(reference);
    }

    @Override
    public boolean exists(String reference, Integer version) {
        return rulesetDao.exists(reference, version);
    }

    @Override
    public boolean exists(VersionableModel ruleset) {
        return exists(ruleset.getReference(), Integer.parseInt(ruleset.getVersion()));
    }

    @Override
    public boolean exists(String reference) {
        return rulesetDao.exists(reference);
    }

    @Override
    public void init() {

        RulesetWs ruleset = demoRulesetDao.get();

        if (!exists(ruleset.getReference(), ruleset.getVersion())) {
            Timber.i("Message=Initializing the default ruleset;RulesetRef=%s;RulesetVersion=%s", ruleset.getReference(), ruleset.getVersion());
            save(ruleset);
        }
    }

    @Override
    public boolean isCached() {
        return defaultRulesetPreferences.findAll().isEmpty();
    }

    @Override
    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        long lastUpdateTime = this.defaultRulesetPreferences.getLastCacheTime();
        return currentTime - lastUpdateTime > EXPIRATION_TIME;
    }

    @Override
    public void resetLastCacheTime() {
        this.defaultRulesetPreferences.setLastCacheTime(System.currentTimeMillis());
    }
}
