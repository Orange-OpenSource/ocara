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



import com.orange.ocara.domain.models.RuleAnswerModel;
import com.orange.ocara.utils.enums.Answer;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
class RuleAnswerPresenter extends Presenter<RuleAnswerModel> {

    public RuleAnswerPresenter(RuleAnswerModel ruleAnswer) {
        super(ruleAnswer);
    }

    public String getId() {
        return notNull(value.getRule().getRef());
    }

    public String getDescription() {
        return toHtmlNotNull(value.getRule().getLabel());
    }

    public ResponsePresenter getResponse() {
        return new ResponsePresenter(value.getAnswer());
    }

    public boolean isAnomalyOrDoubt() {
        Answer response = value.getAnswer();
        return response.equals(Answer.NOK) || response.equals(Answer.DOUBT);
    }

    public boolean getBlocking() {
        //   TOOD boolean result = value.ruleIsBlocking();
        return false;
    }
}
