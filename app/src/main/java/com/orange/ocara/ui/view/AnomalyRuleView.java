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

package com.orange.ocara.ui.view;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orange.ocara.data.cache.model.RuleAnswerEntity;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(resName = "anomaly_rule")
public class AnomalyRuleView extends LinearLayout {

    @ViewById(resName = "rule_id")
    TextView ruleId;

    @ViewById(resName = "rule_title")
    TextView ruleTitle;


    /**
     * Constructor.
     *
     * @param context Context
     */
    public AnomalyRuleView(Context context) {
        this(context, null);
    }

    /**
     * Constructor.
     *
     * @param context Context
     * @param attrs   AttributeSet
     */
    public AnomalyRuleView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void bind(RuleAnswerEntity ruleAnswer) {
        ruleId.setText(ruleAnswer.getRule().getReference());
        ruleTitle.setText(Html.fromHtml(ruleAnswer.getRule().getLabel()));
    }
}
