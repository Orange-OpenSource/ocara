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
import com.orange.ocara.data.net.model.RuleEntity;

import java.util.List;

/**
 * default implementation of {@link RuleDao}
 */
public class RuleDaoImpl implements RuleDao {

    @Override
    public List<RuleEntity> findAll(Long rulesetId) {

        return new Select()
                .from(RuleEntity.class)
                .where(RuleEntity.RULESET_DETAILS + " = ?", rulesetId)
                .execute();
    }
}