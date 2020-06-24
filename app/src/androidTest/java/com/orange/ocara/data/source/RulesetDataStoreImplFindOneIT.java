package com.orange.ocara.data.source;

import com.activeandroid.ActiveAndroid;
import com.orange.ocara.TestOcaraApplication;
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.data.net.model.RulesetWs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.orange.ocara.TestUtils.instrumentationContext;
import static com.orange.ocara.TestUtils.intNb;
import static com.orange.ocara.TestUtils.str;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/** {@link RulesetDataStoreImpl#findOne(String, Integer)} */
@RunWith(RobolectricTestRunner.class)
@Config(application = TestOcaraApplication.class, sdk = 21)
public class RulesetDataStoreImplFindOneIT {


    @Before
    public void setUp() {
        ActiveAndroid.initialize(instrumentationContext());
    }

    @After
    public void tearDown() {
        ActiveAndroid.dispose();
    }

    @Test
    public void shouldReturnRemoteRulesetWhenItDoesNotExistInCache() {

        // given
        String inputRef = str();
        Integer inputVersion = intNb();

        RulesetWs mock = mock(RulesetWs.class);
        RulesetSource.RulesetRemote rulesetRemote = mock(RulesetSource.RulesetRemote.class);
        when(rulesetRemote.findOne(inputRef, inputVersion)).thenReturn(mock);

        RulesetSource.RulesetCache rulesetCache = mock(RulesetSource.RulesetCache.class);
        when(rulesetCache.exists(inputRef, inputVersion)).thenReturn(false);

        ImageSource.ImageRemote imageRemote = mock(ImageSource.ImageRemote.class);

        ImageSource.ImageCache imageCache = mock(ImageSource.ImageCache.class);

        RulesetDataStoreImpl subject = new RulesetDataStoreImpl(rulesetCache, rulesetRemote, imageCache, imageRemote);

        // when
        subject.findOne(inputRef, inputVersion);

        // then
        verify(rulesetCache).exists(inputRef, inputVersion);
        verify(rulesetRemote).findOne(inputRef, inputVersion);
        verifyNoMoreInteractions(rulesetCache, rulesetRemote);
    }

    @Test
    public void shouldReturnCacheRulesetWhenItExistsInCache() {

        // given
        String inputRef = str();
        Integer inputVersion = intNb();

        RulesetEntity mock = mock(RulesetEntity.class);
        RulesetSource.RulesetRemote rulesetRemote = mock(RulesetSource.RulesetRemote.class);

        RulesetSource.RulesetCache rulesetCache = mock(RulesetSource.RulesetCache.class);
        when(rulesetCache.exists(inputRef, inputVersion)).thenReturn(true);
        when(rulesetCache.findOne(inputRef, inputVersion)).thenReturn(mock);

        ImageSource.ImageRemote imageRemote = mock(ImageSource.ImageRemote.class);

        ImageSource.ImageCache imageCache = mock(ImageSource.ImageCache.class);

        RulesetDataStoreImpl subject = new RulesetDataStoreImpl(rulesetCache, rulesetRemote, imageCache, imageRemote);

        // when
        subject.findOne(inputRef, inputVersion);

        // then
        verify(rulesetCache).exists(inputRef, inputVersion);
        verify(rulesetCache).findOne(inputRef, inputVersion);
        verifyNoMoreInteractions(rulesetCache, rulesetRemote);
    }
}