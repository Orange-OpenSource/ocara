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

package com.orange.ocara.data.source;

import com.orange.ocara.data.net.model.RuleEntity;

import java.util.List;

/** default implementation of RuleSource.RuleDataStore */
public class RuleDataStoreImpl implements RuleSource.RuleDataStore {

    private final RuleSource.RuleCache ruleCache;

    public RuleDataStoreImpl(RuleSource.RuleCache ruleCache) {
        this.ruleCache = ruleCache;
    }

    @Override
    public List<RuleEntity> findAll(Long rulesetId) {
        return ruleCache.findAll(rulesetId);
    }
}
