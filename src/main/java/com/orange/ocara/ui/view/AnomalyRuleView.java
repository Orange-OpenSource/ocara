/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.view;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orange.ocara.model.RuleAnswer;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(resName="anomaly_rule")
public class AnomalyRuleView extends LinearLayout {

    @ViewById(resName="rule_id")
    TextView ruleId;

    @ViewById(resName="rule_title")
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

    public void bind(RuleAnswer ruleAnswer) {
        ruleId.setText(ruleAnswer.getRuleId());
        ruleTitle.setText(Html.fromHtml(ruleAnswer.getRule().getDescription()));
    }


}
