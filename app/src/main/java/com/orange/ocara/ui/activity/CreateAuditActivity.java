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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.orange.ocara.R;
import com.orange.ocara.business.binding.ErrorBundle;
import com.orange.ocara.business.interactor.CheckTermsOfUseTask;
import com.orange.ocara.business.interactor.UseCase.UseCaseCallback;
import com.orange.ocara.business.model.RuleSetStat;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.model.UserModel;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.cache.model.AuditorEntity;
import com.orange.ocara.data.cache.model.SiteEntity;
import com.orange.ocara.data.cache.model.SortCriteria;
import com.orange.ocara.ui.CreateAuditUiConfig;
import com.orange.ocara.ui.adapter.AuditorItemListAdapter;
import com.orange.ocara.ui.adapter.RulesetStatusAdapter;
import com.orange.ocara.ui.adapter.SiteItemListAdapter;
import com.orange.ocara.ui.contract.CreateAuditContract;
import com.orange.ocara.ui.contract.TermsOfUseDisplayContract;
import com.orange.ocara.ui.fragment.TermsOfUseAcceptanceFragment_;
import com.orange.ocara.ui.model.AuditFormUiModel;
import com.orange.ocara.ui.view.Switch;
import com.orange.ocara.ui.widget.AuditorAutoCompleteView;
import com.orange.ocara.ui.widget.SiteAutoCompleteView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.RequiredArgsConstructor;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.orange.ocara.business.interactor.CheckTermsOfUseTask.CheckTermsOfUseResponse;
import static com.orange.ocara.tools.StringUtils.isBlank;
import static com.orange.ocara.tools.StringUtils.trim;
import static com.orange.ocara.ui.widget.SiteAutoCompleteView.format;
import static timber.log.Timber.d;
import static timber.log.Timber.e;
import static timber.log.Timber.i;
import static timber.log.Timber.v;
import static timber.log.Timber.w;

/**
 * View that displays a form which aims on creating an audit
 */
@EActivity(R.layout.activity_create_audit)
public class CreateAuditActivity
        extends BaseActivityManagingAudit
        implements CreateAuditContract.CreateAuditView, AuditFormUiModel {

    public static final Pattern AUTHOR_PATTERN = Pattern.compile("^.{3,}+$");
    private static final int CREATE_SITE_REQUEST_CODE = 1;

    @ViewById(R.id.editAuditLayout)
    LinearLayout editAuditLayout;

    @ViewById(R.id.author_complete_view)
    AuditorAutoCompleteView authorCompleteView;

    @ViewById(R.id.site)
    SiteAutoCompleteView siteCompleteView;

    @ViewById(R.id.create_site)
    View createSite;

    @ViewById(R.id.audit_name)
    EditText auditNameText;

    @ViewById(R.id.ruleset_type)
    Spinner rulesetSelector;

    @ViewById(R.id.level)
    Switch levelSwitch;

    @ViewById(R.id.rules_info)
    ImageView showRulesInfoImageView;

    @ViewById(R.id.start_audit_button)
    Button startAuditButton;

    private CreateAuditContract.CreateAuditUserActionsListener actionsListener;

    // adapters for auto-complete
    @Bean
    SiteItemListAdapter siteItemListAdapter;

    @Bean
    AuditorItemListAdapter authorItemListAdapter;

    private RulesetStatusAdapter rulesetStatusAdapter;

    private SiteEntity currentSite = null;

    private ProgressDialog mInitPageDialog;

    @Bean(CreateAuditUiConfig.class)
    CreateAuditUiConfig uiConfig;

    @AfterViews
    void afterViews() {
        // as this is our main activity, we must ensure that the main theme is used (for instance, the splash screen uses its own theme)
        setTheme(R.style.AppTheme);

        initAuthor();
        initSite();
        initAuditType();
        initLevel();

        actionsListener = uiConfig.actionsListener(this);

        editAuditLayout.setOnClickListener(view -> hideSoftKeyboard(CreateAuditActivity.this));

        // creation mode
        setTitle(R.string.edit_audit_create_title);
    }

    private void initAuthor() {
        d("Setting up the authorCompleteView");
        authorItemListAdapter.setModelManager(modelManager);
        authorCompleteView.setAdapter(authorItemListAdapter);
    }

    private void initSite() {
        d("Setting up the siteCompleteView");
        siteItemListAdapter.setModelManager(modelManager);
        siteCompleteView.setAdapter(siteItemListAdapter);
        siteCompleteView.setThreshold(1);
        siteCompleteView.setValidator(new AutoCompleteTextView.Validator() {
            @Override
            public boolean isValid(CharSequence text) {
                v("Check is Valid : " + text + " current " + format(currentSite).toString());
                return text.toString().equals(format(currentSite).toString());
            }

            @Override
            public CharSequence fixText(CharSequence invalidText) {
                v("Returning fixed text");
                displayErrorBox(R.string.edit_audit_site_unknown_title, R.string.edit_audit_site_unknown);
                if (audit.getSite() != null) {
                    setSite(audit.getSite());
                    return format(audit.getSite()).toString();
                } else {
                    return "";
                }
            }
        });

        siteCompleteView.setOnFocusChangeListener((v, hasFocus) -> {
            if (v.equals(siteCompleteView) && !hasFocus && v instanceof AutoCompleteTextView) {
                ((AutoCompleteTextView) v).performValidation();
            }
        });

        createSite.setOnClickListener(v -> {
            v("create siteCompleteView");
            CreateSiteActivity_
                    .intent(CreateAuditActivity.this)
                    .startForResult(CREATE_SITE_REQUEST_CODE);
        });
    }

    private void initAuditType() {
        d("Setting up the audit type");
        //init Adapter ruleset Info
        showProgressDialog();
        initRuleset();

        rulesetSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                runOnUiThread(() -> {
                    d("ActivityMessage=setting preferred ruleset");
                    actionsListener.savePreferredRuleset(rulesetStatusAdapter.getItem(position));
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing yet
            }
        });
    }

    private void initLevel() {

        levelSwitch.setChecked(audit == null || AuditEntity.Level.EXPERT.equals(audit.getLevel()));
        levelSwitch.requestFocus();
        levelSwitch.setOnCheckedChangeListener((compoundButton, b) -> actionsListener.validateAudit(this));
    }

    private void initRuleset() {
        disableRulesInfo();
    }

    @Override
    public RulesetModel getActualRuleset() {
        return rulesetStatusAdapter.getItem(rulesetSelector.getSelectedItemPosition());
    }


    @TextChange(R.id.author_complete_view)
    void onAuthorTextChange(CharSequence text) {
        actionsListener.validateAudit(this);
        authorItemListAdapter.getFilter().filter(text);
    }

    @TextChange(R.id.site)
    void onSiteTextChange(CharSequence text) {
        final String[] split = text.toString().split(" -- ");
        d("ActivityMessage=on siteCompleteView text change;Text=%s", text.toString());
        if (split.length == 2
                && modelManager.checkSiteExistenceByNoImmo(split[0].trim())
                && modelManager.checkSiteExistenceByName(split[1].trim())) {
            currentSite = modelManager.searchSites(split[1].trim()).get(0);
            actionsListener.validateAudit(this);
            siteItemListAdapter.getFilter().filter(text);
        } else {
            disableSaveButton();
        }
    }

    @Override
    public void showRulesetList(List<RulesetModel> rulesets) {
        rulesetStatusAdapter = new RulesetStatusAdapter(this, rulesets);
        initRuleSetInfoAdapter();
        if (!rulesets.isEmpty()) {
            showRulesetIcons();
        } else {
            hideRulesetIcons();
        }
    }

    @UiThread
    void initPosition(final RulesetModel item) {
        if (item != null) {
            final int position = rulesetStatusAdapter.getPosition(item);
            rulesetSelector.setSelection(position);
        }
    }

    @Override
    @UiThread
    public void showRulesetIcons() {
        showRulesInfoImageView.setVisibility(VISIBLE);
    }

    @Override
    @UiThread
    public void hideRulesetIcons() {
        showRulesInfoImageView.setVisibility(INVISIBLE);
    }

    @UiThread
    void initRuleSetInfoAdapter() {
        rulesetSelector.setAdapter(rulesetStatusAdapter);
        if (mInitPageDialog != null) {
            mInitPageDialog.dismiss();
        }
    }

    @TextChange(R.id.name)
    void onNameTextChange() {
        actionsListener.validateAudit(this);
    }

    @Click(R.id.rules_info)
    void showRulesInfoClicked() {
        d("Show RulesetInfoActivity...");
        final RulesetModel selectedRuleset = rulesetStatusAdapter.getItem(rulesetSelector.getSelectedItemPosition());
        RulesetInfoActivity_
                .intent(this)
                .ruleset(selectedRuleset)
                .rulesetUpgradable(true)
                .start();
    }

    @OnActivityResult(CREATE_SITE_REQUEST_CODE)
    void onResult(int resultCode, @OnActivityResult.Extra(value = "siteId") Long siteId) {
        d("On activity result, we do some stuff...");
        if (resultCode == Activity.RESULT_OK) {
            v("Site accepted");
            currentSite = modelManager.getSite(siteId);
            setSite(currentSite);
            siteCompleteView.requestFocus();
        }
    }

    private AuditorEntity makeAuthor() {
        AuditorEntity newAuthor = new AuditorEntity();

        Matcher matcher = AUTHOR_PATTERN.matcher(getUserName());
        if (matcher.find()) {
            newAuthor.setUserName(trim(matcher.group(0)));
        }

        return newAuthor;
    }

    @Override
    public UserModel getActualAuthor() {
        return AuditorEntity
                .builder()
                .userName(getUserName())
                .build();
    }

    @Override
    public SiteEntity getActualSite() {
        return currentSite;
    }

    private void setSite(SiteEntity site) {
        this.siteCompleteView.setCurrentSite(site);
        currentSite = site; // Order is very important because setCurrentSite will call textWatcher which will set currentSite to null
    }

    private String getName() {
        return trim(auditNameText.getText().toString());
    }

    private String getUserName() {
        return trim(authorCompleteView.getText().toString());
    }

    public RulesetModel getRuleSet() {
        final int selectedItemPosition = rulesetSelector.getSelectedItemPosition();
        if (rulesetStatusAdapter == null || selectedItemPosition == rulesetStatusAdapter.getCount() || selectedItemPosition < 0) {
            return null;
        }

        return rulesetStatusAdapter.getItem(selectedItemPosition);
    }

    @Click(R.id.start_audit_button)
    void onAuditStartClicked() {
        i("ActivityMessage=Start button has been clicked...");
        touActionsListener.checkTerms(new CreateAuditActivityCheckTermsOfUseCallback(this));
    }

    /**
     * Check if fields are valid and deals with consequences by modifying the view elements
     */
    public void validateFields() {
        if (!checkAuditFields()) {
            disableSaveButton();
        } else {
            enableSaveButton();
        }
    }

    private boolean auditIsValid() {
        if (!checkAuditFields()) {
            return false;
        }

        if (!isAuditNameAndVersionValid()) {
            displayErrorBox(R.string.edit_audit_error_title, R.string.edit_audit_duplicate_audit);
            this.auditNameText.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isAuditNameAndVersionValid() {
        final String oldAuditName = audit.getName();
        final String newAuditName = getName();
        final SiteEntity oldSite = audit.getSite();
        final SiteEntity newSite = currentSite;

        // Check that name changed
        if ((oldAuditName != null && oldAuditName.equalsIgnoreCase(newAuditName)) &&
                oldSite.equals(newSite)) {
            return true;
        }

        return !modelManager.checkAuditExistenceByNameAndVersion(newAuditName, newSite, audit.getVersion());
    }

    /**
     * Checks audit fields.
     *
     * @return true if audit mandatory fields are set, false otherwise
     */
    private boolean checkAuditFields() {
        boolean userNameIsValid = AUTHOR_PATTERN.matcher(getUserName()).find();
        boolean siteIsNotNull = currentSite != null;
        boolean rulesetIsValid = getRuleSet() != null && RuleSetStat.OFFLINE.equals(getRuleSet().getStat());
        boolean auditNameIsValid = !isBlank(getName());

        return userNameIsValid && siteIsNotNull && rulesetIsValid && auditNameIsValid;
    }

    @Override
    protected void retrieveAudit() {
        // creating new audit : retrieve last audits
        List<AuditEntity> lastAudits = modelManager.getAllAudits("", SortCriteria.Type.DATE.build());

        if (lastAudits.isEmpty()) {
            this.audit = new AuditEntity();
        } else {
            this.audit = lastAudits.get(0);
            this.audit.setName("");
        }
    }

    @Override
    public AuditEntity getActualAudit() {
        return audit;
    }

    /**
     * mandatory function. Inherits from {@link BaseActivity}
     * @return always false
     */
    @Override
    protected boolean isChildActivity() {
        return false;
    }

    /**
     * Convenient function, so as to use the UI Thread
     * @param audit an {@link AuditEntity}
     */
    @Background
    void createAudit(AuditEntity audit) {
        i("ActivityMessage=Start audit creation... ");
        AuditEntity newAudit = modelManager.createAudit(getRuleSet(), audit.getSite(), audit.getName(), audit.getAuthor(), audit.getLevel());
        i("ActivityMessage=Audit created;AuditName=%s;AuditId=%d", newAudit.getName(), newAudit.getId());
        auditCreated(newAudit);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void auditCreated(AuditEntity audit) {
        i("ActivityMessage=Audit has been created. Starting Setup...");
        this.audit = audit;
        SetupAuditPathActivity_
                .intent(this)
                .fromListAudit(false)
                .flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .auditId(audit.getId())
                .start();
    }

    @LongClick(R.id.rules_info)
    public void showRulesInfoLabel() {
        makeText(this, R.string.edit_audit_show_rules_information, LENGTH_SHORT).show();
    }

    @Override
    @UiThread
    public void showDownloadError() {
        makeText(this, "Erreur de téléchargement", LENGTH_LONG).show();
    }

    /**
     * hides a {@link ProgressDialog}, if it exists
     */
    @Override
    public void hideProgressDialog() {
        if (mInitPageDialog != null) {
            mInitPageDialog.dismiss();
        }
    }

    /**
     * displays a {@link ProgressDialog}
     */
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

    @Override
    public void disableRulesInfo() {
        showRulesInfoImageView.setEnabled(false);
    }

    @UiThread
    @Override
    public void enableRulesInfo() {
        showRulesInfoImageView.setEnabled(true);
    }

    @Override
    protected void onResume() {
        showRuleset();
        super.onResume();
    }

    @Background
    void showRuleset() {
        d("ActivityMessage=retrieving the rulesets list");
        actionsListener.loadRulesets();
    }

    @Override
    public void enableSaveButton() {
        startAuditButton.setEnabled(true);
    }

    @Override
    public void disableSaveButton() {
        startAuditButton.setEnabled(false);
    }

    @Override
    public AuditEntity getInitialAudit() {
        // we are creating new audits, so there is no initial audit to compare with.
        // maybe this function is not necessary...
        return null;
    }

    /**
     *
     * extends {@link BaseActivity#showTerms()}
     * implements {@link TermsOfUseDisplayContract.TermsOfUseDisplayView#showTerms()}
     */
    @Override
    public void acceptTerms() {

        if (auditIsValid()) {
            i("ActivityMessage=Validation succeeded, after start button has been clicked...");
            audit.setAuthor(makeAuthor());
            audit.setSite(currentSite);

            final String referenceString = getString(R.string.audit_item_name_format, audit.getName(), audit.getVersion());
            String currentAuditName = getName();
            if (!currentAuditName.equals(referenceString)) {
                audit.setName(currentAuditName);
            }
            audit.setLevel(levelSwitch.isChecked() ? AuditEntity.Level.EXPERT : AuditEntity.Level.BEGINNER);
            createAudit(audit);
        } else {
            w("ActivityMessage=Validation failed after start button has been clicked.");
        }
    }

    @Override
    public void showTerms() {
        d("ActivityMessage=Showing terms");
        TermsOfUseAcceptanceFragment_
                .intent(CreateAuditActivity.this)
                .start();
    }

    /**
     * Callback class for {@link CheckTermsOfUseTask} dedicated to the current activity
     */
    @RequiredArgsConstructor
    public static class CreateAuditActivityCheckTermsOfUseCallback implements UseCaseCallback<CheckTermsOfUseResponse> {

        private final CreateAuditActivity view;

        @Override
        public void onComplete(CheckTermsOfUseResponse terms) {

            if (terms.isAccepted()) {
                d( "Message=Terms already checked and accepted");
                view.acceptTerms();
            } else {
                d( "Message=Terms not checked yet, or already refused");
                view.showTerms();
            }
        }

        @Override
        public void onError(ErrorBundle errors) {
            e(errors.getCause(), "Message=Item upgrading failed;Error=%s", errors.getMessage());
            view.showError(errors.getMessage());
        }
    }
}
