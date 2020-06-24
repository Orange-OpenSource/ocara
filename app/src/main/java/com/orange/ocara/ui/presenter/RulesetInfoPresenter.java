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

package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.binding.ErrorBundle;
import com.orange.ocara.business.interactor.CheckRulesetIsUpgradableTask.CheckRulesetIsUpgradableRequest;
import com.orange.ocara.business.interactor.CheckRulesetIsUpgradableTask.CheckRulesetIsUpgradableResponse;
import com.orange.ocara.business.interactor.CheckRulesetRulesExistenceTask.CheckRulesetRulesExistenceRequest;
import com.orange.ocara.business.interactor.CheckRulesetRulesExistenceTask.CheckRulesetRulesExistenceResponse;
import com.orange.ocara.business.interactor.UpgradeRulesetTask.UpgradeRulesetRequest;
import com.orange.ocara.business.interactor.UpgradeRulesetTask.UpgradeRulesetResponse;
import com.orange.ocara.business.interactor.UseCase;
import com.orange.ocara.business.interactor.UseCase.UseCaseCallback;
import com.orange.ocara.business.model.VersionableModel;
import com.orange.ocara.ui.contract.RulesetInfoContract;

import lombok.RequiredArgsConstructor;
import timber.log.Timber;

import static com.orange.ocara.business.interactor.LoadRulesetTask.LoadRulesetRequest;
import static com.orange.ocara.business.interactor.LoadRulesetTask.LoadRulesetResponse;

/**
 * Mediator between a {@link RulesetInfoContract.RulesetInfoView} and its {@link UseCase}s
 */
@RequiredArgsConstructor
public class RulesetInfoPresenter implements RulesetInfoContract.RulesetInfoUserActionsListener {

    private final RulesetInfoContract.RulesetInfoView view;

    private final UseCase<UpgradeRulesetRequest, UpgradeRulesetResponse> upgradeRulesetTask;

    private final UseCase<CheckRulesetRulesExistenceRequest, CheckRulesetRulesExistenceResponse> checkRulesetRulesExistenceTask;

    private final UseCase<CheckRulesetIsUpgradableRequest, CheckRulesetIsUpgradableResponse> checkRulesetIsUpgradableTask;

    private final UseCase<LoadRulesetRequest, LoadRulesetResponse> loadRulesetTask;

    @Override
    public void upgradeRuleset(final VersionableModel ruleset) {
        Timber.i(
                "PresenterMessage=Start upgrading ruleset;RulesetReference=%s;RulesetVersion=%s",
                ruleset.getReference(), ruleset.getVersion());

        view.showProgressDialog();

        upgradeRulesetTask.executeUseCase(
                new UpgradeRulesetRequest(ruleset),
                new UseCaseCallback<UpgradeRulesetResponse>() {
                    @Override
                    public void onComplete(UpgradeRulesetResponse response) {
                        Timber.d("PresenterMessage=Item upgraded successfully;PresenterResponse=%s", response.getCode());
                        view.showDownloadSucceeded();
                        view.hideProgressDialog();
                        view.showRuleset(response.getRuleset());
                    }

                    @Override
                    public void onError(ErrorBundle errors) {
                        Timber.e(errors.getCause(), "PresenterMessage=Item upgrading failed;PresenterError=%s", errors.getMessage());
                        view.showDownloadFailed();
                        view.hideProgressDialog();
                    }
                }
        );
    }

    @Override
    public void checkRulesetIsValid(final VersionableModel ruleset) {

        view.disableRulesetDetails();
        view.disableRulesetUpgrade();

        checkRulesetRulesExistenceTask.executeUseCase(
                new CheckRulesetRulesExistenceRequest(ruleset),
                new UseCaseCallback<CheckRulesetRulesExistenceResponse>() {
                    @Override
                    public void onComplete(CheckRulesetRulesExistenceResponse response) {
                        Timber.d("PresenterMessage=Item availability checking is successful;PresenterResponse=%s", response.getCode());
                        if (response.isOk()) {
                            view.enableRulesetDetails();
                        } else {
                            view.disableRulesetDetails();
                        }
                    }

                    @Override
                    public void onError(ErrorBundle errors) {
                        Timber.e(errors.getCause(), "PresenterMessage=Item availability checking failed;TaskError=%s", errors.getMessage());
                        view.disableRulesetDetails();
                        view.showInvalidRuleset();
                    }
                }
        );

        checkRulesetIsUpgradableTask.executeUseCase(
                new CheckRulesetIsUpgradableRequest(ruleset),
                new UseCaseCallback<CheckRulesetIsUpgradableResponse>() {
                    @Override
                    public void onComplete(CheckRulesetIsUpgradableResponse response) {
                        Timber.d("PresenterMessage=Item upgrade checking is successful;PresenterResponse=%s", response.getCode());
                        if (response.isOk()) {
                            view.enableRulesetUpgrade();
                        } else {
                            view.disableRulesetUpgrade();
                        }
                    }

                    @Override
                    public void onError(ErrorBundle errors) {
                        Timber.e(errors.getCause(), "PresenterMessage=Item upgrade checking failed;PresenterError=%s", errors.getMessage());
                        view.disableRulesetUpgrade();
                        view.showInvalidRuleset();
                    }
                }
        );
    }

    @Override
    public void loadRuleset(VersionableModel ruleset) {

        loadRulesetTask.executeUseCase(new LoadRulesetRequest(ruleset), new UseCaseCallback<LoadRulesetResponse>() {
            @Override
            public void onComplete(LoadRulesetResponse response) {

                view.showRuleset(response.getContent());
            }

            @Override
            public void onError(ErrorBundle errors) {

                view.showInvalidRuleset();
            }
        });
    }
}
