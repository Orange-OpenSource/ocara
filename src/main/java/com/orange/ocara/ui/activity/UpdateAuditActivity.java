/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.activity;

import android.view.Menu;

import com.orange.ocara.R;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

@EActivity(resName="activity_edit_audit")
public class UpdateAuditActivity extends EditAuditActivity {

    @Override
    protected boolean isChildActivity() {
        return true;
    }

    // If no help page is proposed is hide item showhelp
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean b = super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.action_help).setVisible(false);
        return b;
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    @Override
    void auditUpdated() {
        finish(); // terminate this activity
    }

}
