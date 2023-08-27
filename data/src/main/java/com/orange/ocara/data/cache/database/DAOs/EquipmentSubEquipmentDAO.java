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

import com.orange.ocara.data.cache.database.NonTables.EquipmentWithSubEquipment;
import com.orange.ocara.data.cache.database.crossRef.EquipmentSubEquipmentCrossRef;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface EquipmentSubEquipmentDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(EquipmentSubEquipmentCrossRef equipmentSubEquipmentCrossRef);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<EquipmentSubEquipmentCrossRef> equipmentSubEquipmentCrossRef);

    @Query("select * from objects left join equipment_subequipments " +
            "on objects.objectReference = equipment_subequipments.childRef " +
            "where equipment_subequipments.parentRef in (:parentRef)" +
            " and equipment_subequipments.rulesetRef=:rulesetRef" +
            " and equipment_subequipments.version=:version")
    Single<List<EquipmentWithSubEquipment>> loadSubEquipments(List<String> parentRef, String rulesetRef, int version);


    @Query("select * from objects left join equipment_subequipments " +
            "on objects.objectReference = equipment_subequipments.childRef " +
            "where equipment_subequipments.parentRef in (:parentRef)" +
            " and equipment_subequipments.rulesetRef=:rulesetRef" +
            " and equipment_subequipments.version=:version")
    Single<List<EquipmentWithSubEquipment>> loadSubEquipments(String parentRef, String rulesetRef, int version);
}
