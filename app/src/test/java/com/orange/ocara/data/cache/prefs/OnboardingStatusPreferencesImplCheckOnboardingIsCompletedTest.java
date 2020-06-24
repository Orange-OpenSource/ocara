package com.orange.ocara.data.cache.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Copyright (C) 2015 Orange
 * <p>
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
public class OnboardingStatusPreferencesImplCheckOnboardingIsCompletedTest {

    private OnboardingStatusPreferencesImpl subject;

    private SharedPreferences reader;

    @Before
    public void setUp() {
        reader = mock(SharedPreferences.class);
        Context context = mock(Context.class);
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(reader);
        when(context.getString(anyInt())).thenReturn(RandomStringUtils.randomAlphabetic(10));

        subject = new OnboardingStatusPreferencesImpl(context);
    }

    @Test
    public void shouldReturnTrueWhenPrefExistsAndPrefIsTrue() {
        // Given
        when(reader.getBoolean("INTRO_COMPLETED", false)).thenReturn(true);

        // When
        boolean result = subject.checkOnboardingIsCompleted();

        // Then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenPrefExistsAndPrefIsFalse() {
        // Given
        when(reader.getBoolean("INTRO_COMPLETED", false)).thenReturn(false);

        // When
        boolean result = subject.checkOnboardingIsCompleted();

        // Then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldReturnFalseWhenPrefDoesNotExist() {
        // Given

        // When
        boolean result = subject.checkOnboardingIsCompleted();

        // Then
        assertThat(result).isFalse();
    }
}