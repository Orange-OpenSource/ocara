package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.service.QuestionService;
import com.orange.ocara.data.net.model.EquipmentWithReference;
import com.orange.ocara.data.net.model.QuestionEntity;
import com.orange.ocara.data.net.model.RulesetByReferenceAndVersion;
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.ui.contract.ListQuestionsContract;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static com.orange.ocara.tools.ListUtils.emptyList;
import static com.orange.ocara.tools.ListUtils.newArrayList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * see {@link ListQuestionsPresenter#loadAllQuestionsByEquipmentId(RulesetByReferenceAndVersion, EquipmentWithReference)}
 */
public class ListQuestionsPresenterLoadAllQuestionsByEquipmentIdTest {

    private ListQuestionsPresenter subject;

    private QuestionService service;

    private ListQuestionsContract.ListQuestionsView view;

    @Before
    public void setUp() {
        view = mock(ListQuestionsContract.ListQuestionsView.class);

        service = mock(QuestionService.class);

        subject = new ListQuestionsPresenter(service, view);
    }


    @Test
    public void shouldShowNoResultWhenRepositoryRetrievesNoEquipment() {
        // Given
        RulesetEntity ruleset = mock(RulesetEntity.class);
        EquipmentWithReference equipment = mock(EquipmentWithReference.class);
        when(service.retrieveAllQuestionsByRulesetAndEquipment(Mockito.any(), Mockito.any())).thenReturn(emptyList());

        // When
        subject.loadAllQuestionsByEquipmentId(ruleset, equipment);

        // Then
        verify(service).retrieveAllQuestionsByRulesetAndEquipment(ruleset, equipment);
        verify(view).showNoQuestions();
    }

    @Test
    public void shouldShowResultWhenRepositoryRetrievesSeveralEquipments() {
        // Given
        RulesetEntity ruleset = mock(RulesetEntity.class);
        EquipmentWithReference equipment = mock(EquipmentWithReference.class);

        List<QuestionEntity> expectedQuestions = newArrayList(mock(QuestionEntity.class));
        when(service.retrieveAllQuestionsByRulesetAndEquipment(Mockito.any(), Mockito.any())).thenReturn(expectedQuestions);

        // When
        subject.loadAllQuestionsByEquipmentId(ruleset, equipment);

        // Then
        verify(service).retrieveAllQuestionsByRulesetAndEquipment(ruleset, equipment);
        verify(view).showQuestions(expectedQuestions);
    }
}