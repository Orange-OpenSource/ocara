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

import com.orange.ocara.data.net.model.EquipmentWithReference;
import com.orange.ocara.data.net.model.QuestionEntity;
import com.orange.ocara.data.net.model.RulesetByReferenceAndVersion;
import com.orange.ocara.data.net.model.RulesetEntity;

import java.util.List;

/**
 * Behaviour of a service that retrieves {@link QuestionEntity}s
 */
public interface QuestionService {

    /**
     * retrieves a bunch of questions
     *
     * @param ruleset an implementation of {@link RulesetByReferenceAndVersion}
     * @param equipment an implementation of {@link EquipmentWithReference}
     * @return a {@link List} of {@link QuestionEntity}s
     */
    List<QuestionEntity> retrieveAllQuestionsByRulesetAndEquipment(RulesetEntity ruleset, EquipmentWithReference equipment);
}
