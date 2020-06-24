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

import com.orange.ocara.data.net.model.ProfileTypeEntity;
import com.orange.ocara.data.net.model.RulesetCategoryEntity;

import java.util.List;

/** DAO for {@link RulesetCategoryEntity} */
public interface RuleCategoryDao {

    /**
     *
     * @param rulesetId an identifier for a {@link ProfileTypeEntity}
     * @return a bunch of {@link RulesetCategoryEntity}es
     */
    List<RulesetCategoryEntity> findAllByRulesetId(Long rulesetId);
}
