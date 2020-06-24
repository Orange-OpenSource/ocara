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
import com.orange.ocara.business.model.ExplanationModel;
import com.orange.ocara.business.repository.ExplanationRepository;
import com.orange.ocara.data.net.model.RuleEntity;

import java.util.List;

import static timber.log.Timber.e;

/** a {@link UseCase} dedicated to the retrieval of explanations (aka illustrations) that concern a given {@link RuleEntity} */
public class LoadRuleExplanationsTask
        implements UseCase<LoadRuleExplanationsTask.LoadRuleExplanationsRequest, LoadRuleExplanationsTask.LoadRuleExplanationsResponse> {

    private final ExplanationRepository explanationRepository;

    public LoadRuleExplanationsTask(ExplanationRepository explanationRepository) {
        this.explanationRepository = explanationRepository;
    }

    @Override
    public void executeUseCase(@NonNull LoadRuleExplanationsRequest request, UseCaseCallback<LoadRuleExplanationsResponse> callback) {

        try {
            List<ExplanationModel> data = explanationRepository.findAllByRuleId(request.getRuleId());

            callback.onComplete(LoadRuleExplanationsResponse.ok(data));
        } catch (Exception ex) {
            String message = "illustrations retrieval failed";
            e(ex, "TaskMessage=" + message + ";TaskError=%s", ex.getMessage());
            callback.onError(new BizError(message, ex));
        }
    }

    /** an implementation of {@link UseCase.RequestValues} for {@link LoadRuleExplanationsTask} */
    public static class LoadRuleExplanationsRequest implements UseCase.RequestValues {

        private final long ruleId;

        public LoadRuleExplanationsRequest(long ruleId) {
            this.ruleId = ruleId;
        }

        public long getRuleId() {
            return ruleId;
        }
    }

    /** an implementation of {@link UseCase.ResponseValue} for {@link LoadRuleExplanationsTask} */
    public static final class LoadRuleExplanationsResponse implements UseCase.ResponseValue {

        private final TaskStatus status;

        private final List<ExplanationModel> data;

        LoadRuleExplanationsResponse(TaskStatus status, List<ExplanationModel> data) {
            this.status = status;
            this.data = data;
        }

        public String getCode() {
            return status.name();
        }

        public List<ExplanationModel> getData() {
            return data;
        }

        public boolean isEmpty() {
            return data.isEmpty();
        }

        static LoadRuleExplanationsResponse ok(List<ExplanationModel> data) {
            return new LoadRuleExplanationsResponse(TaskStatus.SUCCESS, data);
        }
    }
}
