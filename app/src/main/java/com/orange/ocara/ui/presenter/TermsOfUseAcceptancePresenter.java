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

import com.orange.ocara.business.binding.ErrorBundle;
import com.orange.ocara.business.interactor.UseCase;

import lombok.RequiredArgsConstructor;

import static com.orange.ocara.business.interactor.AcceptTermsOfUseTask.AcceptTermsOfUseRequest;
import static com.orange.ocara.business.interactor.AcceptTermsOfUseTask.AcceptTermsOfUseResponse;
import static com.orange.ocara.business.interactor.AcceptTermsOfUseTask.UseCaseCallback;
import static com.orange.ocara.business.interactor.DeclineTermsOfUseTask.DeclineTermsOfUseRequest;
import static com.orange.ocara.business.interactor.DeclineTermsOfUseTask.DeclineTermsOfUseResponse;
import static com.orange.ocara.business.interactor.LoadTermsOfUseTask.LoadTermsOfUseRequest;
import static com.orange.ocara.business.interactor.LoadTermsOfUseTask.LoadTermsOfUseResponse;
import static com.orange.ocara.ui.contract.TermsOfUseAcceptanceContract.TermsOfUseAcceptanceUserActionsListener;
import static com.orange.ocara.ui.contract.TermsOfUseAcceptanceContract.TermsOfUseAcceptanceView;
import static timber.log.Timber.d;
import static timber.log.Timber.e;

/**
 * Mediator between a {@link TermsOfUseAcceptanceView} and its {@link UseCase}s
 */
@RequiredArgsConstructor
public class TermsOfUseAcceptancePresenter implements TermsOfUseAcceptanceUserActionsListener {

    private final TermsOfUseAcceptanceView view;

    private final UseCase<AcceptTermsOfUseRequest, AcceptTermsOfUseResponse> acceptanceTask;

    private final UseCase<DeclineTermsOfUseRequest, DeclineTermsOfUseResponse> declineTask;

    private final UseCase<LoadTermsOfUseRequest, LoadTermsOfUseResponse> loadingTask;

    @Override
    public void acceptTerms() {

        d( "Message=Accepting terms");
        acceptanceTask.executeUseCase(
                new AcceptTermsOfUseRequest(),
                new UseCaseCallback<AcceptTermsOfUseResponse>() {
                    @Override
                    public void onComplete(AcceptTermsOfUseResponse response) {
                        d( "Message=Acceptance successful");
                        view.showTermsAccepted();
                    }

                    @Override
                    public void onError(ErrorBundle errors) {
                        e( "Message=Acceptance failed");
                        view.showError(errors.getMessage());
                    }
                });
    }

    @Override
    public void declineTerms() {

        d( "Message=Declining terms");

        declineTask.executeUseCase(
                new DeclineTermsOfUseRequest(),
                new UseCaseCallback<DeclineTermsOfUseResponse>() {
                    @Override
                    public void onComplete(DeclineTermsOfUseResponse response) {
                        d( "Message=Decline successful");
                        view.showTermsDeclined();
                    }

                    @Override
                    public void onError(ErrorBundle errors) {

                        e( "Message=Decline failed");
                        view.showError(errors.getMessage());
                    }
                }
        );
    }

    @Override
    public void loadTerms() {
        d( "Message=Loading terms");

        loadingTask.executeUseCase(
                new LoadTermsOfUseRequest(),
                new UseCaseCallback<LoadTermsOfUseResponse>() {
                    @Override
                    public void onComplete(LoadTermsOfUseResponse response) {

                        if (response.isRemote()) {
                            view.showTerms(response.getUrl());
                        } else if (response.isRaw()) {
                            view.showTerms(response.getRawData());
                        }
                    }

                    @Override
                    public void onError(ErrorBundle errors) {

                        e( "Message=Loading failed");
                        view.showError(errors.getMessage());
                    }
                }
        );
    }
}
