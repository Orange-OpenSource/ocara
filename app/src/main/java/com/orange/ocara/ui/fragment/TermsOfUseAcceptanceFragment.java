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

import android.app.AlertDialog;
import android.content.Context;
import android.net.http.SslError;
import android.os.Build;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

import com.orange.ocara.R;
import com.orange.ocara.business.binding.BizException;
import com.orange.ocara.ui.TermsAcceptanceUiConfig;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.net.URL;

import lombok.RequiredArgsConstructor;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.orange.ocara.ui.contract.TermsOfUseAcceptanceContract.TermsOfUseAcceptanceUserActionsListener;
import static com.orange.ocara.ui.contract.TermsOfUseAcceptanceContract.TermsOfUseAcceptanceView;

/**
 * Fragment that displays a form for accepting Terms-of-use
 */
@EActivity(R.layout.fragment_layout_terms_acceptance)
public class TermsOfUseAcceptanceFragment extends FragmentActivity implements TermsOfUseAcceptanceView {

    /**
     * the {@link WebView} that displays the content of TOU
     */
    @ViewById(R.id.terms_of_use_reading_webview)
    WebView mWebView;

    /**
     * the {@link Button} for accepting the content
     */
    @ViewById(R.id.terms_of_use_accept_button)
    Button mAcceptButton;

    /**
     * the {@link Button} for declining the content
     */
    @ViewById(R.id.terms_of_use_quit_button)
    Button mDeclineButton;

    /**
     * the actions that can be executed by a user
     */
    private TermsOfUseAcceptanceUserActionsListener actionsListener;

    /**
     * the UI config
     */
    @Bean(TermsAcceptanceUiConfig.class)
    TermsAcceptanceUiConfig uiConfig;

    /**
     * Init function
     */
    @AfterViews
    @Background
    public void afterViews() {

        actionsListener = uiConfig.actionsListener(this);

        actionsListener.loadTerms();
    }

    /**
     * displays the webview that manages the terms-of-use, by loading it as raw text.
     */
    @UiThread
    @Override
    public void showTerms(String rawData) {
        mWebView.loadDataWithBaseURL("", rawData, "text/html", "UTF-8", "");
    }

    /**
     * displays the webview that contains the terms-of-use, by loading it from a URL. As the URL may
     * be in HTTPS, we use a {@link WebViewClient} that can handle SSL.
     *
     * This method should not be used. One should rather use the function showTerms(String), in
     * which the loading of the content is handled by the data layer.
     * See {@link com.orange.ocara.data.source.TermsSource.TermsRemote} and its default implementation.
     */
    @UiThread
    @Override
    public void showTerms(URL url) {

        mWebView.setWebViewClient(new SSLTolerentWebViewClient(this));
        mWebView.loadUrl(String.valueOf(url));
    }

    /**
     * displays an error message
     *
     * @param message a cause for the error
     */
    @UiThread
    @Override
    public void showError(String message) {
        makeText(getApplicationContext(), R.string.terms_error, LENGTH_SHORT)
                .show();
    }

    /**
     * event triggered when the user clicked on the accept button
     */
    @Click(R.id.terms_of_use_accept_button)
    public void acceptTerms() {
        actionsListener.acceptTerms();
    }

    /**
     * event triggered when the user clicked on the decline button
     */
    @Click(R.id.terms_of_use_quit_button)
    public void declineTerms() {
        actionsListener.declineTerms();
    }

    /**
     * goes back to {@link com.orange.ocara.ui.activity.CreateAuditActivity}
     */
    @Override
    public void showTermsAccepted() {
        makeText(getApplicationContext(), R.string.terms_accepted, LENGTH_SHORT)
                .show();

        super.onBackPressed();
    }

    /**
     * closes the current activity
     */
    @Override
    public void showTermsDeclined() {

        makeText(getApplicationContext(), R.string.terms_declined, LENGTH_SHORT)
                .show();

        // Terms refused. We leave the app.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else {
            finish();
        }
    }

    /**
     * a {@link WebViewClient} that can handle SSL
     */
    @RequiredArgsConstructor
    private static class SSLTolerentWebViewClient extends WebViewClient {

        private final Context context;

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

            int message;
            switch (error.getPrimaryError()) {
                case SslError.SSL_UNTRUSTED:
                    message = R.string.ssl_error_untrusted;
                    break;
                case SslError.SSL_EXPIRED:
                    message = R.string.ssl_error_expired;
                    break;
                case SslError.SSL_IDMISMATCH:
                    message = R.string.ssl_error_mismatched;
                    break;
                case SslError.SSL_NOTYETVALID:
                    message = R.string.ssl_error_not_yet_valid;
                    break;
                default:
                    throw new BizException("Unexpected SSL error");
            }

            final AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setTitle(R.string.ssl_error_title)
                    .setMessage(message)
                    .setPositiveButton(R.string.action_continue, (dialog, which) -> handler.proceed())
                    .setNegativeButton(R.string.action_cancel, (dialog, which) -> handler.cancel())
                    .create();

            alertDialog.show();
        }

    }
}
