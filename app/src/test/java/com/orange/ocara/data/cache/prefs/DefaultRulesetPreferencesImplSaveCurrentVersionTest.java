package com.orange.ocara.data.cache.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.orange.ocara.business.model.RuleSetStat;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.utils.TestUtils;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** see {@link DefaultRulesetPreferencesImpl#saveRuleset(RulesetModel)} */
public class DefaultRulesetPreferencesImplSaveCurrentVersionTest {

    private DefaultRulesetPreferencesImpl subject;

    private SharedPreferences.Editor editor;

    private String key = RandomStringUtils.randomAlphabetic(10);

    @Before
    public void setUp() {
        SharedPreferences sharedPrefs = mock(SharedPreferences.class);
        Context context = mock(Context.class);
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPrefs);
        when(context.getString(anyInt())).thenReturn(key);

        subject = new DefaultRulesetPreferencesImpl(context);

        editor = mock(SharedPreferences.Editor.class);
        when(editor.putString(anyString(), anyString())).thenReturn(editor);
        when(sharedPrefs.edit()).thenReturn(editor);

    }

    @Test
    public void shouldDelegateSavingToEditor() {
        // Given
        String expectedReference = TestUtils.str();
        int expectedVersion = TestUtils.digit();
        RuleSetStat expectedStatus = TestUtils.randomRulesetStat();

        RulesetModel input = RulesetModel.builder().reference(expectedReference).version(expectedVersion+"").stat(expectedStatus).build();

        // When
        subject.saveRuleset(input);

        // Then
        verify(editor).putString("default_ruleset", "{\"reference\":\"" + expectedReference + "\",\"version\":\"" + expectedVersion + "\",\"stat\":\""+expectedStatus.toString().toLowerCase()+"\"}");
    }
}