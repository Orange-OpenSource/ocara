package com.orange.ocara.data.cache.db;

import android.content.Context;
import android.os.Build;

import com.activeandroid.ActiveAndroid;
import com.orange.ocara.TestOcaraApplication;
import com.orange.ocara.data.net.model.RulesetWs;
import com.orange.ocara.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

/** see {@link DemoRulesetDao#get()} */
@RunWith(RobolectricTestRunner.class)
@Config(
        application = TestOcaraApplication.class,
        sdk = { Build.VERSION_CODES.KITKAT })
public class DemoRulesetDaoReadIT {

    private DemoRulesetDao subject;

    @Before
    public void setUp() {
        Context context = TestUtils.instrumentationContext();
        ActiveAndroid.initialize(context);

        subject = givenSubjectWithValidFilePath(context);
    }

    @After
    public void tearDown() {

        ActiveAndroid.dispose();
    }

    @Test
    public void shouldRetrieveOneRulesetWhenSubjectFilePathIsValid() {

        // given
        String expectedReference = "DEMO";
        Integer expectedVersion = 1;

        // when
        RulesetWs result = subject.get();

        // then
        assertThat(result).isNotNull();
        assertThat(result.getReference()).isEqualTo(expectedReference);
        assertThat(result.getVersion()).isEqualTo(expectedVersion);
        assertThat(result.getQuestions()).hasSize(48);
        assertThat(result.getRules()).hasSize(82);
        assertThat(result.getQuestionnaires()).hasSize(17);
    }

    private DemoRulesetDaoImpl givenSubjectWithValidFilePath(Context context) {

        return new DemoRulesetDaoImpl(context);
    }
}