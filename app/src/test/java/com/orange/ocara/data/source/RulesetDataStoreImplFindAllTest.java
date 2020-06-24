package com.orange.ocara.data.source;

import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.data.net.model.RulesetLightWs;
import com.orange.ocara.tools.ListUtils;
import com.orange.ocara.utils.TestUtils;

import org.assertj.core.api.Condition;
import org.junit.Test;

import java.util.List;

import static com.orange.ocara.utils.TestUtils.str;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** see {@link RulesetDataStoreImpl#findAll(boolean)} */
public class RulesetDataStoreImplFindAllTest {

    @Test
    public void shouldReturnEmptyResultWhenThereIsNoListInCacheNorInRemote() {

        // given
        RulesetSource.RulesetRemote rulesetRemote = mock(RulesetSource.RulesetRemote.class);

        RulesetSource.RulesetCache rulesetCache = mock(RulesetSource.RulesetCache.class);

        ImageSource.ImageRemote imageRemote = mock(ImageSource.ImageRemote.class);

        ImageSource.ImageCache imageCache = mock(ImageSource.ImageCache.class);

        RulesetDataStoreImpl subject = new RulesetDataStoreImpl(rulesetCache, rulesetRemote, imageCache, imageRemote);
        // when
        List<RulesetModel> result = subject.findAll(false);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnListWith2ItemsWhenCacheHasOneRulesetAndRemoteHasOneRulesetAndBothHaveDifferentReference() {

        // given
        String remoteRulesetReference = str();
        Integer remoteRulesetVersion = 2;

        RulesetLightWs remoteRuleset = mockRulesetLightWs(remoteRulesetReference, remoteRulesetVersion);
        RulesetSource.RulesetRemote rulesetRemote = mock(RulesetSource.RulesetRemote.class);
        when(rulesetRemote.findAll()).thenReturn(ListUtils.newArrayList(remoteRuleset));

        String cacheRulesetReference = str();
        Integer cacheRulesetVersion = 1;
        RulesetModel cacheRuleset = mockRulesetEntity(cacheRulesetReference, cacheRulesetVersion);
        RulesetSource.RulesetCache rulesetCache = mock(RulesetSource.RulesetCache.class);
        when(rulesetCache.findAll()).thenReturn(ListUtils.newArrayList(cacheRuleset));

        ImageSource.ImageRemote imageRemote = mock(ImageSource.ImageRemote.class);

        ImageSource.ImageCache imageCache = mock(ImageSource.ImageCache.class);

        RulesetDataStoreImpl subject = new RulesetDataStoreImpl(rulesetCache, rulesetRemote, imageCache, imageRemote);

        // when
        List<RulesetModel> result = subject.findAll(false);

        // then
        assertThat(result)
                .isNotEmpty()
                .hasSize(2)
                .haveExactly(1, new Condition<>(model -> model.getReference().equals(cacheRulesetReference) && model.getVersion().equals("" + cacheRulesetVersion), "model is from cache source"))
                .haveExactly(1, new Condition<>(model -> model.getReference().equals(remoteRulesetReference) && model.getVersion().equals("" + remoteRulesetVersion), "model is from remote source"));
    }

    @Test
    public void shouldReturnListWith2ItemWhenCacheHasOneRulesetAndRemoteHasOneRulesetAndBothHaveSameReferenceAndDifferentVersion() {

        // given
        String rulesetReference = str();
        Integer remoteRulesetVersion = 2;
        RulesetLightWs remoteRuleset = mockRulesetLightWs(rulesetReference, remoteRulesetVersion);
        RulesetSource.RulesetRemote rulesetRemote = mock(RulesetSource.RulesetRemote.class);
        when(rulesetRemote.findAll()).thenReturn(ListUtils.newArrayList(remoteRuleset));

        Integer cacheRulesetVersion = 1;
        RulesetModel cacheRuleset = mockRulesetEntity(rulesetReference, cacheRulesetVersion);
        RulesetSource.RulesetCache rulesetCache = mock(RulesetSource.RulesetCache.class);
        when(rulesetCache.findAll()).thenReturn(ListUtils.newArrayList(cacheRuleset));

        ImageSource.ImageRemote imageRemote = mock(ImageSource.ImageRemote.class);

        ImageSource.ImageCache imageCache = mock(ImageSource.ImageCache.class);

        RulesetDataStoreImpl subject = new RulesetDataStoreImpl(rulesetCache, rulesetRemote, imageCache, imageRemote);

        // when
        List<RulesetModel> result = subject.findAll(false);

        // then
        assertThat(result)
                .isNotEmpty()
                .hasSize(2);
    }

    @Test
    public void shouldReturnListWith1ItemWhenCacheHasOneRulesetAndRemoteHasOneRulesetAndBothHaveSameReferenceAndSameVersion() {

        // given
        String rulesetReference = str();
        Integer remoteRulesetVersion = 1;
        RulesetLightWs remoteRuleset = mockRulesetLightWs(rulesetReference, remoteRulesetVersion);
        RulesetSource.RulesetRemote rulesetRemote = mock(RulesetSource.RulesetRemote.class);
        when(rulesetRemote.findAll()).thenReturn(ListUtils.newArrayList(remoteRuleset));

        Integer cacheRulesetVersion = 1;
        RulesetModel cacheRuleset = mockRulesetEntity(rulesetReference, cacheRulesetVersion);
        RulesetSource.RulesetCache rulesetCache = mock(RulesetSource.RulesetCache.class);
        when(rulesetCache.findAll()).thenReturn(ListUtils.newArrayList(cacheRuleset));

        ImageSource.ImageRemote imageRemote = mock(ImageSource.ImageRemote.class);

        ImageSource.ImageCache imageCache = mock(ImageSource.ImageCache.class);

        RulesetDataStoreImpl subject = new RulesetDataStoreImpl(rulesetCache, rulesetRemote, imageCache, imageRemote);

        // when
        List<RulesetModel> result = subject.findAll(false);

        // then
        assertThat(result)
                .isNotEmpty()
                .hasSize(1);
    }

    private RulesetModel mockRulesetEntity(String ref, Integer version) {
        RulesetModel mock = mock(RulesetModel.class);
        when(mock.getReference()).thenReturn(ref);
        when(mock.getVersion()).thenReturn(version + "");
        when(mock.getType()).thenReturn(TestUtils.str());
        return mock;
    }

    private RulesetLightWs mockRulesetLightWs(String ref, Integer version) {
        RulesetLightWs mock = mock(RulesetLightWs.class);
        when(mock.getReference()).thenReturn(ref);
        when(mock.getVersion()).thenReturn(version + "");
        when(mock.getType()).thenReturn(TestUtils.str());
        return mock;
    }
}