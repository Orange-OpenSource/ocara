/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2020 Orange
 * SPDX-License-Identifier: MPL-2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0, 
 * the text of which is available at http://mozilla.org/MPL/2.0/ or 
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.business.interactor;

import androidx.annotation.NonNull;

import com.orange.ocara.business.common.Result;
import com.orange.ocara.business.common.TaskStatus;
import com.orange.ocara.business.repository.HealthRepository;

import lombok.RequiredArgsConstructor;

import static timber.log.Timber.d;
import static timber.log.Timber.e;

/**
 * {@link UseCase} dedicated to the acceptance of Terms-Of-Use
 */
@RequiredArgsConstructor
public class CheckRemoteStatusTask implements UseCase<CheckRemoteStatusTask.CheckRemoteStatusRequest, CheckRemoteStatusTask.CheckRemoteStatusResponse> {

    private final HealthRepository repository;

    @Override
    public void executeUseCase(@NonNull CheckRemoteStatusRequest request, UseCaseCallback<CheckRemoteStatusTask.CheckRemoteStatusResponse> callback) {

        CheckRemoteStatusResponse response;

        try {
            repository.ping();
            response = CheckRemoteStatusResponse.success();

            d("TaskMessage=App is online;TaskResponse=%s", response.getCode());
        } catch (Exception ex) {
            e(ex, "TaskMessage=App is offline;TaskError=%s", ex.getMessage());
            response = CheckRemoteStatusResponse.error("offline");
        }
        callback.onComplete(response);
    }

    /**
     * the request dedicated to the {@link CheckRemoteStatusTask}
     */
    public static final class CheckRemoteStatusRequest implements UseCase.RequestValues {

    }

    /**
     * when the request is successful, the response contains the list of items to display
     */
    public static final class CheckRemoteStatusResponse extends Result {

        CheckRemoteStatusResponse(TaskStatus state, Object data, String message) {
            super(state, data, message);
        }

        public boolean isSuccess() {
            return state == TaskStatus.SUCCESS;
        }

        public static CheckRemoteStatusResponse success() {
            return new CheckRemoteStatusResponse(TaskStatus.SUCCESS, null, null);
        }

        public static CheckRemoteStatusResponse error(String message) {
            return new CheckRemoteStatusResponse(TaskStatus.ERROR, null, message);
        }

        public static CheckRemoteStatusResponse loading() {
            return new CheckRemoteStatusResponse(TaskStatus.IN_PROGRESS, null, null);
        }
    }
}
