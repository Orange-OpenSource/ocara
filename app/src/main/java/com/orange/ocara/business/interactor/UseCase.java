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

import com.orange.ocara.business.binding.ErrorBundle;

/**
 * Behaviour of interlocutors between the domain layer and the UI layer.
 * The implementations are clearly named, unit pieces of use-cases. They shall shall be suffixed with -Task.
 *
 * For instance : RetrieveRuleSetsTask, SavePreferredRulesetTask, CheckRulesetIsUpgradableTask, and so on.
 *
 * @param <REQUEST> information that is required so that the task can be performed shall implement this interface
 * @param <RESPONSE> information returned to the UI after a successful execution of the task shall implement this interface
 */
public interface UseCase<REQUEST extends UseCase.RequestValues, RESPONSE extends UseCase.ResponseValue> {

    /**
     * executes the task
     *
     * @param request information needed for the execution
     * @param callback function that handles the response
     */
    void executeUseCase(@NonNull REQUEST request, UseCaseCallback<RESPONSE> callback);

    /**
     * Data passed to a request.
     */
    interface RequestValues {
    }

    /**
     * Data received from a request.
     */
    interface ResponseValue {
    }

    /**
     * Behaviour of functions that handle the response after executing the task
     *
     * @param <RESPONSE>
     */
    interface UseCaseCallback<RESPONSE> {

        /**
         * shall be called when the response is successful
         *
         * @param response
         */
        void onComplete(RESPONSE response);

        /**
         * shall be called when the request failed
         */
        void onError(ErrorBundle errors);
    }
}