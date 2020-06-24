package com.orange.ocara.business.interactor;

import com.orange.ocara.business.binding.BizError;
import com.orange.ocara.business.common.TaskStatus;
import com.orange.ocara.business.model.RuleGroupModel;
import com.orange.ocara.business.model.RuleModel;
import com.orange.ocara.business.repository.QuestionRepository;
import com.orange.ocara.business.repository.RuleRepository;
import com.orange.ocara.utils.TestUtils;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;

import edu.emory.mathcs.backport.java.util.Collections;

import static com.orange.ocara.business.interactor.LoadRulesTask.LoadRulesRequest;
import static com.orange.ocara.business.interactor.LoadRulesTask.LoadRulesResponse;
import static com.orange.ocara.tools.ListUtils.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * see {@link LoadRulesTask#executeUseCase(LoadRulesRequest, UseCase.UseCaseCallback)}
 */
public class LoadRulesTaskExecuteUseCaseTest {

    private LoadRulesTask subject;

    private QuestionRepository questionRepository;

    private RuleRepository ruleRepository;

    @Test
    public void shouldCallOnCompleteWithNoDataWhenQuestionRepositoryHasEmptyListAndRuleRepositoryHasEmptyList() {

        // given
        questionRepository = mock(QuestionRepository.class);
        ruleRepository = mock(RuleRepository.class);

        subject = new LoadRulesTask(questionRepository, ruleRepository);

        UseCase.UseCaseCallback callback = mock(UseCase.UseCaseCallback.class);

        LoadRulesRequest inputRequest = new LoadRulesRequest(TestUtils.longNb(), TestUtils.longNb());

        // when
        subject.executeUseCase(inputRequest, callback);

        // then
        verify(callback, never()).onError(ArgumentMatchers.any(BizError.class));

        ArgumentCaptor<LoadRulesResponse> argumentCaptor = forClass(LoadRulesResponse.class);
        verify(callback).onComplete(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getCode()).isEqualTo(TaskStatus.SUCCESS.name());
        assertThat(argumentCaptor.getValue().getGroups()).isEmpty();
        assertThat(argumentCaptor.getValue().getRules()).isEmpty();
    }

    @Test
    public void shouldCallOnCompleteWithNoDataWhenQuestionRepositoryHasNoList() {

        // given
        long rulesetId = TestUtils.longNb();
        long equipmentId = TestUtils.longNb();

        questionRepository = mock(QuestionRepository.class);
        when(questionRepository.findAll(rulesetId, equipmentId)).thenReturn(Collections.emptyList());

        ruleRepository = mock(RuleRepository.class);
        when(ruleRepository.findAll(rulesetId, equipmentId)).thenReturn(Collections.emptyList());

        subject = new LoadRulesTask(questionRepository, ruleRepository);

        UseCase.UseCaseCallback callback = mock(UseCase.UseCaseCallback.class);

        LoadRulesRequest inputRequest = new LoadRulesRequest(rulesetId, equipmentId);

        // when
        subject.executeUseCase(inputRequest, callback);

        // then
        verify(callback, never()).onError(ArgumentMatchers.any(BizError.class));

        ArgumentCaptor<LoadRulesResponse> argumentCaptor = forClass(LoadRulesResponse.class);
        verify(callback).onComplete(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getCode()).isEqualTo(TaskStatus.SUCCESS.name());
        assertThat(argumentCaptor.getValue().getGroups()).isEmpty();
        assertThat(argumentCaptor.getValue().getRules()).isEmpty();
    }

    @Test
    public void shouldCallOnCompleteWithNoDataWhenRuleRepositoryHasNoList() {

        // given
        long rulesetId = TestUtils.longNb();
        long equipmentId = TestUtils.longNb();

        questionRepository = mock(QuestionRepository.class);
        when(questionRepository.findAll(rulesetId, equipmentId)).thenReturn(null);

        ruleRepository = mock(RuleRepository.class);
        when(ruleRepository.findAll(rulesetId, equipmentId)).thenReturn(null);

        subject = new LoadRulesTask(questionRepository, ruleRepository);

        UseCase.UseCaseCallback callback = mock(UseCase.UseCaseCallback.class);

        LoadRulesRequest inputRequest = new LoadRulesRequest(TestUtils.longNb(), TestUtils.longNb());

        // when
        subject.executeUseCase(inputRequest, callback);

        // then
        verify(callback, never()).onError(ArgumentMatchers.any(BizError.class));

        ArgumentCaptor<LoadRulesResponse> argumentCaptor = forClass(LoadRulesResponse.class);
        verify(callback).onComplete(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getCode()).isEqualTo(TaskStatus.SUCCESS.name());
        assertThat(argumentCaptor.getValue().getGroups()).isEmpty();
        assertThat(argumentCaptor.getValue().getRules()).isEmpty();
    }

    @Test
    public void shouldCallOnCompleteWithDataWhenQuestionRepositoryHasQuestionsAndRuleRepositoryHasRules() {

        // given
        long rulesetId = TestUtils.longNb();
        long equipmentId = TestUtils.longNb();

        String ruleRef = TestUtils.str();
        RuleGroupModel mockQuestion = mock(RuleGroupModel.class);
        questionRepository = mock(QuestionRepository.class);
        when(questionRepository.findAll(rulesetId, equipmentId)).thenReturn(newArrayList(mockQuestion));

        RuleModel mockRule = mock(RuleModel.class);
        when(mockRule.getLabel()).thenReturn(ruleRef);
        ruleRepository = mock(RuleRepository.class);
        when(ruleRepository.findAll(rulesetId, equipmentId)).thenReturn(newArrayList(mockRule));

        subject = new LoadRulesTask(questionRepository, ruleRepository);

        UseCase.UseCaseCallback callback = mock(UseCase.UseCaseCallback.class);

        LoadRulesRequest inputRequest = new LoadRulesRequest(rulesetId, equipmentId);

        // when
        subject.executeUseCase(inputRequest, callback);

        // then
        verify(callback, never()).onError(ArgumentMatchers.any(BizError.class));

        ArgumentCaptor<LoadRulesResponse> argumentCaptor = forClass(LoadRulesResponse.class);
        verify(callback).onComplete(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getCode()).isEqualTo(TaskStatus.SUCCESS.name());
        assertThat(argumentCaptor.getValue().getGroups())
                .isNotEmpty()
                .hasSize(1);
        assertThat(argumentCaptor.getValue().getRules())
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    public void shouldCallOnErrorWithNoDataWhenQuestionRepositoryThrowsException() {
        // Given
        long rulesetId = TestUtils.longNb();
        long equipmentId = TestUtils.longNb();

        ruleRepository = mock(RuleRepository.class);
        questionRepository = mock(QuestionRepository.class);
        doThrow(RuntimeException.class).when(questionRepository).findAll(rulesetId, equipmentId);

        UseCase.UseCaseCallback callback = mock(UseCase.UseCaseCallback.class);

        subject = new LoadRulesTask(questionRepository, ruleRepository);

        LoadRulesRequest inputRequest = new LoadRulesRequest(rulesetId, equipmentId);


        // when
        subject.executeUseCase(inputRequest, callback);

        // Then
        verify(callback).onError(ArgumentMatchers.any());
        verify(callback, never()).onComplete(ArgumentMatchers.any());
    }
}