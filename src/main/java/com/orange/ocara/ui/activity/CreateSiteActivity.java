/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
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

import com.orange.ocara.model.ModelManager;
import com.orange.ocara.model.Site;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

@EActivity(resName="activity_create_site")
public class CreateSiteActivity extends BaseActivity {

    private static final Pattern SITE_CODE_POSTAL_PATTERN = Pattern.compile("^(\\d{5}$)");

    @Inject
    ModelManager modelManager;

    @ViewById(resName="createSiteLayout")
    LinearLayout createSiteLayout;

    @ViewById(resName="site_noimmo")
    EditText siteNoImmo;
    @ViewById(resName="site_name")
    EditText siteName;
    @ViewById(resName="site_street")
    EditText siteStreet;
    @ViewById(resName="site_postal_code")
    EditText sitePostalCode;
    @ViewById(resName="site_city")
    EditText siteCity;

    @ViewById(resName="create_site_button")
    Button createSitetButton;

    Site site;


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
     * Checks site fields.
     *
     * @return true if site mandatory fields are set, false otherwise
     */
    protected boolean checkSiteFields() {
        if (StringUtils.isBlank(getSiteNoImmo()) || StringUtils.isBlank(getSiteName())) {
            return false;
        }
        if (!getSitePostalCode().isEmpty()) {
            return checkSitePostalCode(getSitePostalCode());
        }
        return true;
    }


    protected boolean checkSitePostalCode(String sitePostalCode) {
        if (!StringUtils.isBlank(sitePostalCode)) {
            Matcher matcher = SITE_CODE_POSTAL_PATTERN.matcher(sitePostalCode);
            if (matcher.matches()) {
                return true;
            }
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    protected boolean isChildActivity() {
        return true;
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
            updateCreateSiteButton();
        }
    }

    @Click(resName="create_site_button")
    void onCreateSiteButtonClicked() {

        if (validateSite()) {
            site.save();
            Intent intent = new Intent();
            intent.putExtra("siteId",site.getId());

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

        site = new Site();
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

}
