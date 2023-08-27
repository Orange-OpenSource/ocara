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
import com.orange.ocara.data.cache.database.crossRef.RuleProfileTypeImpactCrossRef;
import com.orange.ocara.data.oldEntities.RuleImpactEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class CopyRuleProfileImpactRelation extends Interactor<RuleImpactEntity> {
    RuleRepository repository;

    @Inject
    public CopyRuleProfileImpactRelation(RuleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Completable execute() {
        List<RuleProfileTypeImpactCrossRef> res=new ArrayList<>();
        List<RuleImpactEntity> ruleImpactEntities=getAll(RuleImpactEntity.class);
        for(RuleImpactEntity entity:ruleImpactEntities){
            String version=entity.getRule().getRuleSeDetail().getVersion();

            res.add(new RuleProfileTypeImpactCrossRef.Builder()
                    .setProfileRef(entity.profileTypeRef)
                    .setRulesetRef(entity.getRule().getRuleSeDetail().getReference())
                    .setVersion(version==null?0:Integer.parseInt(version))
                    .setRuleImpactRef(entity.impactValueRef)
                    .setRuleRef(entity.getRule().reference)
                    .createRuleProfileTypeImpactCrossRef());
        }
        return repository.insertRuleImpact(res);
    }
}
