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

package com.orange.ocara.data.net.model;

import java.io.Serializable;
import java.util.List;

/**
 * Behaviour of a model of ruleset
 *
 * Extends {@link RulesetById} and {@link RulesetByReferenceAndVersion}, because these are the 2 ways a ruleset can be identified
 * Extends {@link Serializable}, so that we can use the annotation {@link org.androidannotations.annotations.Extra} in some @{@link org.androidannotations.annotations.EActivity}
 */
public interface Ruleset extends RulesetByReferenceAndVersion, Serializable {

    String getType();

    List<QuestionnaireGroupEntity> getQuestionnaireGroupDb();

    List<EquipmentEntity> getObjectDescriptionsDb();

    List<IllustrationEntity> getIllustrationsDb();

    List<ImpactValueEntity> getImpactValuesDb();

    List<QuestionEntity> getQuestionsDb();

    List<ProfileTypeEntity> getProfilTypesDb();

    RuleEntity getRule(String ruleRef);

    AuthorEntity getUserCredentials();

    String getComment();

    String getDate();

    String getAuthorName();

    String getRuleCategoryName();
}
