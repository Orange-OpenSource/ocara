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

package com.orange.ocara.business;

import android.content.Context;

import com.orange.ocara.business.interactor.AcceptTermsOfUseTask;
import com.orange.ocara.business.interactor.ChangeOnboardingStepTask;
import com.orange.ocara.business.interactor.CheckOnboardingStatusTask;
import com.orange.ocara.business.interactor.CheckRemoteStatusTask;
import com.orange.ocara.business.interactor.CheckRulesetIsUpgradableTask;
import com.orange.ocara.business.interactor.CheckRulesetRulesExistenceTask;
import com.orange.ocara.business.interactor.CheckTermsOfUseTask;
import com.orange.ocara.business.interactor.CheckTermsStatusTask;
import com.orange.ocara.business.interactor.CheckTermsStatusTask.CheckTermsStatusRequest;
import com.orange.ocara.business.interactor.CompleteOnboardingTask;
import com.orange.ocara.business.interactor.DeclineTermsOfUseTask;
import com.orange.ocara.business.interactor.InitTask;
import com.orange.ocara.business.interactor.LoadOnboardingItemsTask;
import com.orange.ocara.business.interactor.LoadRuleExplanationsTask;
import com.orange.ocara.business.interactor.LoadRuleExplanationsTask.LoadRuleExplanationsRequest;
import com.orange.ocara.business.interactor.LoadRulesTask;
import com.orange.ocara.business.interactor.LoadRulesTask.LoadRulesResponse;
import com.orange.ocara.business.interactor.LoadRulesetTask;
import com.orange.ocara.business.interactor.LoadRulesetTask.LoadRulesetResponse;
import com.orange.ocara.business.interactor.LoadTermsOfUseTask;
import com.orange.ocara.business.interactor.RedoAuditTask;
import com.orange.ocara.business.interactor.RetrieveRulesetsTask;
import com.orange.ocara.business.interactor.SavePreferredRulesetTask;
import com.orange.ocara.business.interactor.SkipOnboardingTask;
import com.orange.ocara.business.interactor.SkipOnboardingTask.SkipOnboardingResponse;
import com.orange.ocara.business.interactor.UpgradeRulesetTask;
import com.orange.ocara.business.interactor.UseCase;
import com.orange.ocara.business.interactor.UseCaseHandler;
import com.orange.ocara.business.interactor.UseCaseThreadPoolScheduler;
import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.business.service.impl.RuleSetServiceImpl;
import com.orange.ocara.data.DataConfig;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import static com.orange.ocara.business.interactor.AcceptTermsOfUseTask.AcceptTermsOfUseRequest;
import static com.orange.ocara.business.interactor.AcceptTermsOfUseTask.AcceptTermsOfUseResponse;
import static com.orange.ocara.business.interactor.CheckOnboardingStatusTask.CheckOnboardingStatusRequest;
import static com.orange.ocara.business.interactor.CheckOnboardingStatusTask.CheckOnboardingStatusResponse;
import static com.orange.ocara.business.interactor.CheckRulesetIsUpgradableTask.CheckRulesetIsUpgradableRequest;
import static com.orange.ocara.business.interactor.CheckRulesetIsUpgradableTask.CheckRulesetIsUpgradableResponse;
import static com.orange.ocara.business.interactor.CheckRulesetRulesExistenceTask.CheckRulesetRulesExistenceRequest;
import static com.orange.ocara.business.interactor.CheckRulesetRulesExistenceTask.CheckRulesetRulesExistenceResponse;
import static com.orange.ocara.business.interactor.CheckTermsOfUseTask.CheckTermsOfUseRequest;
import static com.orange.ocara.business.interactor.CheckTermsOfUseTask.CheckTermsOfUseResponse;
import static com.orange.ocara.business.interactor.CompleteOnboardingTask.CompleteOnboardingRequest;
import static com.orange.ocara.business.interactor.CompleteOnboardingTask.CompleteOnboardingResponse;
import static com.orange.ocara.business.interactor.DeclineTermsOfUseTask.DeclineTermsOfUseRequest;
import static com.orange.ocara.business.interactor.DeclineTermsOfUseTask.DeclineTermsOfUseResponse;
import static com.orange.ocara.business.interactor.LoadOnboardingItemsTask.LoadOnboardingItemsRequest;
import static com.orange.ocara.business.interactor.LoadOnboardingItemsTask.LoadOnboardingItemsResponse;
import static com.orange.ocara.business.interactor.LoadRuleExplanationsTask.LoadRuleExplanationsResponse;
import static com.orange.ocara.business.interactor.LoadRulesTask.LoadRulesRequest;
import static com.orange.ocara.business.interactor.LoadTermsOfUseTask.LoadTermsOfUseRequest;
import static com.orange.ocara.business.interactor.LoadTermsOfUseTask.LoadTermsOfUseResponse;
import static com.orange.ocara.business.interactor.RetrieveRulesetsTask.RetrieveRulesetsRequest;
import static com.orange.ocara.business.interactor.RetrieveRulesetsTask.RetrieveRulesetsResponse;
import static com.orange.ocara.business.interactor.SavePreferredRulesetTask.SavePreferredRulesetRequest;
import static com.orange.ocara.business.interactor.SavePreferredRulesetTask.SavePreferredRulesetResponse;
import static com.orange.ocara.business.interactor.UpgradeRulesetTask.UpgradeRulesetRequest;
import static com.orange.ocara.business.interactor.UpgradeRulesetTask.UpgradeRulesetResponse;

/**
 * Factory for the {@link UseCase}s
 *
 * Should only expose {@link UseCase}s to the upper layers.
 */
@EBean(scope = EBean.Scope.Singleton)
public class BizConfig {

    /**
     * Configuration for the Data layer
     */
    @Bean(DataConfig.class)
    DataConfig dataConfig;

    /**
     * Useful way to findAll access to the main {@link Context}
     */
    @RootContext
    Context rootContext;

    /************************************************************************
     * A bunch of beans, that should be refactored ASAP
     *
     * The main objective is to findAll rid of the annotation @Bean everywhere (excluding config classes)
     */
    @Bean(RuleSetServiceImpl.class)
    RuleSetService rulesetService;

    public final static UseCaseHandler USE_CASE_HANDLER = new UseCaseHandler(new UseCaseThreadPoolScheduler());

    /**
     * End of the refactorable beans
     ***********************************************************************/

    /**
     *
     * @return an instance of {@link UseCase} to apply the terms acceptance
     */
    public UseCase<AcceptTermsOfUseRequest, AcceptTermsOfUseResponse> acceptTermsOfUseTask() {
        return new AcceptTermsOfUseTask(dataConfig.termsRepository());
    }

    /**
     *
     * @return an instance of {@link UseCase} to reject the terms of use
     */
    public UseCase<DeclineTermsOfUseRequest, DeclineTermsOfUseResponse> declineTermsOfUseTask() {
        return new DeclineTermsOfUseTask(dataConfig.termsRepository());
    }

    /**
     *
     * @return an instance of {@link UseCase} to check if terms are already accepted
     */
    public UseCase<CheckTermsOfUseRequest, CheckTermsOfUseResponse> checkTermsOfUseTask() {
        return new CheckTermsOfUseTask(dataConfig.termsRepository());
    }

    /**
     *
     * @return an instance of {@link UseCase} to retrieve the content of the terms
     */
    public UseCase<LoadTermsOfUseRequest, LoadTermsOfUseResponse> loadTermsOfUseTask() {
        return new LoadTermsOfUseTask(dataConfig.termsRepository());
    }

    /**
     *
     * @return an instance of {@link UseCase} to check if a ruleset is upgradable
     */
    public UseCase<CheckRulesetIsUpgradableRequest, CheckRulesetIsUpgradableResponse> checkRulesetIsUpgradableTask() {

        return new CheckRulesetIsUpgradableTask(dataConfig.rulesetRepository());
    }

    /**
     *
     * @return an instance of {@link UseCase} to check if a ruleset exists
     */
    public UseCase<CheckRulesetRulesExistenceRequest, CheckRulesetRulesExistenceResponse> checkRulesetRulesExistenceTask() {
        return new CheckRulesetRulesExistenceTask(dataConfig.rulesetRepository());
    }

    /**
     *
     * @return an instance of {@link UseCase} to redo an audit
     */
    public UseCase<RedoAuditTask.RedoAuditRequest, RedoAuditTask.RedoAuditResponse> redoAuditTask() {
        return new RedoAuditTask(dataConfig.auditRepository());
    }

    /**
     *
     * @return an instance of {@link UseCase} to retrieve a ruleset
     */
    public UseCase<RetrieveRulesetsRequest, RetrieveRulesetsResponse> retrieveRulesetsTask() {
        return new RetrieveRulesetsTask(dataConfig.rulesetRepository());
    }

    /**
     *
     * @return an instance of {@link UseCase} to save a ruleset as a user's preference
     */
    public UseCase<SavePreferredRulesetRequest, SavePreferredRulesetResponse> savePreferredRulesetTask() {
        return new SavePreferredRulesetTask(dataConfig.rulesetRepository());
    }

    /**
     *
     * @return an instance of {@link UseCase} to upgrade a ruleset
     */
    public UseCase<UpgradeRulesetRequest, UpgradeRulesetResponse> upgradeRulesetTask() {
        return new UpgradeRulesetTask(dataConfig.rulesetRepository());
    }

    /** @return an instance of {@link UseCase} to check if the tutorial is already done or not */
    public UseCase<CheckOnboardingStatusRequest, CheckOnboardingStatusResponse> checkOnboardingStatusTask() {

        return new CheckOnboardingStatusTask(dataConfig.onboardingRepository());
    }

    /** @return an instance of {@link UseCase} to notify that onboarding should be notified as completed */
    public UseCase<CompleteOnboardingRequest, CompleteOnboardingResponse> completeOnboardingTask() {

        return new CompleteOnboardingTask(dataConfig.onboardingRepository());
    }

    /** @return an instance of {@link UseCase} to notify that onboarding should be skipped */
    public UseCase<SkipOnboardingTask.SkipOnboardingRequest, SkipOnboardingResponse> skipOnboardingTask() {

        return new SkipOnboardingTask();
    }

    /** @return an instance of {@link UseCase} to check if terms were accepted or not */
    public UseCase<CheckTermsStatusRequest, CheckTermsStatusTask.CheckTermsStatusResponse> checkTermsStatusTask() {
        return new CheckTermsStatusTask(dataConfig.termsRepository());
    }

    /** @return an instance of {@link UseCase} to retrieve the content of the tutorial */
    public UseCase<LoadOnboardingItemsRequest, LoadOnboardingItemsResponse> loadOnboardingItemsTask() {
        return new LoadOnboardingItemsTask(dataConfig.onboardingRepository());
    }

    /** @return an instance of {@link UseCase} to retrieve a new step in the tutorial */
    public UseCase<ChangeOnboardingStepTask.ChangeOnboardingStepRequest, ChangeOnboardingStepTask.ChangeOnboardingStepResponse> changeOnboardingItemTask() {
        return new ChangeOnboardingStepTask();
    }

    /** @return an instance of {@link UseCase} that loads a ruleset */
    public UseCase<LoadRulesetTask.LoadRulesetRequest, LoadRulesetResponse> loadRulesetTask() {
        return new LoadRulesetTask(dataConfig.rulesetRepository());
    }

    /** @return an instance of {@link UseCase} that initializes the app */
    public UseCase<InitTask.InitRequest, InitTask.InitResponse> initTask() {
        return new InitTask(dataConfig.rulesetRepository(), dataConfig.locationRepository());
    }

    /** @return an instance of {@link UseCase} */
    public UseCase<LoadRuleExplanationsRequest, LoadRuleExplanationsResponse> browseRuleExplanationTask() {
        return new LoadRuleExplanationsTask(dataConfig.explanationRepository());
    }

    /** @return an instance of {@link UseCase} */
    public UseCase<LoadRulesRequest, LoadRulesResponse> browseRulesTask() {
        return new LoadRulesTask(dataConfig.questionRepository(), dataConfig.ruleRepository());
    }

    /** @return an instance of {@link UseCase} that loads {@link com.orange.ocara.business.model.ExplanationModel}s */
    public UseCase<LoadRuleExplanationsRequest, LoadRuleExplanationsResponse> loadExplanationsTask() {
        return new LoadRuleExplanationsTask(dataConfig.explanationRepository());
    }

    /** @return a service for {@link com.orange.ocara.data.net.model.Ruleset}s */
    public RuleSetService rulesetService() {
        return rulesetService;
    }

    /** @return an instance of {@link UseCase} */
    public UseCase<CheckRemoteStatusTask.CheckRemoteStatusRequest, CheckRemoteStatusTask.CheckRemoteStatusResponse> checkRemoteStatusTask() {
        return new CheckRemoteStatusTask(dataConfig.healthRepository());
    }
}
