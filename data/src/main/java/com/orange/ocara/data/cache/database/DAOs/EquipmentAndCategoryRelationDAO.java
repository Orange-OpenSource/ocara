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

import com.orange.ocara.data.cache.database.crossRef.EquipmentAndCategoryCrossRef;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface EquipmentAndCategoryRelationDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<EquipmentAndCategoryCrossRef> equipmentAndCategoryCrossRef);

    @Query("delete from objects_categories where objects_categories.category_id in (" +
            " select EquipmentCategory.category_id from EquipmentCategory left join ruleset_details on" +
            " EquipmentCategory.rulesetVer=ruleset_details.ruleset_version and EquipmentCategory.rulesetRef = " +
            "ruleset_details.reference where ruleset_details.rulesetId=:rulesetId)")
    Completable delete(int rulesetId);
}
