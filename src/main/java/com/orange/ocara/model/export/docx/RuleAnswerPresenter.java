/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model.export.docx;

import com.orange.ocara.model.RuleAnswer;
import com.orange.ocara.modelStatic.Response;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
class RuleAnswerPresenter extends Presenter<RuleAnswer> {

    public RuleAnswerPresenter(RuleAnswer ruleAnswer) {
        super(ruleAnswer);
    }

    public String getId() {
        return notNull(value.getRule().getId());
    }

    public String getDescription() {
        return toHtmlNotNull(value.getRule().getDescription());
    }

    public ResponsePresenter getResponse() {
        return new ResponsePresenter(value.getResponse());
    }

    public boolean isAnomalyOrDoubt() {
        Response response = value.getResponse();
        return response.equals(Response.NOK) || response.equals(Response.DOUBT);
    }

    public boolean getBlocking() {
        boolean result = value.ruleIsBlocking();
        return result;
    }
}
