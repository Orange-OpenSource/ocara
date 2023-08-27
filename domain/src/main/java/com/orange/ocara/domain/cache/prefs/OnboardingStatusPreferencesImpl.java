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

package com.orange.ocara.domain.cache.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import timber.log.Timber;

/** default implementation for {@link OnboardingStatusPreferences} */
public class OnboardingStatusPreferencesImpl implements OnboardingStatusPreferences {

    private static final String KEY = "INTRO_COMPLETED";

    private final Context context;

    /**
     * a reader for {@link SharedPreferences}
     */
    private final SharedPreferences reader;

    /**
     * instantiates
     *
     * @param context a {@link Context}
     */
    public OnboardingStatusPreferencesImpl(Context context) {
        this.context = context;
        reader = context
                .getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }

    @Override
    public boolean checkOnboardingIsCompleted() {

        boolean result = reader.getBoolean(KEY, false);

        Timber.d("DataMessage=Checking onboarding completion;ResultCode=%b", result);

        return result;
    }

    @Override
    public void markOnboardingAsCompleted() {

        Timber.d("DataMessage=Marking onboarding as completed");
        reader
                .edit()
                .putBoolean(KEY, true)
                .apply();
    }
}
