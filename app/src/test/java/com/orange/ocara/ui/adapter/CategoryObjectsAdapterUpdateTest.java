package com.orange.ocara.ui.adapter;

import android.content.Context;

import com.orange.ocara.data.net.model.EquipmentEntity;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * see {@link CategoryObjectsAdapter}
 * also see CategoryObjectsAdapter's inner private static class named ObjectDescriptionComparator
 */
public class CategoryObjectsAdapterUpdateTest {

    private CategoryObjectsAdapter subject;

    @Before
    public void setUp() {
        subject = new CategoryObjectsAdapter(mock(Context.class));
    }

    @Test
    public void shouldOrderContentWhenUpdated() {
        // Given
        EquipmentEntity o1 = mock(EquipmentEntity.class);
        when(o1.getName()).thenReturn("3");
        EquipmentEntity o2 = mock(EquipmentEntity.class);
        when(o2.getName()).thenReturn("1");
        EquipmentEntity o3 = mock(EquipmentEntity.class);
        when(o3.getName()).thenReturn("2");
        List<EquipmentEntity> descriptions = Lists.newArrayList(o1, o2, o3);

        // When
        subject.update(descriptions);

        // Then
        List<EquipmentEntity> result = subject.getObjects();
        assertThat(result).isNotNull();
        assertThat(result).hasSize(descriptions.size());
        assertThat(result.get(0)).isEqualTo(o2);
        assertThat(result.get(1)).isEqualTo(o3);
        assertThat(result.get(2)).isEqualTo(o1);
    }

    @Test
    public void shouldOrderContentWithNullItemsAndEqualItemsWhenUpdated() {
        // Given
        String expectedName = "2";
        EquipmentEntity o1 = mock(EquipmentEntity.class);
        when(o1.getName()).thenReturn("3");
        EquipmentEntity o2 = mock(EquipmentEntity.class);
        when(o2.getName()).thenReturn(expectedName);
        EquipmentEntity o3 = mock(EquipmentEntity.class);
        when(o3.getName()).thenReturn(expectedName);
        List<EquipmentEntity> descriptions = Lists.newArrayList(o1, null, o2, o3);

        // When
        subject.update(descriptions);

        // Then
        List<EquipmentEntity> result = subject.getObjects();
        assertThat(result).isNotNull();
        assertThat(result).hasSize(descriptions.size());
        assertThat(result.get(0)).isEqualTo(null);
        assertThat(result.get(1).getName()).isEqualTo(expectedName);
        assertThat(result.get(2).getName()).isEqualTo(expectedName);
        assertThat(result.get(3)).isEqualTo(o1);
    }
}
