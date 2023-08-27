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
import com.orange.ocara.domain.models.QuestionAnswerModel;
import com.orange.ocara.domain.models.RuleAnswerModel;
import com.orange.ocara.utils.enums.Answer;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
class QuestionAnswerPresenter extends Presenter<QuestionAnswerModel> {
    private final List<RuleAnswerPresenter> rulesInAnomalyOrDoubt = new ArrayList<RuleAnswerPresenter>();
    AuditEquipmentModel auditEq;

    QuestionAnswerPresenter(AuditEquipmentModel auditEq, QuestionAnswerModel answer) {
        super(answer);
        this.auditEq = auditEq;
        buildAnomaliesOrDoubts();
    }

    public String getAuditObjectName() {
        return notNull(getAuditObjectName(auditEq));
    }

    protected String getAuditObjectName(AuditEquipmentModel auditObject) {

        if (auditObject.getParent() == null) {
            return auditObject.getEquipment().getName();
        }
        return auditObject.getParent().getEquipment().getName();

    }


    public String getTitle() {
        return notNull(value.getQuestion().getLabel());
    }

    public String getAuditObjectId() {
        return notNull("" + auditEq.getId());
    }

    public ResponsePresenter getResponse() {

        return new ResponsePresenter(value.getAnswer());

    }

    private void buildAnomaliesOrDoubts() {
        rulesInAnomalyOrDoubt.clear();

        for (RuleAnswerModel ruleAnswer : value.getRuleAnswers()) {
            RuleAnswerPresenter ruleAnswerPresenter = new RuleAnswerPresenter(ruleAnswer);

            rulesInAnomalyOrDoubt.add(ruleAnswerPresenter);
//            if (ruleAnswerPresenter.isAnomalyOrDoubt()) {
//                rulesInAnomalyOrDoubt.add(ruleAnswerPresenter);
//            }
        }
    }


    public boolean getBlocking() {
        for (RuleAnswerPresenter ruleAnswerPresenter : rulesInAnomalyOrDoubt) {
            if (ruleAnswerPresenter.getBlocking()) {
                return true;
            }
        }
        return false;
    }

    public boolean isAnomalyOrDoubt() {
       Answer response = value.getAnswer();
        return Answer.NOK == response
                ||Answer.ANNOYING == response
                ||Answer.BLOCKING == response
                ||Answer.DOUBT == response
                ||Answer.OK == response
                || Answer.NO_ANSWER == response;
    }
}
