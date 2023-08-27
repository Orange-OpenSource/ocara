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

import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import timber.log.Timber;

//import static com.orange.ocara.ui.model.AccessibilityStatsUiModel.NA_IMPACT_REF;

@Table(name = RuleAnswerEntity.TABLE_NAME)
@ToString(exclude = {"questionAnswer"})
@EqualsAndHashCode(callSuper = true, exclude = {"questionAnswer"})
public class RuleAnswerEntity extends Model implements Refreshable, RuleResponse {

    public static final String TABLE_NAME = "ruleAnswers";
    public static final String COLUMN_RULE_ID = "ruleId";
    public static final String COLUMN_RESPONSE = "response";
    public static final String COLUMN_QUESTION_ANSWER = "questionAnswer";
    @Column(name = COLUMN_RULE_ID, notNull = true)
    private String ruleId;
    @Column(name = COLUMN_RESPONSE)
    private ResponseModel response = ResponseModel.NO_ANSWER;  // not answered yet
    @Column(name = COLUMN_QUESTION_ANSWER, onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private QuestionAnswerEntity questionAnswer;
    /**
     * Default constructor. Needed by ORM.
     */
    public RuleAnswerEntity() {
        super();
    }
    /**
     * Constructor.
     *
     * @param questionAnswer QuestionAnswer
     * @param rule           Rule
     */
    public RuleAnswerEntity(QuestionAnswerEntity questionAnswer, RuleEntity rule) {
        super();

        this.questionAnswer = questionAnswer;
        this.ruleId = rule.getReference();
    }

    @Override
    public ResponseModel getResponse() {
        return response;
    }

    public void setResponse(ResponseModel response) {
        this.response = response;
    }

    public QuestionAnswerEntity getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(QuestionAnswerEntity questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    /**
     * @return Associated RuleSet
     */
    public RulesetEntity getRuleSet() {
        return questionAnswer.getRuleSet();
    }

    /**
     * @return Associated Rule
     */
    @Override
    public RuleEntity getRule() {

        return questionAnswer.getRuleSet().getRule(ruleId);
    }

    boolean isNotOk() {
        return response.equals(ResponseModel.NOK);
    }

    /**
     * @return Associated question
     */
    public QuestionEntity getQuestion() {
        return getQuestionAnswer().getQuestion();
    }

    public void updateResponse(ResponseModel response) {
        Timber.i("Message=Saving response for RuleAnswer;RuleAnswerId=%d;SavedResponse=%s", getId(), response);

        this.response = response;
        this.save();
        // triggers parent response update
        questionAnswer.updateResponse();
    }

    public void refresh(RefreshStrategy strategy) {
        Timber.d("RuleAnswer is being refreshed... but does nothing.");
    }

    /**
     * Report content is being calculated.
     *
     * @param result a {@link Map} that will contain some aggregation values.
     */
//    void computeStatsByHandicap(Map<String, AccessibilityStatsUiModel> result) {
//
//        RulesetEntity ruleSet = questionAnswer.getRuleSet();
//        RuleEntity ruleFromRef = ruleSet.getRule(ruleId);
//        List<RuleImpactEntity> ruleImpactDb = ruleFromRef.getRuleImpactDb();
//        Map<String, List<RuleImpactEntity>> ruleImpactMap = new HashMap<>();
//
//        for (RuleImpactEntity ruleImpact : ruleImpactDb) {
//            String profileTypeRef = ruleImpact.getProfileTypeRef();
//            if (!NA_IMPACT_REF.equals(ruleImpact.getImpactValueRef())) {
//                if (ruleImpactMap.containsKey(profileTypeRef)) {
//                    List<RuleImpactEntity> profileTypeImpacts = ruleImpactMap.get(profileTypeRef);
//                    profileTypeImpacts.add(ruleImpact);
//                    ruleImpactMap.put(profileTypeRef, profileTypeImpacts);
//                } else {
//                    List<RuleImpactEntity> profileTypeImpacts = new ArrayList<>();
//                    profileTypeImpacts.add(ruleImpact);
//                    ruleImpactMap.put(profileTypeRef, profileTypeImpacts);
//                }
//            }
//        }
//
//        final ResponseModel currentResponse = getResponse();
//        List<RuleImpactEntity> ruleImpacts;
//        for (String handicapId : result.keySet()) {
//            ruleImpacts = ruleImpactMap.get(handicapId);
//            if (result.containsKey(handicapId)) {
//                result.get(handicapId).compute(currentResponse, ruleImpacts);
//            }
//        }
//    }
}