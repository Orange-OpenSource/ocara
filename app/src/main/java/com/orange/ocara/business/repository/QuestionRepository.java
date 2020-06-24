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

package com.orange.ocara.business.repository;

import com.orange.ocara.business.model.RuleGroupModel;
import com.orange.ocara.data.net.model.QuestionEntity;

import java.util.List;

/**
 * Behaviour of a repository that handles queries for {@link QuestionEntity}s
 */
public interface QuestionRepository {

    /**
     * Retrieves a bunch of questions
     *
     * @param rulesetId an identifier for a {@link com.orange.ocara.data.net.model.Ruleset}
     * @param equipmentId an identifier for a {@link com.orange.ocara.data.net.model.Equipment}
     * @return a {@link List} of {@link QuestionEntity}s
     */
    List<RuleGroupModel> findAll(Long rulesetId, Long equipmentId);
}
