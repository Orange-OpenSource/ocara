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

import com.orange.ocara.data.cache.database.crossRef.ImpactValueRulesetCrossref;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface ImpactValueRulesetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(ImpactValueRulesetCrossref impactValue);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<ImpactValueRulesetCrossref> impactValue);

    @Query("delete from impact_ruleset where impact_ruleset.rulesetRef = " +
            "(select ruleset_details.reference from ruleset_details where ruleset_details.rulesetId=:rulesetId)" +
            "and impact_ruleset.rulesetVersion = (select ruleset_details.ruleset_version from ruleset_details " +
            "where ruleset_details.rulesetId=:rulesetId)")
    Completable delete(int rulesetId);
}
