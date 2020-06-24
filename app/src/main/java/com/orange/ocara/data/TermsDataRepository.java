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

import com.orange.ocara.business.repository.TermsRepository;
import com.orange.ocara.data.source.TermsSource;

import lombok.RequiredArgsConstructor;

/**
 * Behaviour of a service that retrieves the app's Terms-Of-Use
 */
@RequiredArgsConstructor
public class TermsDataRepository implements TermsRepository {

    private final TermsSource.TermsDataStore termsDataStore;

    /**
     * First, we look for the text remotely. If it not available, we retrieve the one that is stored
     * in the app.
     *
     * @return the terms as text
     */
    @Override
    public String find() {
        return termsDataStore.find();
    }

    @Override
    public void markAsAccepted() {

        termsDataStore.markAsAccepted();
    }

    @Override
    public void markAsRefused() {

        termsDataStore.markAsRefused();
    }

    @Override
    public boolean checkTermsAreDefined() {
        return termsDataStore.checkTermsAreDefined();
    }

    @Override
    public boolean checkTermsAreAccepted() {
        return termsDataStore.checkTermsAreAccepted();
    }
}
