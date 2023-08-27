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

package com.orange.ocara.domain.docexport.models;

import com.orange.ocara.domain.models.DocReportProfileQuestionsRulesAnswersModel;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
class AuditProfileAnomalyQuestionPresenter extends Presenter<ArrayList<DocReportProfileQuestionsRulesAnswersModel.UnVerifiedRuleAnswerWithQuestionName>> {

    private final List<AuditProfileAnomalyRulePresenter> rulesProfileInAnomalyOrDoubt = new ArrayList<>();
    String questionRef;
    String questionName;

    AuditProfileAnomalyQuestionPresenter(ArrayList<DocReportProfileQuestionsRulesAnswersModel.UnVerifiedRuleAnswerWithQuestionName> list, String questionRef, String questionName) {
        super(list);
        this.questionName = questionName;
        this.questionRef = questionRef;

        buildAnomaliesOrDoubts();
    }

    public String getQuestionName() {
        return questionName;
    }



    public String getTitle() {
        return getQuestionName();
    }


    private void buildAnomaliesOrDoubts() {
        rulesProfileInAnomalyOrDoubt.clear();

        for (DocReportProfileQuestionsRulesAnswersModel.UnVerifiedRuleAnswerWithQuestionName ruleAnswer : value) {
            AuditProfileAnomalyRulePresenter ruleAnswerPresenter = new AuditProfileAnomalyRulePresenter(ruleAnswer);

            rulesProfileInAnomalyOrDoubt.add(ruleAnswerPresenter);
//            if (ruleAnswerPresenter.isAnomalyOrDoubt()) {
//                rulesInAnomalyOrDoubt.add(ruleAnswerPresenter);
//            }
        }
    }


}
