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

import com.orange.ocara.domain.repositories.EquipmentRepository;
import com.orange.ocara.data.cache.database.crossRef.EquipmentSubEquipmentCrossRef;
import com.orange.ocara.data.oldEntities.EquipmentEntity;
import com.orange.ocara.data.oldEntities.RulesetEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class CopyEquipmentSubEquipmentRelation extends Interactor<RulesetEntity>{
    EquipmentRepository repository;

    @Inject
    public CopyEquipmentSubEquipmentRelation(EquipmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Completable execute() {
        List<RulesetEntity> rulesetEntities=getAll(RulesetEntity.class);
        List<EquipmentSubEquipmentCrossRef> equipmentSubEquipmentCrossRefs=new ArrayList<>();
        for(RulesetEntity rulesetEntity:rulesetEntities){

            for(EquipmentEntity equipmentEntity:rulesetEntity.getObjectDescriptionsDb()){
                for(String subobject:equipmentEntity.subObject){
                    equipmentSubEquipmentCrossRefs.add(EquipmentSubEquipmentCrossRef.EquipmentSubEquipmentCrossRefBuilder.anEquipmentSubEquipmentCrossRef()
                            .withChildRef(subobject)
                            .withParentRef(equipmentEntity.reference)
                            .withRulesetRef(rulesetEntity.getReference())
                            .withVersion(Integer.parseInt(rulesetEntity.getVersion()))
                            .build());
                }
            }
        }
        return repository.insertEquipmentSubEquipment(equipmentSubEquipmentCrossRefs);
    }
}
