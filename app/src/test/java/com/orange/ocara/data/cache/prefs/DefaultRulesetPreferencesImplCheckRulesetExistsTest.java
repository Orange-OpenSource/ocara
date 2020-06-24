package com.orange.ocara.data.cache.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** see {@link DefaultRulesetPreferencesImpl#checkRulesetExists()} */
public class DefaultRulesetPreferencesImplCheckRulesetExistsTest {

    private DefaultRulesetPreferencesImpl subject;

    private SharedPreferences reader;

    @Before
    public void setUp() {
        reader = mock(SharedPreferences.class);
        Context context = mock(Context.class);
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(reader);
        when(context.getString(anyInt())).thenReturn(RandomStringUtils.randomAlphabetic(10));

        subject = new DefaultRulesetPreferencesImpl(context);
    }

    @Test
    public void shouldReturnTrueWhenItExistsAndItIsValid() {
        // Given
        when(reader.getString(anyString(), anyString())).thenReturn("{\"reference\" : \"RS1\", \"version\":1}");

        // When
        boolean result = subject.checkRulesetExists();

        // Then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenItExistsAndItIsNotValid() {
        // Given
        when(reader.getString(anyString(), anyString())).thenReturn("");

        // When
        boolean result = subject.checkRulesetExists();

        // Then
        assertThat(result).isFalse();
    }
}