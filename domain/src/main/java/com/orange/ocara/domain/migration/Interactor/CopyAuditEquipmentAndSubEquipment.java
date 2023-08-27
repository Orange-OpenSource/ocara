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

import com.activeandroid.query.Select;
import com.orange.ocara.domain.repositories.AuditEquipmentRepository;
import com.orange.ocara.data.cache.database.crossRef.AuditEquipmentSubEquipment;
import com.orange.ocara.data.cache.database.crossRef.AuditEquipments;
import com.orange.ocara.data.oldEntities.AuditObjectEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import timber.log.Timber;

public class CopyAuditEquipmentAndSubEquipment extends Interactor<AuditObjectEntity> {
    AuditEquipmentRepository repository;

    @Inject
    public CopyAuditEquipmentAndSubEquipment(AuditEquipmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Completable execute() {
        // these are the parent objects
        List<AuditObjectEntity> auditObjectEntities = new Select()
                .from(AuditObjectEntity.class)
                .where(AuditObjectEntity.COLUMN_PARENT_OBJECT + " IS NULL")
                .execute();
        Timber.d("size = " + auditObjectEntities.size());
        List<AuditEquipments> parents = new ArrayList<>();
        List<AuditEquipmentSubEquipment> children = new ArrayList<>();
        for (AuditObjectEntity parentEntity : auditObjectEntities) {
            parents.add(new AuditEquipments.AuditEquipmentsBuilder()
                    .setId(parentEntity.getId().intValue())
                    .setName(parentEntity.getName())
                    .setAudit_id(parentEntity.getAudit().getId().intValue())
                    .setObjectRef(parentEntity.getObjectDescriptionId())
                    .createAuditEquipments());

            parentEntity.refreshChildren();
            Timber.d("children = "+parentEntity.getChildren().size());
            for(AuditObjectEntity childEntity:parentEntity.getChildren()){
                children.add(new AuditEquipmentSubEquipment.AuditEquipmentSubEquipmentBuilder()
                        .setAudit_id(childEntity.getAudit().getId().intValue())
                        .setAuditEquipmentId(parentEntity.getId().intValue())
                        .setChildRef(childEntity.getObjectDescriptionId())
                        .createAuditEquipmentSubEquipment());
            }
        }
        Timber.d("parents size = "+parents.size());
        return repository.insertAuditEquipments(parents).concatWith(repository.insertAuditSubEquipments(children));
    }
}
