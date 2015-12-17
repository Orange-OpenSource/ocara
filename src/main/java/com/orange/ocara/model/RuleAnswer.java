/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.orange.ocara.modelStatic.Accessibility;
import com.orange.ocara.modelStatic.HandicapAccessibilities;
import com.orange.ocara.modelStatic.Question;
import com.orange.ocara.modelStatic.Response;
import com.orange.ocara.modelStatic.Rule;
import com.orange.ocara.modelStatic.RuleSet;

import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Table(name = RuleAnswer.TABLE_NAME)
@Data
@ToString(exclude = {"questionAnswer"})
@EqualsAndHashCode(callSuper = true, exclude = {"questionAnswer"})
public class RuleAnswer extends Model implements Refreshable {

    static final String TABLE_NAME = "ruleAnswers";

    static final String COLUMN_RULE_ID = "ruleId";
    static final String COLUMN_RESPONSE = "response";
    static final String COLUMN_QUESTION_ANSWER = "questionAnswer";

    @Column(name = COLUMN_RULE_ID, notNull = true)
    private String ruleId;
    @Column(name = COLUMN_RESPONSE)
    private Response response = Response.NoAnswer;  // not answered yet
    @Column(name = COLUMN_QUESTION_ANSWER, onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private QuestionAnswer questionAnswer;

    /**
     * Default constructor. Needed by ORM.
     */
    public RuleAnswer() {
        super();
    }

    /**
     * Constructor.
     *
     * @param questionAnswer QuestionAnswer
     * @param rule           Rule
     */
    public RuleAnswer(QuestionAnswer questionAnswer, Rule rule) {
        super();

        this.questionAnswer = questionAnswer;
        this.ruleId = rule.getId();
    }

    /**
     * @return Associated RuleSet
     */
    public RuleSet getRuleSet() {
        return questionAnswer.getRuleSet();
    }

    /**
     * @return Associated Rule
     */
    public Rule getRule() {
        return getQuestion().getRuleById(ruleId);
    }


    public boolean isBlocking() {
        return response.equals(Response.NOK) && ruleIsBlocking();
    }


    public boolean ruleIsBlocking() {

        return getRule().hasAccessibility(Accessibility.BLOCKING);
    }

    /**
     * @return Associated question
     */
    public Question getQuestion() {
        return getQuestionAnswer().getQuestion();
    }


    public void setResponse(Response response) {
        this.response = response;
    }

    public void updateResponse(Response response) {
        this.response = response;
        // triggers parent response update
        questionAnswer.updateReponse();
    }

    @Override
    public void refresh(RefreshStrategy strategy) {
    }


    public void computeStatsByHandicap(Map<String, AccessibilityStats> result) {
        final HandicapAccessibilities handicapAccessibilities = getRule().getHandicapAccessibilities();
        final Response response = getResponse();

        for (String handicapId : result.keySet()) {
            Accessibility accessibility = handicapAccessibilities.getHandicapAccessibility(handicapId);
            AccessibilityStats stats = result.get(handicapId);

            if (stats != null) {
                stats.compute(response, accessibility);
            }
        }
    }



}