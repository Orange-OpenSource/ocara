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

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.orange.ocara.business.interactor.SkipOnboardingTask.SkipOnboardingRequest;
import static com.orange.ocara.business.interactor.SkipOnboardingTask.SkipOnboardingResponse;
import static com.orange.ocara.business.interactor.SkipOnboardingTask.SkipOnboardingResponse.ok;
import static timber.log.Timber.d;

/**
 * {@link UseCase} dedicated to the acceptance of Terms-Of-Use
 */
@RequiredArgsConstructor
public class SkipOnboardingTask implements UseCase<SkipOnboardingRequest, SkipOnboardingResponse> {

    @Override
    public void executeUseCase(@NonNull SkipOnboardingRequest request, UseCaseCallback<SkipOnboardingResponse> callback) {

        SkipOnboardingResponse response;

        response = ok();

        d("TaskMessage=Onboarding completion succeeded;TaskResponse=%s", response.getCode());
        callback.onComplete(response);
    }

    /**
     * the request dedicated to the {@link SkipOnboardingTask}
     */
    public static final class SkipOnboardingRequest implements UseCase.RequestValues {

    }

    /**
     * when the request is successful, the response contains the list of items to display
     */
    @RequiredArgsConstructor
    public static final class SkipOnboardingResponse implements UseCase.ResponseValue {

        @Getter
        private final String code;

        public boolean isOk() {
            return "OK".equals(code);
        }

        /**
         * gives an instance of a positive {@link SkipOnboardingResponse}
         *
         * @return a valid {@link SkipOnboardingResponse}
         */
        public static SkipOnboardingResponse ok() {
            return new SkipOnboardingResponse("OK");
        }
    }
}
