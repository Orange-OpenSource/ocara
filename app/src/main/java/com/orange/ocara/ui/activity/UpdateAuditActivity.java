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
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.orange.ocara.R;
import com.orange.ocara.business.model.AuditModel;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.model.SiteModel;
import com.orange.ocara.business.model.UserModel;
import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.business.service.impl.RuleSetServiceImpl;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.cache.model.AuditorEntity;
import com.orange.ocara.data.cache.model.SiteEntity;
import com.orange.ocara.tools.ListUtils;
import com.orange.ocara.ui.adapter.AuditorItemListAdapter;
import com.orange.ocara.ui.adapter.RulesetStatusAdapter;
import com.orange.ocara.ui.adapter.SiteItemListAdapter;
import com.orange.ocara.ui.contract.UpdateAuditContract;
import com.orange.ocara.ui.model.AuditFormUiModel;
import com.orange.ocara.ui.model.AuditUiModel;
import com.orange.ocara.ui.presenter.UpdateAuditPresenter;
import com.orange.ocara.ui.view.Switch;
import com.orange.ocara.ui.widget.AuditorAutoCompleteView;
import com.orange.ocara.ui.widget.SiteAutoCompleteView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.orange.ocara.tools.StringUtils.trim;
import static timber.log.Timber.d;
import static timber.log.Timber.i;

/**
 * View that displays a form which aims on editing an audit
 *
 * This is a view (in the MVP way), so it implements {@link UpdateAuditContract.UpdateAuditView}.
 * Then it is also related to {@link UpdateAuditContract.UpdateAuditUserActionsListener}, whose
 * implementation is a presenter cleverly named {@link UpdateAuditPresenter}.
 *
 * This also implements {@link AuditFormUiModel} as it requires fields validations, as forms usually do.
 *
 */
@EActivity(R.layout.activity_edit_audit)
public class UpdateAuditActivity extends BaseActivityManagingAudit implements UpdateAuditContract.UpdateAuditView, AuditFormUiModel {

    @ViewById(R.id.editAuditLayout)
    LinearLayout editAuditLayout;

    @ViewById(R.id.author_complete_view)
    AuditorAutoCompleteView authorCompleteView;

    /**
     * this field shall be inactive. But it's still an EditText, so as to keep the same design than
     * in creation mode (@see {@link CreateAuditActivity}).
     */
    @ViewById(R.id.site)
    EditText siteEditText;

    @ViewById(R.id.audit_name)
    EditText auditNameText;

    @ViewById(R.id.ruleset_type)
    Spinner mRulesetTypeSpinner;

    @ViewById(R.id.level)
    Switch levelSwitch;

    @ViewById(R.id.rules_info)
    ImageView showRulesInfoImageView;

    @ViewById(R.id.start_audit_button)
    Button saveAuditButton;

    @Bean(RuleSetServiceImpl.class)
    RuleSetService mRuleSetService;

    @Bean
    SiteItemListAdapter siteItemListAdapter;

    @Bean
    AuditorItemListAdapter authorItemListAdapter;

    private ProgressDialog mInitPageDialog;

    /**
     * Current value of the {@link AuditorEntity} of the audit
     */
    private AuditorEntity currentAuthor;

    /**
     * Current value of the {@link AuditEntity.Level} of the audit
     */
    private AuditEntity.Level currentAuditLevel;

    /**
     * Current value of the {@link AuditEntity#getName()}
     */
    private String currentAuditName;

    private RulesetStatusAdapter rulesAdapter;

    /**
     * Listener for user's actions, aka the presenter in the MVP pattern
     */
    private UpdateAuditContract.UpdateAuditUserActionsListener actionsListener;

    /**
     * This occurs AFTER <code>setContentView(View)</code> which is called at the
     * end of super.onCreate().
     */
    @AfterViews
    void afterViews() {

        setTitle(R.string.edit_audit_update_title);

        actionsListener = new UpdateAuditPresenter(this, mRuleSetService, modelManager);

        actionsListener.loadAudit(audit.getId());

        editAuditLayout.setOnClickListener(view -> hideSoftKeyboard(UpdateAuditActivity.this));
    }

    /**
     * displays informations about the {@link AuditorEntity} who is responsible for the current {@link AuditEntity}
     *
     * @param author an {@link AuditorEntity}
     */
    @UiThread
    @Override
    public void showAuthor(AuditorEntity author) {
        d("ActivityMessage=Setting up the author;AuthorName=%s", author.getUserName());

        currentAuthor = author;

        authorItemListAdapter.setModelManager(modelManager);

        authorCompleteView.setAdapter(authorItemListAdapter);

        // Current auditor shall be set AFTER setting the adapter. Otherwise, an exception may be thrown.
        authorCompleteView.setCurrentAuditor(author);

        authorCompleteView.setOnItemClickListener((parent, view, position, id) -> {
            currentAuthor = (AuditorEntity) authorCompleteView.getAdapter().getItem(position);
            d("ActivityMessage=Selecting an authorCompleteView in the items list;CurrentAuthor=%s", currentAuthor.getUserName());
        });
    }

    /**
     * displays informations about the {@link SiteEntity} where the current {@link AuditEntity} takes place
     *
     * @param site a {@link SiteEntity}
     */
    @UiThread
    @Override
    public void showSite(SiteEntity site) {
        d("Setting up the siteCompleteView");
        siteEditText.setText(SiteAutoCompleteView.format(site));
    }

    /**
     * displays the kind of experience required by the {@link AuditorEntity} to make the current {@link AuditEntity}
     *
     * @param level a {@link AuditEntity.Level}
     */
    @UiThread
    @Override
    public void showLevel(AuditEntity.Level level) {
        currentAuditLevel = level;

        levelSwitch.requestFocus();
        levelSwitch.setChecked(AuditEntity.Level.EXPERT.equals(level));
        levelSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            currentAuditLevel = levelSwitch.isChecked() ? AuditEntity.Level.EXPERT : AuditEntity.Level.BEGINNER;
            UpdateAuditActivity.this.validateAudit();
        });
    }

    /**
     * displays informations about the current {@link AuditEntity}
     *
     * @param audit an {@link AuditEntity}
     */
    @Background
    @Override
    public void showAudit(AuditEntity audit) {
        String name = audit.getName();

        d("ActivityMessage=Initializing audit form;AuditOriginalName=%s", name);

        currentAuditName = name;
        auditNameText.setText(currentAuditName);

        showProgressDialog();

        actionsListener.loadAuthor(audit);
        actionsListener.loadSite(audit);
        actionsListener.loadLevel(audit);
        actionsListener.loadRuleset(audit);
    }

    @TextChange(R.id.author_complete_view)
    void onAuthorTextChange(CharSequence text) {
        d("ActivityMessage=Listening before just changed in authorCompleteView's complete view");

        validateAudit();
        authorItemListAdapter.getFilter().filter(text);
    }

    @UiThread
    @Override
    public void showRuleset(RulesetModel ruleset) {
        d("ActivityMessage=Showing a ruleset;RulesetRef=%s;RulesetVersion=%s", ruleset.getReference(), ruleset.getVersion());

        rulesAdapter = new RulesetStatusAdapter(this, ListUtils.newArrayList(ruleset));
        mRulesetTypeSpinner.setAdapter(rulesAdapter);

        showRulesetIcons();
        hideProgressDialog();
    }

    @Override
    @UiThread
    public void showRulesetIcons() {
        showRulesInfoImageView.setVisibility(View.VISIBLE);
    }

    @Override
    @UiThread
    public void hideRulesetIcons() {
        showRulesInfoImageView.setVisibility(View.INVISIBLE);
    }

    @TextChange(R.id.audit_name)
    void onAuditNameTextChange() {
        String newValue =  trim(auditNameText.getText().toString());

        d("ActivityMessage=Changing audit's name;AuditNewName=%s", newValue);

        currentAuditName = newValue;
        validateAudit();
    }

    @Background
    void validateAudit() {
        actionsListener.validateAudit(this);
    }

    @UiThread
    @Override
    public void showSaveAuditButton() {
        saveAuditButton.setText(R.string.edit_audit_start_audit_edit_button);
    }

    @UiThread
    @Override
    public void showContinueAuditButton() {
        saveAuditButton.setText(R.string.edit_audit_start_audit_back_button);
    }

    @Click(R.id.start_audit_button)
    void onAuditStartClicked() {
        i("ActivityMessage=Start button has been clicked...");
        updateAudit();
    }

    @UiThread
    @Override
    public void showValidationErrors() {
        displayErrorBox(R.string.edit_audit_error_title, R.string.edit_audit_duplicate_audit);
        this.auditNameText.requestFocus();
    }

    @Background
    void updateAudit() {
        i("ActivityMessage=Starting the update of the current audit");
        actionsListener.updateAudit(audit.getId(), getActualAudit());
    }

    @Override
    @LongClick(R.id.rules_info)
    public void showRulesInfoLabel() {
        makeText(this, R.string.edit_audit_show_rules_information, LENGTH_SHORT).show();
    }

    /**
     * displays a {@link ProgressDialog}
     */
    @UiThread
    @Override
    public void showProgressDialog() {
        mInitPageDialog = new ProgressDialog(this);
        mInitPageDialog.setMessage(getString(R.string.init_page));
        mInitPageDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mInitPageDialog.setIndeterminate(true);
        mInitPageDialog.setCanceledOnTouchOutside(false);
        mInitPageDialog.setCancelable(false);
        mInitPageDialog.show();
    }

    /**
     * hides a {@link ProgressDialog}, if it exists
     */
    @Override
    @UiThread
    public void hideProgressDialog() {
        if (mInitPageDialog != null) {
            mInitPageDialog.dismiss();
        }
    }

    @Override
    @UiThread(propagation = UiThread.Propagation.REUSE)
    public void showAuditUpdatedSuccessfully() {

        // terminate this activity
        finish();
    }

    @UiThread
    @Override
    public void enableSaveButton() {
        saveAuditButton.setEnabled(true);
    }

    @UiThread
    @Override
    public void disableSaveButton() {
        saveAuditButton.setEnabled(false);
    }

    @Override
    public AuditModel getActualAudit() {
        return AuditUiModel
                .builder()
                .id(audit.getId())
                .name(currentAuditName)
                .level(currentAuditLevel)
                .authorName(userName())
                .build();
    }

    @Override
    public AuditModel getInitialAudit() {
        return modelManager.getAudit(audit.getId());
    }

    @Override
    public UserModel getActualAuthor() {
        return AuditorEntity
                .builder()
                .userName(userName())
                .build();
    }

    @Override
    public RulesetModel getActualRuleset() {
        return rulesAdapter != null && !rulesAdapter.isEmpty() ? rulesAdapter.getItem(0) : null;
    }

    @Override
    public SiteModel getActualSite() {
        return audit.getSite();
    }

    private String userName() {
        String value = trim(authorCompleteView.getText().toString());
        d("ActivityMessage=Retrieving the text from the author's complete view;UserName=%s", value);
        return value;
    }
}
