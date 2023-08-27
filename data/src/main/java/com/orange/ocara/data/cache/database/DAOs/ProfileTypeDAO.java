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

import com.orange.ocara.data.cache.database.NonTables.ProfileTypeWithRuleset;
import com.orange.ocara.data.cache.database.Tables.ProfileType;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface ProfileTypeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(ProfileType profileType);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<ProfileType> profileType);

//    @Query("select * from profile_type" +
//            " left join " +
//            "ruleset_details on profile_type.profile_type_ruleset=" +
//            "ruleset_details.reference "+
//            "where profile_type.profile_type_ruleset=:rulesetRef " +
//            "and profile_type.profile_type_ruleset_ver=:ruleVer ")

    @Query("select * from profile_type" +
            " left join " +
            "profile_type_ruleset on profile_type.profileRef = profile_type_ruleset.profileRef"
            + " left join ruleset_details on profile_type_ruleset.rulesetRef=" +
            "ruleset_details.reference and profile_type_ruleset.rulesetVersion=ruleset_details.ruleset_version " +
            "where ruleset_details.ruleset_version=:ruleVer " +
            "and ruleset_details.reference=:rulesetRef ")
    Single<List<ProfileTypeWithRuleset>> getProfileTypesForRuleset(String rulesetRef, int ruleVer);

}
