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

import com.orange.ocara.business.service.QuestionService;
import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.data.net.model.EquipmentWithReference;
import com.orange.ocara.data.net.model.QuestionEntity;
import com.orange.ocara.data.net.model.RulesetEntity;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.List;

/**
 * Default implementation of {@link QuestionService}
 */
@EBean
public class QuestionServiceImpl implements QuestionService {

    /**
     * Target service
     *
     * We use this service because the function it embeds seems over-complicated. May require some
     * refactoring later.
     */
    @Bean(RuleSetServiceImpl.class)
    RuleSetService ruleSetService;

    @Override
    public List<QuestionEntity> retrieveAllQuestionsByRulesetAndEquipment(RulesetEntity ruleset, EquipmentWithReference equipment) {
        return ruleSetService
                .getQuestionsFromObjectRef(ruleset.getReference(), Integer.valueOf(ruleset.getVersion()), equipment.getReference(), equipment.getReference());
    }
}
