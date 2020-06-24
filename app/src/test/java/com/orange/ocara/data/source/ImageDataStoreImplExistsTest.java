package com.orange.ocara.data.source;

import com.orange.ocara.utils.TestUtils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Copyright (C) 2015 Orange
 * <p>
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
public class ImageDataStoreImplExistsTest {

    private ImageDataStoreImpl subject;

    @Test
    public void shouldReturnTrueWhenFilenameExistsInCache() {

        // given
        String input = TestUtils.str();

        ImageSource.ImageCache illustrationCache = mock(ImageSource.ImageCache.class);
        when(illustrationCache.fileExists(input)).thenReturn(true);

        ImageSource.ImageRemote illustrationRemote = mock(ImageSource.ImageRemote.class);

        subject = new ImageDataStoreImpl(illustrationCache, illustrationRemote);

        // when
        boolean result = subject.exists(input);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnTrueWhenFilenameDoesNotExistInCacheButInRemote() {

        // given
        String input = TestUtils.str();

        ImageSource.ImageCache illustrationCache = mock(ImageSource.ImageCache.class);
        when(illustrationCache.fileExists(input)).thenReturn(false);

        ImageSource.ImageRemote illustrationRemote = mock(ImageSource.ImageRemote.class);
        when(illustrationRemote.get(input)).thenReturn(TestUtils.is());

        subject = new ImageDataStoreImpl(illustrationCache, illustrationRemote);

        // when
        boolean result = subject.exists(input);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenFilenameDoesNotExistInCacheNorInRemote() {

        // given
        String input = TestUtils.str();

        ImageSource.ImageCache illustrationCache = mock(ImageSource.ImageCache.class);
        when(illustrationCache.fileExists(input)).thenReturn(false);

        ImageSource.ImageRemote illustrationRemote = mock(ImageSource.ImageRemote.class);
        when(illustrationRemote.get(input)).thenReturn(null);

        subject = new ImageDataStoreImpl(illustrationCache, illustrationRemote);

        // when
        boolean result = subject.exists(input);

        // then
        assertThat(result).isFalse();
    }
}