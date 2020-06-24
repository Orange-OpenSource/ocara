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

import com.orange.ocara.data.net.model.QuestionEntity;

import java.util.List;

/**
 * default implementation for {@link com.orange.ocara.data.source.QuestionSource.QuestionDataStore}
 */
public class QuestionDataStoreImpl implements QuestionSource.QuestionDataStore {

    private final QuestionSource.QuestionCache questionCache;

    public QuestionDataStoreImpl(QuestionSource.QuestionCache questionCache) {
        this.questionCache = questionCache;
    }

    @Override
    public List<QuestionEntity> findAll(Long rulesetId, Long equipmentId) {
        return questionCache.findAll(rulesetId, equipmentId);
    }
}
