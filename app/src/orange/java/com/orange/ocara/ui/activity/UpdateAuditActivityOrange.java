/*
 * Copyright (C) 2015 Orange
 * Authors: IDBA6391
 *
 * This software is the confidential and proprietary information of Orange.
 * You shall not disclose such confidential information and shall use it only
 * in accordance with the terms of the license agreement you entered into
 * with Orange.
 */

package com.orange.ocara.ui.activity;

import android.content.SharedPreferences;
import android.view.Menu;

import com.orange.ocara.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

/**
 * activity dedicated to the updating of audits
 */
@EActivity(resName = "activity_edit_audit")
public class UpdateAuditActivityOrange extends UpdateAuditActivity {

    public static final String MY_PREFERENCES = "MyPrefs";

    // If no help page is proposed is hide item showhelp
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean b = super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.action_help).setVisible(false);
        return b;
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    @Override
    public void showAuditUpdatedSuccessfully() {
        finish(); // terminate this activity
    }

    @AfterViews
    void SetUpAuthor() {
        SharedPreferences sharedPreferences = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        String lastnamePref = sharedPreferences.getString("lastname", null);
        String firstnamePref = sharedPreferences.getString("firstname", null);
        if ((lastnamePref != null) && (firstnamePref != null)) {
            authorCompleteView.setEnabled(false);
            authorCompleteView.setFocusable(false);
            authorCompleteView.setFocusableInTouchMode(false);
        }
    }

}