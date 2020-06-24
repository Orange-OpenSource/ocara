package com.orange.ocara.data.source;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Copyright (C) 2015 Orange
 * <p>
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
public class TermsDataStoreImplCheckTermsAreDefinedTest {

    @Test
    public void shouldDelegateOperationToCache() {

        // given
        TermsSource.TermsCache termsCache = mock(TermsSource.TermsCache.class);

        TermsSource.TermsRemote termsRemote = mock(TermsSource.TermsRemote.class);

        TermsDataStoreImpl subject = new TermsDataStoreImpl(termsCache, termsRemote);

        // when
        subject.checkTermsAreDefined();

        // then
        verify(termsCache).checkTermsAreDefined();
        verifyNoMoreInteractions(termsRemote, termsCache);
    }
}