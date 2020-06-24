package com.orange.ocara.business.service.impl;

import com.orange.ocara.business.service.QuestionService;
import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.data.net.model.EquipmentWithReference;
import com.orange.ocara.data.net.model.QuestionEntity;
import com.orange.ocara.data.net.model.RulesetByReferenceAndVersion;
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.utils.ReflectionTestUtils;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @see QuestionService#retrieveAllQuestionsByRulesetAndEquipment(com.orange.ocara.data.net.model.RulesetEntity, EquipmentWithReference)
 */
public class QuestionServiceImplRetrieveAllQuestionsByRulesetAndEquipmentTest {

    private QuestionService subject = new QuestionServiceImpl();

    private RuleSetService ruleSetService;

    @Before
    public void setUp() {
        ruleSetService = mock(RuleSetService.class);
        ReflectionTestUtils.setField(subject, "ruleSetService", ruleSetService);
    }

    @Test
    public void shouldDelegateRetrievalToOtherService() {
        // Given
        RulesetEntity ruleset = mock(RulesetEntity.class);
        String reference = randomAlphanumeric(5);
        Integer version = nextInt();
        when(ruleset.getReference()).thenReturn(reference);
        when(ruleset.getVersion()).thenReturn(version.toString());

        EquipmentWithReference equipment = mock(EquipmentWithReference.class);
        String equipmentRef = randomAlphanumeric(5);
        when(equipment.getReference()).thenReturn(equipmentRef);

        // When
        List<QuestionEntity> result = subject.retrieveAllQuestionsByRulesetAndEquipment(ruleset, equipment);

        // Then
        verify(ruleSetService).getQuestionsFromObjectRef(reference, version, equipmentRef, equipmentRef);
    }
}