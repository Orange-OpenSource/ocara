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

import com.orange.ocara.business.model.RuleGroupModel;

/**
 * a UiModel for rules'groups
 */
public class OrderedRuleGroupUiModel {

    private final CharSequence groupText;

    private final long groupId;

    private final int order;

    public OrderedRuleGroupUiModel(RuleGroupModel model) {

        groupText = Html.fromHtml(model.getLabel());

        groupId = model.getId();

        this.order = model.getIndex();
    }

    public CharSequence getGroupText() {
        return groupText;
    }

    public int getOrder() {
        return order;
    }

    public long getGroupId() {
        return groupId;
    }
}
