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
import com.orange.ocara.ui.activity.CreateAuditActivity;
import com.orange.ocara.ui.contract.CreateAuditContract;
import com.orange.ocara.ui.presenter.CreateAuditPresenter;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Configuration for the UI layer of the Audits Creation module
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EBean(scope = EBean.Scope.Singleton)
public class CreateAuditUiConfig {

    @Bean(BizConfig.class)
    BizConfig bizConfig;

    /**
     * Retrieves an Action Listener
     *
     * @param view a View that uses the listener
     * @return an implementation of {@link CreateAuditContract.CreateAuditUserActionsListener}
     */
    public CreateAuditContract.CreateAuditUserActionsListener actionsListener(CreateAuditActivity view) {

        return new CreateAuditPresenter(view, bizConfig.retrieveRulesetsTask(), bizConfig.savePreferredRulesetTask());
    }
}
