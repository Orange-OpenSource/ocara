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

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import timber.log.Timber;

import static com.orange.ocara.tools.ListUtils.newArrayList;

/**
 * implementation of {@link UseCase} that manages the retrieval of {@link RulesetModel}s that will be
 * displayed in the audit creation form.
 */
@RequiredArgsConstructor
public class RetrieveRulesetsTask implements UseCase<RetrieveRulesetsTask.RetrieveRulesetsRequest, RetrieveRulesetsTask.RetrieveRulesetsResponse> {

    private final RulesetRepository rulesetRepository;

    @Override
    public void executeUseCase(@NonNull RetrieveRulesetsRequest requestValues, UseCaseCallback<RetrieveRulesetsResponse> callback) {

        try {
            List<RulesetModel> list = rulesetRepository.findAll();

            callback.onComplete(RetrieveRulesetsResponse.ok(list));
        } catch (Exception ex) {
            Timber.e(ex);
            callback.onError(new BizError(ex));
        }
    }

    /**
     * the request embeds the {@link Context} only
     */
    public static final class RetrieveRulesetsRequest implements UseCase.RequestValues {

    }

    /**
     * when the request is successful, the response contains the list of items to display
     */
    @RequiredArgsConstructor
    public static final class RetrieveRulesetsResponse implements UseCase.ResponseValue {

        @Getter
        private final String code;

        @Getter
        private final List<RulesetModel> items;

        /**
         *
         * @return the count of elements
         */
        public int getCount() {
            return items == null ? 0 : items.size();
        }

        /**
         * gives an instantiate of {@link RetrieveRulesetsResponse}
         *
         * @param data the content to transfer
         * @return a valid {@link RetrieveRulesetsResponse}
         */
        public static RetrieveRulesetsResponse ok(Iterable<RulesetModel> data) {
            return new RetrieveRulesetsResponse("OK", newArrayList(data));
        }
    }


}
