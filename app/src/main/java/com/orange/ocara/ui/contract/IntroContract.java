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

import com.orange.ocara.business.interactor.CheckRemoteStatusTask.CheckRemoteStatusResponse;
import com.orange.ocara.business.interactor.CheckTermsStatusTask.CheckTermsStatusResponse;
import com.orange.ocara.business.interactor.InitTask;
import com.orange.ocara.business.interactor.UseCase.UseCaseCallback;

import static com.orange.ocara.business.interactor.CheckOnboardingStatusTask.CheckOnboardingStatusResponse;

/**
 * contract between a view and a presenter that deal with the onboarding of the app
 */
public interface IntroContract {

    /** description of the view */
    interface IntroView {

        void attemptNextDialog(int code);

        void navigateToHome();

        void start();

        void showTerms();

        void showTutorial();

        void showError(String message);

    }

    /** description of the presenter */
    interface IntroUserActionsListener {

        void checkInit(UseCaseCallback<InitTask.InitResponse> callback);

        void checkTerms(UseCaseCallback<CheckTermsStatusResponse> callback);

        void checkTutorial(UseCaseCallback<CheckOnboardingStatusResponse> callback);

        void checkRemote(UseCaseCallback<CheckRemoteStatusResponse> callback);
    }
}
