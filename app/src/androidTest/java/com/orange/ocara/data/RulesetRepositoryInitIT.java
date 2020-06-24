package com.orange.ocara.data;

import android.content.Context;
import android.os.Build;

import com.orange.ocara.TestUtils;
import com.orange.ocara.business.repository.RulesetRepository;

import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

/** see {@link RulesetRepository#init()}*/
@RunWith(RobolectricTestRunner.class)
@Config(
        sdk = Build.VERSION_CODES.KITKAT)
public class RulesetRepositoryInitIT {

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
        assertThat(subject.findAll()).hasSize(1);
        assertThat(subject.exists("DEMO")).isTrue();
        assertThat(subject.findOne("DEMO", 1))
                .isNotNull()
                .has(new Condition<>(ruleset -> ruleset.getReference() != null, "reference shall not be null"))
                .has(new Condition<>(ruleset -> !"".equals(ruleset.getReference()), "reference shall not be empty"))
                .has(new Condition<>(ruleset -> "DEMO".equals(ruleset.getReference()), "reference shall equal 'DEMO'"))
                .has(new Condition<>(ruleset -> ruleset.getVersion() != null, "version shall not be null"))
                .has(new Condition<>(ruleset -> "1".equals(ruleset.getVersion()), "version shall equal 1"))
                .has(new Condition<>(ruleset -> ruleset.getAuthor() != null , "author shall not be null"))
                .has(new Condition<>(ruleset -> !"".equals(ruleset.getAuthor()) , "author shall not be empty"))
                .has(new Condition<>(ruleset -> "Gzan".equals(ruleset.getAuthor()), "author shall equal 'Gzan'"))
                .has(new Condition<>(ruleset -> ruleset.getRuleCategoryName() != null, "category shall not be null"))
                .has(new Condition<>(ruleset -> !"".equals(ruleset.getRuleCategoryName()), "category shall not be empty"))
                .has(new Condition<>(ruleset -> "Accessibilité".equals(ruleset.getRuleCategoryName()), "category shall equal 'Accessibilité'"))
                .has(new Condition<>(ruleset -> ruleset.getType() != null, "type shall not be null"))
                .has(new Condition<>(ruleset -> !"".equals(ruleset.getType()), "type shall not be empty"))
                .has(new Condition<>(ruleset -> "Quelques bases sur l'accessibilité des circulations".equals(ruleset.getType()), "category shall equal 'Quelques bases...'"));
    }
}
