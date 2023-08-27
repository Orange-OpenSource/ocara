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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocReportProfileQuestionsRulesAnswersModel {

    String profileRef;
    String profileName;
    Map<String, ArrayList<UnVerifiedRuleAnswerWithQuestionName>> questionRulesMap; //questionRef & rule answers
//    String questionName;
//    String ruleRef;
//    String ruleLable;
//    String ruleAnswer;
//    String ruleImpactRef;
//    String impactName;
//    String questionRef;


    public DocReportProfileQuestionsRulesAnswersModel(String profileRef, String profileName
//            , String questionName, String ruleRef, String ruleLable, String ruleAnswer, String ruleImpactRef, String impactName, String questionRef
    ) {
        this.profileName = profileName;
        this.profileRef = profileRef;
        questionRulesMap = new HashMap < String, ArrayList<UnVerifiedRuleAnswerWithQuestionName> >();
//        this.questionName = questionName;
//        this.ruleRef = ruleRef;
//        this.ruleLable = ruleLable;
//        this.ruleAnswer = ruleAnswer;
//        this.ruleImpactRef = ruleImpactRef;
//        this.impactName = impactName;
//        this.questionRef = questionRef;
    }

    public Map<String, ArrayList<UnVerifiedRuleAnswerWithQuestionName>> getQuestionRulesMap() {
        return questionRulesMap;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileRef() {
        return profileRef;
    }

    public void setProfileRef(String profileRef) {
        this.profileRef = profileRef;
    }


//    public String getQuestionRef() {
//        return questionRef;
//    }
//
//    public void setQuestionRef(String questionRef) {
//        this.questionRef = questionRef;
//    }

//    public class DocReportQuestionWithUnVerifiedRuleAnswer {
//        String questionName;
//
//        public String getQuestionName() {
//            return questionName;
//        }
//
//        public void setQuestionName(String questionName) {
//            this.questionName = questionName;
//        }
//
//    }


    public static class UnVerifiedRuleAnswerWithQuestionName {
        String ruleRef;
        String ruleLable;
        Answer ruleAnswer;
        String ruleImpactRef;
        String impactName;
        String questionName;

        public UnVerifiedRuleAnswerWithQuestionName(String ruleRef, String ruleLable, Answer ruleAnswer, String ruleImpactRef, String impactName, String questionName) {

            this.ruleRef = ruleRef;
            this.ruleLable = ruleLable;
            this.ruleAnswer = ruleAnswer;
            this.ruleImpactRef = ruleImpactRef;
            this.impactName = impactName;
            this.questionName = questionName;
        }

        public String getRuleRef() {
            return ruleRef;
        }

        public void setRuleRef(String ruleRef) {
            this.ruleRef = ruleRef;
        }

        public String getRuleLable() {
            return ruleLable;
        }

        public void setRuleLable(String ruleLable) {
            this.ruleLable = ruleLable;
        }

        public Answer getRuleAnswer() {
            return ruleAnswer;
        }

        public void setRuleAnswer(Answer ruleAnswer) {
            this.ruleAnswer = ruleAnswer;
        }

        public String getRuleImpactRef() {
            return ruleImpactRef;
        }

        public void setRuleImpactRef(String ruleImpactRef) {
            this.ruleImpactRef = ruleImpactRef;
        }

        public String getImpactName() {
            return impactName;
        }

        public void setImpactName(String impactName) {
            this.impactName = impactName;
        }

        public String getQuestionName() {
            return questionName;
        }

        public void setQuestionName(String questionName) {
            this.questionName = questionName;
        }
    }
}
