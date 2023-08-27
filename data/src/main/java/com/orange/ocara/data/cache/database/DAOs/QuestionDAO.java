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

import com.orange.ocara.data.cache.database.NonTables.QuestionWithRuleAnswer;
import com.orange.ocara.data.cache.database.Tables.Question;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface QuestionDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<Question> question);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Question question);

    // this query will return all the rules each one associated
    // with its question
    @Query("select questions.*,rule.*,questions_objects.objectReference " +
            "from questions left join questions_objects " +
            "on questions_objects.questionRef=questions.questionRef " +
            "left join questions_rules on questions.questionRef = " +
            "questions_rules.questionRef left join rule " +
            "on questions_rules.ruleRef=rule.ruleRef " +
            "where questions_objects.rulesetVer=:version and " +
            "questions_objects.rulesetRef=:rulesetRef " +
            "and questions_objects.objectReference in (:objRef) " +
            "and questions_rules.rulesetRef=:rulesetRef " +
            "and questions_rules.rulesetVersion=:version order by questions.questionRef asc,rule.ruleRef asc")
    Single<List<QuestionWithRuleAnswer>> getQuestionsWithRules(String rulesetRef, List<String> objRef, int version);

    @Query("select * from questions left join questions_objects " +
            "on questions_objects.questionRef=questions.questionRef"+
            " where questions_objects.rulesetRef=:rulesetRef and " +
            "questions_objects.objectReference=:objRef and "+
            "questions_objects.rulesetVer=:version order by questionRef asc")
    Single<List<Question>> getQuestions(String rulesetRef,String objRef,int version);

}
