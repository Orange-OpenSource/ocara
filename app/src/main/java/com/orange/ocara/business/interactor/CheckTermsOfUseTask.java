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

import static com.orange.ocara.business.interactor.CheckTermsOfUseTask.CheckTermsOfUseResponse.accepted;
import static com.orange.ocara.business.interactor.CheckTermsOfUseTask.CheckTermsOfUseResponse.declined;
import static com.orange.ocara.business.interactor.CheckTermsOfUseTask.CheckTermsOfUseResponse.notDefined;
import static timber.log.Timber.d;
import static timber.log.Timber.e;

/**
 * {@link UseCase} dedicated to the acceptance of Terms-Of-Use
 */
@RequiredArgsConstructor
public class CheckTermsOfUseTask implements UseCase<CheckTermsOfUseTask.CheckTermsOfUseRequest, CheckTermsOfUseTask.CheckTermsOfUseResponse> {

    private final TermsRepository repository;

    @Override
    public void executeUseCase(@NonNull CheckTermsOfUseRequest request, UseCaseCallback<CheckTermsOfUseResponse> callback) {

        CheckTermsOfUseResponse response;

        try {
            if (!repository.checkTermsAreDefined()) {
                response = notDefined();
            } else if(repository.checkTermsAreAccepted()) {
                response = accepted();
            } else {
                response = declined();
            }

            d("TaskMessage=Terms-Of-Use acceptance succeeded;TaskResponse=%s", response.getCode());
            callback.onComplete(response);
        } catch (Exception ex) {
            e(ex, "TaskMessage=Terms-Of-Use acceptance failed;TaskError=%s", ex.getMessage());
            callback.onError(new BizError("Terms-Of-Use acceptance failed", ex));
        }
    }

    /**
     * the request dedicated to the {@link CheckTermsOfUseTask}
     */
    public static final class CheckTermsOfUseRequest implements UseCase.RequestValues {

    }

    /**
     * when the request is successful, the response contains the list of items to display
     */
    @RequiredArgsConstructor
    public static final class CheckTermsOfUseResponse implements UseCase.ResponseValue {

        private static final String TERMS_ACCEPTED = "OK";

        private static final String TERMS_DECLINED = "KO";

        private static final String TERMS_NOT_DEFINED = "NOT_DEFINED";

        @Getter
        private final String code;

        public boolean isAccepted() {
            return TERMS_ACCEPTED.equals(code);
        }

        public boolean isDeclined() {
            return TERMS_DECLINED.equals(code);
        }

        public boolean isDefined() {
            return !TERMS_NOT_DEFINED.equals(code);
        }

        /**
         * gives an instance of a positive {@link CheckTermsOfUseResponse}
         *
         * @return a valid {@link CheckTermsOfUseResponse}
         */
        public static CheckTermsOfUseResponse accepted() {
            return new CheckTermsOfUseResponse(TERMS_ACCEPTED);
        }

        /**
         * gives an instance of a negative {@link CheckTermsOfUseResponse}
         *
         * @return a valid {@link CheckTermsOfUseResponse}
         */
        public static CheckTermsOfUseResponse declined() {
            return new CheckTermsOfUseResponse(TERMS_DECLINED);
        }

        /**
         * gives an instance of a negative {@link CheckTermsOfUseResponse}
         *
         * @return a valid {@link CheckTermsOfUseResponse}
         */
        public static CheckTermsOfUseResponse notDefined() {
            return new CheckTermsOfUseResponse(TERMS_NOT_DEFINED);
        }
    }
}
