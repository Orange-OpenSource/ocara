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

import static com.orange.ocara.business.interactor.LoadTermsOfUseTask.LoadTermsOfUseRequest;
import static com.orange.ocara.business.interactor.LoadTermsOfUseTask.LoadTermsOfUseResponse;
import static com.orange.ocara.ui.contract.TermsOfUseReadingContract.TermsOfUseReadingUserActionsListener;
import static com.orange.ocara.ui.contract.TermsOfUseReadingContract.TermsOfUseReadingView;
import static timber.log.Timber.d;
import static timber.log.Timber.e;

/**
 * Mediator between a {@link TermsOfUseReadingView} and its {@link UseCase}s
 */
@RequiredArgsConstructor
public class TermsOfUseReadingPresenter implements TermsOfUseReadingUserActionsListener {

    private final TermsOfUseReadingView view;

    private final UseCase<LoadTermsOfUseRequest, LoadTermsOfUseResponse> loadingTask;

    @Override
    public void loadTerms() {
        d("Message=Loading terms");

        loadingTask.executeUseCase(
                new LoadTermsOfUseRequest(),
                new UseCase.UseCaseCallback<LoadTermsOfUseResponse>() {
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

                        e("Message=Loading failed");
                        view.showError(errors.getMessage());
                    }
                }
        );
    }

}
