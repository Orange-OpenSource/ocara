/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model.export.docx;

import com.orange.ocara.model.AuditObject;
import com.orange.ocara.model.QuestionAnswer;
import com.orange.ocara.model.RuleAnswer;
import com.orange.ocara.modelStatic.Response;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
class QuestionAnswerPresenter extends Presenter<QuestionAnswer> {
    private final List<RuleAnswerPresenter> rulesInAnomalyOrDoubt = new ArrayList<RuleAnswerPresenter>();

    QuestionAnswerPresenter(QuestionAnswer answer) {
        super(answer);

        buildAnomaliesOrDoubts();
    }

    public String getAuditObjectName() {
        return notNull(getAuditObjectName(value.getAuditObject()));
    }

    protected String getAuditObjectName(AuditObject auditObject) {

        if (auditObject.getParent() == null) {
            return auditObject.getName();
        }
        return auditObject.getParent().getName();

    }


    public String getTitle() {
        return notNull(value.getQuestion().getTitle());
    }

    public String getAuditObjectId() {
        return notNull("" + value.getAuditObject().getId());
    }

    public ResponsePresenter getResponse() {

        return new ResponsePresenter( value.getResponse() );

    }

    private void buildAnomaliesOrDoubts() {
        rulesInAnomalyOrDoubt.clear();

        for (RuleAnswer ruleAnswer : value.getRuleAnswers()) {
            RuleAnswerPresenter ruleAnswerPresenter = new RuleAnswerPresenter(ruleAnswer);

            if (ruleAnswerPresenter.isAnomalyOrDoubt()) {
                rulesInAnomalyOrDoubt.add(ruleAnswerPresenter);
            }
        }
    }


    public boolean getBlocking() {
        for(RuleAnswerPresenter ruleAnswerPresenter : rulesInAnomalyOrDoubt) {
            if (ruleAnswerPresenter.getBlocking()) {
                return true;
            }
        }
        return false;
    }

    public boolean isAnomalyOrDoubt() {
        Response response = value.getResponse();
        return response.equals(Response.NOK) || response.equals(Response.DOUBT);
    }
}
