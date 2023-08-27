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

import com.orange.ocara.data.cache.database.crossRef.RuleProfileTypeImpactCrossRef;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface RuleProfileTypeImpactDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(RuleProfileTypeImpactCrossRef ruleProfileTypeImpactCrossRef);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<RuleProfileTypeImpactCrossRef> ruleProfileTypeImpactCrossRef);

    @Query("delete from rule_profile_type where rule_profile_type.rulesetRef = " +
            "(select ruleset_details.reference from ruleset_details where rulesetId=:rulesetId) " +
            "and rule_profile_type.version = (select ruleset_details.ruleset_version from ruleset_details " +
            "where rulesetId=:rulesetId)")
    Completable delete(int rulesetId);

}
