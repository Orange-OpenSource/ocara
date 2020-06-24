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
import com.orange.ocara.business.repository.TermsRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.orange.ocara.business.interactor.CheckTermsStatusTask.CheckTermsStatusResponse.accepted;
import static com.orange.ocara.business.interactor.CheckTermsStatusTask.CheckTermsStatusResponse.notAccepted;
import static timber.log.Timber.d;
import static timber.log.Timber.e;

/**
 * {@link UseCase} dedicated to the acceptance of Terms-Of-Use
 */
@RequiredArgsConstructor
public class CheckTermsStatusTask implements UseCase<CheckTermsStatusTask.CheckTermsStatusRequest, CheckTermsStatusTask.CheckTermsStatusResponse> {

    private final TermsRepository repository;

    @Override
    public void executeUseCase(@NonNull CheckTermsStatusRequest request, UseCase.UseCaseCallback<CheckTermsStatusTask.CheckTermsStatusResponse> callback) {

        CheckTermsStatusResponse response;

        try {
            if(repository.checkTermsAreAccepted()) {
                response = accepted();
            } else {
                response = notAccepted();
            }

            d("TaskMessage=Terms-Of-Use loading succeeded;TaskResponse=%s", response.getCode());
            callback.onComplete(response);
        } catch (Exception ex) {
            e(ex, "TaskMessage=Terms-Of-Use loading failed;TaskError=%s", ex.getMessage());
            callback.onError(new BizError("Terms-Of-Use loading failed", ex));
        }
    }

    /**
     * the request dedicated to the {@link CheckTermsStatusTask}
     */
    public static final class CheckTermsStatusRequest implements UseCase.RequestValues {

    }

    /**
     * when the request is successful, the response contains the list of items to display
     */
    @RequiredArgsConstructor
    public static final class CheckTermsStatusResponse implements UseCase.ResponseValue {

        @Getter
        private final String code;

        public boolean isAccepted() {
            return "ACCEPTED".equals(code);
        }

        /**
         *
         * @return a {@link CheckTermsStatusResponse}
         */
        public static CheckTermsStatusResponse accepted() {
            return new CheckTermsStatusResponse("ACCEPTED");
        }


        /**
         *
         * @return a {@link CheckTermsStatusResponse}
         */
        public static CheckTermsStatusResponse notAccepted() {
            return new CheckTermsStatusResponse("NOT_ACCEPTED");
        }

    }
}
