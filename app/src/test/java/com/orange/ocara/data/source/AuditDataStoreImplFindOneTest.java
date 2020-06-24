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
public class AuditDataStoreImplFindOneTest {

    @Test
    public void shouldDelegateOperationToCache() {

        // given
        AuditSource.AuditCache cacheMock = mock(AuditSource.AuditCache.class);

        AuditDataStoreImpl subject = new AuditDataStoreImpl(cacheMock);

        Long input = TestUtils.longNb();

        // when
        subject.findOne(input);

        // then
        verify(cacheMock).findOne(input);
        verifyNoMoreInteractions(cacheMock);
    }
}