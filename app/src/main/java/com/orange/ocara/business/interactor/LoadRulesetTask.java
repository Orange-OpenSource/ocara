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

import com.orange.ocara.business.binding.BizError;
import com.orange.ocara.business.common.TaskStatus;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.model.VersionableModel;
import com.orange.ocara.business.repository.RulesetRepository;

import lombok.RequiredArgsConstructor;

import static com.orange.ocara.business.interactor.LoadRulesetTask.LoadRulesetRequest;
import static com.orange.ocara.business.interactor.LoadRulesetTask.LoadRulesetResponse;
import static com.orange.ocara.business.interactor.LoadRulesetTask.LoadRulesetResponse.ok;

/**
 * {@link UseCase} dedicated to the retrieval of a ruleset
 */
@RequiredArgsConstructor
public class LoadRulesetTask implements UseCase<LoadRulesetRequest, LoadRulesetResponse> {

    private final RulesetRepository rulesetRepository;

    @Override
    public void executeUseCase(@NonNull LoadRulesetRequest request, UseCaseCallback<LoadRulesetResponse> callback) {

        RulesetModel data = rulesetRepository.findOne(request.getReference(), request.getVersion());
        if (data != null) {
            callback.onComplete(ok(data));
        } else {
            callback.onError(new BizError("No ruleset found"));
        }
    }

    /**
     * the request dedicated to the {@link LoadRulesetTask}
     */
    @RequiredArgsConstructor
    public static final class LoadRulesetRequest implements UseCase.RequestValues {

        private final VersionableModel ruleset;

        public String getReference() {
            return ruleset.getReference();
        }

        public Integer getVersion() {
            return Integer.valueOf(ruleset.getVersion());
        }
    }

    /**
     * when the request is successful, the response contains the item
     */
    public static final class LoadRulesetResponse implements UseCase.ResponseValue {

        private final TaskStatus status;

        private final RulesetModel content;

        LoadRulesetResponse(TaskStatus status, RulesetModel content) {
            this.status = status;
            this.content = content;
        }

        public RulesetModel getContent() {
            return content;
        }

        static LoadRulesetResponse ok(RulesetModel data) {
            return new LoadRulesetResponse(TaskStatus.SUCCESS, data);
        }
    }
}
