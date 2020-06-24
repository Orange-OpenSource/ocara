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

/** default implementation of {@link com.orange.ocara.data.source.OnboardingSource.OnboardingDataStore} */
public class OnboardingDataStoreImpl implements OnboardingSource.OnboardingDataStore {

    private final OnboardingSource.OnboardingCache onboardingCache;

    public OnboardingDataStoreImpl(OnboardingSource.OnboardingCache onboardingCache) {
        this.onboardingCache = onboardingCache;
    }

    @Override
    public List<OnboardingItemModel> findAll() {
        return onboardingCache.findAll();
    }

    @Override
    public boolean checkOnboardingIsCompleted() {
        return onboardingCache.checkOnboardingIsCompleted();
    }

    @Override
    public void saveCompletedOnboarding() {

        onboardingCache.saveCompletedOnboarding();
    }
}
