/**
 * Copyright (C) 2015 Orange
 * <p>
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.data.net.model;

import com.activeandroid.ActiveAndroid;
import com.orange.ocara.TestOcaraApplication;
import com.orange.ocara.data.cache.model.AuditObjectEntity;
import com.orange.ocara.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(
        application = TestOcaraApplication.class,
        sdk = 21)
public class AuditObjectEntityTest extends ModelTestBase<AuditObjectEntity> {

    @Before
    public void setUp() {
        ActiveAndroid.initialize(TestUtils.instrumentationContext());
    }

    @After
    public void tearDown() {
        ActiveAndroid.dispose();
    }

    public AuditObjectEntityTest() {
        super(AuditObjectEntity.class);
    }
}