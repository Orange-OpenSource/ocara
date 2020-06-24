package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.service.EquipmentService;
import com.orange.ocara.data.net.model.Equipment;
import com.orange.ocara.ui.contract.ListEquipmentsContract;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static com.orange.ocara.tools.ListUtils.emptyList;
import static com.orange.ocara.tools.ListUtils.newArrayList;
import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ListEquipmentsPresenterLoadAllEquipmentsByRulesetIdTest {

    private ListEquipmentsPresenter subject;

    private ListEquipmentsContract.ListEquipmentsView view;

    private EquipmentService equipmentService;

    @Before
    public void setUp() {
        view = mock(ListEquipmentsContract.ListEquipmentsView.class);

        equipmentService = mock(EquipmentService.class);

        subject = new ListEquipmentsPresenter(equipmentService, view);

    }

    @Test
    public void shouldShowNoResultWhenRepositoryRetrievesNoEquipment() {
        // Given
        Long expectedId = nextLong();
        when(equipmentService.retrieveObjectDescriptionsByRulesetId(Mockito.anyLong())).thenReturn(emptyList());

        // When
        subject.loadAllEquipmentsByRulesetId(expectedId);

        // Then
        verify(equipmentService).retrieveObjectDescriptionsByRulesetId(expectedId);
        verify(view).showNoEquipments();
    }

    @Test
    public void shouldShowResultWhenRepositoryRetrievesSeveralEquipments() {
        // Given
        Long expectedId = nextLong();
        List<Equipment> expectedEquipments = newArrayList(mock(Equipment.class));
        when(equipmentService.retrieveObjectDescriptionsByRulesetId(Mockito.anyLong())).thenReturn(expectedEquipments);

        // When
        subject.loadAllEquipmentsByRulesetId(expectedId);

        // Then
        verify(equipmentService).retrieveObjectDescriptionsByRulesetId(expectedId);
        verify(view).showEquipments(expectedEquipments);
    }
}