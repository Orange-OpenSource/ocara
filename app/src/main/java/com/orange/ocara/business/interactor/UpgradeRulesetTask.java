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

import android.content.Context;

import androidx.annotation.NonNull;

import com.orange.ocara.business.binding.BizError;
import com.orange.ocara.business.common.TaskStatus;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.model.VersionableModel;
import com.orange.ocara.business.repository.RulesetRepository;
import com.orange.ocara.data.net.model.Ruleset;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Task dedicated to the upgrading of rulesets
 */
public class UpgradeRulesetTask implements UseCase<UpgradeRulesetTask.UpgradeRulesetRequest, UpgradeRulesetTask.UpgradeRulesetResponse> {

    private final RulesetRepository repository;

    public UpgradeRulesetTask(RulesetRepository repository) {
        this.repository = repository;
    }

    @Override
    public void executeUseCase(@NonNull UpgradeRulesetRequest request, UseCaseCallback<UpgradeRulesetResponse> callback) {
        try {
            RulesetModel updatedRuleset = repository.upgradeRuleset(request.getRuleset());
            callback.onComplete(UpgradeRulesetResponse.ok(updatedRuleset));
        } catch (Exception ex) {
            callback.onError(new BizError(ex));
        }
    }

    /**
     * the request embeds the {@link Context} and a {@link Ruleset}
     */
    @RequiredArgsConstructor
    public static final class UpgradeRulesetRequest implements UseCase.RequestValues {

        @Getter
        private final VersionableModel ruleset;
    }

    /**
     * the response related to this Use-Case
     */
    @RequiredArgsConstructor
    public static final class UpgradeRulesetResponse implements UseCase.ResponseValue {

        private final TaskStatus status;

        /**
         * the upgraded ruleset
         */
        @Getter
        private final RulesetModel ruleset;

        public String getCode() {
            return status.name();
        }

        /**
         * creates a pre-configured {@link UpgradeRulesetResponse}
         *
         * @param updatedRuleset a {@link RulesetModel}
         * @return an instance of {@link RetrieveRulesetsTask.RetrieveRulesetsResponse}
         */
        public static UpgradeRulesetResponse ok(RulesetModel updatedRuleset) {
            return new UpgradeRulesetResponse(TaskStatus.SUCCESS, updatedRuleset);
        }
    }
}
