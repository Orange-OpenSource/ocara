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

package com.orange.ocara.business.interactor;

import androidx.annotation.NonNull;

import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.model.VersionableModel;
import com.orange.ocara.business.repository.RulesetRepository;
import com.orange.ocara.data.net.model.RulesetEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import timber.log.Timber;

import static com.orange.ocara.business.interactor.CheckRulesetRulesExistenceTask.CheckRulesetRulesExistenceRequest;
import static com.orange.ocara.business.interactor.CheckRulesetRulesExistenceTask.CheckRulesetRulesExistenceResponse;

/**
 * a {@link UseCase} dedicated to check if a {@link RulesetModel} has {@link RulesetEntity} or not.
 */
public class CheckRulesetRulesExistenceTask implements UseCase<CheckRulesetRulesExistenceRequest, CheckRulesetRulesExistenceResponse> {

    private final RulesetRepository repository;

    public CheckRulesetRulesExistenceTask(RulesetRepository repository) {
        this.repository = repository;
    }

    @Override
    public void executeUseCase(@NonNull CheckRulesetRulesExistenceRequest request, UseCase.UseCaseCallback<CheckRulesetRulesExistenceResponse> callback) {
        CheckRulesetRulesExistenceResponse response;

        RulesetModel ruleset = repository.findOne(request.getReference(), request.getVersion());
        if (ruleset != null && ruleset.isLocallyAvailable()) {
            response = CheckRulesetRulesExistenceResponse.ok();
        } else {
            response = CheckRulesetRulesExistenceResponse.ko();
        }

        Timber.d("TaskMessage=Checking rules existence succeeded;TaskResponse=%s", response.getCode());
        callback.onComplete(response);
    }

    /**
     * the request dedicated to the {@link CheckRulesetRulesExistenceTask}
     */
    @RequiredArgsConstructor
    public static final class CheckRulesetRulesExistenceRequest implements UseCase.RequestValues {
        private final VersionableModel ruleset;

        public String getReference() {
            return ruleset.getReference();
        }

        public Integer getVersion() {
            return Integer.valueOf(ruleset.getVersion());
        }
    }

    /**
     * when the request is successful, the response contains the list of items to display
     */
    @RequiredArgsConstructor
    public static final class CheckRulesetRulesExistenceResponse implements UseCase.ResponseValue {

        @Getter
        private final String code;

        public boolean isOk() {
            return "OK".equals(code);
        }

        /**
         * gives an instance of a positive {@link CheckRulesetRulesExistenceResponse}
         *
         * @return a valid {@link CheckRulesetRulesExistenceResponse}
         */
        public static CheckRulesetRulesExistenceResponse ok() {
            return new CheckRulesetRulesExistenceResponse("OK");
        }

        /**
         * gives an instance of a negative {@link CheckRulesetRulesExistenceResponse}
         *
         * @return a valid {@link CheckRulesetRulesExistenceResponse}
         */
        public static CheckRulesetRulesExistenceResponse ko() {
            return new CheckRulesetRulesExistenceResponse("KO");
        }
    }
}
