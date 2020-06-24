package com.orange.ocara.data.cache.file;

/**
 * Copyright (C) 2015 Orange
 * <p>
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;

import static com.orange.ocara.utils.TestUtils.mockContext;
import static com.orange.ocara.utils.TestUtils.str;
import static org.assertj.core.api.Assertions.assertThat;

/** see {@link FileStorage#fileExists(String, String)} */
public class AssetStorageFileExistsTest {

    private AssetStorage subject;

    @Rule
    public Timeout globalTimeout = Timeout.seconds(60); // 60 seconds max per method tested

    @Test
    public void shouldReturnTrueWhenFileExistsInContextAndDirectory() throws IOException {

        String inputFilename = str();
        String inputDirectory = str();
        subject = new AssetStorage(mockContext(getClass().getClassLoader(), inputFilename, inputDirectory));

        // when
        boolean result = subject.fileExists(inputFilename, inputDirectory);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenFileDoesNotExistInContextAndDirectory() throws IOException {

        String inputFilename = str();
        String inputDirectory = str();
        subject = new AssetStorage(mockContext(getClass().getClassLoader(), str(), inputDirectory));

        // when
        boolean result = subject.fileExists(inputFilename, inputDirectory);

        // then
        assertThat(result).isFalse();
    }
}