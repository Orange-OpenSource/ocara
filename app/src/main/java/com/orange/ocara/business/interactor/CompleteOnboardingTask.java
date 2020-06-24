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

import static com.orange.ocara.business.interactor.CompleteOnboardingTask.CompleteOnboardingResponse.ok;
import static timber.log.Timber.d;
import static timber.log.Timber.e;

/**
 * {@link UseCase} dedicated to the acceptance of Terms-Of-Use
 */
@RequiredArgsConstructor
public class CompleteOnboardingTask implements UseCase<CompleteOnboardingTask.CompleteOnboardingRequest, CompleteOnboardingTask.CompleteOnboardingResponse> {

    private final OnboardingRepository repository;

    @Override
    public void executeUseCase(@NonNull CompleteOnboardingRequest request, UseCaseCallback<CompleteOnboardingResponse> callback) {

        CompleteOnboardingResponse response;

        try {
            repository.saveCompletedOnboarding();
            response = ok();

            d("TaskMessage=Onboarding completion succeeded;TaskResponse=%s", response.getCode());
            callback.onComplete(response);
        } catch (Exception ex) {
            String message = "Onboarding completion failed";
            e(ex, "TaskMessage=" + message + ";TaskError=%s", ex.getMessage());
            callback.onError(new BizError(message, ex));
        }
    }

    /**
     * the request dedicated to the {@link CompleteOnboardingTask}
     */
    public static final class CompleteOnboardingRequest implements UseCase.RequestValues {

    }

    /**
     * when the request is successful, the response contains the list of items to display
     */
    @RequiredArgsConstructor
    public static final class CompleteOnboardingResponse implements UseCase.ResponseValue {

        @Getter
        private final String code;

        public boolean isOk() {
            return "OK".equals(code);
        }

        /**
         * gives an instance of a positive {@link CompleteOnboardingResponse}
         *
         * @return a valid {@link CompleteOnboardingResponse}
         */
        public static CompleteOnboardingResponse ok() {
            return new CompleteOnboardingResponse("OK");
        }
    }
}
