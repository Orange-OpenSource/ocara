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

import com.orange.ocara.utils.R;
import com.orange.ocara.utils.exceptions.BizException;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * default implementation of {@link TermsOfUsePreferences}
 */
public class TermsOfUsePreferencesImpl implements TermsOfUsePreferences {

    private static final String TERMS_NOT_DEFINED = "0";

    private static final String TERMS_REFUSED = "1";

    private static final String TERMS_ACCEPTED = "2";

    /**
     * a reader for {@link SharedPreferences}
     */
    private final SharedPreferences reader;

    /**
     * the name of the preference to retrieve or save
     */
    private final String key;

    /**
     * instantiate.
     *
     * @param context a {@link Context}
     */
    public TermsOfUsePreferencesImpl(Context context) {

        reader = getDefaultSharedPreferences(context);

        key = context.getString(R.string.setting_global_terms);
    }

    @Override
    public void saveAcceptedTerms() {
        reader
                .edit()
                .putString(key, TERMS_ACCEPTED)
                .apply();
    }

    @Override
    public void saveRefusedTerms() {
        reader
                .edit()
                .putString(key, TERMS_REFUSED)
                .apply();
    }

    @Override
    public boolean checkTermsAreDefined() {
        String value = reader.getString(key, TERMS_NOT_DEFINED);

        return !TERMS_NOT_DEFINED.equals(value);
    }

    @Override
    public boolean checkTermsAreAccepted() {
        String value = reader.getString(key, TERMS_NOT_DEFINED);

        return TERMS_ACCEPTED.equals(value);
    }

    @Override
    public String retrieveTerms() {

        throw new BizException("Invalid");
    }
}
