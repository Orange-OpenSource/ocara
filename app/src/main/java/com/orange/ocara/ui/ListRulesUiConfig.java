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
import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.business.service.impl.RuleSetServiceImpl;
import com.orange.ocara.ui.contract.ListRulesetsContract;
import com.orange.ocara.ui.presenter.ListRulesPresenter;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Configuration for the UI layer of the rules listing module
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EBean(scope = EBean.Scope.Singleton)
public class ListRulesUiConfig {

    @Bean(BizConfig.class)
    BizConfig bizConfig;

    @Bean(RuleSetServiceImpl.class)
    RuleSetService mRuleSetService;

    /**
     * Retrieves an Action Listener
     *
     * @param view a View that uses the listener
     * @return an implementation of {@link ListRulesetsContract.ListRulesUserActionsListener}
     */
    public ListRulesetsContract.ListRulesUserActionsListener actionsListener(ListRulesetsContract.ListRulesView view) {

        ListRulesPresenter presenter = new ListRulesPresenter();
        presenter.setView(view);
        presenter.setMRuleSetService(bizConfig.rulesetService());
        return presenter;
    }
}
