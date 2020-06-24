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

import com.orange.ocara.data.cache.db.TermsDao;
import com.orange.ocara.data.cache.prefs.TermsOfUsePreferences;
import com.orange.ocara.data.source.TermsSource;

/** default implementation of {@link com.orange.ocara.data.source.TermsSource.TermsCache} */
public class TermsCacheImpl implements TermsSource.TermsCache {

    private final TermsDao termsDao;

    private final TermsOfUsePreferences termsPreferences;

    TermsCacheImpl(TermsDao termsDao, TermsOfUsePreferences termsPreferences) {
        this.termsDao = termsDao;
        this.termsPreferences = termsPreferences;
    }

    @Override
    public String find() {
        return termsDao.get();
    }

    @Override
    public void markAsAccepted() {

        termsPreferences.saveAcceptedTerms();
    }

    @Override
    public void markAsRefused() {

        termsPreferences.saveRefusedTerms();
    }

    @Override
    public boolean checkTermsAreDefined() {
        return termsPreferences.checkTermsAreDefined();
    }

    @Override
    public boolean checkTermsAreAccepted() {
        return termsPreferences.checkTermsAreAccepted();
    }
}
