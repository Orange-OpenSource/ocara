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
import com.orange.ocara.business.common.Result;
import com.orange.ocara.business.common.TaskStatus;
import com.orange.ocara.business.repository.TermsRepository;

import lombok.RequiredArgsConstructor;

import static com.orange.ocara.business.interactor.AcceptTermsOfUseTask.AcceptTermsOfUseResponse.success;
import static timber.log.Timber.d;
import static timber.log.Timber.e;

/**
 * {@link UseCase} dedicated to the acceptance of Terms-Of-Use
 */
@RequiredArgsConstructor
public class AcceptTermsOfUseTask implements UseCase<AcceptTermsOfUseTask.AcceptTermsOfUseRequest, AcceptTermsOfUseTask.AcceptTermsOfUseResponse> {

    private final TermsRepository repository;

    @Override
    public void executeUseCase(@NonNull AcceptTermsOfUseRequest request, UseCaseCallback<AcceptTermsOfUseResponse> callback) {

        AcceptTermsOfUseResponse response;

        try {
            repository.markAsAccepted();
            response = success();

            d("TaskMessage=Terms-Of-Use acceptance succeeded;TaskResponse=%s", response.getCode());
            callback.onComplete(response);
        } catch (Exception ex) {
            e(ex, "TaskMessage=Terms-Of-Use acceptance failed;TaskError=%s", ex.getMessage());
            callback.onError(new BizError("Terms-Of-Use acceptance failed", ex));
        }
    }

    /**
     * the request dedicated to the {@link AcceptTermsOfUseTask}
     */
    public static final class AcceptTermsOfUseRequest implements UseCase.RequestValues {

    }

    /**
     * when the request is successful, the response contains the list of items to display
     */
    public static final class AcceptTermsOfUseResponse extends Result {

        public AcceptTermsOfUseResponse(TaskStatus state, Object data, String message) {
            super(state, data, message);
        }

        public static AcceptTermsOfUseResponse success() {
            return new AcceptTermsOfUseResponse(TaskStatus.SUCCESS, null, null);
        }

        public static AcceptTermsOfUseResponse error(String message) {
            return new AcceptTermsOfUseResponse(TaskStatus.ERROR, null, message);
        }

        public static AcceptTermsOfUseResponse loading() {
            return new AcceptTermsOfUseResponse(TaskStatus.IN_PROGRESS, null, null);
        }
    }
}
