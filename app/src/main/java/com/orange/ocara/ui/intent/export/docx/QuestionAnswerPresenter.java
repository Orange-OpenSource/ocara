/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2020 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.ui.intent.export.docx;

import com.orange.ocara.data.cache.model.AuditObjectEntity;
import com.orange.ocara.data.cache.model.QuestionAnswerEntity;
import com.orange.ocara.data.cache.model.ResponseModel;
import com.orange.ocara.data.cache.model.RuleAnswerEntity;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
class QuestionAnswerPresenter extends Presenter<QuestionAnswerEntity> {
    private final List<RuleAnswerPresenter> rulesInAnomalyOrDoubt = new ArrayList<RuleAnswerPresenter>();

    QuestionAnswerPresenter(QuestionAnswerEntity answer) {
        super(answer);

        buildAnomaliesOrDoubts();
    }

    public String getAuditObjectName() {
        return notNull(getAuditObjectName(value.getAuditObject()));
    }

    protected String getAuditObjectName(AuditObjectEntity auditObject) {

        if (auditObject.getParent() == null) {
            return auditObject.getName();
        }
        return auditObject.getParent().getName();

    }


    public String getTitle() {
        return notNull(value.getQuestion().getLabel());
    }

    public String getAuditObjectId() {
        return notNull("" + value.getAuditObject().getId());
    }

    public ResponsePresenter getResponse() {

        return new ResponsePresenter(value.getResponse());

    }

    private void buildAnomaliesOrDoubts() {
        rulesInAnomalyOrDoubt.clear();

        for (RuleAnswerEntity ruleAnswer : value.getRuleAnswers()) {
            RuleAnswerPresenter ruleAnswerPresenter = new RuleAnswerPresenter(ruleAnswer);

            if (ruleAnswerPresenter.isAnomalyOrDoubt()) {
                rulesInAnomalyOrDoubt.add(ruleAnswerPresenter);
            }
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
        ResponseModel response = value.getResponse();
        return ResponseModel.NOK == response
                || ResponseModel.ANNOYING == response
                || ResponseModel.BLOCKING == response
                || ResponseModel.DOUBT == response;
    }
}
