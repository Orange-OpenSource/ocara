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
import com.orange.ocara.business.model.RuleGroupModel;
import com.orange.ocara.business.model.RuleModel;
import com.orange.ocara.business.repository.QuestionRepository;
import com.orange.ocara.business.repository.RuleRepository;

import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;

import static com.orange.ocara.business.interactor.LoadRulesTask.LoadRulesResponse.noContent;
import static com.orange.ocara.business.interactor.LoadRulesTask.LoadRulesResponse.ok;
import static timber.log.Timber.e;

/**
 * {@link UseCase} dedicated to the retrieval of a ruleset
 */
public class LoadRulesTask implements UseCase<LoadRulesTask.LoadRulesRequest, LoadRulesTask.LoadRulesResponse> {

    private final QuestionRepository questionRepository;

    private final RuleRepository ruleRepository;

    public LoadRulesTask(QuestionRepository questionRepository, RuleRepository ruleRepository) {
        this.questionRepository = questionRepository;
        this.ruleRepository = ruleRepository;
    }

    @Override
    public void executeUseCase(@NonNull LoadRulesRequest request, UseCase.UseCaseCallback<LoadRulesResponse> callback) {

        try {
            List<RuleGroupModel> questions = questionRepository.findAll(request.getRulesetId(), request.getEquipmentId());
            List<RuleModel> rules = ruleRepository.findAll(request.getRulesetId(), request.getEquipmentId());
            if (questions != null && rules != null) {
                callback.onComplete(ok(questions, rules));
            } else {
                callback.onComplete(noContent());
            }

        } catch (Exception ex) {
            String message = "Rules retrieval failed";
            e(ex, "TaskMessage=" + message + ";TaskError=%s", ex.getMessage());
            callback.onError(new BizError(message, ex));
        }
    }

    /**
     * the request dedicated to the {@link LoadRulesTask}
     */
    @RequiredArgsConstructor
    public static final class LoadRulesRequest implements UseCase.RequestValues {

        private final long rulesetId;

        private final long equipmentId;

        public long getRulesetId() {
            return rulesetId;
        }

        public long getEquipmentId() {
            return equipmentId;
        }
    }

    /**
     * when the request is successful, the response contains the item
     */
    public static final class LoadRulesResponse implements UseCase.ResponseValue {

        private final TaskStatus status;

        private final List<RuleGroupModel> groups;

        private final List<RuleModel> rules;

        LoadRulesResponse(TaskStatus status, List<RuleGroupModel> groups, List<RuleModel> rules) {
            this.status = status;
            this.groups = groups;
            this.rules = rules;
        }

        public String getCode() {
            return status.name();
        }

        public TaskStatus getStatus() {
            return status;
        }

        public List<RuleGroupModel> getGroups() {
            return groups;
        }

        public List<RuleModel> getRules() {
            return rules;
        }

        static LoadRulesResponse ok(List<RuleGroupModel> questions, List<RuleModel> rules) {

            return new LoadRulesResponse(TaskStatus.SUCCESS, questions, rules);
        }

        static LoadRulesResponse noContent() {
            return new LoadRulesResponse(
                    TaskStatus.SUCCESS,
                    Collections.emptyList(),
                    Collections.emptyList());
        }
    }
}
