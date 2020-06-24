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
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.repository.RulesetRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import timber.log.Timber;

/**
 * implementation of {@link UseCase} dedicated to the saving of a ruleset as a {@link android.content.SharedPreferences}
 */
@RequiredArgsConstructor
public class SavePreferredRulesetTask implements UseCase<SavePreferredRulesetTask.SavePreferredRulesetRequest, SavePreferredRulesetTask.SavePreferredRulesetResponse> {

    private final RulesetRepository rulesetRepository;

    @Override
    public void executeUseCase(@NonNull SavePreferredRulesetRequest request, UseCaseCallback<SavePreferredRulesetResponse> callback) {
        try {
            rulesetRepository.saveDefaultRuleset(request.getRuleset());
            callback.onComplete(SavePreferredRulesetResponse.ok());
        } catch (Exception ex) {
            Timber.e(ex);
            callback.onError(new BizError(ex));
        }
    }

    /**
     * the request embeds the {@link Context} and a {@link RulesetModel}
     */
    @RequiredArgsConstructor
    public static final class SavePreferredRulesetRequest implements UseCase.RequestValues {

        @Getter
        private final RulesetModel ruleset;
    }

    /**
     * the response related to this Use-Case
     */
    @RequiredArgsConstructor
    public static final class SavePreferredRulesetResponse implements UseCase.ResponseValue {

        private final String code;

        /**
         * @return an instance of {@link RetrieveRulesetsTask.RetrieveRulesetsResponse}
         */
        public static SavePreferredRulesetResponse ok() {
            return new SavePreferredRulesetResponse("OK");
        }
    }
}
