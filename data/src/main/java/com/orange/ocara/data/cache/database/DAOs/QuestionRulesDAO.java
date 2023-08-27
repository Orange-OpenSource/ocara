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

import com.orange.ocara.data.cache.database.crossRef.QuestionsRulesCrossRef;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface QuestionRulesDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(QuestionsRulesCrossRef questionsRulesCrossRef);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<QuestionsRulesCrossRef> questionsRulesCrossRef);

    @Query("delete from questions_rules where questions_rules.rulesetRef=" +
            "(select ruleset_details.reference from ruleset_details where ruleset_details.rulesetId=:rulesetId)" +
            " and questions_rules.rulesetVersion=(select ruleset_details.ruleset_version from ruleset_details" +
            " where ruleset_details.rulesetId=:rulesetId)")
    Completable delete(int rulesetId);
}
