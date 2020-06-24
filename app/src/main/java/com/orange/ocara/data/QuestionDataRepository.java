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

package com.orange.ocara.data;

import com.orange.ocara.business.model.RuleGroupModel;
import com.orange.ocara.business.repository.QuestionRepository;
import com.orange.ocara.data.net.model.QuestionEntity;
import com.orange.ocara.data.source.QuestionSource;
import com.orange.ocara.tools.ListUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.orange.ocara.tools.ListUtils.newArrayList;

/**
 * default implementation for {@link QuestionRepository}
 */
public class QuestionDataRepository implements QuestionRepository {

    private final QuestionSource.QuestionDataStore dataStore;

    QuestionDataRepository(QuestionSource.QuestionDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public List<RuleGroupModel> findAll(Long rulesetId, Long equipmentId) {

        List<QuestionEntity> questions = dataStore.findAll(rulesetId, equipmentId);

        List<RuleGroupModel> groups = ListUtils.newArrayList();
        for (int i = 0; i < questions.size(); i++) {
            groups.add(new RuleGroupModel(questions.get(i), i));
        }
        Collections.sort(groups, (o1, o2) -> Integer.compare(o1.getIndex(), o2.getIndex()));

        return groups;
    }
}
