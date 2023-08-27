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


package com.orange.ocara.domain.models;

import com.orange.ocara.utils.enums.Answer;

public class QuestionRuleAnswer {
    private String questionReference;
    private String ruleReference;
    private int auditEquipmentId;
    private Answer answer;

    private QuestionRuleAnswer(String questionReference, String ruleReference, int auditEquipmentId, Answer answer) {
        this.questionReference = questionReference;
        this.ruleReference = ruleReference;
        this.auditEquipmentId = auditEquipmentId;
        this.answer = answer;
    }

    public String getQuestionReference() {
        return questionReference;
    }

    public String getRuleReference() {
        return ruleReference;
    }

    public int getAuditEquipmentId() {
        return auditEquipmentId;
    }

    public Answer getAnswer() {
        return answer;
    }

    public static class QuestionRuleAnswerBuilder {
        private String questionReference;
        private String ruleReference;
        private int auditEquipmentId;
        private Answer answer;

        public QuestionRuleAnswerBuilder setQuestionReference(String questionReference) {
            this.questionReference = questionReference;
            return this;
        }

        public QuestionRuleAnswerBuilder setRuleReference(String ruleReference) {
            this.ruleReference = ruleReference;
            return this;
        }

        public QuestionRuleAnswerBuilder setAuditEquipmentId(int auditEquipmentId) {
            this.auditEquipmentId = auditEquipmentId;
            return this;
        }

        public QuestionRuleAnswerBuilder setAnswer(Answer answer) {
            this.answer = answer;
            return this;
        }

        public QuestionRuleAnswer build() {
            return new QuestionRuleAnswer(questionReference, ruleReference, auditEquipmentId, answer);
        }
    }
}
