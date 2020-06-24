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

import com.orange.ocara.business.interactor.LoadOnboardingItemsTask;
import com.orange.ocara.business.interactor.SkipOnboardingTask.SkipOnboardingResponse;
import com.orange.ocara.business.interactor.UseCase.UseCaseCallback;
import com.orange.ocara.business.model.OnboardingItemModel;

import java.util.List;

import static com.orange.ocara.business.interactor.ChangeOnboardingStepTask.ChangeOnboardingStepResponse;
import static com.orange.ocara.business.interactor.CompleteOnboardingTask.CompleteOnboardingResponse;

/**
 * contract between a view and a presenter that deal with the displaying of a tutorial
 */
public interface TutorialDisplayContract {

    /** description of the view */
    interface TutorialDisplayView {

        void finishView();

        void cancelView();

        void showItems(List<OnboardingItemModel> items);

        void showError(String message);

        void showStartButton();

        void showNextButton();

        void showStep(int position);
    }

    /** description of the presenter */
    interface TutorialDisplayUserActionsListener {

        void loadOnboarding(UseCaseCallback<LoadOnboardingItemsTask.LoadOnboardingItemsResponse> callback);

        void completeOnboarding(UseCaseCallback<CompleteOnboardingResponse> callback);

        void skipOnboarding(UseCaseCallback<SkipOnboardingResponse> callback);

        void openNextStep(UseCaseCallback<ChangeOnboardingStepResponse> callback);

        void notifyStepChanged(int position, UseCaseCallback<ChangeOnboardingStepResponse> callback);
    }
}
