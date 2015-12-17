/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.orange.ocara.conf.OcaraConfiguration;
import com.orange.ocara.model.Audit;
import com.orange.ocara.model.Auditor;
import com.orange.ocara.model.ModelManager;
import com.orange.ocara.model.Site;
import com.orange.ocara.model.SortCriteria;
import com.orange.ocara.modelStatic.Response;
import com.orange.ocara.modelStatic.RuleSet;
import com.orange.ocara.ui.adapter.ItemListAdapter;
import com.orange.ocara.ui.view.AuditorItemView;
import com.orange.ocara.ui.view.AuditorItemView_;
import com.orange.ocara.ui.view.SiteItemView;
import com.orange.ocara.ui.view.SiteItemView_;
import com.orange.ocara.ui.view.Switch;
import com.orange.ocara.ui.widget.AuditorAutoCompleteView;
import com.orange.ocara.ui.widget.SiteAutoCompleteView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import timber.log.Timber;

@EActivity(resName="activity_edit_audit")
abstract class EditAuditActivity extends BaseActivityManagingAudit {

    private static final Pattern SITE_PATTERN = Pattern.compile("^(\\d\\w?\\d{0,4})?\\s*(.{1,}+)?$");
    private static final Pattern AUTHOR_PATTERN = Pattern.compile("^.{3,}+$");
    private static final int CREATE_SITE_REQUEST_CODE = 1;

    @Inject
    ModelManager modelManager;

    @ViewById(resName="editAuditLayout")
    LinearLayout editAuditLayout;
    @ViewById(resName="author")
    AuditorAutoCompleteView author;
    @ViewById(resName="site")
    SiteAutoCompleteView site;
    @ViewById(resName="create_site")
    View createSite;
    @ViewById(resName="name")
    EditText name;
    @ViewById(resName="type")
    Spinner type;
    @ViewById(resName="level")
    Switch level;

    @ViewById(resName="show_rules_button")
    View showRulesButton;

    @ViewById(resName="start_audit_button")
    Button startAuditButton;

    // adapters for auto-complete
    private SiteItemListAdapter siteItemListAdapter;
    private AuditorItemListAdapter authorItemListAdapter;
    private RulesAdapter rulesAdapter;

    private Site currentSite = null;
    private Auditor currentAuthor = null;
    private boolean auditHasChanged = false;
    private Audit.Level initialLevel;

    @AfterViews
    void setUpAuthor() {
        authorItemListAdapter = new AuditorItemListAdapter();

        author.setAdapter(authorItemListAdapter);
        author.addTextChangedListener(new CheckTextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentAuthor = null;
                authorItemListAdapter.getFilter().filter(s);
                super.onTextChanged(s, start, before, count);
            }
        });
    }

    @AfterViews
    void setUpSite() {
        siteItemListAdapter = new SiteItemListAdapter();

        site.setThreshold(1);
        site.setAdapter(siteItemListAdapter);
        site.setValidator(new AutoCompleteTextView.Validator() {
            SiteAutoCompleteView current;

            @Override
            public boolean isValid(CharSequence text) {
                Timber.v("Check is Valid : " + text);
                Timber.v("Check : " + current.format(currentSite, true));
                if (text.toString().equals(current.format(currentSite, true).toString())) {
                    return true;
                }

                return false;
            }

            @Override
            public CharSequence fixText(CharSequence invalidText) {
                Timber.v("Returning fixed text");
                displayErrorBox(com.orange.ocara.R.string.edit_audit_site_unknown_title, com.orange.ocara.R.string.edit_audit_site_unknown);
                return "";
            }
        });
        site.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.getId() == com.orange.ocara.R.id.site && !hasFocus) {
                    ((AutoCompleteTextView) v).performValidation();
                }
            }
        });

        site.addTextChangedListener(new CheckTextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSite = null;
                siteItemListAdapter.getFilter().filter(s);
                super.onTextChanged(s, start, before, count);
            }
        });

        createSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.v("create site");
                OcaraConfiguration.get().getCreateSite().createSite(EditAuditActivity.this);
            }
        });
    }

    @AfterViews
    void setUpAuditType() {
        rulesAdapter = new RulesAdapter(this, modelManager.getAllRuleSet());
        type.setAdapter(rulesAdapter);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateStartAuditButton();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        type.setFocusable(true);
        type.setFocusableInTouchMode(true);
    }

    @AfterViews
    void setUpLevel() {

        level.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                level.setFocusable(true);
                level.setFocusableInTouchMode(true);
                level.requestFocus();
                updateStartAuditButton();
            }
        });

    }

    @AfterViews
    void setUpName() {
        name.addTextChangedListener(new CheckTextWatcher());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        editAuditLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(EditAuditActivity.this);
            }
        });
        updateFields();

    }



    @Override
    protected void onResume() {
        super.onResume();

        if (isAuditCreation()) {
            // creation mode
            setTitle(com.orange.ocara.R.string.edit_audit_create_title);
            startAuditButton.setText(com.orange.ocara.R.string.edit_audit_start_audit_button);
            this.audit.setName("");
            setName(audit.getName());
            type.setEnabled(true);
        } else {
            // modification mode
            setTitle(com.orange.ocara.R.string.edit_audit_update_title);
            startAuditButton.setText(com.orange.ocara.R.string.edit_audit_start_audit_back_button);
            type.setEnabled(false);
            updateStartAuditButton();
        }
        name.requestFocus();
        showKeyboard(getBaseContext());

    }

    @OnActivityResult(CREATE_SITE_REQUEST_CODE)
    void onResult(int resultCode, @org.androidannotations.annotations.OnActivityResult.Extra(value = "siteId") Long siteId) {

        if (resultCode == Activity.RESULT_OK) {
            Timber.v("Site ok");
            currentSite = modelManager.getSite(siteId);
            setSite(currentSite);
            site.requestFocus();

        }
    }

    private boolean isAuditCreation() {
        return auditId == null;
    }


    private void updateFields() {
        setAuthor(audit.getAuthor());
        setSite(audit.getSite());
        setName(audit.getName());
        setRuleSet(audit.getRuleSet());
        setLevel(audit.getLevel());
        initialLevel = audit.getLevel();
    }


    protected void updateStartAuditButton() {
        auditHasChanged = auditHasChanged() ;
        if ( !isAuditCreation() ) {
            if (auditHasChanged) {
                // edit button change to 'Modifier'
                startAuditButton.setText(com.orange.ocara.R.string.edit_audit_start_audit_edit_button);
            } else {
                startAuditButton.setText(com.orange.ocara.R.string.edit_audit_start_audit_back_button);
            }
        }
        if (checkAuditFields()) {
            startAuditButton.setEnabled(true);
        } else {
            startAuditButton.setEnabled(false);
        }
    }

    private boolean auditHasChanged() {

        if (isAuditCreation()) {
            return true;
        }
        return (currentSite != audit.getSite())
                || (currentAuthor != audit.getAuthor())
                || (! getName().equals(audit.getName()))
                || ( getRuleSet() !=  audit.getRuleSet())
                || ( getLevel() !=  audit.getLevel())
                ;
    }

    protected Auditor getAuthor() {
        Auditor author = currentAuthor;

        if (author == null) {
            Matcher matcher = AUTHOR_PATTERN.matcher(this.author.getText());
            if (matcher.find()) {
                author = new Auditor();
                author.setUserName(StringUtils.trim(matcher.group(0)));
            }
        }

        return author;
    }

    protected void setAuthor(Auditor author) {
        this.author.setCurrentAuditor(author);
        currentAuthor = author;  // Order is very important because setCurrentAuthor will call textWatcher which will set currentAuthor to null
    }

    protected Site getSite() {
        Site site = currentSite;
        return site;
    }

    protected void setSite(Site site) {
        this.site.setCurrentSite(site);
        currentSite = site; // Order is very important because setCurrentSite will call textWatcher which will set currentSite to null
    }

    protected String getName() {
        return StringUtils.trim(name.getText().toString());
    }

    protected void setName(String name) {
        this.name.setText(StringUtils.trimToEmpty(name));
    }

    protected RuleSet getRuleSet() {
        final int selectedItemPosition = type.getSelectedItemPosition();
        if (selectedItemPosition == rulesAdapter.getCount()) {
            return null;
        }

        return rulesAdapter.getItem(selectedItemPosition);
    }

    protected void setRuleSet(RuleSet ruleSet) {
        if (ruleSet == null) {
            this.type.setSelection(rulesAdapter.getCount());
        } else {
            this.type.setSelection(rulesAdapter.getPosition(ruleSet));
        }
    }

    protected Audit.Level getLevel() {
        return level.isChecked() ? Audit.Level.EXPERT : Audit.Level.BEGINNER;
    }

    protected void setLevel(Audit.Level level) {
        this.level.setChecked(Audit.Level.EXPERT.equals(level));
    }

    @Click(resName="start_audit_button")
    void onAuditStartClicked() {
        if (!auditHasChanged) {
            finish();
        }
        if (validateAudit()) {
            onAuditValidated();
        }
    }

    @Click(resName="show_rules_button")
    void showRulesButtonClicked() {
        String ruleSetId = getRuleSet().getId();
        String objectDescriptionId = null;
        ListRulesActivity_.intent(this).ruleSetId(ruleSetId).objectDescriptionId(objectDescriptionId).start();
    }

    protected boolean validateAudit() {
        if (!checkAuditFields()) {
            return false;
        }

        if (!checkAuditNameAndVersion()) {
            displayErrorBox(com.orange.ocara.R.string.edit_audit_error_title, com.orange.ocara.R.string.edit_audit_duplicate_audit);
            this.name.requestFocus();
            return false;
        }

        audit.setAuthor(getAuthor());
        audit.setSite(getSite());
        audit.setName(getName());
        audit.setRuleSet(getRuleSet());
        audit.setLevel(getLevel());

        return true;
    }

    private boolean checkAuditNameAndVersion() {

        final String oldAuditName = audit.getName();
        final String newAuditName = getName();
        final Site oldSite = audit.getSite();
        final Site newSite = getSite();


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
    protected boolean checkAuditFields() {
        if (getAuthor() == null || getSite() == null || StringUtils.isBlank(getName()) || getRuleSet() == null) {
            return false;
        }
        return true;
    }

    @Override
    void retrieveAudit() {

        if (auditId != null) {
            // modifying existing audit
            this.audit = this.modelManager.getAudit(auditId);
        } else {
            // creating new audit : retrieve last audits
            List<Audit> lastAudits = modelManager.getAllAudits(null, SortCriteria.Type.DATE.build());

            if (lastAudits.isEmpty()) {
                this.audit = new Audit();
            } else {
                this.audit = lastAudits.get(0);
                this.audit.setName("");

            }
        }
    }

    @Override
    protected boolean isChildActivity() {
        return false;
    }


    protected void onAuditValidated() {
        if (isAuditCreation()) {
            createAudit(audit);
        } else {
            updateAudit();
        }
    }


    @ItemClick(resName="site")
    void onSiteSuggestionSelected(Site site) {
        currentSite = site;
        updateStartAuditButton();
    }

    @ItemClick(resName="author")
    void onAuthorSuggestionSelected(Auditor author) {
        currentAuthor = author;
    }

    @Background
    void createAudit(Audit audit) {
        Audit newAudit = modelManager.createAudit(audit.getRuleSet(), audit.getSite(), audit.getName(), audit.getAuthor(), audit.getLevel());
        auditCreated(newAudit);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void auditCreated(Audit audit) {
        this.auditId = audit.getId(); // next time, come back in 'modification mode'
        this.audit = audit;
        SetupAuditPathActivity_.intent(this).auditId(audit.getId()).start();
    }

    @Background
    void updateAudit() {
        Audit.Level newAuditLevel = audit.getLevel();
        if (initialLevel.equals(Audit.Level.BEGINNER)
                && newAuditLevel.equals(Audit.Level.EXPERT)) {
            // change from BEGINNER to EXPERT
            modelManager.updateAuditRules(audit, Response.DOUBT, Response.NoAnswer);
        }
        modelManager.updateAudit(audit);
        auditUpdated();
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void auditUpdated() {
        SetupAuditPathActivity_.intent(this).auditId(audit.getId()).start();
    }

    /**
     * RuleSet adapter.
     */
    private class RulesAdapter extends ArrayAdapter<RuleSet> {

        public RulesAdapter(Context context, Collection<RuleSet> ruleSets) {
            super(context, com.orange.ocara.R.layout.support_simple_spinner_dropdown_item);
            setDropDownViewResource(com.orange.ocara.R.layout.support_simple_spinner_dropdown_item);

            addAll(ruleSets);
            add(new RuleSet());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);

            TextView text = (TextView) v.findViewById(android.R.id.text1);

            if (position == getCount()) {
                text.setText(StringUtils.EMPTY);
                text.setHint(getString(com.orange.ocara.R.string.edit_audit_rule_choice_hint)); //"Hint to be displayed"
                showRulesButton.setEnabled(false);
            } else {
                text.setText(getItem(position).getType());
                showRulesButton.setEnabled(true);
            }

            return v;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View v = super.getDropDownView(position, convertView, parent);

            TextView text = (TextView) v.findViewById(android.R.id.text1);
            text.setText(getItem(position).getType());

            return v;
        }

        @Override
        public int getCount() {
            return super.getCount() - 1;
        }
    }

    ;

    /**
     * Adapter for site
     */
    private class SiteItemListAdapter extends ItemListAdapter<Site> implements Filterable {
        private Filter filter;

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            SiteItemView siteItemView;

            if (convertView == null) {
                siteItemView = SiteItemView_.build(EditAuditActivity.this);
            } else {
                siteItemView = (SiteItemView) convertView;
            }

            siteItemView.bind(getItem(position));
            return siteItemView;
        }

        @Override
        public Filter getFilter() {
            if (filter == null) {
                filter = new SiteFilter();
            }

            return filter;
        }

        /**
         * Internal Site Filter.
         */
        private class SiteFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint != null && constraint.length() > 0) {

                    Matcher matcher = SITE_PATTERN.matcher(constraint);
                    if (matcher.find()) {

                        List<Site> sites = modelManager.searchSites(StringUtils.trim(matcher.group(1)), StringUtils.trim(matcher.group(2)));

                        results.values = sites;
                        results.count = sites.size();
                    }
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.count == 0) {
                    notifyDataSetInvalidated();
                } else {
                    update((Collection<Site>) results.values);
                }
            }
        }
    }

    /**
     * Adapter for user name
     */
    private class AuditorItemListAdapter extends ItemListAdapter<Auditor> implements Filterable {
        private Filter filter;

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            AuditorItemView auditorItemView;

            if (convertView == null) {
                auditorItemView = AuditorItemView_.build(EditAuditActivity.this);
            } else {
                auditorItemView = (AuditorItemView) convertView;
            }

            auditorItemView.bind(getItem(position));
            return auditorItemView;
        }

        @Override
        public Filter getFilter() {
            if (filter == null) {
                filter = new AuditorFilter();
            }

            return filter;
        }

        /**
         * Internal user name Filter.
         */
        private class AuditorFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint != null && constraint.length() > 0) {

                    Matcher matcher = AUTHOR_PATTERN.matcher(constraint);
                    if (matcher.find()) {

                        List<Auditor> auditors = modelManager.searchAuditors(StringUtils.trim(matcher.group(0)));

                        results.values = auditors;
                        results.count = auditors.size();
                    }
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.count == 0) {
                    notifyDataSetInvalidated();
                } else {
                    update((Collection<Auditor>) results.values);
                }
            }
        }
    }


    private class CheckTextWatcher implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence userInput, int start, int before, int count) {
            updateStartAuditButton();
        }
    }

}
