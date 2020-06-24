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
import com.orange.ocara.business.binding.BizException;
import com.orange.ocara.business.repository.TermsRepository;

import java.net.URL;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.orange.ocara.business.interactor.LoadTermsOfUseTask.LoadTermsOfUseResponse.ok;
import static timber.log.Timber.d;
import static timber.log.Timber.e;

/**
 * {@link UseCase} dedicated to the acceptance of Terms-Of-Use
 */
@RequiredArgsConstructor
public class LoadTermsOfUseTask implements UseCase<LoadTermsOfUseTask.LoadTermsOfUseRequest, LoadTermsOfUseTask.LoadTermsOfUseResponse> {

    private final TermsRepository repository;

    @Override
    public void executeUseCase(@NonNull LoadTermsOfUseRequest request, UseCaseCallback<LoadTermsOfUseResponse> callback) {

        LoadTermsOfUseResponse response;

        try {
            String rawData = repository.find();
            if (rawData != null && !rawData.isEmpty()) {
                response = ok(rawData);
            } else {
                throw new BizException("No valid terms could be retrieved");
            }

            d("TaskMessage=Terms-Of-Use loading succeeded;TaskResponse=%s", response.getCode());
            callback.onComplete(response);
        } catch (Exception ex) {
            e(ex, "TaskMessage=Terms-Of-Use loading failed;TaskError=%s", ex.getMessage());
            callback.onError(new BizError("Terms-Of-Use loading failed", ex));
        }
    }

    /**
     * the request dedicated to the {@link LoadTermsOfUseTask}
     */
    public static final class LoadTermsOfUseRequest implements UseCase.RequestValues {

    }

    /**
     * when the request is successful, the response contains the list of items to display
     */
    @RequiredArgsConstructor
    public static final class LoadTermsOfUseResponse implements UseCase.ResponseValue {

        @Getter
        private final String code;

        @Getter
        private final String rawData;

        @Getter
        private final URL url;

        public boolean isRaw() {
            return "OK_RAW".equals(code);
        }

        public boolean isRemote() {
            return "OK_REMOTE".equals(code);
        }

        /**
         * gives an instance of a positive {@link LoadTermsOfUseResponse}
         * @param data some content
         * @return a valid {@link LoadTermsOfUseResponse}
         */
        public static LoadTermsOfUseResponse ok(String data) {
            return new LoadTermsOfUseResponse("OK_RAW", data, null);
        }

        /**
         * gives an instance of a positive {@link LoadTermsOfUseResponse}
         *
         * @param data a {@link URL}
         * @return a valid {@link LoadTermsOfUseResponse}
         */
        public static LoadTermsOfUseResponse ok(URL data) {
            return new LoadTermsOfUseResponse("OK_REMOTE", null, data);
        }
    }
}
