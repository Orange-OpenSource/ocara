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

package com.orange.ocara.data.source;

import com.orange.ocara.business.model.OnboardingItemModel;

import java.util.List;

/** contract between a data store that exposes onboarding items and repositories that handle them locally */
public interface OnboardingSource {

    /** description of the repository that deals with local database */
    interface OnboardingCache {

        /**
         *
         * @return a bunch of {@link OnboardingItemModel}s
         */
        List<OnboardingItemModel> findAll();

        /**
         *
         * @return true if the onboarding has already been completed by the user
         */
        boolean checkOnboardingIsCompleted();

        /**
         * marks the onboarding status as completed
         */
        void saveCompletedOnboarding();
    }

    /** description of the data store that exposes onboarding items  */
    interface OnboardingDataStore {

        /**
         *
         * @return a bunch of {@link OnboardingItemModel}s
         */
        List<OnboardingItemModel> findAll();

        /**
         *
         * @return true if the onboarding has already been completed by the user
         */
        boolean checkOnboardingIsCompleted();

        /**
         * marks the onboarding status as completed
         */
        void saveCompletedOnboarding();
    }

}
