package com.orange.ocara.data.cache.db;

import android.content.Context;
import android.os.Build;

import com.orange.ocara.BuildConfig;
import com.orange.ocara.TestOcaraApplication;
import com.orange.ocara.utils.TestUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * see {@link DemoRulesetDao#exists()}
 */
@RunWith(RobolectricTestRunner.class)
@Config(
        application = TestOcaraApplication.class,
        sdk = Build.VERSION_CODES.KITKAT)
public class DemoRulesetDaoExistsIT {

    private DemoRulesetDao subject;

    @Test
    public void shouldReturnTrueWhenSubjectFilePathIsValid() {

        // given
        subject = givenSubjectWithValidFilePath();

        // when
        boolean result = subject.exists();

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenSubjectFilePathIsNotValid() {

        // given
        subject = givenSubjectWithWrongFilePath();

        // when
        boolean result = subject.exists();

        // then
        assertThat(result).isFalse();
    }

    private DemoRulesetDaoImpl givenSubjectWithValidFilePath() {
        return new DemoRulesetDaoImpl(
                TestUtils.instrumentationContext());
    }

    private DemoRulesetDaoImpl givenSubjectWithWrongFilePath() {
        return new DemoRulesetDaoImpl(
                Mockito.mock(Context.class));
    }
}