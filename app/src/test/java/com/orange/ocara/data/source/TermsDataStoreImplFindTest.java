package com.orange.ocara.data.source;

import com.orange.ocara.utils.TestUtils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Copyright (C) 2015 Orange
 * <p>
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
public class TermsDataStoreImplFindTest {

    private TermsDataStoreImpl subject;

    @Test
    public void shouldReturnTextFromRemoteWhenItExistsAndIsNotEmpty() {

        // given
        TermsSource.TermsCache termsCache = mock(TermsSource.TermsCache.class);

        TermsSource.TermsRemote termsRemote = mock(TermsSource.TermsRemote.class);
        String expected = TestUtils.str();
        when(termsRemote.find()).thenReturn(expected);

        subject = new TermsDataStoreImpl(termsCache, termsRemote);

        // when
        String result = subject.find();

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void shouldReturnTextFromCacheWhenRemoteTextIsNull() {

        // given
        TermsSource.TermsCache termsCache = mock(TermsSource.TermsCache.class);
        String expected = TestUtils.str();
        when(termsCache.find()).thenReturn(expected);

        TermsSource.TermsRemote termsRemote = mock(TermsSource.TermsRemote.class);
        when(termsRemote.find()).thenReturn(null);

        subject = new TermsDataStoreImpl(termsCache, termsRemote);

        // when
        String result = subject.find();

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void shouldReturnTextFromCacheWhenRemoteTextIsEmpty() {

        // given
        TermsSource.TermsCache termsCache = mock(TermsSource.TermsCache.class);
        String expected = TestUtils.str();
        when(termsCache.find()).thenReturn(expected);

        TermsSource.TermsRemote termsRemote = mock(TermsSource.TermsRemote.class);
        when(termsRemote.find()).thenReturn("");

        subject = new TermsDataStoreImpl(termsCache, termsRemote);

        // when
        String result = subject.find();

        //then
        assertThat(result).isEqualTo(expected);
    }
}