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
import com.orange.ocara.ui.presenter.TermsOfUseReadingPresenter;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.orange.ocara.ui.contract.TermsOfUseReadingContract.TermsOfUseReadingUserActionsListener;
import static com.orange.ocara.ui.contract.TermsOfUseReadingContract.TermsOfUseReadingView;

/**
 * Configuration for the UI layer of the Terms-Of-Use reading module
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EBean(scope = EBean.Scope.Singleton)
public class TermsReadingUiConfig {

    @Bean(BizConfig.class)
    BizConfig bizConfig;

    /**
     * Retrieves a listener for reading actions, according to {@link com.orange.ocara.ui.contract.TermsOfUseReadingContract}
     *
     * @param view a {@link TermsOfUseReadingView}
     * @return an instance of {@link TermsOfUseReadingUserActionsListener}
     */
    public TermsOfUseReadingUserActionsListener actionsListener(TermsOfUseReadingView view) {
        return new TermsOfUseReadingPresenter(view, bizConfig.loadTermsOfUseTask());
    }
}
