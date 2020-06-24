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

package com.orange.ocara.ui.model;

import android.text.Html;

import com.orange.ocara.business.model.RuleModel;

/**
 * a UiModel for rules
 */
public class OrderedRuleUiModel {

    private final CharSequence ruleText;

    private final long ruleId;

    private final int order;

    private final boolean illustrated;

    public OrderedRuleUiModel(RuleModel model) {

        ruleId = model.getId();
        ruleText = Html.fromHtml(model.getLabel());
        this.order = model.getIndex();
        this.illustrated = model.isIllustrated();
    }

    public CharSequence getRuleText() {
        return ruleText;
    }

    public int getOrder() {
        return order;
    }

    public long getRuleId() {
        return ruleId;
    }

    public boolean isIllustrated() {
        return illustrated;
    }
}