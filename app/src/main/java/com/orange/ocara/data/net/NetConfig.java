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

package com.orange.ocara.data.net;

import android.content.Context;
import android.util.Patterns;

import com.orange.ocara.BuildConfig;
import com.orange.ocara.data.net.model.RulesetLightWs;
import com.orange.ocara.data.net.model.RulesetWs;
import com.orange.ocara.data.source.ImageSource.ImageRemote;
import com.orange.ocara.data.source.RulesetSource.RulesetRemote;
import com.orange.ocara.data.source.TermsSource.TermsRemote;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import timber.log.Timber;

/**
 * a factory for creating connectors from the data/net layer
 */
@EBean(scope = EBean.Scope.Singleton)
public class NetConfig {

    private ImageRemote imageRemote;

    private RulesetRemote rulesetRemote;

    private TermsRemote termsRemote;

    private boolean online = false;

    @RootContext
    void setContext(Context rootContext) {

        // configuring remote access is only relevant if it is correctly configured.
        if (isConfigValid()) {
            OkHttpClient client = ApiClientFactory.makeOkHttpClient(rootContext, BuildConfig.DEBUG);

            imageRemote = new ImageRemoteImpl();
            rulesetRemote = new RulesetRemoteImpl(RulesetServiceFactory.makeRulesetService(client));
            termsRemote = new TermsRemoteImpl(TermsServiceFactory.makeTermsService(client));
            online = true;
        } else {
            // else, connectors are stubbed, so that they bring only empty or null data.
            imageRemote = filename -> null;
            rulesetRemote = rulesetRemoteStub();
            termsRemote = () -> null;
        }
    }

    public boolean isConnected() {
        return online;
    }

    /** @return true if the parameters are correct */
    private static boolean isConfigValid() {
        String url = BuildConfig.OCARA_SERVEUR;
        boolean result = Patterns.WEB_URL.matcher(url).matches();
        Timber.i("Message=Checking remote is available;Param=%s;Result=%b", url, result);
        return result;
    }

    /**
     * @return a {@link ImageRemote}
     */
    public ImageRemote imageRemote() {
        return imageRemote;
    }

    /**
     * @return a {@link RulesetRemote}
     */
    public RulesetRemote rulesetRemote() {
        return rulesetRemote;
    }

    /**
     * @return a {@link TermsRemote}
     */
    public TermsRemote termsRemote() {
        return termsRemote;
    }

    /** @return a stub for {@link RulesetRemote} */
    private RulesetRemote rulesetRemoteStub() {

        return new RulesetRemote() {
            @Override
            public List<RulesetLightWs> findAll() {
                return Collections.emptyList();
            }

            @Override
            public RulesetWs findOne(String reference, Integer version) {
                return null;
            }

            @Override
            public RulesetLightWs findLast(String reference) {
                return null;
            }
        };
    }
}
