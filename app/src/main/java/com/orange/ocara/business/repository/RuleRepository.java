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

import com.orange.ocara.business.model.RuleModel;
import com.orange.ocara.data.net.model.RuleEntity;

import java.util.List;

/**
 * description of a repository for {@link RuleEntity}s
 */
public interface RuleRepository {

    /**
     *
     * @param rulesetId an identifier for a {@link com.orange.ocara.data.net.model.Ruleset}
     * @param equipmentId an identifier for an {@link com.orange.ocara.data.net.model.Equipment}
     * @return a bunch of {@link RuleEntity}s
     */
    List<RuleModel> findAll(long rulesetId, long equipmentId);
}
