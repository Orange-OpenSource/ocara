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
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.orange.ocara.data.cache.database.Tables.Equipment;
import com.orange.ocara.data.cache.database.crossRef.EquipmentRulesetVersion;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface EquipmentRulesetVersionDAO {
    @Insert
    Completable insert(List<EquipmentRulesetVersion> equipmentRulesetVersionList);

    @Query("select objects.* from objects left join equipment_ruleset_version " +
            "on objects.objectReference=equipment_ruleset_version.objRef " +
            "where equipment_ruleset_version.rulesetRef=:ref " +
            "and version=:version")
    Single<List<Equipment>> getEquipmentsByVersion(String ref, int version);

    @Query("select objects.* from objects left join equipment_ruleset_version " +
            "on objects.objectReference = equipment_ruleset_version.objRef left join ruleset_details" +
            " on ruleset_details.reference=equipment_ruleset_version.rulesetRef and " +
            "ruleset_details.ruleset_version=equipment_ruleset_version.version " +
            "where ruleset_details.rulesetId = :rulesetId" +
            " order by objects.equipment_name asc")
    Single<List<Equipment>> getEquipmentsById(long rulesetId);

//    @Query("delete from equipment_ruleset_version where equipment_ruleset_version.objRef in ( " +
//            "select equipment_ruleset_version.objRef " +
//            "from equipment_ruleset_version left join ruleset_details " +
//            "on equipment_ruleset_version.rulesetRef = ruleset_details.reference and " +
//            "equipment_ruleset_version.version = ruleset_details.ruleset_version " +
//            "where ruleset_details.rulesetId = :rulesetId)")
//    Completable delete(int rulesetId);

    @Query("delete from equipment_ruleset_version where " +
            "equipment_ruleset_version.rulesetRef =(select ruleSetRef from ruleset_details where rulesetId = :rulesetId )")
    Completable delete(int rulesetId);
}
