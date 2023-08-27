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

import java.util.ArrayList;
import java.util.List;

public class QuestionAnswerModel {
    private Answer answer;
    private QuestionModel question;
    private final List<RuleAnswerModel> ruleAnswers = new ArrayList<>();
    private String equipmentRef;

    public QuestionAnswerModel(Answer answer, QuestionModel question, String equipmentRef) {
        this.answer = answer;
        this.equipmentRef = equipmentRef;
        this.question = question;
    }

    public String getEquipmentRef() {
        return equipmentRef;
    }

    public void setEquipmentRef(String equipmentRef) {
        this.equipmentRef = equipmentRef;
    }

    public void addRuleAnswer(RuleAnswerModel ruleAnswerModel) {
        ruleAnswers.add(ruleAnswerModel);
    }

    public List<RuleAnswerModel> getRuleAnswers() {
        return ruleAnswers;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public QuestionModel getQuestion() {
        return question;
    }

    public void setQuestion(QuestionModel question) {
        this.question = question;
    }

    public void updateAnswer(){
        answer = Answer.NO_ANSWER;
        for(RuleAnswerModel ruleAnswer:ruleAnswers){
            checkIfThisRuleAnswerIsBigger(ruleAnswer);
        }
    }

    private void checkIfThisRuleAnswerIsBigger(RuleAnswerModel ruleAnswer) {
        if(ruleAnswer.getAnswer().ordinal() > answer.ordinal()){
            answer = ruleAnswer.getAnswer();
        }
    }
//    public enum Answer {
//        NO_ANSWER,
//        NOT_APPLICABLE,
//        OK,
//        DOUBT,
//        NOK,
//        ANNOYING,
//        BLOCKING,
//    }

}
