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
import androidx.room.Query;

import com.orange.ocara.data.cache.database.NonTables.DocReportProfileQuestionsRulesAnswers;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface DocReportProfilesDAO {

    //get profiles & questions & rules with answers for word report
//    @Query("select  rule_profile_type.profileRef, profile_type.name as profileName , questions.questionLabel as questionName, questions.questionRef,  rule_answers.ruleRef , " +
//            " rule.label as ruleLable, rule_answers.answer as ruleAnswer, rule_profile_type.ruleImpactRef , impact_values.name as impactName " +
//            " from rule_answers left join rule_profile_type on rule_answers.ruleRef = rule_profile_type.ruleRef " +
//            " left join profile_type on profile_type.profileRef = rule_profile_type.profileRef " +
//            " left join impact_values on impact_values.impactRef = rule_profile_type.ruleImpactRef " +
//            " left join questions on rule_answers.questionRef = questions.questionRef " +
//            " left join rule on rule.ruleRef = rule_answers.ruleRef " +
//            " where rule_answers.answer in (0,1,3,4) and rule_profile_type.ruleImpactRef != 1 " +
//            " and rule_answers.auditEquipmentId in (select audit_object_id from AuditEquipments where audit_id = :auditId) " +
//            " group by  rule_answers.answer , rule_profile_type.profileRef , questions.questionRef, rule_profile_type.ruleImpactRef order by  rule_profile_type.profileRef , questions.questionRef")

    @Query(
            "select rule_profile_type.profileRef, profile_type.name as profileName ," +
                    " questions.questionLabel as questionName, questions.questionRef,  " +
                    "rule_answers.ruleRef ,  rule.label as ruleLable, rule_answers.answer as ruleAnswer," +
                    " rule_profile_type.ruleImpactRef , impact_values.name as impactName  " +
                    "from rule_answers left join rule_profile_type on rule_answers.ruleRef = rule_profile_type.ruleRef left join profile_type on profile_type.profileRef = rule_profile_type.profileRef \n" +
                    "left join impact_values on impact_values.impactRef = rule_profile_type.ruleImpactRef " +
                    "left join questions on rule_answers.questionRef = questions.questionRef " +
                    " left join rule on rule.ruleRef = rule_answers.ruleRef " +
                    " where rule_answers.answer in (0,1,2,3,4) and rule_profile_type.ruleImpactRef != 1 " +
                    " and rule_answers.auditEquipmentId in (select audit_object_id from AuditEquipments where audit_id = :auditId) " +
                    " and rulesetRef = :ruleSetRef and version = :ruleSetVer" +
                    "  order by  rule_profile_type.profileRef , rule_answers.ruleRef")
    Single<List<DocReportProfileQuestionsRulesAnswers>> getProfilesQuestionsRulesAnswers(Long auditId, String ruleSetRef, int ruleSetVer);


}
