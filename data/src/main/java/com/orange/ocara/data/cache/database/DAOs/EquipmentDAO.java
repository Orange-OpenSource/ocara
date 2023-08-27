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
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.orange.ocara.data.cache.database.Tables.Equipment;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface EquipmentDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Equipment equipment);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<Equipment> equipment);

    @Query("select * from objects where objectReference=:objRef")
    Single<Equipment> getEquipmentByRef(String objRef);

    @Query("select objects.* " +
            "from equipment_subequipments " +
            " left join objects on objects.objectReference = childRef " +
            " where parentRef =:equipmentRef " +
            " and rulesetRef in (select audit_ruleset from audit where audit_id =:auditId)" +
            " and version in (select rulesetVer from audit where audit_id =:auditId) ")
    Single<List<Equipment>> getAllChildrenOfEquipment(int auditId, String equipmentRef);

    @Query("delete from objects where objectReference in ( " +
            "select equipment_ruleset_version.objRef " +
            "from equipment_ruleset_version left join ruleset_details " +
            "on equipment_ruleset_version.rulesetRef = ruleset_details.reference and " +
            "equipment_ruleset_version.version = ruleset_details.ruleset_version " +
            "where ruleset_details.rulesetId = :rulesetId) ")
    Completable delete(int rulesetId);

    @Query("select objects.* from objects left join equipment_subequipments on objects.objectReference=" +
            "equipment_subequipments.childRef where parentRef=:equipmentRef and " +
            "rulesetRef = :rulesetRef "+
            "and version = :rulesetVer")
    Single<List<Equipment>> getChildrenOfEquipment(String rulesetRef,int rulesetVer,String equipmentRef);

    @Query("select count(objects.objectReference) from objects left join equipment_subequipments" +
            " on objects.objectReference = equipment_subequipments.childRef , " +
            "(select audit.audit_ruleset as rulesetRef," +
            " audit.rulesetVer as version ,auditequipments.objectReference as equipmentRef" +
            " from auditequipments left join audit" +
            " on audit.audit_id = auditequipments.audit_id " +
            "where auditequipments.audit_object_id=:auditEquipmentId)" +
            "as data where " +
            "equipment_subequipments.parentRef = data.equipmentRef and equipment_subequipments.rulesetRef = data.rulesetRef and " +
            "equipment_subequipments.version = data.version group by equipment_subequipments.parentRef")
    Single<Integer> getNumberOfChildren(int auditEquipmentId);


}
