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

package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.interactor.CheckOnboardingStatusTask;
import com.orange.ocara.business.interactor.CheckRemoteStatusTask;
import com.orange.ocara.business.interactor.CheckTermsStatusTask;
import com.orange.ocara.business.interactor.CheckTermsStatusTask.CheckTermsStatusRequest;
import com.orange.ocara.business.interactor.CheckTermsStatusTask.CheckTermsStatusResponse;
import com.orange.ocara.business.interactor.InitTask;
import com.orange.ocara.business.interactor.InitTask.InitRequest;
import com.orange.ocara.business.interactor.UseCase;
import com.orange.ocara.business.interactor.UseCase.UseCaseCallback;
import com.orange.ocara.business.interactor.UseCaseHandler;

import static com.orange.ocara.business.interactor.CheckOnboardingStatusTask.CheckOnboardingStatusRequest;
import static com.orange.ocara.business.interactor.CheckOnboardingStatusTask.CheckOnboardingStatusResponse;
import static com.orange.ocara.business.interactor.CheckRemoteStatusTask.CheckRemoteStatusRequest;
import static com.orange.ocara.business.interactor.CheckRemoteStatusTask.CheckRemoteStatusResponse;
import static com.orange.ocara.business.interactor.InitTask.InitResponse;
import static com.orange.ocara.ui.contract.IntroContract.IntroUserActionsListener;

/**
 * a presenter that implements {@link IntroUserActionsListener}
 */
public class IntroPresenter implements IntroUserActionsListener {

    private final UseCaseHandler useCaseHandler;

    /** see {@link InitTask} */
    private final UseCase<InitRequest, InitResponse> initTask;

    /** see {@link CheckTermsStatusTask} */
    private final UseCase<CheckTermsStatusRequest, CheckTermsStatusResponse> checkTermsStatusTask;

    /** see {@link CheckOnboardingStatusTask}*/
    private final UseCase<CheckOnboardingStatusRequest, CheckOnboardingStatusResponse> checkOnboardingStatusTask;

    /** see {@link CheckRemoteStatusTask} */
    private final UseCase<CheckRemoteStatusRequest, CheckRemoteStatusResponse> checkRemoteStatusTask;

    public IntroPresenter(UseCaseHandler useCaseHandler,
                          UseCase<InitRequest, InitResponse> initTask,
                          UseCase<CheckTermsStatusRequest, CheckTermsStatusResponse> checkTermsStatusTask,
                          UseCase<CheckOnboardingStatusRequest, CheckOnboardingStatusResponse> checkOnboardingStatusTask,
                          UseCase<CheckRemoteStatusRequest, CheckRemoteStatusResponse> checkRemoteStatusTask) {
        this.useCaseHandler = useCaseHandler;
        this.initTask = initTask;
        this.checkTermsStatusTask = checkTermsStatusTask;
        this.checkOnboardingStatusTask = checkOnboardingStatusTask;
        this.checkRemoteStatusTask = checkRemoteStatusTask;
    }

    @Override
    public void checkInit(UseCaseCallback<InitResponse> callback) {
        useCaseHandler.execute(initTask, new InitRequest(), callback);
    }

    @Override
    public void checkTerms(UseCaseCallback<CheckTermsStatusResponse> callback) {

        useCaseHandler.execute(checkTermsStatusTask, new CheckTermsStatusRequest(), callback);
    }

    @Override
    public void checkTutorial(UseCaseCallback<CheckOnboardingStatusResponse> callback) {

        useCaseHandler.execute(checkOnboardingStatusTask, new CheckOnboardingStatusRequest(), callback);
    }

    @Override
    public void checkRemote(UseCaseCallback<CheckRemoteStatusResponse> callback) {
        useCaseHandler.execute(checkRemoteStatusTask, new CheckRemoteStatusRequest(), callback);
    }
}
