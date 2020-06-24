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
import com.orange.ocara.data.net.model.RuleEntity;

import java.util.List;

/**
 * Behaviour of a local repository for {@link QuestionEntity}s
 */
public interface RuleDao {

    /**
     *
     * @param rulesetId an identifier for a {@link com.orange.ocara.data.net.model.Ruleset}
     * @return a list of {@link QuestionEntity}s
     */
    List<RuleEntity> findAll(Long rulesetId);
}
