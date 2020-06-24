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

import com.activeandroid.query.Select;
import com.orange.ocara.data.net.model.ProfileTypeEntity;
import com.orange.ocara.data.net.model.RulesetCategoryEntity;
import com.orange.ocara.tools.ListUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * default implementation of {@link RuleCategoryDao}
 */
public class RuleCategoryDaoImpl implements RuleCategoryDao {

    @Override
    public List<RulesetCategoryEntity> findAllByRulesetId(Long rulesetId) {
        List<ProfileTypeEntity> profileTypes = new Select()
                .from(ProfileTypeEntity.class)
                .where(ProfileTypeEntity.DETAILS + " = ?", rulesetId)
                .execute();

        Set<RulesetCategoryEntity> output = new HashSet<>();

        if (profileTypes != null && !profileTypes.isEmpty()) {
            for (ProfileTypeEntity type : profileTypes) {
                List<RulesetCategoryEntity> items = new Select()
                        .from(RulesetCategoryEntity.class)
                        .where(RulesetCategoryEntity.PROFILTYPE + " = ?", type.getId())
                        .execute();

                output.addAll(items);
            }
        }
        return ListUtils.newArrayList(output);
    }
}
