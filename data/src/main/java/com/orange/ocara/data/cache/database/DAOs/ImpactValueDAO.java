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

import com.orange.ocara.data.cache.database.NonTables.ImpactValueWithRuleset;
import com.orange.ocara.data.cache.database.Tables.ImpactValue;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface ImpactValueDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(ImpactValue impactValue);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<ImpactValue> impactValue);


    @Query("select * from impact_ruleset" +
            " left join " +
            "impact_values on impact_values.impactRef = impact_ruleset.impactRef"
            + " left join ruleset_details on impact_ruleset.rulesetRef=" +
            "ruleset_details.reference and impact_ruleset.rulesetVersion=ruleset_details.ruleset_version " +
            "where ruleset_details.ruleset_version=:ruleVer " +
            "and ruleset_details.reference=:rulesetRef ")
    Single<List<ImpactValueWithRuleset>> getImpactValuesForRuleset(String rulesetRef, int ruleVer);

}
