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

import com.orange.ocara.business.binding.ErrorBundle;

import lombok.RequiredArgsConstructor;

/**
 * Runs {@link UseCase}s using a {@link UseCaseScheduler}.
 */
@RequiredArgsConstructor
public class UseCaseHandler {

    private final UseCaseScheduler mUseCaseScheduler;

    /**
     *
     * @param useCase a task to execute
     * @param values request data
     * @param callback function to execute
     * @param <T> type of the request
     * @param <R> type of the response
     */
    public <T extends UseCase.RequestValues, R extends UseCase.ResponseValue> void execute(
            final UseCase<T, R> useCase, T values, UseCase.UseCaseCallback<R> callback) {

        UseCaseRunner runner = new UseCaseRunner(useCase, values, new UiCallbackWrapper(callback, this));

        mUseCaseScheduler.execute(runner);
    }

    private <V extends UseCase.ResponseValue> void notifyResponse(final V response,
                                                                  final UseCase.UseCaseCallback<V> useCaseCallback) {
        mUseCaseScheduler.notifyResponse(response, useCaseCallback);
    }

    private <V extends UseCase.ResponseValue> void notifyError(
            final UseCase.UseCaseCallback<V> useCaseCallback) {

        mUseCaseScheduler.onError(useCaseCallback);
    }

    @RequiredArgsConstructor
    private static final class UseCaseRunner<T extends UseCase.RequestValues, R extends UseCase.ResponseValue>
            implements Runnable {

        private final UseCase<T, R> useCase;

        private final T mRequestValues;

        private final UseCase.UseCaseCallback<R> mUseCaseCallback;

        @Override
        public void run() {

            useCase.executeUseCase(mRequestValues, mUseCaseCallback);
        }
    }

    private static final class UiCallbackWrapper<V extends UseCase.ResponseValue>
            implements UseCase.UseCaseCallback<V> {

        private final UseCase.UseCaseCallback<V> mCallback;
        private final UseCaseHandler mUseCaseHandler;

        UiCallbackWrapper(UseCase.UseCaseCallback<V> callback,
                          UseCaseHandler useCaseHandler) {
            mCallback = callback;
            mUseCaseHandler = useCaseHandler;
        }

        @Override
        public void onComplete(V response) {
            mUseCaseHandler.notifyResponse(response, mCallback);
        }

        @Override
        public void onError(ErrorBundle errors) {
            mUseCaseHandler.notifyError(mCallback);
        }
    }

}
