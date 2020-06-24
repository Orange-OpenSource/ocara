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

package com.orange.ocara.business.service;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.orange.ocara.data.net.model.EquipmentCategoryEntity;
import com.orange.ocara.data.net.model.EquipmentEntity;
import com.orange.ocara.data.net.model.IllustrationEntity;
import com.orange.ocara.data.net.model.ProfileTypeEntity;
import com.orange.ocara.data.net.model.QuestionEntity;
import com.orange.ocara.data.net.model.RuleEntity;
import com.orange.ocara.data.net.model.RulesetEntity;

import java.util.List;
import java.util.Map;


public interface RuleSetService {

    @WorkerThread
    @CheckResult
    @NonNull
    RulesetEntity getRuleSet(String reference, Integer version, boolean download);

    @WorkerThread
    @CheckResult
    @NonNull
    List<EquipmentCategoryEntity> getRulsetObjects(RulesetEntity ruleSetDetails);

    @WorkerThread
    @CheckResult
    @NonNull
    List<RulesetEntity> getDownloadedRulesetDetails();

    @WorkerThread
    @CheckResult
    @NonNull
    EquipmentEntity getObjectDescriptionFromRef(String ruleSetRef, Integer version, String objectRef);

    @WorkerThread
    @CheckResult
    @NonNull
    List<QuestionEntity> getQuestionsFromObjectRef(String ruleSetRef, Integer version, String objectRef, String parentObjectId);

    @WorkerThread
    @CheckResult
    @NonNull
    RuleEntity getRuleFromRef(final String ruleSetRef, Integer version, String ruleRef);

    @WorkerThread
    @CheckResult
    IllustrationEntity getIllutrationFormRef(final String ruleSetRef, final  Integer version, final String ref);

    @WorkerThread
    @CheckResult
    @NonNull
    List<IllustrationEntity> getIllutrationsFormRef(final String ruleSetRef, final Integer version, List<String> refs);

    @WorkerThread
    @CheckResult
    @NonNull
    Map<String, ProfileTypeEntity> getProfilTypeFormRuleSet(RulesetEntity ruleSet);
}

