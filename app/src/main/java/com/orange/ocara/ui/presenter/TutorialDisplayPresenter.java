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
import com.orange.ocara.business.interactor.ChangeOnboardingStepTask;
import com.orange.ocara.business.interactor.ChangeOnboardingStepTask.ChangeOnboardingStepResponse;
import com.orange.ocara.business.interactor.CompleteOnboardingTask;
import com.orange.ocara.business.interactor.LoadOnboardingItemsTask;
import com.orange.ocara.business.interactor.SkipOnboardingTask;
import com.orange.ocara.business.interactor.SkipOnboardingTask.SkipOnboardingRequest;
import com.orange.ocara.business.interactor.SkipOnboardingTask.SkipOnboardingResponse;
import com.orange.ocara.business.interactor.UseCase;
import com.orange.ocara.business.interactor.UseCase.UseCaseCallback;
import com.orange.ocara.business.interactor.UseCaseHandler;
import com.orange.ocara.business.model.OnboardingItemModel;
import com.orange.ocara.ui.contract.TutorialDisplayContract.TutorialDisplayUserActionsListener;

import java.util.Collections;
import java.util.List;

import timber.log.Timber;

import static com.orange.ocara.business.interactor.ChangeOnboardingStepTask.ChangeOnboardingStepRequest;
import static com.orange.ocara.business.interactor.CompleteOnboardingTask.CompleteOnboardingRequest;
import static com.orange.ocara.business.interactor.CompleteOnboardingTask.CompleteOnboardingResponse;
import static com.orange.ocara.business.interactor.LoadOnboardingItemsTask.LoadOnboardingItemsRequest;
import static com.orange.ocara.business.interactor.LoadOnboardingItemsTask.LoadOnboardingItemsResponse;

/**
 * a presenter that implements {@link TutorialDisplayUserActionsListener}
 */
public class TutorialDisplayPresenter implements TutorialDisplayUserActionsListener {

    private final UseCaseHandler useCaseHandler;

    /**
     * see {@link CompleteOnboardingTask}
     */
    private final UseCase<CompleteOnboardingRequest, CompleteOnboardingResponse> completeUseCase;

    /**
     * see {@link LoadOnboardingItemsTask}
     */
    private final UseCase<LoadOnboardingItemsRequest, LoadOnboardingItemsResponse> loadUseCase;

    /**
     * see {@link SkipOnboardingTask}
     */
    private final UseCase<SkipOnboardingRequest, SkipOnboardingResponse> skipUseCase;

    /**
     * see {@link ChangeOnboardingStepTask}
     */
    private final UseCase<ChangeOnboardingStepRequest, ChangeOnboardingStepResponse> changeStepUseCase;

    /**
     * index of the initial item to display from the pager
     */
    private int currentPosition = 0;

    /**
     * the items of the tutorial
     */
    private List<OnboardingItemModel> list = Collections.emptyList();

    public TutorialDisplayPresenter(
            UseCaseHandler useCaseHandler,
            UseCase<CompleteOnboardingRequest, CompleteOnboardingResponse> completeUseCase,
            UseCase<LoadOnboardingItemsRequest, LoadOnboardingItemsResponse> loadUseCase,
            UseCase<SkipOnboardingRequest, SkipOnboardingResponse> skipUseCase,
            UseCase<ChangeOnboardingStepRequest, ChangeOnboardingStepResponse> changeStepUseCase) {
        this.useCaseHandler = useCaseHandler;
        this.completeUseCase = completeUseCase;
        this.loadUseCase = loadUseCase;
        this.skipUseCase = skipUseCase;
        this.changeStepUseCase = changeStepUseCase;
    }

    @Override
    public void completeOnboarding(UseCaseCallback<CompleteOnboardingResponse> callback) {

        useCaseHandler.execute(completeUseCase, new CompleteOnboardingRequest(), callback);
    }

    @Override
    public void loadOnboarding(UseCaseCallback<LoadOnboardingItemsResponse> callback) {

        useCaseHandler.execute(
                loadUseCase,
                new LoadOnboardingItemsRequest(),
                new UseCaseCallback<LoadOnboardingItemsResponse>() {

                    @Override
                    public void onComplete(LoadOnboardingItemsResponse response) {
                        list = response.getItems();
                        currentPosition = 0;
                        callback.onComplete(response);
                    }

                    @Override
                    public void onError(ErrorBundle errors) {
                        list = Collections.emptyList();
                        currentPosition = 0;
                        callback.onError(errors);
                    }
                });
    }

    @Override
    public void skipOnboarding(UseCaseCallback<SkipOnboardingResponse> callback) {

        useCaseHandler.execute(skipUseCase, new SkipOnboardingRequest(), callback);
    }

    @Override
    public void openNextStep(UseCaseCallback<ChangeOnboardingStepResponse> callback) {

        useCaseHandler.execute(
                changeStepUseCase,
                new ChangeOnboardingStepRequest(currentPosition, currentPosition + 1, list.size() - 1),
                new UseCaseCallback<ChangeOnboardingStepResponse>() {

                    @Override
                    public void onComplete(ChangeOnboardingStepResponse response) {

                        Timber.d("PresenterMessage=Changing step completed;NewIndex=%d;OldIndex=%d", response.getTargetPosition(), currentPosition);
                        currentPosition = response.getTargetPosition();
                        callback.onComplete(response);
                    }

                    @Override
                    public void onError(ErrorBundle errors) {

                        callback.onError(errors);
                    }
                });
    }

    @Override
    public void notifyStepChanged(int newPosition, UseCaseCallback<ChangeOnboardingStepResponse> callback) {

        useCaseHandler.execute(
                changeStepUseCase,
                new ChangeOnboardingStepRequest(currentPosition, newPosition, list.size() - 1),
                new UseCaseCallback<ChangeOnboardingStepResponse>() {

                    @Override
                    public void onComplete(ChangeOnboardingStepResponse response) {

                        Timber.d("PresenterMessage=Changing step completed;NewIndex=%d;OldIndex=%d", response.getTargetPosition(), currentPosition);
                        currentPosition = response.getTargetPosition();
                        callback.onComplete(response);
                    }

                    @Override
                    public void onError(ErrorBundle errors) {

                        callback.onError(errors);
                    }
                });
    }
}
