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
import com.orange.ocara.data.net.model.QuestionnaireEntity;
import com.orange.ocara.data.net.model.RulesetById;
import com.orange.ocara.data.net.model.WithReference;

/**
 * Behaviour of a local repository for {@link QuestionnaireEntity}s
 */
public interface QuestionnaireDao {

    /**
     * Retrieves a {@link QuestionnaireEntity}
     * @param questionnaireId an identifier
     * @return a {@link QuestionEntity}, if one is matching the id. null, if not
     */
    QuestionnaireEntity findOne(Long questionnaireId);

    /**
     * Retrieves a {@link QuestionnaireEntity}
     * @param ruleset an identifier
     * @param objectRef an identifier
     * @return a {@link QuestionnaireEntity}, if one is matching the arguments. null, if not
     */
    QuestionnaireEntity findOne(RulesetById ruleset, WithReference objectRef);

    /**
     * Check if questionnaires are matching the arguments
     *
     * @param ruleset an identifier for a {@link com.orange.ocara.data.net.model.Ruleset}
     * @param objectRef an identifier
     * @return true if at least one is matching the arguments. false, if not.
     */
    boolean exists(RulesetById ruleset, WithReference objectRef);
}
