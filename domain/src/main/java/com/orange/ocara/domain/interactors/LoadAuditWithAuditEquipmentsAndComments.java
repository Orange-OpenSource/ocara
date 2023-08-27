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


package com.orange.ocara.domain.interactors;

import com.orange.ocara.domain.models.AuditEquipmentForCurrentRouteModel;
import com.orange.ocara.domain.models.AuditModel;
import com.orange.ocara.domain.repositories.AuditEquipmentRepository;
import com.orange.ocara.domain.repositories.AuditRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class LoadAuditWithAuditEquipmentsAndComments {
    AuditEquipmentRepository auditEquipmentRepository;
    AuditRepository auditRepository;

    @Inject
    public LoadAuditWithAuditEquipmentsAndComments(AuditEquipmentRepository auditEquipmentRepository, AuditRepository auditRepository) {
        this.auditEquipmentRepository = auditEquipmentRepository;
        this.auditRepository = auditRepository;
    }

    public Single<AuditModel> execute(int auditId) {
        return Single.fromObservable(auditEquipmentRepository.loadAuditEquipmentsWithOrder((long)auditId)
                .toObservable()
                .concatMap(loadAuditAndAddAuditEquipmentsToIt(auditId))
        );
    }

    private Function<List<AuditEquipmentForCurrentRouteModel>, ObservableSource<AuditModel>> loadAuditAndAddAuditEquipmentsToIt(int auditId){
        return auditEquipments -> auditRepository.getAuditById((long)auditId)
                .map(audit -> addAuditEquipmentsToAudit(audit,auditEquipments)).toObservable();
    }
    private AuditModel addAuditEquipmentsToAudit(AuditModel audit,List<AuditEquipmentForCurrentRouteModel> auditEquipments){
        for(AuditEquipmentForCurrentRouteModel auditEquipment:auditEquipments){
            audit.addObject(auditEquipment);
        }
        return audit;
    }
}
