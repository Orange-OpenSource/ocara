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

package com.orange.ocara.data.oldEntities;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import com.orange.ocara.utils.tools.RefreshStrategy;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Table(name = QuestionAnswerEntity.TABLE_NAME)
@Data
@ToString(exclude = {"auditObject", "ruleAnswers"})
@EqualsAndHashCode(callSuper = true, exclude = {"auditObject", "ruleAnswers"})
public class QuestionAnswerEntity extends Model implements Refreshable, RulesGroupResponse {

    public static final String TABLE_NAME = "questionAnswers";

    public static final String COLUMN_QUESTION_ID = "questionId";
    public static final String COLUMN_AUDIT_OBJECT = "auditObject";
    @Column(name = COLUMN_QUESTION_ID, notNull = true)
    private String questionId;
    @Column(name = COLUMN_AUDIT_OBJECT, onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private AuditObjectEntity auditObject;
    private List<RuleAnswerEntity> ruleAnswers = new ArrayList<>();
    private ResponseModel response = null;

    /**
     * Default constructor. Needed by ORM
     */
    public QuestionAnswerEntity() {
        super();
    }

    /**
     * Constructor.
     *
     * @param auditObject AuditObject
     * @param question    Question
     */
    public QuestionAnswerEntity(AuditObjectEntity auditObject, QuestionEntity question) {
        super();
        this.auditObject = auditObject;
        this.questionId = question.getReference();
    }

    public String getQuestionId() {
        return questionId;
    }

    public List<RuleAnswerEntity> getRuleAnswers() {
        return getMany(RuleAnswerEntity.class, RuleAnswerEntity.COLUMN_QUESTION_ANSWER);
    }

    /**
     * @return Associated RuleSet
     */
    public RulesetEntity getRuleSet() {

        return auditObject.getRuleSet();
    }

    /**
     * @return Associated Question
     */
    @Override
    public QuestionEntity getQuestion() {
        RulesetEntity ruleset = getRuleSet();
        List<QuestionEntity> questions = ruleset.getQuestionsDb();

        for (QuestionEntity question : questions) {
            if (question != null && question.getReference().equals(questionId)) {
                return question;
            }
        }
        return null;
    }

    @Override
    public ResponseModel getResponse() {
        if (response == null) {
            this.response = computeResponse();
        }
        return response;
    }

    public void setResponse(ResponseModel response) {
        this.response = response;
    }

    /**
     * @return true if this question contains at least one blocking rule with 'NOK' answer
     */
    boolean hasAtLeastOneBlockingRule() {
        for (RuleAnswerEntity ruleAnswer : getRuleAnswers()) {
            if (ruleAnswer.isNotOk()) {
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
//    void computeStatsByHandicap(Map<String, AccessibilityStatsUiModel> result) {
//        Timber.i("Message=Updating AccessibilityStats for QuestionAnswer;QuestionAnswerId=%d", getId());
//        final List<RuleAnswerEntity> list = getRuleAnswers();
//        for (RuleAnswerEntity ruleAnswer : list) {
//            ruleAnswer.computeStatsByHandicap(result);
//        }
//    }

    @Override
    public void refresh(RefreshStrategy strategy) {
        if (strategy.getDepth() != null && RefreshStrategy.DependencyDepth.RULE_ANSWER.compareTo(strategy.getDepth()) <= 0) {
            refreshRuleAnswers();

            for (RuleAnswerEntity ruleAnswer : getRuleAnswers()) {
                ruleAnswer.refresh(strategy);
            }

            updateResponse();
        }
    }

    private void refreshRuleAnswers() {
        ruleAnswers.clear();
        ruleAnswers.addAll(getMany(RuleAnswerEntity.class, RuleAnswerEntity.COLUMN_QUESTION_ANSWER));
    }

    void updateResponse() {
        this.response = computeResponse();
        auditObject.updateResponse();
    }

    /**
     * To compute the response from rules.
     *
     * @return computed response.
     */
    ResponseModel computeResponse() {

        final List<RuleAnswerEntity> list = getRuleAnswers();
        ResponseModel result = ResponseModel.NOT_APPLICABLE;

        if (list.isEmpty()) {
            result = ResponseModel.OK;
        } else {
            for (RuleAnswerEntity ruleAnswer : list) {
                ResponseModel ruleResponse = ruleAnswer.getResponse();
                result = ResponseModel.max(result, ruleResponse);
            }
        }

        return result;

    }
}
