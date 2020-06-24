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
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.orange.ocara.R;
import com.orange.ocara.data.cache.db.ModelManager;
import com.orange.ocara.data.cache.db.ModelManagerImpl;
import com.orange.ocara.data.cache.model.SiteEntity;
import com.orange.ocara.tools.StringUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.orange.ocara.R.id.create_site_button;

@EActivity(R.layout.activity_create_site)
public class CreateSiteActivity extends BaseActivity {

    private static final Pattern SITE_CODE_POSTAL_PATTERN = Pattern.compile("^(\\d{5}$)");

    @Bean(ModelManagerImpl.class)
    ModelManager modelManager;
    @ViewById(R.id.createSiteLayout)
    LinearLayout createSiteLayout;
    @ViewById(R.id.site_noimmo)
    EditText siteNoImmo;
    @ViewById(R.id.site_name)
    EditText siteName;
    @ViewById(R.id.site_street)
    EditText siteStreet;
    @ViewById(R.id.site_postal_code)
    EditText sitePostalCode;
    @ViewById(R.id.site_city)
    EditText siteCity;
    @ViewById(create_site_button)
    Button createSitetButton;

    SiteEntity site;


    @AfterViews
    void setUpSite() {
        siteNoImmo.addTextChangedListener(new CheckTextWatcher());
        siteName.addTextChangedListener(new CheckTextWatcher());
        siteStreet.addTextChangedListener(new CheckTextWatcher());
        sitePostalCode.addTextChangedListener(new CheckTextWatcher());
        siteCity.addTextChangedListener(new CheckTextWatcher());
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        createSiteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(CreateSiteActivity.this);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(com.orange.ocara.R.string.create_site_create_site_title);
    }


    protected void updateCreateSiteButton() {
        if (checkSiteFields()) {
            createSitetButton.setEnabled(true);
        } else {
            createSitetButton.setEnabled(false);
        }
    }


    protected String getSiteNoImmo() {
        return StringUtils.trim(siteNoImmo.getText().toString());
    }

    protected String getSiteName() {
        return StringUtils.trim(siteName.getText().toString());
    }

    protected String getSiteStreet() {
        return StringUtils.trim(siteStreet.getText().toString());
    }

    protected String getSitePostalCode() {
        return sitePostalCode.getText().toString();
    }

    protected String getSiteCity() {
        return StringUtils.trim(siteCity.getText().toString());
    }


    /**
     * Checks siteCompleteView fields.
     *
     * @return true if siteCompleteView mandatory fields are set, false otherwise
     */
    protected boolean checkSiteFields() {
        return !(StringUtils.isBlank(getSiteNoImmo()) || StringUtils.isBlank(getSiteName())) && (getSitePostalCode().isEmpty() || checkSitePostalCode(getSitePostalCode()));
    }

    protected boolean checkSitePostalCode(String sitePostalCode) {
        if (!StringUtils.isBlank(sitePostalCode)) {
            Matcher matcher = SITE_CODE_POSTAL_PATTERN.matcher(sitePostalCode);
            return matcher.matches();
        } else {
            return true;
        }
    }

    @Override
    protected boolean isChildActivity() {
        return true;
    }

    @Click(R.id.create_site_button)
    void onCreateSiteButtonClicked() {
        if (validateSite()) {
            site.save();
            Intent intent = new Intent();
            intent.putExtra("siteId", site.getId());

            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    protected boolean validateSite() {
        if (!checkSiteFields()) {
            return false;
        }

        if (!checkSiteNoImmo()) {
            displayErrorBox(com.orange.ocara.R.string.create_site_error_title, com.orange.ocara.R.string.create_site_duplicate_site_noImmo);
            this.siteNoImmo.requestFocus();
            return false;
        }

        if (!checkSiteName()) {
            displayErrorBox(com.orange.ocara.R.string.create_site_error_title, com.orange.ocara.R.string.create_site_duplicate_site_name);
            this.siteName.requestFocus();
            return false;
        }

        site = new SiteEntity();
        site.setNoImmo(getSiteNoImmo());
        site.setName(getSiteName());
        site.setStreet(getSiteStreet());
        if (!getSitePostalCode().isEmpty()) {
            site.setCode(Integer.parseInt(getSitePostalCode()));
        }
        site.setCity(getSiteCity());

        return true;
    }

    private boolean checkSiteNoImmo() {
        return !modelManager.checkSiteExistenceByNoImmo(getSiteNoImmo());
    }

    private boolean checkSiteName() {
        return !modelManager.checkSiteExistenceByName(getSiteName());
    }

    private class CheckTextWatcher implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {
            // do nothing yet
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // do nothing yet
        }

        @Override
        public void onTextChanged(CharSequence userInput, int start, int before, int count) {
            updateCreateSiteButton();
        }
    }

}
