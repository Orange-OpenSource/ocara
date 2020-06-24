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

package com.orange.ocara.business.service.impl;

import android.content.Context;

import androidx.annotation.NonNull;

import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.data.net.RulsetConnector;
import com.orange.ocara.data.net.RulsetConnectorImpl;
import com.orange.ocara.data.net.model.ChainEntity;
import com.orange.ocara.data.net.model.EquipmentCategoryEntity;
import com.orange.ocara.data.net.model.EquipmentEntity;
import com.orange.ocara.data.net.model.IllustrationEntity;
import com.orange.ocara.data.net.model.ProfileTypeEntity;
import com.orange.ocara.data.net.model.QuestionEntity;
import com.orange.ocara.data.net.model.QuestionnaireEntity;
import com.orange.ocara.data.net.model.QuestionnaireGroupEntity;
import com.orange.ocara.data.net.model.RuleEntity;
import com.orange.ocara.data.net.model.RulesetEntity;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import timber.log.Timber;

/**
 * Default implementation of {@link RuleSetService}
 *
 * Service dedicated to retrieving data based on Rulesets data.
 */
@EBean
public class RuleSetServiceImpl implements RuleSetService {

    @RootContext
    Context mContext;

    @Bean(RulsetConnectorImpl.class)
    RulsetConnector mRulsetConnector;

    @NonNull
    @Override
    public RulesetEntity getRuleSet(final String reference, final Integer version, final boolean download) {
        return mRulsetConnector.getRuleSetDetails(reference, version, download);
    }

    @NonNull
    @Override
    public List<EquipmentCategoryEntity> getRulsetObjects(final RulesetEntity ruleSetDetails) {
        List<EquipmentCategoryEntity> categories = new ArrayList<>();
        Timber.d(
                "ServiceMessage=Querying ruleset for categories;RulesetName=%s;RulesetId=%d,QuestionnaireGroupsCount=%d",
                ruleSetDetails.getType(),
                ruleSetDetails.getId(),
                ruleSetDetails.getQuestionnaireGroupDb() != null ? ruleSetDetails.getQuestionnaireGroupDb().size() : 0);

        for (QuestionnaireGroupEntity questionnaireGroup : ruleSetDetails.getQuestionnaireGroupDb()) {
            final EquipmentCategoryEntity category = new EquipmentCategoryEntity();
            category.setName(questionnaireGroup.getName());
            List<EquipmentEntity> objectDescriptions = new ArrayList<>();

            for (String ref : questionnaireGroup.getObjectRef()) {
                objectDescriptions.add(mRulsetConnector.getObjectDescriptionFromRef(ruleSetDetails.getReference(), Integer.valueOf(ruleSetDetails.getVersion()), ref));
            }

            category.setObjects(objectDescriptions);
            Timber.d("ServiceMessage=Retrieving category named %s with %d objects", category.getName(), objectDescriptions.size());
            categories.add(category);
        }
        return categories;
    }

    @NonNull
    @Override
    public List<RulesetEntity> getDownloadedRulesetDetails() {
        return mRulsetConnector.getDownloadedRulesetDetails();
    }

    @NonNull
    @Override
    public EquipmentEntity getObjectDescriptionFromRef(String ruleSetRef, Integer version, final String objectRef) {
        return mRulsetConnector.getObjectDescriptionFromRef(ruleSetRef, version, objectRef);
    }

    /**
     * Retrieves a {@link List} of {@link QuestionEntity}s
     *
     * @param ruleSetRef an identifier for a {@link RulesetEntity}
     * @param version a revision number for a {@link RulesetEntity}
     * @param objectRef an identifier for a {@link EquipmentEntity}
     * @param parentObjectId an identifier for another {@link EquipmentEntity}
     *
     * @return a bunch of {@link QuestionEntity}s that matches the given arguments
     */
    @NonNull
    @Override
    public List<QuestionEntity> getQuestionsFromObjectRef(final String ruleSetRef, Integer version, final String objectRef, final String parentObjectId) {
        Timber.d("QuestionAnswer creating %s %d %s %s", ruleSetRef, version, objectRef, parentObjectId);
        Timber.d(
                "Message=Retrieving questions from object;RulesetRef=%s;RulesetVersion=%d;ObjectRef=%s;ParentObjectId=%s",
                ruleSetRef, version, objectRef, parentObjectId);

        final RulesetEntity ruleset = mRulsetConnector.getRuleSetDetails(ruleSetRef, version, false);
        final EquipmentEntity description = mRulsetConnector.getObjectDescriptionFromRef(ruleSetRef, version, objectRef);

        List<QuestionEntity> questions = new ArrayList<>();
        if (ruleset.containsQuestionnaires(objectRef)) {
            final QuestionnaireEntity questionnaire = ruleset.getQuestionnaire(objectRef);

            Timber.d("Message=Found a questionnaire;QuestionnaireRef=%s;ObjectDescriptionQuestionnaireRef=%s", questionnaire.getReference(), description.getQuestionaireRef());

            boolean questionnaireMatchesDescription = objectRef.equals(parentObjectId) && questionnaire.getReference().equals(description.getQuestionaireRef());
            boolean questionnaireParentMatchesParentDescription = !objectRef.equals(parentObjectId) && questionnaire.getParentObjectRefs().contains(parentObjectId);
            boolean descriptionIsNotDirectlyRelatedToQuestionnaire = description.isCharacteristic();

            if (questionnaireMatchesDescription || questionnaireParentMatchesParentDescription || descriptionIsNotDirectlyRelatedToQuestionnaire) {
                final List<ChainEntity> chains = questionnaire.getChaineDb();

                Timber.d(
                        "Message=Found %d Chains;QuestionnaireRef=%s;RulesetRef=%s;RulesetVersion=%s;ObjectRef=%s;ParentObjectId=%s",
                        chains.size(), questionnaire.getReference(), ruleset.getReference(), ruleset.getVersion(), objectRef, parentObjectId);

                Set<String> questionsReferences = new HashSet<>();
                for (ChainEntity chain : chains) {
                    questionsReferences.addAll(chain.getQuestionsRef());
                }

                Timber.d("Message=Trying to retrieve %d questions", questionsReferences.size());
                QuestionEntity question;
                for (String ref : questionsReferences) {
                    question = mRulsetConnector.getQuestionFromRef(ruleSetRef, version, ref);
                    questions.add(question);
                    Timber.d("Message=Added one question;ObjectRef=%s;QuestionRef=%s;QuestionLabel=%s", objectRef, question.getReference(), question.getLabel());
                }
            }
        } else {
            Timber.d("Message=Found no questionnaire;RulesetRef=%s;RulesetVersion=%s;ObjectDescriptionRef=%s", ruleSetRef, version, objectRef);
        }

        return questions;
    }

    @NonNull
    @Override
    public RuleEntity getRuleFromRef(final String ruleSetRef, final Integer version, final String ruleRef) {
        return mRulsetConnector.getRule(ruleSetRef, version, ruleRef);
    }

    @NonNull
    @Override
    public IllustrationEntity getIllutrationFormRef(final String ruleSetRef, final Integer version, final String ref) {
        return mRulsetConnector.getIllutrationFromRef(ruleSetRef, version, ref);
    }

    @NonNull
    @Override
    public List<IllustrationEntity> getIllutrationsFormRef(final String ruleSetRef, final Integer version, final List<String> refs) {
        final List<IllustrationEntity> illustrations = new ArrayList<>();
        for (String ref : refs) {
            illustrations.add(this.getIllutrationFormRef(ruleSetRef, version, ref));
        }

        return illustrations;
    }

    @NonNull
    @Override
    public Map<String, ProfileTypeEntity> getProfilTypeFormRuleSet(final RulesetEntity ruleSet) {
        Map<String, ProfileTypeEntity> returnValues = new HashMap<>();
        for (ProfileTypeEntity profileType : ruleSet.getProfilTypesDb()) {
            returnValues.put(profileType.getReference(), profileType);
        }
        return returnValues;
    }
}
