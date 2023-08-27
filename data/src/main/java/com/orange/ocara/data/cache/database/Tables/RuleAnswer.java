package com.orange.ocara.data.cache.database.Tables;
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
import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.orange.ocara.utils.enums.Answer;


@Entity(tableName = "rule_answers", primaryKeys = {"ruleRef", "auditEquipmentId", "questionRef"})
public class RuleAnswer {
    private Answer answer;
    @NonNull
    private String ruleRef;
    private int auditEquipmentId;
    @NonNull
    private String questionRef;

    public RuleAnswer(int auditEquipmentId, Answer answer, String ruleRef, String questionRef) {
        this.answer = answer;
        this.ruleRef = ruleRef;
        this.auditEquipmentId = auditEquipmentId;
        this.questionRef = questionRef;
    }

    public int getAuditEquipmentId() {
        return auditEquipmentId;
    }

    public void setAuditEquipmentId(int auditEquipmentId) {
        this.auditEquipmentId = auditEquipmentId;
    }

    public String getQuestionRef() {
        return questionRef;
    }

    public void setQuestionRef(String questionRef) {
        this.questionRef = questionRef;
    }

    public String getRuleRef() {
        return ruleRef;
    }

    public void setRuleRef(String ruleRef) {
        this.ruleRef = ruleRef;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public static class RuleAnswerBuilder{

        private int auditEquipmentId;
        private Answer answer;
        private String ruleRef;
        private String questionRef;

        public RuleAnswerBuilder setAuditEquipmentId(int auditEquipmentId) {
            this.auditEquipmentId = auditEquipmentId;
            return this;
        }

        public RuleAnswerBuilder setAnswer(Answer answer) {
            this.answer = answer;
            return this;
        }

        public RuleAnswerBuilder setRuleRef(String ruleRef) {
            this.ruleRef = ruleRef;
            return this;
        }

        public RuleAnswerBuilder setQuestionRef(String questionRef) {
            this.questionRef = questionRef;
            return this;
        }

        public RuleAnswer createRuleAnswer() {
            return new RuleAnswer(auditEquipmentId, answer, ruleRef, questionRef);
        }
    }
}
