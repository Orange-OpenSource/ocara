package com.orange.ocara.data;

import android.content.Context;
import android.os.Build;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.orange.ocara.TestUtils;
import com.orange.ocara.business.repository.RulesetRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

/** see {@link RulesetRepository#init()}*/
@RunWith(AndroidJUnit4.class)
@Config(
        sdk = Build.VERSION_CODES.KITKAT)
public class RulesetRepositoryInitFT {

    private RulesetRepository subject;

    @Before
    public void setUp() {

        Context context = TestUtils.instrumentationContext();

        subject = DataConfig_.getInstance_(context).rulesetRepository();
    }

    @Test
    public void shouldCreateDemoRulesetWhenInitSucceeds() {

        // given

        // when
        subject.init();

        // then
        assertThat(subject.exists("DEMO")).isTrue();
        assertThat(subject.findOne("DEMO", 1)).isNotNull();
    }
}
