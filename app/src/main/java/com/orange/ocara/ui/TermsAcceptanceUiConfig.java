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

package com.orange.ocara.ui;

import com.orange.ocara.business.BizConfig;
import com.orange.ocara.ui.contract.TermsOfUseAcceptanceContract;
import com.orange.ocara.ui.presenter.TermsOfUseAcceptancePresenter;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.orange.ocara.ui.contract.TermsOfUseAcceptanceContract.TermsOfUseAcceptanceUserActionsListener;
import static com.orange.ocara.ui.contract.TermsOfUseAcceptanceContract.TermsOfUseAcceptanceView;

/**
 * Configuration for the UI layer of the Terms-Of-Use acceptance module
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EBean(scope = EBean.Scope.Singleton)
public class TermsAcceptanceUiConfig {

    @Bean(BizConfig.class)
    BizConfig bizConfig;

    /**
     * Retrieves a listener for actions, according to {@link TermsOfUseAcceptanceContract}
     *
     * @param view a {@link TermsOfUseAcceptanceView}
     * @return an instance of {@link TermsOfUseAcceptanceUserActionsListener}
     */
    public TermsOfUseAcceptanceUserActionsListener actionsListener(TermsOfUseAcceptanceView view) {
        return new TermsOfUseAcceptancePresenter(
                view,
                bizConfig.acceptTermsOfUseTask(),
                bizConfig.declineTermsOfUseTask(),
                bizConfig.loadTermsOfUseTask());
    }
}
