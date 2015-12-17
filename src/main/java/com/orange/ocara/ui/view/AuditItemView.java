/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orange.ocara.model.Audit;
import com.orange.ocara.ui.widget.SiteAutoCompleteView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.text.DateFormat;

@EViewGroup(resName="audit_item")
public class AuditItemView extends RelativeLayout {

    @ViewById(resName="status_icon")
    ImageView statusIcon;
    @ViewById(resName="name")
    TextView name;
    @ViewById(resName="ruleset")
    TextView ruleSet;
    @ViewById(resName="status_text_date")
    TextView statusTextDate;
    @ViewById(resName="site")
    TextView site;
    @ViewById(resName="action_export_audit_docx")
    View exportAuditDocx;
    @ViewById(resName="more")
    View more;

    public AuditItemView(Context context) {
        super(context);
    }

    public void bind(Audit audit) {
        final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        final String formatedDate = dateFormat.format(audit.getDate());

        name.setText(String.format("%s (V%d)", audit.getName(), audit.getVersion()));
        ruleSet.setText(getContext().getString(com.orange.ocara.R.string.audit_item_ruleset_format, audit.getRuleSet().getType(), audit.getRuleSet().getVersion()));
        site.setText(SiteAutoCompleteView.format(audit.getSite(), true));

        switch (audit.getStatus()) {
            case IN_PROGRESS:
                statusIcon.setImageResource(com.orange.ocara.R.drawable.ic_status_in_progress);
                statusTextDate.setText(getContext().getString(com.orange.ocara.R.string.audit_item_status_in_progress_format, formatedDate));
                break;
            case TERMINATED:
            default:
                statusIcon.setImageResource(com.orange.ocara.R.drawable.ic_status_terminated);
                statusTextDate.setText(getContext().getString(com.orange.ocara.R.string.audit_item_status_terminated_format, formatedDate));
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
