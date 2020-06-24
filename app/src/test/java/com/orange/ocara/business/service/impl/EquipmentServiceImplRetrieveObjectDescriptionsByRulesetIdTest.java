package com.orange.ocara.business.service.impl;

import com.orange.ocara.business.repository.EquipmentRepository;
import com.orange.ocara.data.net.model.Equipment;
import com.orange.ocara.utils.ReflectionTestUtils;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @see EquipmentServiceImpl#retrieveObjectDescriptionsByRulesetId(Long)
 */
public class EquipmentServiceImplRetrieveObjectDescriptionsByRulesetIdTest {

    private EquipmentServiceImpl subject;

    private EquipmentRepository repository;

    @Before
    public void setUp() {

        repository = mock(EquipmentRepository.class);

        subject = new EquipmentServiceImpl();
        ReflectionTestUtils.setField(subject, "objectDescriptionRepository", repository);
    }

    @Test
    public void shouldDelegateRetrievalToRepository() {
        // Given
        Long expectedId = nextLong();

        // When
        List<Equipment> result = subject.retrieveObjectDescriptionsByRulesetId(expectedId);

        // Then
        verify(repository).findAll(expectedId);
    }
}