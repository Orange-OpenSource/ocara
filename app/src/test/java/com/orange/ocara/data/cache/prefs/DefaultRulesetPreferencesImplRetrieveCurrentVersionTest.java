package com.orange.ocara.data.cache.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.orange.ocara.business.model.RuleSetStat;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.utils.TestUtils;

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Condition;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * see {@link DefaultRulesetPreferencesImpl#retrieveRuleset()}
 */
public class DefaultRulesetPreferencesImplRetrieveCurrentVersionTest {

    private DefaultRulesetPreferencesImpl subject;

    @Test
    public void shouldDelegateRetrievalToSharedPreferences() {
        // Given
        String expectedReference = TestUtils.str();
        int expectedVersion = TestUtils.digit();
        RuleSetStat expectedStatus = TestUtils.randomRulesetStat();

        subject = new DefaultRulesetPreferencesImpl(context(expectedReference, expectedVersion, expectedStatus));

        // When
        RulesetModel result = subject.retrieveRuleset();

        // Then
        assertThat(result)
                .isNotNull()
                .has(new Condition<>(model ->
                        expectedReference.equals(model.getReference()), "should have expected reference"))
                .has(new Condition<>(model ->
                        ("" + expectedVersion).equals(model.getVersion()), "should retrieve expected version"))
                .has(new Condition<>(model ->
                        expectedStatus.equals(model.getStat()), "should retrieve expected status " + expectedStatus + ", but retrieved " + result.getStat()));

    }

    @Test
    public void shouldRetrieveNullWhenSharedPreferencesRetrievesDefaultValue() {
        // Given
        subject = new DefaultRulesetPreferencesImpl(context());

        // When
        RulesetModel result = subject.retrieveRuleset();

        // Then
        assertThat(result).isNull();
    }

    private Context context(String reference, Integer version, RuleSetStat status) {

        String serializedStatusName = status.toString().toLowerCase();

        SharedPreferences reader = mock(SharedPreferences.class);
        when(reader.getString(anyString(), anyString()))
                .thenReturn("{\"reference\" : \"" + reference + "\", \"version\":" + version + ",\"stat\":\"" + serializedStatusName + "\"}");

        Context mock = mock(Context.class);
        when(mock.getString(anyInt())).thenReturn(RandomStringUtils.randomAlphabetic(10));
        when(mock.getSharedPreferences(DefaultRulesetPreferencesImpl.PREF_BUFFER_PACKAGE_NAME, Context.MODE_PRIVATE)).thenReturn(reader);

        return mock;
    }

    private Context context() {

        SharedPreferences reader = mock(SharedPreferences.class);
        when(reader.getString(anyString(), anyString()))
                .thenReturn(DefaultRulesetPreferencesImpl.DEFAULT_VALUE);

        Context mock = mock(Context.class);
        when(mock.getString(anyInt())).thenReturn(RandomStringUtils.randomAlphabetic(10));
        when(mock.getSharedPreferences(DefaultRulesetPreferencesImpl.PREF_BUFFER_PACKAGE_NAME, Context.MODE_PRIVATE)).thenReturn(reader);

        return mock;
    }
}