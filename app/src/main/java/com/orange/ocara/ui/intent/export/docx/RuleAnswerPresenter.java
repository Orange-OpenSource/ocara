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

import com.orange.ocara.data.cache.model.ResponseModel;
import com.orange.ocara.data.cache.model.RuleAnswerEntity;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
class RuleAnswerPresenter extends Presenter<RuleAnswerEntity> {

    public RuleAnswerPresenter(RuleAnswerEntity ruleAnswer) {
        super(ruleAnswer);
    }

    public String getId() {
        return notNull(value.getRule().getReference());
    }

    public String getDescription() {
        return toHtmlNotNull(value.getRule().getLabel());
    }

    public ResponsePresenter getResponse() {
        return new ResponsePresenter(value.getResponse());
    }

    public boolean isAnomalyOrDoubt() {
        ResponseModel response = value.getResponse();
        return response.equals(ResponseModel.NOK) || response.equals(ResponseModel.DOUBT);
    }

    public boolean getBlocking() {
        //   TOOD boolean result = value.ruleIsBlocking();
        return false;
    }
}
