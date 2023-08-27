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

public class RuleAnswerModel {

    private Answer answer;
    private RuleModel rule;
    private AuditModel audit;
    private QuestionAnswerModel questionAnswer;

    public RuleAnswerModel(RuleModel rule, Answer answer) {
        this.rule = rule;
        this.answer = answer;
    }

    public QuestionAnswerModel getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(QuestionAnswerModel questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public RuleModel getRule() {
        return rule;
    }

    public void setRule(RuleModel rule) {
        this.rule = rule;
    }

    public AuditModel getAudit() {
        return audit;
    }

    public void setAudit(AuditModel audit) {
        this.audit = audit;
    }

    public static class RuleAnswerModelBuilder{

    }
}
