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
import com.orange.ocara.business.binding.BizError;
import com.orange.ocara.business.interactor.UpgradeRulesetTask;
import com.orange.ocara.business.interactor.UseCase;
import com.orange.ocara.ui.contract.RulesetInfoContract;
import com.orange.ocara.ui.presenter.RulesetInfoPresenter;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import timber.log.Timber;

import static com.orange.ocara.business.interactor.CheckRulesetIsUpgradableTask.CheckRulesetIsUpgradableRequest;
import static com.orange.ocara.business.interactor.CheckRulesetIsUpgradableTask.CheckRulesetIsUpgradableResponse;

/**
 * Configuration for the UI layer of the Ruleset Information module
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EBean(scope = EBean.Scope.Singleton)
public class RulesetInfoUiConfig {

    @Bean(BizConfig.class)
    BizConfig bizConfig;

    /**
     * Retrieves an Action Listener
     *
     * @param view a View that uses the listener
     * @param rulesetUpgradable a parameter
     * @return an implementation of {@link RulesetInfoContract.RulesetInfoUserActionsListener}
     */
    public RulesetInfoContract.RulesetInfoUserActionsListener actionsListener(RulesetInfoContract.RulesetInfoView view, boolean rulesetUpgradable) {
        RulesetInfoContract.RulesetInfoUserActionsListener actionsListener;
        if (rulesetUpgradable) {
            Timber.i("ConfigMessage=Ruleset is readable and upgradable");
            actionsListener = new RulesetInfoPresenter(
                    view,
                    bizConfig.upgradeRulesetTask(),
                    bizConfig.checkRulesetRulesExistenceTask(),
                    bizConfig.checkRulesetIsUpgradableTask(),
                    bizConfig.loadRulesetTask());
        } else {
            Timber.i("ConfigMessage=Ruleset is only readable");
            actionsListener = new RulesetInfoPresenter(
                    view,
                    alwaysError(),
                    bizConfig.checkRulesetRulesExistenceTask(),
                    alwaysFalse(),
                    bizConfig.loadRulesetTask());
        }
        return actionsListener;
    }

    /**
     * @return a basic {@link UseCase} that returns a KO-{@link CheckRulesetIsUpgradableResponse},
     * whatever the given {@link CheckRulesetIsUpgradableRequest}
     */
    private static UseCase<CheckRulesetIsUpgradableRequest, CheckRulesetIsUpgradableResponse> alwaysFalse() {
        return (request, callback) -> callback.onComplete(CheckRulesetIsUpgradableResponse.ko());
    }

    /**
     *
     * @return a basic {@link UseCase} that raises a {@link com.orange.ocara.business.binding.ErrorBundle}, whatever the given {@link UpgradeRulesetTask.UpgradeRulesetRequest}
     */
    private static UseCase<UpgradeRulesetTask.UpgradeRulesetRequest, UpgradeRulesetTask.UpgradeRulesetResponse> alwaysError() {
        return (request, callback) -> callback.onError(new BizError("Impossible operation"));
    }
}
