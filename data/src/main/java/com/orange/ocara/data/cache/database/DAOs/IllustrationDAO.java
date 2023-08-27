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

import com.orange.ocara.data.cache.database.NonTables.IllustrationWithRuleRef;
import com.orange.ocara.data.cache.database.Tables.Illustration;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface IllustrationDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Illustration illustration);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<Illustration> illustration);

    @Query("select illustrations.*,rule_illustrations.ruleRef from illustrations left join rule_illustrations " +
            "on illustrations.illustRef=rule_illustrations.illustRef where " +
            "rule_illustrations.ruleRef in (:ruleRef) and " +
            "rule_illustrations.rulesetRef=:rulesetRef and " +
            "rule_illustrations.version=:rulesetVer")
    Single<List<IllustrationWithRuleRef>> getIllustrationsForRule(List<String> ruleRef, String rulesetRef, int rulesetVer);

    @Query("select * from illustrations where illustRef=:ref")
    Single<Illustration> query(String ref);

    @Query("select illustrations.* from illustrations" +
            " inner join rule_illustrations on rule_illustrations.illustRef =illustrations.illustRef " +
            " where rule_illustrations.ruleRef = :ruleRef and " +
            " rule_illustrations.rulesetRef = :rulesetRef  and " +
            " rule_illustrations.version  = :rulesetVersion ")
    Single<List<Illustration>> getIllustrations(String ruleRef, String rulesetRef, int rulesetVersion);

    @Query("select illustrations.* from illustrations inner join rule_illustrations " +
            "on illustrations.illustRef = rule_illustrations.illustRef where " +
            "rule_illustrations.ruleRef = :ruleRef and rule_illustrations.rulesetRef = :ruleSetRef")
    Single<List<Illustration>> getIllustrations(String ruleRef, String ruleSetRef);

//    @Query("delete from rule_illustrations where rule_illustrations.rulesetRef = :ruleSetRef")
//    Completable deleteRulesetIllustrations(String ruleSetRef);

    @Query("delete from rule_illustrations where rule_illustrations.rulesetRef = (select ruleSetRef from ruleset_details where rulesetId = :ruleSetId )")
    Completable deleteRulesetIllustrations(int ruleSetId);

}
