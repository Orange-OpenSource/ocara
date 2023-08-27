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


import com.orange.ocara.data.cache.database.NonTables.scoreCalc.AnswerCount;
import com.orange.ocara.data.cache.database.NonTables.scoreCalc.CountImpactName;
import com.orange.ocara.data.cache.database.NonTables.scoreCalc.NoAnsWithProfileAndImpact;
import com.orange.ocara.data.cache.database.NonTables.scoreCalc.YesDoubtNoAnsNAAnsWithprofileAndAns;
import com.orange.ocara.data.cache.database.Tables.Rule;


import java.util.List;

import io.reactivex.Single;

@Dao
public interface ReportScoresDAO {

    //get number of rules answered YES (2) or D (3) grouped by profile & answer for audit
    //this is used for the first 2 columns in the audit report table view except for the last row
    //return count : profileName : profileRef : answer
    @Query("select count (*) as count , profile_type.name as profileName  , rule_profile_type.profileRef,  rule_answers.answer " +
            "from rule_answers left join rule_profile_type on rule_answers.ruleRef = rule_profile_type.ruleRef " +
            "left join profile_type on profile_type.profileRef = rule_profile_type.profileRef " +
            "left join impact_values on impact_values.impactRef = rule_profile_type.ruleImpactRef " +
            "where rule_answers.answer in (0,1,2,3) and rule_profile_type.ruleImpactRef != 1 " +
            "and rule_answers.auditEquipmentId in (select audit_object_id from AuditEquipments where audit_id = :auditId) " +
            "group by  rule_answers.answer , rule_profile_type.profileRef")
    Single<List<YesDoubtNoAnsNAAnsWithprofileAndAns>> getNumberOfRulesAnsweredByYesOrDoubtOrNoAnsOrNAGrpByAnsAndProfileForAudit(Long auditId);

    //get number of rules answered No (4) grouped by profile &impact for audit
    //this is used for the last columns in the audit report table view except for the last row
    //return count : profileName : impactName : profileRef : answer
    @Query(
            "select count (*) as count , profile_type.name as profileName, impact_values.name as impactName , " +
                    "rule_profile_type.profileRef,  rule_answers.answer " +
                    "from rule_answers left join rule_profile_type on rule_answers.ruleRef = rule_profile_type.ruleRef " +
                    "left join profile_type on profile_type.profileRef = rule_profile_type.profileRef " +
                    "left join impact_values on impact_values.impactRef = rule_profile_type.ruleImpactRef " +
                    "where rule_answers.answer = 4  and rule_profile_type.ruleImpactRef != 1 " +
                    "and rule_answers.auditEquipmentId in (select audit_object_id from AuditEquipments where audit_id = :auditId) " +
                    "group by  rule_profile_type.ruleImpactRef, rule_profile_type.profileRef")
    Single<List<NoAnsWithProfileAndImpact>> getNumberOfRulesAnsweredByNoGrpByImpactAndProfileForAudit(Long auditId);


    //select number of rules answered yes or doubt , has impact other than 1 (non concern)
    // & impact on any profile group by answer
    // this is used for the last row col1&2
    //return count : answer
    @Query(
            "select count (distinct (rule_answers.ruleRef)) as count , rule_answers.answer " +
                    " from  rule_profile_type left join rule_answers on rule_answers.ruleRef = rule_profile_type.ruleRef  " +
                    "where rule_answers.answer in (2,3) and rule_profile_type.ruleImpactRef != 1 " +
                    "and rule_answers.auditEquipmentId in (select audit_object_id from AuditEquipments where audit_id = :auditId) " +
                    " group by rule_answers.answer")
    Single<List<AnswerCount>> getTotalNumRulesYesOrDoubtHasAnyImpact(Long auditId);


    //select number of rules answered No , has impact other than 1 (non concern)
    // & impact on any profile group by answer
    // this is used for the last row & last cols with impact name
    // return count : impactName
    @Query(
            "select count (distinct (rule_answers.ruleRef)) as count , impact_values.name as impactName  " +
                    "from rule_profile_type left join rule_answers on rule_answers.ruleRef = rule_profile_type.ruleRef " +
                    "left join impact_values on impact_values.impactRef = rule_profile_type.ruleImpactRef  " +
                    "where rule_answers.answer = 4 and rule_profile_type.ruleImpactRef !=1 " +
                    "and rule_answers.auditEquipmentId in (select audit_object_id from AuditEquipments where audit_id = :auditId) " +
                    "group by rule_profile_type.ruleImpactRef ")
    Single<List<CountImpactName>> getTotalNumRulesNoHasAnyImpact(Long auditId);

    //Get Anomalies which all rules which answered with (NOK, BLOCKING,ANNOYING,DOUBT)
    // Get All rules with answer (D,B,NOK,A) for auditEquipment by id

    @Query("select rule.*" +
            "from rule_answers " +
            "left join rule on rule.ruleRef =rule_answers.ruleRef " +
            "where answer in (3 ,4 ,5,6) and " +
            "rule_answers.auditEquipmentId =:equipmentId")
    Single<List<Rule>> getAnomaliesAuditEquipment(Long equipmentId);
}
