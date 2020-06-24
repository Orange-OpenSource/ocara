package com.orange.ocara.data.source;

import com.orange.ocara.utils.TestUtils;

import org.assertj.core.util.Files;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.File;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Copyright (C) 2015 Orange
 * <p>
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
public class ImageDataStoreImplGetTest {

    private ImageDataStoreImpl subject;

    @Test
    public void shouldReturnCachedStreamWhenFilenameExistsInCache() {

        // given
        String input = TestUtils.str();
        File expectedFile = Files.newTemporaryFile();

        ImageSource.ImageCache illustrationCache = mock(ImageSource.ImageCache.class);
        when(illustrationCache.fileExists(input)).thenReturn(true);
        when(illustrationCache.get(input)).thenReturn(expectedFile);

        ImageSource.ImageRemote illustrationRemote = mock(ImageSource.ImageRemote.class);

        subject = new ImageDataStoreImpl(illustrationCache, illustrationRemote);

        // when
        File result = subject.get(input);

        // then
        assertThat(result).isEqualTo(expectedFile);
    }

    @Test
    public void shouldReturnRemoteStreamWhenFilenameExistsInRemote() {

        // given
        String input = TestUtils.str();
        InputStream expectedInputStream = TestUtils.is();
        File expectedFile = Files.newTemporaryFile();

        ImageSource.ImageCache illustrationCache = mock(ImageSource.ImageCache.class);
        when(illustrationCache.fileExists(input)).thenReturn(false);
        when(illustrationCache.get(input)).thenReturn(expectedFile);

        ImageSource.ImageRemote illustrationRemote = mock(ImageSource.ImageRemote.class);
        when(illustrationRemote.get(input)).thenReturn(expectedInputStream);

        subject = new ImageDataStoreImpl(illustrationCache, illustrationRemote);

        // when
        File result = subject.get(input);

        // then
        ArgumentCaptor<InputStream> argumentCaptor = ArgumentCaptor.forClass(InputStream.class);
        verify(illustrationCache).write(argumentCaptor.capture(), ArgumentMatchers.eq(input));

        assertThat(result).isEqualTo(expectedFile);
    }
}