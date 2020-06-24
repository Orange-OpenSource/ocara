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

package com.orange.ocara.data;

import com.orange.ocara.business.model.OnboardingItemModel;
import com.orange.ocara.business.repository.OnboardingRepository;
import com.orange.ocara.data.source.OnboardingSource;

import java.util.List;

import lombok.RequiredArgsConstructor;

/**
 * default implementation of {@link OnboardingRepository}
 */
@RequiredArgsConstructor
public class OnboardingItemDataRepository implements OnboardingRepository {

    private final OnboardingSource.OnboardingDataStore dataStore;

    @Override
    public List<OnboardingItemModel> findAll() {
        return dataStore.findAll();
    }

    @Override
    public boolean checkOnboardingIsCompleted() {
        return dataStore.checkOnboardingIsCompleted();
    }

    @Override
    public void saveCompletedOnboarding() {

        dataStore.saveCompletedOnboarding();
    }
}
