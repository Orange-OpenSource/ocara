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
import com.orange.ocara.data.cache.database.NonTables.RuleWithIllustrationsCount;
import com.orange.ocara.data.cache.database.NonTables.RuleWithObjName;
import com.orange.ocara.data.cache.database.NonTables.RuleWithProfiletypeImpact;
import com.orange.ocara.data.cache.database.Tables.Rule;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface RuleDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Rule rule);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<Rule> rule);

    @Query("select * from rule_profile_type")
//    @Query("select * from rule_profile_type left join rule on rule_profile_type.ruleRef = rule.ruleRef left join impact_values on impact_values.impactRef =  rule_profile_type.ruleImpactRef left join profile_type on rule_profile_type.profileRef = profile_type.profileRef where version =4")
    Single<List<RuleWithProfiletypeImpact>> getRuleWithImpactValue();

    @Query("select * from  rule_profile_type where rulesetRef=:rulesetRef and version =:rulesetVersion and  ruleRef =:ruleRef")
    Single<List<RuleWithProfiletypeImpact>> getRuleWithImpactValueForRule(String rulesetRef,int rulesetVersion, String ruleRef);

    @Query("select rule.ruleRef,rule.origin,rule.label,rule.date,count(rule_illustrations.illustRef) as " +
            "illustrations from rule left join questions_rules" +
            " on questions_rules.ruleRef=rule.ruleRef left join " +
            "rule_illustrations on rule_illustrations.ruleRef=rule.ruleRef " +
            "where questions_rules.questionRef=:questionRef" +
            " and questions_rules.rulesetVersion=:rulesetVersion" +
            " and questions_rules.rulesetRef=:rulesetRef " +
            "and (rule_illustrations.rulesetRef=:rulesetRef or " +
            "rule_illustrations.rulesetRef is null)" +
            " and (rule_illustrations.version=:rulesetVersion or" +
            " rule_illustrations.version is null)" +
            "group by rule.ruleRef")
    Single<List<RuleWithIllustrationsCount>> getRulesWithQuestionAndRuleset(String questionRef, String rulesetRef, int rulesetVersion);

    @Query("select rule.ruleRef,rule.origin,rule.label,rule.date,count(rule_illustrations.illustRef) as " +
            "illustrations ,questions_rules.questionRef as questionRef from rule left join questions_rules" +
            " on questions_rules.ruleRef=rule.ruleRef left join " +
            "rule_illustrations on rule_illustrations.ruleRef=rule.ruleRef " +
            "where questions_rules.questionRef IN (:questionRef)" +
            " and questions_rules.rulesetVersion=:rulesetVersion" +
            " and questions_rules.rulesetRef=:rulesetRef " +
            "and (rule_illustrations.rulesetRef=:rulesetRef or " +
            "rule_illustrations.rulesetRef is null)" +
            " and (rule_illustrations.version=:rulesetVersion or" +
            " rule_illustrations.version is null)" +
            "group by rule.ruleRef,questions_rules.questionRef order by rule.ruleRef asc")
    Single<List<RuleWithIllustrationsCount>> getRulesWithQuestionAndRuleset(List<String> questionRef, String rulesetRef, int rulesetVersion);


//    @Query("select objects.equipment_name,rule.* from questions left join questions_objects on " +
//            "questions_objects.questionRef=questions.questionRef  left join questions_rules on " +
//            "questions.questionRef = questions_rules.questionRef left join rule on " +
//            "questions_rules.ruleRef=rule.ruleRef left join objects on" +
//            " objects.objectReference = questions_objects.objectReference where " +
//            "questions_objects.rulesetVer=:version and" +
//            " questions_objects.rulesetRef=:rulesetRef " +
////            "and questions_objects.objectReference in (:objRef) " +
//            "and questions_rules.rulesetRef=:rulesetRef " +
//            "and questions_rules.rulesetVersion=:version " +
//            "order by questions.questionRef asc," +
//            "rule.ruleRef asc")


    @Query("select objects.equipment_name,rule.*,count(rule_illustrations.illustRef) as illustrationsCount " +
            "from questions left join questions_objects on " +
            "questions_objects.questionRef=questions.questionRef  left join questions_rules on " +
            "questions.questionRef = questions_rules.questionRef left join rule on " +
            "questions_rules.ruleRef=rule.ruleRef left join objects on" +
            " objects.objectReference = questions_objects.objectReference " +
            "left join rule_illustrations on rule_illustrations.ruleRef=rule.ruleRef" +
            " where questions_objects.rulesetVer=:version and" +
            " questions_objects.rulesetRef=:rulesetRef " +
//            "and questions_objects.objectReference in (:objRef) " +
            "and questions_rules.rulesetRef=:rulesetRef " +
            "and questions_rules.rulesetVersion=:version " +
            "group by rule.ruleRef "+
            "order by questions.questionRef asc," +
            "rule.ruleRef asc")
    Single<List<RuleWithObjName>> getRulesWithObjNames(String rulesetRef,String version);

}
