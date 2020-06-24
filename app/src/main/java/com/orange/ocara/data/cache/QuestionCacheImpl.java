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

import com.orange.ocara.data.cache.db.QuestionDao;
import com.orange.ocara.data.net.model.QuestionEntity;
import com.orange.ocara.data.source.QuestionSource;

import java.util.List;

/** default implementation of {@link com.orange.ocara.data.source.QuestionSource.QuestionCache} */
public class QuestionCacheImpl implements QuestionSource.QuestionCache {

    private final QuestionDao questionDao;

    public QuestionCacheImpl(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public List<QuestionEntity> findAll(Long rulesetId, Long equipmentId) {
        return questionDao.findAll(rulesetId, equipmentId);
    }
}
