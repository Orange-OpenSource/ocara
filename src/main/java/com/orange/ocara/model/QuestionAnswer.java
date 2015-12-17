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
import com.orange.ocara.modelStatic.Question;
import com.orange.ocara.modelStatic.Response;
import com.orange.ocara.modelStatic.RuleSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Table(name = QuestionAnswer.TABLE_NAME)
@Data
@ToString(exclude = {"auditObject"})
@EqualsAndHashCode(callSuper = true, exclude = {"auditObject"})
public class QuestionAnswer extends Model implements Refreshable {

    static final String TABLE_NAME = "questionAnswers";

    static final String COLUMN_QUESTION_ID = "questionId";
    static final String COLUMN_AUDIT_OJBECT = "auditObject";

    @Column(name = COLUMN_QUESTION_ID, notNull = true)
    private String questionId;
    @Column(name = COLUMN_AUDIT_OJBECT, onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private AuditObject auditObject;

    private List<RuleAnswer> ruleAnswers = new ArrayList<RuleAnswer>();


    private Response response = null;

//    QuestionAnswer next;

    /**
     * Default constructor. Needed by ORM
     */
    public QuestionAnswer() {
        super();
    }

    /**
     * Constructor.
     *
     * @param auditObject AuditObject
     * @param question    Question
     */
    public QuestionAnswer(AuditObject auditObject, Question question) {
        super();

        this.auditObject = auditObject;
        this.questionId = question.getQuestionId();
    }

    /**
     * @return Associated RuleSet
     */
    public RuleSet getRuleSet() {
        return auditObject.getRuleSet();
    }

    /**
     * @return Associated Question
     */
    public Question getQuestion() {
        return getRuleSet().getQuestionById(questionId);
    }

    public Response getResponse() {
        if (response == null) {
            this.response = computeResponse();
        }
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    /**
     * Returns true if this question contains at least one blocking rule with 'NOK' answer
     */
    public boolean hasAtLeastOneBlockingRule() {
        for(RuleAnswer ruleAnswer : getRuleAnswers()) {
            if (ruleAnswer.isBlocking()) {
                return true;
            }
        }
        return false;
    }

    /**
     * To compute AccessibilityValue by Response
     *
     * @param result AccessibilityValue by Response
     */
    public void computeStatsByHandicap(Map<String, AccessibilityStats> result) {
        final List<RuleAnswer> ruleAnswers = getRuleAnswers();
        for (RuleAnswer ruleAnswer : ruleAnswers) {
            ruleAnswer.computeStatsByHandicap(result);
        }
    }

    @Override
    public void refresh(RefreshStrategy strategy) {

        if (strategy.getDepth() != null && RefreshStrategy.DependencyDepth.RULE_ANSWER.compareTo(strategy.getDepth()) <= 0) {
            refreshRuleAnswers();

            for (RuleAnswer ruleAnswer : getRuleAnswers()) {
                ruleAnswer.refresh(strategy);
            }

            updateReponse();
        }
    }

    private void refreshRuleAnswers() {
        ruleAnswers.clear();
        ruleAnswers.addAll(getMany(RuleAnswer.class, RuleAnswer.COLUMN_QUESTION_ANSWER));
    }




    /* package */ void updateReponse() {
        this.response = computeResponse();
        auditObject.updateReponse();
    }

    /**
     * To compute the response from rules.
     *
     * @return computed response.
     */
    /* package */ Response computeResponse() {

        if (ruleAnswers.size() == 0) {
            return Response.OK;
        }

        Response result = Response.NotApplicable;

        for (RuleAnswer ruleAnswer : getRuleAnswers()) {
            Response ruleResponse = ruleAnswer.getResponse();
            result = Response.max(result, ruleResponse);
        }
        return result;

    }




}
