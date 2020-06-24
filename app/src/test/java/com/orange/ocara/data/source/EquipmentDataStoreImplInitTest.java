package com.orange.ocara.data.source;

import com.orange.ocara.data.net.model.Equipment;
import com.orange.ocara.data.net.model.Explanation;
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
 * see {@link EquipmentDataStoreImpl#init()}
 */
public class EquipmentDataStoreImplInitTest {

    private EquipmentDataStoreImpl subject;

    private RulesetSource.RulesetCache rulesetCache;

    private EquipmentSource.EquipmentCache equipmentCache;

    private ImageSource.ImageCache imageCache;

    @Before
    public void setUp() {
        rulesetCache = mock(RulesetSource.RulesetCache.class);
        equipmentCache = mock(EquipmentSource.EquipmentCache.class);
        imageCache = mock(ImageSource.ImageCache.class);

        subject = new EquipmentDataStoreImpl(rulesetCache, equipmentCache, imageCache);
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
        verifyZeroInteractions(equipmentCache, imageCache);
    }

    @Test
    public void shouldCreateNoImageWhenRulesetHasDemoRulesetAndDemoRulesetHasNoExplanation() {

        // given
        when(rulesetCache.checkDemoRulesetExists()).thenReturn(true);

        Long rulesetId = TestUtils.longNb();
        RulesetEntity demo = mock(RulesetEntity.class);
        ReflectionTestUtils.setField(demo, "mId", rulesetId);
        when(rulesetCache.findDemoRuleset()).thenReturn(demo);

        when(equipmentCache.findAllByRulesetId(rulesetId)).thenReturn(Collections.emptyList());

        // when
        subject.init();

        // then
        verify(rulesetCache).checkDemoRulesetExists();
        verify(rulesetCache).findDemoRuleset();
        verify(equipmentCache).findAllByRulesetId(rulesetId);
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

        List<Equipment> equipments = ListUtils.newArrayList(mock(Equipment.class));
        when(equipmentCache.findAllByRulesetId(rulesetId)).thenReturn(equipments);

        // when
        subject.init();

        // then
        verify(rulesetCache).checkDemoRulesetExists();
        verify(rulesetCache).findDemoRuleset();
        verify(equipmentCache).findAllByRulesetId(rulesetId);
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
        Equipment equipment = mock(Equipment.class);
        when(equipment.getIcon()).thenReturn(expectedIconName);
        List<Equipment> equipments = ListUtils.newArrayList(equipment);
        when(equipmentCache.findAllByRulesetId(rulesetId)).thenReturn(equipments);

        // when
        subject.init();

        // then
        verify(rulesetCache).checkDemoRulesetExists();
        verify(rulesetCache).findDemoRuleset();
        verify(equipmentCache).findAllByRulesetId(rulesetId);
        verify(imageCache).createNewFile(expectedIconName, "objects");
        verifyNoMoreInteractions(rulesetCache);
        verifyNoMoreInteractions(imageCache);
    }
}