package com.orange.ocara.data.cache.database.NonTables;
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
import androidx.room.Embedded;


import com.orange.ocara.data.cache.database.Tables.Question;
import com.orange.ocara.data.cache.database.Tables.Rule;
import com.orange.ocara.utils.enums.Answer;

public class QuestionWithRuleAnswer {
    @Embedded
    private Question question;
    @Embedded
    private Rule rule;
    private Answer answer= Answer.NO_ANSWER;
    private String objectReference="";

    public QuestionWithRuleAnswer(Question question, Rule rule) {
        this.question = question;
        this.rule = rule;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public String getObjectReference() {
        return objectReference;
    }

    public void setObjectReference(String objectReference) {
        this.objectReference = objectReference;
    }

    @Override
    public boolean equals(Object o) {
        QuestionWithRuleAnswer rhs = (QuestionWithRuleAnswer)o;
        return this.getRule().getReference().equals(rhs.getRule().getReference()) &&
                this.getQuestion().getReference().equals(rhs.getQuestion().getReference()) &&
                this.getObjectReference().equals(rhs.getObjectReference());
    }
}
