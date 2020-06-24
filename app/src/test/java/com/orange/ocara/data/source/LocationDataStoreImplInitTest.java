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
public class LocationDataStoreImplInitTest {

    @Test
    public void shouldDelegateOperationToCache() {

        // given
        LocationSource.LocationCache cacheMock = mock(LocationSource.LocationCache.class);

        LocationDataStoreImpl subject = new LocationDataStoreImpl(cacheMock);

        // when
        subject.init();

        // then
        verify(cacheMock).init();
        verifyNoMoreInteractions(cacheMock);
    }
}