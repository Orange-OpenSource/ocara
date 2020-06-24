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

import com.orange.ocara.data.cache.db.RuleDao;
import com.orange.ocara.data.net.model.RuleEntity;
import com.orange.ocara.data.source.RuleSource;

import java.util.List;

/**
 * default implementation of {@link com.orange.ocara.data.source.RuleSource.RuleCache}
 */
public class RuleCacheImpl implements RuleSource.RuleCache {

    private final RuleDao ruleDao;

    public RuleCacheImpl(RuleDao ruleDao) {
        this.ruleDao = ruleDao;
    }

    @Override
    public List<RuleEntity> findAll(Long rulesetId) {
        return ruleDao.findAll(rulesetId);
    }
}
