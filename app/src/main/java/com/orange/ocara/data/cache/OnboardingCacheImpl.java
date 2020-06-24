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
package com.orange.ocara.data.cache;

import com.orange.ocara.business.model.OnboardingItemModel;
import com.orange.ocara.data.cache.db.OnboardingItemDao;
import com.orange.ocara.data.cache.prefs.OnboardingStatusPreferences;
import com.orange.ocara.data.source.OnboardingSource;

import java.util.List;

/** default implementation of {@link com.orange.ocara.data.source.OnboardingSource.OnboardingCache} */
public class OnboardingCacheImpl implements OnboardingSource.OnboardingCache {

    private final OnboardingItemDao itemRepository;

    private final OnboardingStatusPreferences statusRepository;


    public OnboardingCacheImpl(OnboardingItemDao itemRepository, OnboardingStatusPreferences statusRepository) {
        this.itemRepository = itemRepository;
        this.statusRepository = statusRepository;
    }

    @Override
    public List<OnboardingItemModel> findAll() {
        return itemRepository.findAll();
    }

    @Override
    public boolean checkOnboardingIsCompleted() {
        return statusRepository.checkOnboardingIsCompleted();
    }

    @Override
    public void saveCompletedOnboarding() {

        statusRepository.markOnboardingAsCompleted();
    }
}
