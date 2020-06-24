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
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.robolectric.ParameterizedRobolectricTestRunner.Parameters;

@RunWith(ParameterizedRobolectricTestRunner.class)
@Config(
        application = TestOcaraApplication.class,
        sdk = Build.VERSION_CODES.KITKAT)
/** see {@link LocalFileStorage#fileExists(String, String)} */
public class LocalFileStorageFileExistsIT {

    private LocalFileStorage subject;

    private StorageType storageType;

    public LocalFileStorageFileExistsIT(StorageType storageType) {
        this.storageType = storageType;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {StorageType.EXTERNAL}, {StorageType.EXTERNAL_CACHE}, {StorageType.INTERNAL}, {StorageType.INTERNAL_CACHE}
        });
    }

    @Before
    public void setUp() {

        Context context = TestUtils.instrumentationContext();
        subject = new LocalFileStorage(context, storageType);

    }

    @Test
    public void shouldReturnFalseWhenFileDoesNotExistInContextAndDirectory() {

        // given
        String inputFilename = newFilename();
        String inputDirectory = "";

        // when
        boolean result = subject.fileExists(inputFilename, inputDirectory);

        // then
        assertThat(result).isFalse();
    }

    private static String newFilename() {
        return TestUtils.str() + ".txt";
    }
}