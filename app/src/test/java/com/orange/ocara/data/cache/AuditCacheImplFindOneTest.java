package com.orange.ocara.data.cache;

import com.orange.ocara.data.cache.db.AuditCloneDao;
import com.orange.ocara.data.cache.db.AuditQueryDao;
import com.orange.ocara.utils.TestUtils;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Copyright (C) 2015 Orange
 * <p>
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
public class AuditCacheImplFindOneTest {

    private AuditCacheImpl subject;

    private AuditQueryDao queryDao;

    private AuditCloneDao cloneDao;

    @Test
    public void shouldDelegateToCloneDao() {

        // given
        queryDao = mock(AuditQueryDao.class);
        cloneDao = mock(AuditCloneDao.class);

        subject = new AuditCacheImpl(queryDao, cloneDao);

        Long inputAuditId = TestUtils.longNb();

        // when
        subject.findOne(inputAuditId);

        // then
        verify(queryDao).findOne(inputAuditId);
        verifyZeroInteractions(cloneDao);
    }
}