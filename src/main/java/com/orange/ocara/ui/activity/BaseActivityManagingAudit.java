/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.orange.ocara.model.Audit;
import com.orange.ocara.model.AuditObject;
import com.orange.ocara.model.ModelManager;
import com.orange.ocara.model.RefreshStrategy;
import com.orange.ocara.ui.dialog.NotificationDialogBuilder;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


@EActivity
/*package. */ abstract class BaseActivityManagingAudit extends BaseActivity {

    /**
     * Refresh strategy.
     */
    private static final RefreshStrategy STRATEGY = RefreshStrategy.builder().depth(RefreshStrategy.DependencyDepth.AUDIT).build();

    @Inject
    ModelManager modelManager;

    @Extra("auditId")
    Long auditId = null;

    protected Audit audit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrieveAudit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);

        retrieveAudit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshAudit();
    }
    @Override
    protected void onAllRuleSetItemClicked() {
        String ruleSetId = audit.getRuleSetId();
        String objectDescriptionId = null;
        ListRulesActivity_.intent(this).ruleSetId(ruleSetId).objectDescriptionId(objectDescriptionId).start();
    }

    void retrieveAudit() {
        if (auditId != null) {
            this.audit = modelManager.getAudit(auditId);
        }
    }

    /**
     * To get the default refresh strategy called by refreshAudit.
     *
     * @return default RefreshStrategy used by refreshAudit
     */
    RefreshStrategy getRefreshStrategy() {
        return STRATEGY;
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void refreshAudit() {
        refreshAudit(getRefreshStrategy());
    }

    @Background
    void refreshAudit(RefreshStrategy strategy) {
        modelManager.refresh(audit, strategy);
        auditRefreshed();
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void auditRefreshed() {
    }



    protected List<Long> buildSelectedAuditObjectsList(List<Long> selectedObjectIds, AuditObject... auditObjects) {
        for (AuditObject auditObject : auditObjects) {
            selectedObjectIds.add(auditObject.getId());
        }
        return selectedObjectIds;
    }

    public void launchAuditObjectsTest(AuditObject... auditObjects) {
        Long[] selectedAuditObjectIds = buildSelectedAuditObjectsList(new ArrayList<Long>(), auditObjects).toArray(new Long[]{});
        long[] selectedObjects = ArrayUtils.toPrimitive(selectedAuditObjectIds);


            switch (audit.getLevel()) {

            case BEGINNER:
                AuditObjectsNoviceModeActivity_.intent(this).auditId(auditId).selectedObjects(selectedObjects).start();
                break;

            case EXPERT:
            default:

                AuditObjectsExpertModeActivity_.intent(this).auditId(auditId).selectedObjects(selectedObjects).start();
                break;
        }
    }

    public void showAuditObjectProgress(AuditObject auditObject, final boolean terminateActivityWhenDone) {
        CharSequence info = getText(com.orange.ocara.R.string.auditing_progress_info);

        Spannable auditingStatus = new SpannableString("");
        int color = getResources().getColor(com.orange.ocara.R.color.black);
        switch(auditObject.getResponse()) {
            case OK: auditingStatus = new SpannableString(getText(com.orange.ocara.R.string.auditing_progress_status_ok));
                color = getResources().getColor(com.orange.ocara.R.color.green);
                break;
            case NOK:   if (auditObject.hasAtLeastOneBlockingRule()) {
                            auditingStatus = new SpannableString(getText(com.orange.ocara.R.string.auditing_progress_status_nok));
                            color = getResources().getColor(com.orange.ocara.R.color.red);
                        } else {

                            auditingStatus = new SpannableString(getText(com.orange.ocara.R.string.auditing_progress_status_anoying));
                            color = getResources().getColor(com.orange.ocara.R.color.orange);
                        }
                        break;
            case DOUBT: auditingStatus = new SpannableString(getText(com.orange.ocara.R.string.auditing_progress_status_doubt));
                color = getResources().getColor(com.orange.ocara.R.color.yellow);
                break;
            case NoAnswer: auditingStatus = new SpannableString(getText(com.orange.ocara.R.string.auditing_progress_status_no_answer));
                color = getResources().getColor(com.orange.ocara.R.color.blue);
                break;
        }

        auditingStatus.setSpan(new ForegroundColorSpan(color),info.length(), auditingStatus.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        StringBuffer stringBuffer = new StringBuffer(info);
        stringBuffer.append("<br>")
                .append(auditingStatus);



        // get application preference to know if he wants to audit object now

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int displayAuditingProgessDialog = Integer.parseInt(sharedPreferences.getString(getString(com.orange.ocara.R.string.setting_display_auditing_progress_key), "1"));


        switch (displayAuditingProgessDialog) {
            case 1:
                final NotificationDialogBuilder dialogBuilder = new NotificationDialogBuilder(this);
                final AlertDialog dialog = dialogBuilder
                        .setInfo(auditingStatus)
                                .setOption(getString(com.orange.ocara.R.string.auditing_progress_option))
                                .setCancelable(false)
                                .setTitle(com.orange.ocara.R.string.auditing_progress_title)
                                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {

                                        auditingProgressDismiss(dialogBuilder.getOptionValue());

                                        if (terminateActivityWhenDone) {
                                            BaseActivityManagingAudit.this.finish();
                                        }
                                    }
                                })
                                .setPositiveButton(com.orange.ocara.R.string.action_close, null)
                                .create();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        dialog.dismiss();
                    }

                }, 3500);
                dialog.show();

                break;
            default:
                if (terminateActivityWhenDone) {
                    BaseActivityManagingAudit.this.finish();
                }
                break;
        }


    }


    private void auditingProgressDismiss(boolean doNotDisplayThisMessageAgain) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (doNotDisplayThisMessageAgain) {
            editor.putString(getString(com.orange.ocara.R.string.setting_display_auditing_progress_key), "0");
        } else {
            editor.putString(getString(com.orange.ocara.R.string.setting_display_auditing_progress_key), "1");
        }
        editor.commit();
    }
}
