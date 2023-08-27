package com.orange.ocara.data.cache.database.DAOs;
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
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.orange.ocara.data.cache.database.crossRef.AuditEquipmentSubEquipment;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface AuditEquipmentSubEquipmentDAO {
    @Insert
    Completable insert(List<AuditEquipmentSubEquipment> list);

    @Insert
    Single<List<Long>> insertAndReturnSingle(List<AuditEquipmentSubEquipment> list);

    @Query("delete from audit_equipment_subequipment where auditEquipmentId=:audEqId")
    Completable deleteSubEquipments(int audEqId);

    @Query("delete from audit_equipment_subequipment where auditEquipmentId in (:audEqId)")
    Completable deleteSubEquipments(List<Integer> audEqId);


    @Query("delete from audit_equipment_subequipment where audit_id=:auditId" +
            " and auditEquipmentId=:auditEquipmentId and childRef in (:children)")
    Completable deleteChildren(int auditId, int auditEquipmentId, List<String> children);

    @Query("select * from audit_equipment_subequipment where audit_id =:auditId")
    Single<List<AuditEquipmentSubEquipment>> getSubEquipmentsOfAudit(int auditId);

}
