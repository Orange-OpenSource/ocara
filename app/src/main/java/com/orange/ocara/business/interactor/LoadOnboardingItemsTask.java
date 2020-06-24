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
import com.orange.ocara.business.model.OnboardingItemModel;
import com.orange.ocara.business.repository.OnboardingRepository;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.orange.ocara.business.interactor.LoadOnboardingItemsTask.LoadOnboardingItemsRequest;
import static com.orange.ocara.business.interactor.LoadOnboardingItemsTask.LoadOnboardingItemsResponse;
import static timber.log.Timber.d;
import static timber.log.Timber.e;

/**
 * {@link UseCase} dedicated to the retrieval of the onboarding status and data
 */
@RequiredArgsConstructor
public class LoadOnboardingItemsTask implements UseCase<LoadOnboardingItemsRequest, LoadOnboardingItemsResponse> {

    private final OnboardingRepository dataRepository;

    @Override
    public void executeUseCase(@NonNull LoadOnboardingItemsRequest request, UseCaseCallback<LoadOnboardingItemsTask.LoadOnboardingItemsResponse> callback) {

        LoadOnboardingItemsResponse response;

        try {
            List<OnboardingItemModel> items = dataRepository.findAll();
            response = LoadOnboardingItemsResponse.ok(items);
            d("TaskMessage=Onboarding items loading succeeded;TaskResponse=%s;ContentSize=%d", response.getCode(), items.size());
            callback.onComplete(response);
        } catch (Exception ex) {
            String message = "Onboarding items loading failed";
            e(ex, "TaskMessage=" + message + ";TaskError=%s", ex.getMessage());
            callback.onError(new BizError(message, ex));
        }
    }

    /**
     * the request dedicated to the {@link LoadOnboardingItemsTask}
     */
    public static final class LoadOnboardingItemsRequest implements UseCase.RequestValues {

    }

    /**
     * when the request is successful, the response contains the list of items to display
     */
    @RequiredArgsConstructor
    public static final class LoadOnboardingItemsResponse implements UseCase.ResponseValue {

        @Getter
        private final String code;

        @Getter
        private final List<OnboardingItemModel> items;

        public int getSize() {
            return items.size();
        }

        /**
         * @param data some content
         * @return an instance of a {@link LoadOnboardingItemsResponse}
         */
        public static LoadOnboardingItemsResponse ok(List<OnboardingItemModel> data) {
            return new LoadOnboardingItemsResponse("OK", data);
        }
    }
}
