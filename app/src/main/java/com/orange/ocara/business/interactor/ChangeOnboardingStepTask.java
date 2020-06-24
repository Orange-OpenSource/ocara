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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import timber.log.Timber;

import static com.orange.ocara.business.interactor.ChangeOnboardingStepTask.ChangeOnboardingStepRequest;
import static com.orange.ocara.business.interactor.ChangeOnboardingStepTask.ChangeOnboardingStepResponse;

/** Task that validates the request new page in the tutorial */
public class ChangeOnboardingStepTask implements UseCase<ChangeOnboardingStepRequest, ChangeOnboardingStepResponse> {

    @Override
    public void executeUseCase(@NonNull ChangeOnboardingStepRequest request, UseCaseCallback<ChangeOnboardingStepResponse> callback) {

        if (request.isValid()) {
            callback.onComplete(ChangeOnboardingStepResponse.ok(
                    request.getInitialPosition(),
                    request.getNextPosition(),
                    request.getNextPosition() == request.getMaxThreshold()));
        } else {
            callback.onError(new BizError("Unexpected step at index " + request.getNextPosition()));
        }
    }

    /**
     * the request dedicated to the {@link ChangeOnboardingStepTask}
     */
    @RequiredArgsConstructor
    public static final class ChangeOnboardingStepRequest implements UseCase.RequestValues {

        /** the actual position */
        @Getter
        private final int initialPosition;

        /** the expected position*/
        @Getter
        private final int nextPosition;

        /** the max index that is allowed */
        @Getter
        private final int maxThreshold;

        public boolean isValid() {
            Timber.d("InitialValue=%d;ActualValue=%d;ExpectedMaxValue=%d", initialPosition, nextPosition, maxThreshold);
            return nextPosition <= maxThreshold;
        }
    }

    /**
     * when the request is successful, the response contains the index of the new item
     */
    @RequiredArgsConstructor
    public static final class ChangeOnboardingStepResponse implements UseCase.ResponseValue {

        @Getter
        private final int previousPosition;

        @Getter
        private final int targetPosition;

        @Getter
        private final boolean lastStep;

        public static ChangeOnboardingStepResponse ok(int prev, int position, boolean lastStep) {
            return new ChangeOnboardingStepResponse(prev, position, lastStep);
        }
    }
}
