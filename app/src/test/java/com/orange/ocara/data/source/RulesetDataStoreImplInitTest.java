package com.orange.ocara.data.source;

import com.orange.ocara.data.source.ImageSource.ImageCache;

import org.junit.Test;

import static com.orange.ocara.data.source.ImageSource.ImageRemote;
import static com.orange.ocara.data.source.RulesetSource.RulesetCache;
import static com.orange.ocara.data.source.RulesetSource.RulesetRemote;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/** see {@link RulesetDataStoreImpl#init()} */
public class RulesetDataStoreImplInitTest {

    @Test
    public void shouldDoNothingWhenRulesetInitReturnsNull() {

        // given
        RulesetRemote rulesetRemote = mock(RulesetRemote.class);

        RulesetCache rulesetCache = mock(RulesetCache.class);

        ImageRemote imageRemote = mock(ImageRemote.class);

        ImageCache imageCache = mock(ImageCache.class);

        RulesetDataStoreImpl subject = new RulesetDataStoreImpl(rulesetCache, rulesetRemote, imageCache, imageRemote);

        // when
        subject.init();

        // then
        verify(rulesetCache).init();
        verifyNoMoreInteractions(rulesetCache, rulesetRemote, imageCache, imageRemote);
    }

}