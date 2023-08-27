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

import com.orange.ocara.domain.repositories.IllustrationsRepository;
import com.orange.ocara.data.cache.database.crossRef.RuleWithIllustrations;
import com.orange.ocara.data.oldEntities.RuleEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class CopyIllustrationsRulesRelation extends Interactor<RuleEntity>{
    IllustrationsRepository repository;
    @Inject
    public CopyIllustrationsRulesRelation(IllustrationsRepository repository){
        this.repository=repository;
    }

    @Override
    public Completable execute() {
        List<RuleEntity> ruleEntities=getAll(RuleEntity.class);
        List<RuleWithIllustrations> ruleWithIllustrations=new ArrayList<>();
        for(RuleEntity ruleEntity:ruleEntities){
            for(String illRef:ruleEntity.illustration){
                ruleWithIllustrations.add(new RuleWithIllustrations.RuleWithIllustrationBuilder()
                        .setIllustRef(illRef)
                        .setRuleRef(ruleEntity.reference)
                        .setRulesetRef(ruleEntity.getRuleSeDetail().getReference())
                        .setVersion(Integer.parseInt(ruleEntity.getRuleSeDetail().getVersion()))
                        .createRuleWithIllustrations());
            }
        }
        return repository.insertRuleIllutrations(ruleWithIllustrations);
    }
}
