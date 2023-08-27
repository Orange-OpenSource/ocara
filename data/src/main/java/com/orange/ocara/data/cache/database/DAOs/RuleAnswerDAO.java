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
import com.orange.ocara.data.cache.database.NonTables.RuleAnswerAndLabel;
import com.orange.ocara.data.cache.database.Tables.RuleAnswer;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface RuleAnswerDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(RuleAnswer answer);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<RuleAnswer> answer);

    @Query("select rule.*,questions.*,rule_answers.answer,AuditEquipments.objectReference " +
            "from rule left join rule_answers on rule.ruleRef=" +
            "rule_answers.ruleRef left join questions on " +
            "questions.questionRef=rule_answers.questionRef left join " +
            "AuditEquipments on rule_answers.auditEquipmentId = AuditEquipments.audit_object_id " +
            "where rule_answers.auditEquipmentId=:auditEquipmentId")
    Single<List<QuestionWithRuleAnswer>> getRulesAnswers(int auditEquipmentId);

    @Query("select * from rule_answers where auditEquipmentId=:auditEquipmentId")
    Single<List<RuleAnswer>> getRuleAnswersList(int auditEquipmentId);

    @Query("delete from rule_answers where auditEquipmentId IN (select AuditEquipments.audit_object_id from AuditEquipments left join audit on AuditEquipments.audit_id = audit.audit_id where audit.audit_id =:auditId)")
    Completable deleteRuleAnswersOfAudit(long auditId);

    @Query("delete from rule_answers where auditEquipmentId = :auditEquipment")
    Completable deleteRuleAnswersOfAuditEquipment(long auditEquipment);

    @Query("delete from rule_answers where auditEquipmentId in (:auditEquipment)")
    Completable deleteRuleAnswersOfAuditEquipments(List<Integer> auditEquipment);


    @Query("select rule.label,rule_answers.answer from rule left join rule_answers on rule.ruleRef=" +
            "rule_answers.ruleRef where answer in (3 ,4 ,5,6) and rule_answers.auditEquipmentId = :auditEqId")
    Single<List<RuleAnswerAndLabel>> getAnomalies(int auditEqId);
}
