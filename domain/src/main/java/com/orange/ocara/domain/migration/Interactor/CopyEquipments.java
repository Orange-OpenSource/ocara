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
import com.orange.ocara.data.cache.database.Tables.Equipment;
import com.orange.ocara.data.cache.database.crossRef.EquipmentRulesetVersion;
import com.orange.ocara.data.oldEntities.EquipmentEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class CopyEquipments extends Interactor<EquipmentEntity> {
    EquipmentRepository repository;

    @Inject
    public CopyEquipments(EquipmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Completable execute() {
        List<EquipmentEntity> equipmentEntities=getAll(EquipmentEntity.class);
        List<Equipment> equipments=new ArrayList<>();
        List<EquipmentRulesetVersion> equipmentRulesetVersionList=new ArrayList<>();
        for(EquipmentEntity equipmentEntity:equipmentEntities){
            equipments.add(Equipment.EquipmentBuilder.anEquipment()
                    .withDate(equipmentEntity.date)
                    .withDefinition(equipmentEntity.definition)
                    .withIcon(equipmentEntity.icon)
                    .withName(equipmentEntity.name)
                    .withReference(equipmentEntity.reference)
                    .withType(equipmentEntity.type)
                    .build());
            equipmentRulesetVersionList.add(new EquipmentRulesetVersion.Builder()
                    .setObjRef(equipmentEntity.reference)
                    .setRulesetRef(equipmentEntity.getRuleSetDetail().getReference())
                    .setVersion(Integer.parseInt(equipmentEntity.getRuleSetDetail().getVersion()))
                    .createEquipmentRulesetVersion());
        }
        return repository.insert(equipments);
    }
}
