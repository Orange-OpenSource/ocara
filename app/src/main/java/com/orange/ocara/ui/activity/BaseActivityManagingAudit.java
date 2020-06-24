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

package com.orange.ocara.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import androidx.appcompat.app.AlertDialog;

import com.activeandroid.ActiveAndroid;
import com.orange.ocara.R;
import com.orange.ocara.data.cache.db.ModelManager;
import com.orange.ocara.data.cache.db.ModelManagerImpl;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.cache.model.AuditObjectEntity;
import com.orange.ocara.ui.dialog.NotificationDialogBuilder;
import com.orange.ocara.ui.tools.RefreshStrategy;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * abstract class that inherits from BasicActivity and which is dedicated to the management of audits
 */
@EActivity
/*package. */ abstract class BaseActivityManagingAudit extends BaseActivity {


    private static final long DIALOG_SHOW_DELAY_IN_MILLISECONDS = 5000;

    private static final long DIALOG_DISMISS_DELAY_IN_MILLISECONDS = 10000;

    /**
     * Refresh strategy.
     */
    private static final RefreshStrategy STRATEGY = RefreshStrategy.builder().depth(RefreshStrategy.DependencyDepth.AUDIT).build();
    protected AuditEntity audit;
    @Bean(ModelManagerImpl.class)
    ModelManager modelManager;
    @Extra
    Long auditId;

    @AfterInject
    void afterInject() {

        retrieveAudit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        retrieveAudit();
    }

    @Override
    protected void onAllRuleSetItemClicked() {
        BrowseRulesetsActivity_
                .intent(this)
                .rulesetReference(audit != null ? audit.getRuleSetRef() : null)
                .start();
    }

    protected void retrieveAudit() {
        if (!ActiveAndroid.inTransaction() && modelManager != null && auditId != null) {
            this.audit = modelManager.getAudit(auditId);
            refreshAudit();
        }
    }

    /**
     * To findAll the default refresh strategy called by refreshAudit.
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
        if (modelManager != null && audit != null) {
            modelManager.refresh(audit, strategy);
        }
        auditRefreshed();
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void auditRefreshed() {
    }

    protected List<Long> buildSelectedAuditObjectsList(AuditObjectEntity... auditObjects) {
        List<Long> selectedObjectIds = new ArrayList<>();
        for (AuditObjectEntity auditObject : auditObjects) {
            selectedObjectIds.add(auditObject.getId());
        }
        return selectedObjectIds;
    }

    /**
     * Triggers the auditing of a bunch of equipments
     *
     * @param editMode
     * @param auditObjects a {@link List} of {@link AuditObjectEntity}s
     */
    public void launchAuditObjectsTest(boolean editMode, AuditObjectEntity... auditObjects) {
        Long[] selectedAuditObjectIds = buildSelectedAuditObjectsList(auditObjects).toArray(new Long[]{});
        long[] selectedObjects = toPrimitive(selectedAuditObjectIds);
        AuditObjectsActivity_
                .intent(this)
                .auditId(auditId)
                .editMode(editMode)
                .selectedObjects(selectedObjects)
                .start();
    }

    /**
     * Updates the view
     *
     * @param auditObject an instance of {@link AuditObjectEntity}
     * @param terminateActivityWhenDone if true, ends the current {@link android.app.Activity}
     */
    public void showAuditObjectProgress(AuditObjectEntity auditObject, final boolean terminateActivityWhenDone) {
        CharSequence info = getText(R.string.auditing_progress_info);

        Timber.d("Show audit object progress;TerminateActivity=%b", terminateActivityWhenDone);

        Spannable auditingStatus = new SpannableString("");
        int color = getResources().getColor(R.color.black);
        switch (auditObject.getResponse()) {
            case OK:
                auditingStatus = new SpannableString(getText(R.string.auditing_progress_status_ok));
                color = getResources().getColor(R.color.green);
                break;
            case NOK:
                if (auditObject.hasAtLeastOneBlockingRule()) {
                    auditingStatus = new SpannableString(getText(R.string.auditing_progress_status_nok));
                    color = getResources().getColor(R.color.red);
                } else {

                    auditingStatus = new SpannableString(getText(R.string.auditing_progress_status_anoying));
                    color = getResources().getColor(R.color.orange);
                }
                break;
            case DOUBT:
                auditingStatus = new SpannableString(getText(R.string.auditing_progress_status_doubt));
                color = getResources().getColor(R.color.yellow);
                break;
            case NO_ANSWER:
                auditingStatus = new SpannableString(getText(R.string.auditing_progress_status_no_answer));
                color = getResources().getColor(R.color.blue);
                break;
        }

        auditingStatus.setSpan(new ForegroundColorSpan(color), info.length(), auditingStatus.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        StringBuilder stringBuffer = new StringBuilder(info);
        stringBuffer.append("<br>")
                .append(auditingStatus);

        if (terminateActivityWhenDone) {
            Timber.d("Terminate activity...");

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            if (sharedPreferences.getString(getString(R.string.setting_display_auditing_progress_key), "1").equals("1")) {
                final NotificationDialogBuilder dialogBuilder = new NotificationDialogBuilder(BaseActivityManagingAudit.this);
                dialog = dialogBuilder
                        .setInfo(auditingStatus)
                        .setOption(getString(R.string.auditing_progress_option))
                        .setCancelable(false)
                        .setTitle(R.string.auditing_progress_title)
                        .setOnDismissListener(currentDialog -> {
                            Timber.d("Message=Dismissing the alert dialog");
                            auditingProgressDismiss(dialogBuilder.getOptionValue());
                            dialog = null;
                            BaseActivityManagingAudit.this.finish();
                        })
                        .setPositiveButton(R.string.action_close, null)
                        .create();

                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    if (dialog != null && dialog.isShowing()) {
                        Timber.d("Message=Triggering the dismissing of the alert dialog");
                        dialog.dismiss();
                        dialog = null;
                    }
                }, DIALOG_DISMISS_DELAY_IN_MILLISECONDS);

                /**
                 * The call to dialog.show() is made in this {@link Handler}, so as to avoid a
                 * WindowLeaked error
                 */
                handler.postDelayed(() -> {
                    if (dialog != null && !dialog.isShowing()) {
                        Timber.d("Message=Triggering the display of the alert dialog");
                        dialog.show();
                    }
                }, DIALOG_SHOW_DELAY_IN_MILLISECONDS);

            }
            auditingProgressDismiss(true);
            BaseActivityManagingAudit.this.finish();
        }
    }

    private AlertDialog dialog = null;

    @Override
    public void onPause() {
        super.onPause();
        if (dialog != null) {
            Timber.d("Message=Triggering the dismissing of the alert dialog while being pausing the activity");
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (dialog != null) {
            Timber.d("Message=Triggering the dismissing of the alert dialog while being stopping the activity");
            dialog.dismiss();
            dialog = null;
        }
    }

    private void auditingProgressDismiss(boolean doNotDisplayThisMessageAgain) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (doNotDisplayThisMessageAgain) {
            editor.putString(getString(R.string.setting_display_auditing_progress_key), "0");
        } else {
            editor.putString(getString(R.string.setting_display_auditing_progress_key), "1");
        }
        editor.apply();
    }

    private static long[] toPrimitive(final Long[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return new long[0];
        }
        final long[] result = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }
}
