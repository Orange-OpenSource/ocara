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

import com.orange.ocara.business.model.RuleModel;
import com.orange.ocara.business.repository.RuleRepository;
import com.orange.ocara.data.net.model.QuestionEntity;
import com.orange.ocara.data.net.model.RuleEntity;
import com.orange.ocara.data.source.QuestionSource;
import com.orange.ocara.data.source.RuleSource;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.orange.ocara.tools.ListUtils.newArrayList;

/**
 * default implementation of {@link RuleRepository}
 */
public class RuleDataRepository implements RuleRepository {

    private final RuleSource.RuleDataStore dataStore;

    private final QuestionSource.QuestionDataStore groupStore;

    RuleDataRepository(RuleSource.RuleDataStore dataStore, QuestionSource.QuestionDataStore groupStore) {
        this.dataStore = dataStore;
        this.groupStore = groupStore;
    }

    @Override
    public List<RuleModel> findAll(long rulesetId, long equipmentId) {
        List<RuleEntity> rules = dataStore.findAll(rulesetId);
        List<QuestionEntity> questions = groupStore.findAll(rulesetId, equipmentId);

        Set<RuleModel> noDuplicates = new HashSet<>();

        int i = 0;
        for (QuestionEntity item : questions) {
            for (RuleEntity element : rules) {
                if (item.getRulesRef().contains(element.getReference())) {
                    noDuplicates.add(new RuleModel(element, item.getId(), i));
                    i++;
                }
            }
        }

        List<RuleModel> children = newArrayList(noDuplicates);
        Collections.sort(children, (o1, o2) -> Integer.compare(o2.getIndex(), o1.getIndex()));

        return children;
    }
}
