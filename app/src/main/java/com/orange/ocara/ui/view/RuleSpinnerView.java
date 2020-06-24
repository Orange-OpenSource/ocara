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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orange.ocara.R;
import com.orange.ocara.data.net.model.RulesetEntity;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by flfb1094 on 14/04/2017.
 */
@EViewGroup(R.layout.view_rule_spinner)
public class RuleSpinnerView extends LinearLayout {

    @ViewById(R.id.ruleset_name)
    TextView mRulesetName;

    public RuleSpinnerView(final Context context) {
        super(context);

    }

    public void bind(RulesetEntity rulsetDetails) {
        if (mRulesetName != null) {
            this.mRulesetName.setText(getContext().getString(R.string.audit_item_ruleset_format, rulsetDetails.getType(), rulsetDetails.getVersion()));
        }
    }

}
