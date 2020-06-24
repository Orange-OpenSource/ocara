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

import com.orange.ocara.data.net.model.EquipmentEntity;
import com.orange.ocara.data.net.model.IllustrationEntity;
import com.orange.ocara.data.net.model.QuestionEntity;
import com.orange.ocara.data.net.model.RuleEntity;
import com.orange.ocara.data.net.model.RulesetEntity;

import java.util.List;

public interface RulsetConnector {

    RulesetEntity getRuleSetDetails(String reference, Integer version, boolean download);

    QuestionEntity getQuestionFromRef(String ruleSetRef, Integer version, String reference);

    EquipmentEntity getObjectDescriptionFromRef(String ruleSetRef, Integer version, String reference);

    RuleEntity getRule(final String ruleSetRef, final Integer version, String ruleRef);

    List<RulesetEntity> getDownloadedRulesetDetails();

    IllustrationEntity getIllutrationFromRef(final String ruleSetRef, final Integer version, String ref);
}
