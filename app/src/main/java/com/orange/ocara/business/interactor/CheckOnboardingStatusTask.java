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
import com.orange.ocara.business.repository.OnboardingRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.orange.ocara.business.interactor.CheckOnboardingStatusTask.CheckOnboardingStatusResponse.completed;
import static com.orange.ocara.business.interactor.CheckOnboardingStatusTask.CheckOnboardingStatusResponse.ongoing;
import static timber.log.Timber.d;
import static timber.log.Timber.e;

/**
 * {@link UseCase} dedicated to the retrieval of the onboarding status
 */
@RequiredArgsConstructor
public class CheckOnboardingStatusTask implements UseCase<CheckOnboardingStatusTask.CheckOnboardingStatusRequest, CheckOnboardingStatusTask.CheckOnboardingStatusResponse> {

    private final OnboardingRepository statusRepository;

    @Override
    public void executeUseCase(@NonNull CheckOnboardingStatusRequest request, UseCaseCallback<CheckOnboardingStatusResponse> callback) {

        CheckOnboardingStatusResponse response;

        try {
            if (statusRepository.checkOnboardingIsCompleted()) {
                response = completed();
            } else {
                response = ongoing();
            }
            d("TaskMessage=Onboarding status loading succeeded;TaskResponse=%s", response.getCode());
            callback.onComplete(response);
        } catch (Exception ex) {
            String message = "Onboarding status loading failed";
            e(ex, "TaskMessage=" + message + ";TaskError=%s", ex.getMessage());
            callback.onError(new BizError(message, ex));
        }
    }

    /**
     * the request dedicated to the {@link CheckOnboardingStatusTask}
     */
    public static final class CheckOnboardingStatusRequest implements UseCase.RequestValues {

    }

    /**
     * when the request is successful, the response contains the list of items to display
     */
    @RequiredArgsConstructor
    public static final class CheckOnboardingStatusResponse implements UseCase.ResponseValue {

        private static final String ONBOARDING_COMPLETED = "COMPLETED";

        private static final String ONBOARDING_ONGOING = "ONGOING";

        @Getter
        private final String code;

        public boolean isCompleted() {
            return ONBOARDING_COMPLETED.equals(code);
        }

        public boolean isOngoing() {
            return ONBOARDING_ONGOING.equals(code);
        }

        /**
         * @return an instance of a {@link CheckOnboardingStatusResponse}
         */
        public static CheckOnboardingStatusResponse completed() {
            return new CheckOnboardingStatusResponse(ONBOARDING_COMPLETED);
        }

        /**
         * @return an instance of a {@link CheckOnboardingStatusResponse}
         */
        public static CheckOnboardingStatusResponse ongoing() {
            return new CheckOnboardingStatusResponse(ONBOARDING_ONGOING);
        }
    }
}
