package com.orange.ocara.data.source;

import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.model.VersionableModel;
import com.orange.ocara.data.net.model.EquipmentWs;
import com.orange.ocara.data.net.model.IllustrationWs;
import com.orange.ocara.data.net.model.ProfileTypeWs;
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.data.net.model.RulesetWs;
import com.orange.ocara.data.source.RulesetSource.RulesetRemote;
import com.orange.ocara.tools.ListUtils;
import com.orange.ocara.utils.TestUtils;

import org.junit.Test;

import java.io.InputStream;

import static com.orange.ocara.data.source.RulesetSource.RulesetCache;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * see {@link RulesetDataStoreImpl#upgrade(VersionableModel)}
 */
public class RulesetDataStoreImplUpgradeTest {

    @Test
    public void shouldRetrieveOnlyRulesetFromRemoteAndSaveItInCache() {

        // given
        String inputRef = TestUtils.str();
        Integer inputVersion = TestUtils.intNb();
        VersionableModel inputRuleset = mock(VersionableModel.class);
        when(inputRuleset.getReference()).thenReturn(inputRef);
        when(inputRuleset.getVersion()).thenReturn(inputVersion + "");

        RulesetWs mock = rulesetWs();
        RulesetRemote rulesetRemote = mock(RulesetRemote.class);
        when(rulesetRemote.findOne(inputRef, inputVersion)).thenReturn(mock);

        RulesetEntity expectedEntity = mock(RulesetEntity.class);
        RulesetCache rulesetCache = mock(RulesetCache.class);
        when(rulesetCache.save(mock)).thenReturn(expectedEntity);

        ImageSource.ImageRemote imageRemote = mock(ImageSource.ImageRemote.class);

        ImageSource.ImageCache imageCache = mock(ImageSource.ImageCache.class);

        RulesetDataStoreImpl subject = new RulesetDataStoreImpl(rulesetCache, rulesetRemote, imageCache, imageRemote);


        // when
        RulesetModel result = subject.upgrade(inputRuleset);

        // then
        assertThat(result.getReference()).isEqualTo(expectedEntity.getReference());
        assertThat(result.getVersion()).isEqualTo(expectedEntity.getVersion());
        verify(rulesetRemote).findOne(inputRef, inputVersion);
        verify(rulesetCache).save(mock);
        verifyNoMoreInteractions(rulesetCache, rulesetRemote, imageCache, imageRemote);
    }

    @Test
    public void shouldRetrieveRulesetAndItsIllustrableElementsFromRemoteAndSaveThemInCacheWhenTheyHaveIcons() {

        // given
        String inputRef = TestUtils.str();
        Integer inputVersion = TestUtils.intNb();
        VersionableModel inputRuleset = mock(VersionableModel.class);
        when(inputRuleset.getReference()).thenReturn(inputRef);
        when(inputRuleset.getVersion()).thenReturn(inputVersion + "");

        String equipmentIcon = TestUtils.str();
        RulesetWs mock = rulesetWsWithOneIllustrableEquipment(equipmentIcon);

        RulesetRemote rulesetRemote = mock(RulesetRemote.class);
        when(rulesetRemote.findOne(inputRef, inputVersion)).thenReturn(mock);

        RulesetEntity expectedEntity = mock(RulesetEntity.class);
        RulesetCache rulesetCache = mock(RulesetCache.class);
        when(rulesetCache.save(mock)).thenReturn(expectedEntity);

        ImageSource.ImageRemote imageRemote = mock(ImageSource.ImageRemote.class);
        InputStream equipmentIS = mock(InputStream.class);
        when(imageRemote.get(equipmentIcon)).thenReturn(equipmentIS);

        ImageSource.ImageCache imageCache = mock(ImageSource.ImageCache.class);

        RulesetDataStoreImpl subject = new RulesetDataStoreImpl(rulesetCache, rulesetRemote, imageCache, imageRemote);


        // when
        RulesetModel result = subject.upgrade(inputRuleset);

        // then
        assertThat(result.getReference()).isEqualTo(expectedEntity.getReference());
        assertThat(result.getVersion()).isEqualTo(expectedEntity.getVersion());
        verify(rulesetRemote).findOne(inputRef, inputVersion);
        verify(rulesetCache).save(mock);
        verify(imageRemote).get(equipmentIcon);
        verify(imageCache).fileExists(equipmentIcon);
        verify(imageCache).write(equipmentIS, equipmentIcon);
        verifyNoMoreInteractions(rulesetCache, rulesetRemote, imageCache, imageRemote);
    }

    private static RulesetWs rulesetWs() {
        return mock(RulesetWs.class);
    }

    private static RulesetWs rulesetWsWithOneIllustrableEquipment(String iconName) {
        RulesetWs mock = mock(RulesetWs.class);
        EquipmentWs equipment = mock(EquipmentWs.class);
        when(equipment.getIcon()).thenReturn(iconName);
        when(mock.getEquipments()).thenReturn(ListUtils.newArrayList(equipment));
        ProfileTypeWs profileType = mock(ProfileTypeWs.class);
        when(mock.getProfileTypes()).thenReturn(ListUtils.newArrayList(profileType));
        IllustrationWs illustration = mock(IllustrationWs.class);
        when(mock.getIllustrations()).thenReturn(ListUtils.newArrayList(illustration));
        return mock;
    }
}