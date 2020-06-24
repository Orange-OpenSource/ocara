package com.orange.ocara.data.source;

import com.orange.ocara.utils.TestUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

public class IllustrationDataStoreImplFindAllByRuleIdTest {

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
    public void shouldDelegateToCache() {

        // given
        Long inputId = TestUtils.longNb();

        // when
        subject.findAllByRuleId(inputId);

        // then
        verify(illustrationCache).findAllByRuleId(inputId);
        verifyNoMoreInteractions(illustrationCache);
        verifyZeroInteractions(rulesetCache, imageCache);

    }
}