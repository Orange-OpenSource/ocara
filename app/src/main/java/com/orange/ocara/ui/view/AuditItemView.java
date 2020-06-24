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
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orange.ocara.R;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.ui.widget.SiteAutoCompleteView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.text.DateFormat;

@EViewGroup(R.layout.audit_item)
public class AuditItemView extends RelativeLayout {

    private final Context mContext;
    @ViewById(R.id.status_icon)
    ImageView statusIcon;
    @ViewById(R.id.name)
    TextView name;
    @ViewById(R.id.ruleset)
    TextView ruleSet;
    @ViewById(R.id.status_text_date)
    TextView statusTextDate;
    @ViewById(R.id.site)
    TextView site;
    @ViewById(R.id.action_export_audit_docx)
    View exportAuditDocx;
    @ViewById(R.id.more)
    View more;

    public AuditItemView(Context context) {
        super(context);
        mContext = context;
    }

    public void bind(AuditEntity audit) {
        final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        final String formatedDate = dateFormat.format(audit.getDate());
        name.setText(mContext.getString(R.string.audit_item_name_format, audit.getName(), audit.getVersion()));
        final RulesetEntity auditRuleSet = audit.getRuleSet();
        if (auditRuleSet != null) {
            this.ruleSet.setText(mContext.getString(R.string.audit_item_ruleset_format, auditRuleSet.getType(), auditRuleSet.getVersion()));
        }
        site.setText(SiteAutoCompleteView.format(audit.getSite()));

        switch (audit.getStatus()) {
            case IN_PROGRESS:
                statusIcon.setImageResource(R.drawable.ic_status_in_progress);
                statusTextDate.setText(mContext.getString(R.string.audit_item_status_in_progress_format, formatedDate));
                break;
            case TERMINATED:
            default:
                statusIcon.setImageResource(R.drawable.ic_status_terminated);
                statusTextDate.setText(mContext.getString(R.string.audit_item_status_terminated_format, formatedDate));
                break;
        }
    }

    public View getExportAuditDocx() {
        return exportAuditDocx;
    }

    public View getMore() {
        return more;
    }

}
