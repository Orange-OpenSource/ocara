package com.orange.ocara.data.source;

import com.orange.ocara.data.net.model.Explanation;
import com.orange.ocara.data.net.model.Ruleset;
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.tools.ListUtils;
import com.orange.ocara.utils.ReflectionTestUtils;
import com.orange.ocara.utils.TestUtils;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * see {@link IllustrationDataStoreImpl#init()}
 */
public class IllustrationDataStoreImplInitTest {

    private IllustrationDataStoreImpl subject;
    
    private RulesetSource.RulesetCache rulesetCache;

    private IllustrationSource.IllustrationCache illustrationCache;

    private ImageSource.ImageCache imageCache;

    @Before
    public void setUp() {
        rulesetCache = mock(RulesetSource.RulesetCache.class);
        illustrationCache = mock(IllustrationSource.IllustrationCache.class);
        imageCache = mock(ImageSource.ImageCache.class);

        subject = new IllustrationDataStoreImpl(rulesetCache, illustrationCache, imageCache);
    }

    @Test
    public void shouldDoNothingWhenRulesetCacheAlreadyHasNoDemoRuleset() {

        // given
        when(rulesetCache.checkDemoRulesetExists()).thenReturn(false);

        // when
        subject.init();

        // then
        verify(rulesetCache).checkDemoRulesetExists();
        verifyNoMoreInteractions(rulesetCache);
        verifyZeroInteractions(illustrationCache, imageCache);
    }


    @Test
    public void shouldCreateNoImageWhenRulesetHasDemoRulesetAndDemoRulesetHasNoExplanation() {

        // given
        when(rulesetCache.checkDemoRulesetExists()).thenReturn(true);

        Long rulesetId = TestUtils.longNb();
        RulesetEntity demo = mock(RulesetEntity.class);
        ReflectionTestUtils.setField(demo, "mId", rulesetId);
        when(rulesetCache.findDemoRuleset()).thenReturn(demo);

        when(illustrationCache.findAllByRulesetId(rulesetId)).thenReturn(Collections.emptyList());

        // when
        subject.init();

        // then
        verify(rulesetCache).checkDemoRulesetExists();
        verify(rulesetCache).findDemoRuleset();
        verify(illustrationCache).findAllByRulesetId(rulesetId);
        verifyNoMoreInteractions(rulesetCache);
        verifyZeroInteractions(imageCache);
    }

    @Test
    public void shouldCreateNoImageWhenRulesetHasDemoRulesetAndExplanationsHaveNoIcons() {

        // given
        when(rulesetCache.checkDemoRulesetExists()).thenReturn(true);

        Long rulesetId = TestUtils.longNb();
        RulesetEntity demo = mock(RulesetEntity.class);
        ReflectionTestUtils.setField(demo, "mId", rulesetId);
        when(rulesetCache.findDemoRuleset()).thenReturn(demo);

        List<Explanation> explanations = ListUtils.newArrayList(mock(Explanation.class));
        when(illustrationCache.findAllByRulesetId(rulesetId)).thenReturn(explanations);

        // when
        subject.init();

        // then
        verify(rulesetCache).checkDemoRulesetExists();
        verify(rulesetCache).findDemoRuleset();
        verify(illustrationCache).findAllByRulesetId(rulesetId);
        verifyNoMoreInteractions(rulesetCache);
        verifyZeroInteractions(imageCache);
    }

    @Test
    public void shouldCreateImageWhenRulesetHasDemoRulesetAndExplanationHasIcon() {

        // given
        when(rulesetCache.checkDemoRulesetExists()).thenReturn(true);

        Long rulesetId = TestUtils.longNb();
        RulesetEntity demo = mock(RulesetEntity.class);
        ReflectionTestUtils.setField(demo, "mId", rulesetId);
        when(rulesetCache.findDemoRuleset()).thenReturn(demo);

        String expectedIconName = TestUtils.str();
        Explanation explanation = mock(Explanation.class);
        when(explanation.getIcon()).thenReturn(expectedIconName);
        List<Explanation> explanations = ListUtils.newArrayList(explanation);
        when(illustrationCache.findAllByRulesetId(rulesetId)).thenReturn(explanations);

        // when
        subject.init();

        // then
        verify(rulesetCache).checkDemoRulesetExists();
        verify(rulesetCache).findDemoRuleset();
        verify(illustrationCache).findAllByRulesetId(rulesetId);
        verify(imageCache).createNewFile(expectedIconName, "ruleset");
        verifyNoMoreInteractions(rulesetCache);
        verifyNoMoreInteractions(imageCache);
    }
}