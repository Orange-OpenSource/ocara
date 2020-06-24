/**
 * Copyright (C) 2015 Orange
 * <p>
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.data.cache.file;

import android.content.Context;
import android.os.Build;

import com.orange.ocara.TestOcaraApplication;
import com.orange.ocara.TestUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

/** see {@link StorageType#rootDirectory(Context)}*/
@RunWith(RobolectricTestRunner.class)
@Config(
        application = TestOcaraApplication.class,
        sdk = Build.VERSION_CODES.KITKAT)
public class StorageTypeExternalCacheRootDirectoryIT {

    private StorageType subject = StorageType.EXTERNAL_CACHE;

    private Context context;

    @Before
    public void setUp() {
        context = TestUtils.instrumentationContext();
    }

    @Test
    public void shouldReturnRootDirectory() {

        // given

        // when
        File result = subject.rootDirectory(context);

        // then
        assertThat(result.isDirectory()).isTrue();
    }
}