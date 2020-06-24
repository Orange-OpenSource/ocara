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

package com.orange.ocara.ui.binding;

import com.orange.ocara.business.model.RuleSetStat;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.ui.model.AuditFormUiModel;

/**
 * an implementation for {@link Validator} that checks if the given {@link AuditFormUiModel} has a
 * valid ruleset or not.
 */
public class RulesetStateValidator implements Validator<AuditFormUiModel> {

    @Override
    public void validate(AuditFormUiModel input, BindingResult result) {
        RulesetModel ruleset = input.getActualRuleset();
        if (ruleset == null) {
            result.rejectValue("ruleset shall not be null");
        } else if (!RuleSetStat.OFFLINE.equals(ruleset.getStat())) {
            result.rejectValue("ruleset has invalid state");
        }
    }
}
