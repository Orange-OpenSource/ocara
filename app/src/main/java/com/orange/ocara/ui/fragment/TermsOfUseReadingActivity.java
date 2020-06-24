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

package com.orange.ocara.ui.fragment;

import android.app.Activity;
import android.webkit.WebView;
import android.widget.Button;

import com.orange.ocara.R;
import com.orange.ocara.ui.TermsReadingUiConfig;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.net.URL;

import timber.log.Timber;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.orange.ocara.ui.contract.TermsOfUseReadingContract.TermsOfUseReadingUserActionsListener;
import static com.orange.ocara.ui.contract.TermsOfUseReadingContract.TermsOfUseReadingView;

/**
 * Activity that uses a dedicated library for the reading of Terms-Of-Use. These Terms-Of-Use shall
 * be stored on the server side.
 */
@EActivity(R.layout.fragment_layout_terms_reading)
public class TermsOfUseReadingActivity extends Activity implements TermsOfUseReadingView {

    /**
     * the {@link WebView} that displays the content of TOU
     */
    @ViewById(R.id.terms_of_use_reading_webview)
    WebView mWebView;

    /**
     * the close button
     */
    @ViewById(R.id.terms_of_use_quit_button)
    Button mCloseButton;

    /**
     * the UI config
     */
    @Bean(TermsReadingUiConfig.class)
    TermsReadingUiConfig uiConfig;

    /**
     * the actions that can be executed by a user
     */
    private TermsOfUseReadingUserActionsListener actionsListener;

    /**
     * init function
     */
    @Background
    @AfterViews
    public void afterViews() {

        actionsListener = uiConfig.actionsListener(this);

        actionsListener.loadTerms();
    }

    /**
     * displays the content of the terms, as a raw text
     */
    @UiThread
    @Override
    public void showTerms(String rawData) {
        mWebView.loadDataWithBaseURL("", rawData, "text/html", "UTF-8", "");
    }

    /**
     * displays the content of the terms, as a remote text
     *
     * @param url a {@link URL} where terms are stored
     */
    @Override
    public void showTerms(URL url) {
        mWebView.loadUrl(String.valueOf(url));
    }

    @Override
    public void showError(String message) {
        makeText(getApplicationContext(), R.string.terms_error, LENGTH_SHORT)
                .show();
    }

    /**
     * closes the current activity
     */
    @Override
    @Click(R.id.terms_of_use_quit_button)
    public void hideTerms() {

        Timber.i("Message=Ending the reading of the terms-of-use");
        super.onBackPressed();
    }
}
