/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */


package com.orange.ocara.domain.migration.Interactor;

import com.orange.ocara.domain.repositories.RuleRepository;
import com.orange.ocara.data.cache.database.Tables.Rule;
import com.orange.ocara.data.oldEntities.RuleEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class CopyRules extends Interactor<RuleEntity>{
    RuleRepository repository;
    @Inject
    public CopyRules(RuleRepository repository){
        this.repository=repository;
    }
    @Override
    public Completable execute() {
        List<RuleEntity> ruleEntities=getAll(RuleEntity.class);
        List<Rule> rules=new ArrayList<>();
        for(RuleEntity entity:ruleEntities){
            rules.add(Rule.RuleBuilder.aRule()
                    .withDate(entity.date)
                    .withLabel(entity.label)
                    .withOrigin(entity.origin)
                    .withReference(entity.reference)
                    .build());
        }
        return repository.insertRules(rules);
    }
}
