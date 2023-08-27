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

import com.orange.ocara.data.cache.database.crossRef.EquipmentWithIllustrations;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface ObjectWithIllustrationDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(EquipmentWithIllustrations object);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<EquipmentWithIllustrations> object);

    @Query("select * from EquipmentWithIllustrations where objectRef=:objRef")
    Single<List<EquipmentWithIllustrations>> query(String objRef);


    @Query("delete from EquipmentWithIllustrations where EquipmentWithIllustrations.rulesetRef = (select ruleSetRef from ruleset_details where rulesetId = :ruleSetId )")
    Completable deleteEquipmentWithIllustrations(int ruleSetId);
}
