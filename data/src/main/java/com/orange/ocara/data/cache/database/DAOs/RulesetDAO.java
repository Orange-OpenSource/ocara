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

import com.orange.ocara.data.cache.database.Tables.RulesetDetails;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface RulesetDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(RulesetDetails rulesetDetails);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<RulesetDetails> rulesetDetails);

    @Query("select * from ruleset_details")
    Single<List<RulesetDetails>> getRulesets();

    //    @Query("select count(reference) from ruleset_details where reference = 'DEMO' group by reference")
    @Query("select count(reference) from ruleset_details where isDemo = 1 group by reference")
    Single<Integer> getDefualtRuleset();

    @Query("select * from ruleset_details where reference=:arg0 " +
            "and ruleset_version=:arg1")
    Single<RulesetDetails> getRuleset(String arg0, int arg1);

    @Query("select * from ruleset_details where rulesetId=:id")
    Single<RulesetDetails> getRulesetById(long id);

    /*
     this method returns the latest added version of this ruleset
     */
    @Query("select * from ruleset_details where reference=:ref" +
            " order by ruleset_version desc limit 1")
    Single<RulesetDetails> getLatestRulesetByReference(String ref);

    @Query("select count(audit_id) " +
            "from audit , (select reference,ruleset_version from ruleset_details where rulesetId=:id) " +
            "where audit_ruleset = reference and rulesetVer = ruleset_version")
    Single<Integer> getNumberOfRelatedAudits(int id);

    @Query("delete from ruleset_details where rulesetId=:rulesetId")
    Completable delete(int rulesetId);
}
