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

import com.orange.ocara.business.interactor.UseCase;
import com.orange.ocara.business.interactor.UseCase.UseCaseCallback;

import lombok.RequiredArgsConstructor;

import static com.orange.ocara.business.interactor.CheckTermsOfUseTask.CheckTermsOfUseRequest;
import static com.orange.ocara.business.interactor.CheckTermsOfUseTask.CheckTermsOfUseResponse;
import static com.orange.ocara.ui.contract.TermsOfUseDisplayContract.TermsOfUseCheckingUserActionsListener;
import static com.orange.ocara.ui.contract.TermsOfUseDisplayContract.TermsOfUseDisplayView;
import static timber.log.Timber.d;

/**
 * Mediator between a {@link TermsOfUseDisplayView} and the business layer
 */
@RequiredArgsConstructor
public class TermsOfUseCheckingPresenter implements TermsOfUseCheckingUserActionsListener {

    private final TermsOfUseDisplayView view;

    private final UseCase<CheckTermsOfUseRequest, CheckTermsOfUseResponse> checkingTask;

    @Override
    public void checkTerms(UseCaseCallback<CheckTermsOfUseResponse> callback) {

        d( "Message=Checking terms");
        checkingTask.executeUseCase(new CheckTermsOfUseRequest(), callback);
    }
}
