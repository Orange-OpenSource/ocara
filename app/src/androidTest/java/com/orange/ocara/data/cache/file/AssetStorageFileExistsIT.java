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

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

/** see {@link FileStorage#fileExists(String, String)} */
@RunWith(ParameterizedRobolectricTestRunner.class)
@Config(
        application = TestOcaraApplication.class,
        sdk = Build.VERSION_CODES.KITKAT)
public class AssetStorageFileExistsIT {

    private AssetStorage subject;

    private Context context;

    private String inputDirectory;

    private int expectedCount;

    private String inputFilename;

    public AssetStorageFileExistsIT(String inputDirectory, int expectedCount, String expectedFilename) {
        this.inputDirectory = inputDirectory;
        this.inputFilename = expectedFilename;
        this.expectedCount = expectedCount;
    }

    @ParameterizedRobolectricTestRunner.Parameters
    public static Collection<Object[]> data() {
        return DataFileTestUtil.assets();
    }

    @Before
    public void setUp() {
        context = TestUtils.instrumentationContext();

        subject = new AssetStorage(context);
    }

    @Test
    public void shouldReturnTrueWhenFileExistsInContextAndDirectory() {

        // given

        // when
        boolean result = subject.fileExists(inputFilename, inputDirectory);

        // then
        assertThat(result).isTrue();
    }
}