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

import com.orange.ocara.domain.models.AuditEquipmentModel;
import com.orange.ocara.domain.models.DocReportProfileQuestionsRulesAnswersModel;
import com.orange.ocara.utils.enums.Answer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
class AuditProfileAnomalyRulePresenter extends Presenter<DocReportProfileQuestionsRulesAnswersModel.UnVerifiedRuleAnswerWithQuestionName> {


    AuditProfileAnomalyRulePresenter(DocReportProfileQuestionsRulesAnswersModel.UnVerifiedRuleAnswerWithQuestionName obj) {
        super(obj);
    }

    public String getId() {
        return notNull(value.getRuleRef());
    }

    public String getDescription() {
        return toHtmlNotNull(value.getRuleLable());
    }

    public AuditProfileAnomalyImpactPresenter getResponse() {
        return new AuditProfileAnomalyImpactPresenter(value.getImpactName());
    }

    public boolean isAnomalyOrDoubt() {
//        Answer response = value.getRuleAnswer();
//        return response.equals(Answer.NOK) || response.equals(Answer.DOUBT);

        Answer response = value.getRuleAnswer();
        return Answer.NOK == response
                || Answer.ANNOYING == response
                || Answer.BLOCKING == response
                || Answer.DOUBT == response
                || Answer.OK == response;
    }

    public boolean getBlocking() {
        //   TOOD boolean result = value.ruleIsBlocking();
        return false;
    }

    public boolean getProfileBlocking() {
        return value.getRuleImpactRef().equals("3");
        //   TOOD boolean result = value.ruleIsBlocking();
//        return false;
    }
    public boolean getProfileAnnoying() {
        return value.getRuleImpactRef().equals("2");
        //   TOOD boolean result = value.ruleIsBlocking();
//        return false;
    }
}
