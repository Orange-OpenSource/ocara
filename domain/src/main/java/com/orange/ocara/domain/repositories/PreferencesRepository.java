/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */


package com.orange.ocara.domain.repositories;

import android.content.Context;

import com.orange.ocara.domain.cache.prefs.OnboardingStatusPreferences;
import com.orange.ocara.domain.cache.prefs.OnboardingStatusPreferencesImpl;
import com.orange.ocara.domain.cache.prefs.TermsOfUsePreferences;
import com.orange.ocara.domain.cache.prefs.TermsOfUsePreferencesImpl;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.qualifiers.ApplicationContext;

public class PreferencesRepository {

    TermsOfUsePreferences termsOfUsePreferences;
    OnboardingStatusPreferences onboardingStatusPreferences;

    @Inject
    public PreferencesRepository(@ApplicationContext Context context) {
        termsOfUsePreferences = new TermsOfUsePreferencesImpl(context);
        onboardingStatusPreferences = new OnboardingStatusPreferencesImpl(context);
    }

    public void setTutorialCompleted() {
        onboardingStatusPreferences.markOnboardingAsCompleted();
    }

    public boolean isTutorialCompleted() {
        return onboardingStatusPreferences.checkOnboardingIsCompleted();
    }

    public boolean checkTermsAccepted() {
        return termsOfUsePreferences.checkTermsAreAccepted();
    }

    public void acceptTerms() {
        termsOfUsePreferences.saveAcceptedTerms();
    }

    public void declineTerms() {
        termsOfUsePreferences.saveRefusedTerms();
    }

}
