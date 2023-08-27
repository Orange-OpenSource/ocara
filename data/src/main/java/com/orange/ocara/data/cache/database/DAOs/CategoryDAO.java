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
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.orange.ocara.data.cache.database.NonTables.CategoryWithEquipments;
import com.orange.ocara.data.cache.database.Tables.EquipmentCategory;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface CategoryDAO {
    @Insert
    Completable insert(EquipmentCategory group);
    @Insert
    Single<List<Long>> insert(List<EquipmentCategory> group);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAndReturnCompletable(List<EquipmentCategory> group);

    @Query("select * from equipmentcategory where category_id in (:rowsIds)")
    Single<List<EquipmentCategory>> getCategories(List<Long> rowsIds);

    @Query("select * from equipmentcategory where category_id = :id")
    Single<EquipmentCategory> getCategory(int id);

    @Query("select objects.*,equipmentcategory.* from equipmentcategory" +
            " left join objects_categories on objects_categories.category_id=" +
            "equipmentcategory.category_id " +
            "left join objects on objects_categories.objectRef" +
            "=objects.objectReference " +
            "where equipmentcategory.rulesetRef=:rulesetRef " +
            "and equipmentcategory.rulesetVer=:ruleVer")
    Single<List<CategoryWithEquipments>> getCategories(String rulesetRef,int ruleVer);

    @Query("delete from EquipmentCategory where EquipmentCategory.category_id in ( " +
            "select EquipmentCategory.category_id " +
            "from EquipmentCategory left join ruleset_details " +
            "on EquipmentCategory.rulesetRef = ruleset_details.reference and " +
            "EquipmentCategory.rulesetVer = ruleset_details.ruleset_version " +
            "where ruleset_details.rulesetId = :rulesetId)")
    Completable delete(int rulesetId);
}
