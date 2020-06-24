package com.orange.ocara.data.source;

import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.model.VersionableModel;
import com.orange.ocara.data.net.model.Ruleset;
import com.orange.ocara.data.net.model.RulesetLightWs;

import org.junit.Test;

import static com.orange.ocara.utils.TestUtils.str;
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
public class RulesetDataStoreImplExistsTest {

    @Test
    public void shouldReturnTrueWhenInputReferenceExistsInCache() {

        // given
        String inputRef = str();

        RulesetSource.RulesetRemote rulesetRemote = mock(RulesetSource.RulesetRemote.class);

        RulesetSource.RulesetCache rulesetCache = mock(RulesetSource.RulesetCache.class);
        when(rulesetCache.exists(inputRef)).thenReturn(true);

        ImageSource.ImageRemote imageRemote = mock(ImageSource.ImageRemote.class);

        ImageSource.ImageCache imageCache = mock(ImageSource.ImageCache.class);

        RulesetDataStoreImpl subject = new RulesetDataStoreImpl(rulesetCache, rulesetRemote, imageCache, imageRemote);

        // when
        boolean result = subject.exists(inputRef);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnTrueWhenInputReferenceDoesNotExistInCacheButExistsInRemote() {

        // given
        String inputRef = str();

        RulesetSource.RulesetRemote rulesetRemote = mock(RulesetSource.RulesetRemote.class);
        when(rulesetRemote.findLast(inputRef)).thenReturn(mock(RulesetLightWs.class));

        RulesetSource.RulesetCache rulesetCache = mock(RulesetSource.RulesetCache.class);
        when(rulesetCache.exists(inputRef)).thenReturn(false);

        ImageSource.ImageRemote imageRemote = mock(ImageSource.ImageRemote.class);

        ImageSource.ImageCache imageCache = mock(ImageSource.ImageCache.class);

        RulesetDataStoreImpl subject = new RulesetDataStoreImpl(rulesetCache, rulesetRemote, imageCache, imageRemote);

        // when
        boolean result = subject.exists(inputRef);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenInputReferenceDoesNotExistInCacheNorInRemote() {

        // given
        String inputRef = str();

        RulesetSource.RulesetRemote rulesetRemote = mock(RulesetSource.RulesetRemote.class);
        when(rulesetRemote.findLast(inputRef)).thenReturn(null);

        RulesetSource.RulesetCache rulesetCache = mock(RulesetSource.RulesetCache.class);
        when(rulesetCache.exists(inputRef)).thenReturn(false);

        ImageSource.ImageRemote imageRemote = mock(ImageSource.ImageRemote.class);

        ImageSource.ImageCache imageCache = mock(ImageSource.ImageCache.class);

        RulesetDataStoreImpl subject = new RulesetDataStoreImpl(rulesetCache, rulesetRemote, imageCache, imageRemote);

        // when
        boolean result = subject.exists(inputRef);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldReturnFalseWhenInputVersionableDoesNotExistInCache() {

        // given
        VersionableModel input = mock(VersionableModel.class);

        RulesetSource.RulesetRemote rulesetRemote = mock(RulesetSource.RulesetRemote.class);

        RulesetSource.RulesetCache rulesetCache = mock(RulesetSource.RulesetCache.class);
        when(rulesetCache.exists(input)).thenReturn(false);

        ImageSource.ImageRemote imageRemote = mock(ImageSource.ImageRemote.class);

        ImageSource.ImageCache imageCache = mock(ImageSource.ImageCache.class);

        RulesetDataStoreImpl subject = new RulesetDataStoreImpl(rulesetCache, rulesetRemote, imageCache, imageRemote);

        // when
        boolean result = subject.exists(input);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldReturnTrueWhenInputVersionableExistsInCache() {

        // given
        VersionableModel input = mock(VersionableModel.class);

        RulesetSource.RulesetRemote rulesetRemote = mock(RulesetSource.RulesetRemote.class);

        RulesetSource.RulesetCache rulesetCache = mock(RulesetSource.RulesetCache.class);
        when(rulesetCache.exists(input)).thenReturn(true);

        ImageSource.ImageRemote imageRemote = mock(ImageSource.ImageRemote.class);

        ImageSource.ImageCache imageCache = mock(ImageSource.ImageCache.class);

        RulesetDataStoreImpl subject = new RulesetDataStoreImpl(rulesetCache, rulesetRemote, imageCache, imageRemote);

        // when
        boolean result = subject.exists(input);

        // then
        assertThat(result).isTrue();
    }
}