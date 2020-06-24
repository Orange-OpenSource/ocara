package com.orange.ocara.data.source;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/** see {@link ProfileTypeDataStoreImpl#init()} */
public class ProfileTypeDataStoreImplInitTest {

    private ProfileTypeDataStoreImpl subject;

    private RulesetSource.RulesetCache rulesetCache;

    private ProfileTypeSource.ProfileTypeCache profileTypeCache;

    private ImageSource.ImageCache imageCache;

    @Before
    public void setUp() {
        rulesetCache = mock(RulesetSource.RulesetCache.class);
        profileTypeCache = mock(ProfileTypeSource.ProfileTypeCache.class);
        imageCache = mock(ImageSource.ImageCache.class);

        subject = new ProfileTypeDataStoreImpl(rulesetCache, profileTypeCache, imageCache);
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
        verifyZeroInteractions(profileTypeCache, imageCache);
    }
}