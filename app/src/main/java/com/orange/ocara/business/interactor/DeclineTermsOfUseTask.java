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

import static com.orange.ocara.business.interactor.DeclineTermsOfUseTask.DeclineTermsOfUseResponse.ok;
import static timber.log.Timber.d;
import static timber.log.Timber.e;

/**
 * {@link UseCase} dedicated to the refusal of Terms-Of-Use
 */
@RequiredArgsConstructor
public class DeclineTermsOfUseTask implements UseCase<DeclineTermsOfUseTask.DeclineTermsOfUseRequest, DeclineTermsOfUseTask.DeclineTermsOfUseResponse> {

    private final TermsRepository repository;

    @Override
    public void executeUseCase(@NonNull DeclineTermsOfUseRequest request, UseCaseCallback<DeclineTermsOfUseResponse> callback) {

        DeclineTermsOfUseResponse response;

        try {
            repository.markAsRefused();
            response = ok();

            d("TaskMessage=Terms-Of-Use refusal succeeded;TaskResponse=%s", response.getCode());
            callback.onComplete(ok());
        } catch (Exception ex) {
            e(ex, "TaskMessage=Terms-Of-Use refusal failed;TaskError=%s", ex.getMessage());
            callback.onError(new BizError("Terms-Of-Use refusal failed", ex));
        }
    }

    /**
     * the request dedicated to the {@link DeclineTermsOfUseTask}
     */
    public static final class DeclineTermsOfUseRequest implements UseCase.RequestValues {

    }

    /**
     * when the request is successful, the response contains the list of items to display
     */
    @RequiredArgsConstructor
    public static final class DeclineTermsOfUseResponse implements UseCase.ResponseValue {

        @Getter
        private final String code;

        public boolean isOk() {
            return "OK".equals(code);
        }

        /**
         * gives an instance of a positive {@link DeclineTermsOfUseResponse}
         *
         * @return a valid {@link DeclineTermsOfUseResponse}
         */
        static DeclineTermsOfUseResponse ok() {
            return new DeclineTermsOfUseResponse("OK");
        }
    }
}
