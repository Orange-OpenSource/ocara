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

package com.orange.ocara.data.cache.prefs;

/** Repository based on {@link android.content.SharedPreferences} */
public interface OnboardingStatusPreferences {

    /**
     *
     * @return true if onboarding is already done
     */
    boolean checkOnboardingIsCompleted();

    /**
     * Changes preferences to set onboarding is done
     */
    void markOnboardingAsCompleted();
}
