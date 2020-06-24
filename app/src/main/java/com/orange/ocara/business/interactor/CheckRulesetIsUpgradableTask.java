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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import timber.log.Timber;

/**
 * a {@link UseCase} dedicated to check if a {@link RulesetModel} is upgradable or not.
 */
public class CheckRulesetIsUpgradableTask implements UseCase<CheckRulesetIsUpgradableTask.CheckRulesetIsUpgradableRequest, CheckRulesetIsUpgradableTask.CheckRulesetIsUpgradableResponse> {

    private final RulesetRepository repository;

    public CheckRulesetIsUpgradableTask(RulesetRepository repository) {
        this.repository = repository;
    }

    @Override
    public void executeUseCase(@NonNull CheckRulesetIsUpgradableRequest request, UseCaseCallback<CheckRulesetIsUpgradableResponse> callback) {
        CheckRulesetIsUpgradableResponse response;

        RulesetModel ruleset = repository.findOne(request.getReference(), request.getVersion());
        if (ruleset != null && ruleset.isUpgradable()) {
            response = CheckRulesetIsUpgradableResponse.ok();
        } else {
            response = CheckRulesetIsUpgradableResponse.ko();
        }

        Timber.d("TaskMessage=Checking upgradability succeeded;TaskResponse=%s", response.getCode());
        callback.onComplete(response);
    }

    /**
     * the request dedicated to the {@link CheckRulesetIsUpgradableTask}
     */
    @RequiredArgsConstructor
    public static final class CheckRulesetIsUpgradableRequest implements UseCase.RequestValues {
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
    public static final class CheckRulesetIsUpgradableResponse implements UseCase.ResponseValue {

        @Getter
        private final String code;

        public boolean isOk() {
            return "OK".equals(code);
        }

        /**
         * gives an instance of a positive {@link CheckRulesetIsUpgradableResponse}
         *
         * @return a valid {@link CheckRulesetIsUpgradableResponse}
         */
        public static CheckRulesetIsUpgradableResponse ok() {
            return new CheckRulesetIsUpgradableResponse("OK");
        }

        /**
         * gives an instance of a negative {@link CheckRulesetIsUpgradableResponse}
         *
         * @return a valid {@link CheckRulesetIsUpgradableResponse}
         */
        public static CheckRulesetIsUpgradableResponse ko() {
            return new CheckRulesetIsUpgradableResponse("KO");
        }
    }

}
