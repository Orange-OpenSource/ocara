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

package com.orange.ocara.ui.contract;

import com.orange.ocara.business.interactor.CheckTermsOfUseTask;
import com.orange.ocara.business.interactor.UseCase;

/**
 * Contract between the view and the presenter that manage the Terms-Of-Use of the application
 */
public interface TermsOfUseDisplayContract {

    /**
     * Behaviour of the view
     */
    interface TermsOfUseDisplayView {

        /**
         * displays the terms-of-use
         */
        void showTerms();

        /**
         * displays information about errors during the terms display
         *
         * @param message a reason
         */
        void showError(String message);
    }

    /**
     * Behaviour of the presenter
     */
    interface TermsOfUseCheckingUserActionsListener {

        /**
         * the user checks the terms
         */
        void checkTerms(UseCase.UseCaseCallback<CheckTermsOfUseTask.CheckTermsOfUseResponse> callback);
    }
}
