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

import android.app.ProgressDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.orange.ocara.R;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.ui.RulesetInfoUiConfig;
import com.orange.ocara.ui.contract.RulesetInfoContract;
import com.orange.ocara.ui.tools.RuleSetUtils;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.Locale;

import timber.log.Timber;

/**
 * Activity dedicated to display information about a given ruleset
 */
@EActivity(R.layout.activity_ruleset_info)
public class RulesetInfoActivity extends BaseActivity implements RulesetInfoContract.RulesetInfoView {

    @Extra
    RulesetModel ruleset;

    @Extra
    boolean rulesetUpgradable;

    @ViewById(R.id.author_text)
    TextView authorTextView;

    @ViewById(R.id.name_text)
    TextView nameTextView;

    @ViewById(R.id.rule_category_text)
    TextView ruleCategoryTextView;

    @ViewById(R.id.comment_text)
    TextView commentTextView;

    @ViewById(R.id.version_text)
    TextView versionTextView;

    @ViewById(R.id.publication_date_text)
    TextView publicationDateTextView;

    @ViewById(R.id.button_download_rules)
    Button mDownloadRulesButton;

    @ViewById(R.id.button_show_rules)
    Button mShowRulesButton;

    private ProgressDialog mDownloadRuleSetProgressDialog;

    private RulesetInfoContract.RulesetInfoUserActionsListener actionsListener;

    @Bean(RulesetInfoUiConfig.class)
    RulesetInfoUiConfig uiConfig;

    @AfterExtras
    void initData() {
        if (ruleset == null) {
            Timber.e("Missing ruleset parameters in the activity extra");
        }

        actionsListener = uiConfig.actionsListener(this, rulesetUpgradable);
    }

    @Background
    @AfterViews
    void initView() {
        actionsListener.loadRuleset(this.ruleset);
    }

    @UiThread
    @Override
    public void showRuleset(RulesetModel ruleset) {
        if (ruleset.getType() != null) {
            nameTextView.setText(ruleset.getType());
        }

        if (ruleset.getVersion() != null) {
            versionTextView.setText(ruleset.getVersion());
        }

        if (ruleset.getAuthor() != null) {
            authorTextView.setText(ruleset.getAuthor());
        }

        if (ruleset.getRuleCategoryName() != null) {
            ruleCategoryTextView.setText(ruleset.getRuleCategoryName());
        }

        if (ruleset.getComment() != null) {
            commentTextView.setText(ruleset.getComment());
        }

        if (ruleset.getDate() != null) {
            Locale locale = Locale.getDefault();
            String date = RuleSetUtils.formatRulesetDate(ruleset.getDate(), locale);
            publicationDateTextView.setText(date);
        }

        if (ruleset.isLocallyAvailable()) {
            mDownloadRulesButton.setText(R.string.upgrade_rules);
        } else if (ruleset.isRemotelyAvailable()) {
            mDownloadRulesButton.setText(getString(R.string.download_rules));
        }

        checkRuleset();
    }

    /**
     * validates the view elements according to the ruleset
     */
    @Background
    void checkRuleset() {
        Timber.d("ActivityMessage=starting ruleset checking");
        actionsListener.checkRulesetIsValid(ruleset);
    }

    @Click(R.id.button_show_rules)
    @Override
    public void showRules() {

        String rulesetReference = ruleset.getReference();
        Integer rulesetVersion = Integer.parseInt(ruleset.getVersion());
        Timber.d("ActivityMessage=showing ruleset's rules;RulesetReference=%s;RulesetVersion=%d", rulesetReference, rulesetVersion);
        BrowseRulesetsActivity_
                .intent(this)
                .rulesetReference(rulesetReference)
                .ruleSetVersion(rulesetVersion)
                .start();
    }

    /**
     * triggers the upgrade of the current ruleset to its newest version
     */
    @Click(R.id.button_download_rules)
    @Override
    public void downloadRuleset() {
        triggerUpgrade();
    }

    @Background
    void triggerUpgrade() {
        Timber.d("ActivityMessage=starting ruleset upgrade");
        actionsListener.upgradeRuleset(ruleset);
    }

    @Override
    @UiThread
    public void showProgressDialog() {
        Timber.d("ActivityMessage=showing progress dialog");
        mDownloadRuleSetProgressDialog = new ProgressDialog(this);
        mDownloadRuleSetProgressDialog.setCanceledOnTouchOutside(false);
        mDownloadRuleSetProgressDialog.setCancelable(false);
        mDownloadRuleSetProgressDialog.setMessage(getString(R.string.downloading));
        mDownloadRuleSetProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDownloadRuleSetProgressDialog.setIndeterminate(true);
        mDownloadRuleSetProgressDialog.show();
    }

    @Override
    @UiThread
    public void hideProgressDialog() {
        Timber.d("ActivityMessage=hiding progress dialog");
        if (mDownloadRuleSetProgressDialog != null) {
            mDownloadRuleSetProgressDialog.dismiss();
        }
    }

    @UiThread
    @Override
    public void showDownloadSucceeded() {
        Timber.d("ActivityMessage=showing success toast");
        Toast.makeText(this, R.string.downloading_succeeded, Toast.LENGTH_LONG).show();
    }

    @UiThread
    @Override
    public void showDownloadFailed() {
        Timber.d("ActivityMessage=showing failure toast");
        Toast.makeText(this, R.string.downloading_failed, Toast.LENGTH_LONG).show();
    }

    @UiThread
    @Override
    public void showInvalidRuleset() {
        Timber.d("ActivityMessage=showing invalid toast");
        Toast.makeText(this, R.string.invalid_ruleset, Toast.LENGTH_LONG).show();
    }

    @UiThread
    @Override
    public void enableRulesetDetails() {
        Timber.d("ActivityMessage=enabling ruleset details");
        mShowRulesButton.setEnabled(true);
        mShowRulesButton.setVisibility(View.VISIBLE);
    }

    @UiThread
    @Override
    public void disableRulesetDetails() {
        Timber.d("ActivityMessage=disabling ruleset details");
        mShowRulesButton.setEnabled(false);
        mShowRulesButton.setVisibility(View.GONE);
    }

    @UiThread
    @Override
    public void enableRulesetUpgrade() {
        Timber.d("ActivityMessage=enabling ruleset upgrade");
        mDownloadRulesButton.setEnabled(true);
        mDownloadRulesButton.setVisibility(View.VISIBLE);
    }

    @UiThread
    @Override
    public void disableRulesetUpgrade() {
        Timber.d("ActivityMessage=disabling ruleset upgrade");
        mDownloadRulesButton.setEnabled(false);
        mDownloadRulesButton.setVisibility(View.GONE);
    }

    @Override
    @LongClick(R.id.button_download_rules)
    public void displayDownloadRulesetLabel() {
        Toast.makeText(this, R.string.edit_audit_download_rules_button, Toast.LENGTH_SHORT).show();
    }

    @Override
    @LongClick(R.id.button_show_rules)
    public void displayShowRulesLabel() {
        Toast.makeText(this, R.string.edit_audit_show_rules_button, Toast.LENGTH_SHORT).show();
    }
}
