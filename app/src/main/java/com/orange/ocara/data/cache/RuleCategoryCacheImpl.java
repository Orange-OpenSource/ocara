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
package com.orange.ocara.data.cache;

import com.orange.ocara.data.cache.db.RuleCategoryDao;
import com.orange.ocara.data.net.model.RulesetCategoryEntity;
import com.orange.ocara.data.source.RuleCategorySource;

import java.util.List;

/** default implementation of {@link com.orange.ocara.data.source.RuleCategorySource.RuleCategoryCache} */
public class RuleCategoryCacheImpl implements RuleCategorySource.RuleCategoryCache {

    private final RuleCategoryDao dao;

    public RuleCategoryCacheImpl(RuleCategoryDao dao) {
        this.dao = dao;
    }

    @Override
    public List<RulesetCategoryEntity> findAllByRulesetId(Long rulesetId) {
        return dao.findAllByRulesetId(rulesetId);
    }
}
