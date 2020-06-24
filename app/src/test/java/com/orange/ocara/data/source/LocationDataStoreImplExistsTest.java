package com.orange.ocara.data.source;

import com.orange.ocara.utils.TestUtils;

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
public class LocationDataStoreImplExistsTest {

    @Test
    public void shouldDelegateOperationToCache() {

        // given
        LocationSource.LocationCache cacheMock = mock(LocationSource.LocationCache.class);

        LocationDataStoreImpl subject = new LocationDataStoreImpl(cacheMock);

        String input = TestUtils.str();

        // when
        subject.exists(input);

        // then
        verify(cacheMock).exists(input);
        verifyNoMoreInteractions(cacheMock);
    }
}