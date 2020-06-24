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

import com.orange.ocara.data.cache.db.IllustrationDao;
import com.orange.ocara.data.net.model.Explanation;
import com.orange.ocara.data.source.IllustrationSource;
import com.orange.ocara.tools.ListUtils;

import java.util.List;

/** default implementation of {@link com.orange.ocara.data.source.IllustrationSource.IllustrationCache} */
public class IllustrationCacheImpl implements IllustrationSource.IllustrationCache {

    private final IllustrationDao illustrationDao;

    IllustrationCacheImpl(IllustrationDao illustrationDao) {
        this.illustrationDao = illustrationDao;
    }

    @Override
    public List<Explanation> findAllByRuleId(Long ruleId) {
        return ListUtils.newArrayList(illustrationDao.findAllByRuleId(ruleId));
    }

    @Override
    public List<Explanation> findAllByRulesetId(Long rulesetId) {
        return ListUtils.newArrayList(illustrationDao.findAllByRulesetId(rulesetId));
    }
}
