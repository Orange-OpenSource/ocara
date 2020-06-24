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

package com.orange.ocara.data.cache.db;

import com.orange.ocara.data.net.model.QuestionEntity;

import java.util.List;

/**
 * Behaviour of a local repository for {@link QuestionEntity}s
 */
public interface QuestionDao {

    /**
     * Retrieves a {@link QuestionEntity}
     * @param questionId an identifier
     * @return a {@link QuestionEntity}, if one is matching the id. null, if not
     */
    QuestionEntity findOne(Long questionId);

    /**
     * Retrieves a {@link QuestionEntity}
     * @param rulesetId an identifier
     * @param reference an identifier
     * @return a {@link QuestionEntity}, if one is matching the arguments. null, if not
     */
    QuestionEntity findOne(Long rulesetId, String reference);

    /**
     * Check if questions are matching the arguments
     *
     * @param rulesetId an identifier for a {@link com.orange.ocara.data.net.model.Ruleset}
     * @param reference an identifier
     * @return true if at least one is matching the arguments. false, if not.
     */
    boolean exists(Long rulesetId, String reference);

    /**
     * Check if questions are matching the arguments
     *
     * @param rulesetId an identifier for a {@link com.orange.ocara.data.net.model.Ruleset}
     * @param references a bunch of identifiers
     * @return a list of {@link QuestionEntity}s
     */
    List<QuestionEntity> findAll(Long rulesetId, List<String> references);

    /**
     *
     * @param rulesetId an identifier for a {@link com.orange.ocara.data.net.model.Ruleset}
     * @param equipmentId an identifier for a {@link com.orange.ocara.data.net.model.Equipment}
     * @return a list of {@link QuestionEntity}s
     */
    List<QuestionEntity> findAll(Long rulesetId, Long equipmentId);
}
