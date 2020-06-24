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
import com.orange.ocara.business.repository.InitializableRepository;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static timber.log.Timber.e;

public class InitTask implements UseCase<InitTask.InitRequest, InitTask.InitResponse> {

    private final List<InitializableRepository> repositories;

    /**
     * instantiates
     *
     * @param repositories some repositories that require initialization
     */
    public InitTask(InitializableRepository... repositories) {
        this.repositories = Arrays.asList(repositories);
    }

    @Override
    public void executeUseCase(@NonNull InitRequest request, UseCaseCallback<InitResponse> callback) {

        try {
            for (InitializableRepository repository : repositories) {
                repository.init();
            }
            callback.onComplete(InitResponse.ok());
        } catch (Exception ex) {
            String message = "initialization failed";
            e(ex, "TaskMessage=" + message + ";TaskError=%s", ex.getMessage());
            callback.onError(new BizError(message, ex));
        }
    }

    /**
     * the request dedicated to the {@link InitTask}
     */
    @Getter
    @RequiredArgsConstructor
    public static final class InitRequest implements UseCase.RequestValues {
    }

    /**
     * the response expected when executing a {@link InitTask}
     */
    @RequiredArgsConstructor
    public static final class InitResponse implements UseCase.ResponseValue {

        static InitResponse ok() {
            return new InitResponse();
        }
    }
}
