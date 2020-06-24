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

import static com.orange.ocara.data.source.TermsSource.TermsCache;
import static com.orange.ocara.data.source.TermsSource.TermsDataStore;
import static com.orange.ocara.data.source.TermsSource.TermsRemote;

public class TermsDataStoreImpl implements TermsDataStore {

    private final TermsCache termsCache;

    private final TermsRemote termsRemote;

    public TermsDataStoreImpl(TermsCache termsCache, TermsRemote termsRemote) {
        this.termsCache = termsCache;
        this.termsRemote = termsRemote;
    }

    @Override
    public String find() {
        String text = termsRemote.find();
        if (text == null || text.isEmpty()) {
            text = termsCache.find();
        }
        return text;
    }

    @Override
    public void markAsAccepted() {

        termsCache.markAsAccepted();
    }

    @Override
    public void markAsRefused() {

        termsCache.markAsRefused();
    }

    @Override
    public boolean checkTermsAreDefined() {

        return termsCache.checkTermsAreDefined();
    }

    @Override
    public boolean checkTermsAreAccepted() {

        return termsCache.checkTermsAreAccepted();
    }
}
