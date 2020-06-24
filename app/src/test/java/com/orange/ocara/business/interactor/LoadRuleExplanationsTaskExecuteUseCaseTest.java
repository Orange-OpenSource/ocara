package com.orange.ocara.business.interactor;

import com.orange.ocara.business.binding.BizError;
import com.orange.ocara.business.common.TaskStatus;
import com.orange.ocara.business.model.ExplanationModel;
import com.orange.ocara.business.repository.ExplanationRepository;
import com.orange.ocara.tools.ListUtils;
import com.orange.ocara.utils.TestUtils;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** see {@link LoadRuleExplanationsTask#executeUseCase(LoadRuleExplanationsTask.LoadRuleExplanationsRequest, UseCase.UseCaseCallback)} */
public class LoadRuleExplanationsTaskExecuteUseCaseTest {

    private LoadRuleExplanationsTask subject;

    private ExplanationRepository explanationRepository;

    @Test
    public void shouldCompleteWithNoDataWhenRuleHasNoExplanation() {

        // given
        long ruleId = TestUtils.longNb();

        explanationRepository = mock(ExplanationRepository.class);
        when(explanationRepository.findAllByRuleId(ruleId)).thenReturn(Collections.emptyList());

        UseCase.UseCaseCallback callback = mock(UseCase.UseCaseCallback.class);

        subject = new LoadRuleExplanationsTask(explanationRepository);

        LoadRuleExplanationsTask.LoadRuleExplanationsRequest inputRequest = new LoadRuleExplanationsTask.LoadRuleExplanationsRequest(ruleId);

        // when
        subject.executeUseCase(inputRequest, callback);

        // then
        verify(callback, never()).onError(ArgumentMatchers.any(BizError.class));

        ArgumentCaptor<LoadRuleExplanationsTask.LoadRuleExplanationsResponse> argumentCaptor = forClass(LoadRuleExplanationsTask.LoadRuleExplanationsResponse.class);
        verify(callback).onComplete(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getCode()).isEqualTo(TaskStatus.SUCCESS.name());
        assertThat(argumentCaptor.getValue().getData()).isEmpty();
    }

    @Test
    public void shouldCompleteWithOneExplanationWhenRuleHasOneExplanation() {

        // given
        long ruleId = TestUtils.longNb();

        explanationRepository = mock(ExplanationRepository.class);

        String icon = TestUtils.str();
        ExplanationModel mock = Mockito.mock(ExplanationModel.class);

        when(explanationRepository.findAllByRuleId(ruleId)).thenReturn(ListUtils.newArrayList(mock));
        UseCase.UseCaseCallback callback = mock(UseCase.UseCaseCallback.class);

        subject = new LoadRuleExplanationsTask(explanationRepository);

        LoadRuleExplanationsTask.LoadRuleExplanationsRequest inputRequest = new LoadRuleExplanationsTask.LoadRuleExplanationsRequest(ruleId);

        // when
        subject.executeUseCase(inputRequest, callback);

        // then
        verify(callback, never()).onError(ArgumentMatchers.any(BizError.class));

        ArgumentCaptor<LoadRuleExplanationsTask.LoadRuleExplanationsResponse> argumentCaptor = forClass(LoadRuleExplanationsTask.LoadRuleExplanationsResponse.class);
        verify(callback).onComplete(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getCode()).isEqualTo(TaskStatus.SUCCESS.name());
        assertThat(argumentCaptor.getValue().getData())
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    public void shouldFailWheepositoryThrowsException() {
        // Given
        long ruleId = TestUtils.longNb();

        explanationRepository = mock(ExplanationRepository.class);

        doThrow(RuntimeException.class).when(explanationRepository).findAllByRuleId(Mockito.any());

        UseCase.UseCaseCallback callback = mock(UseCase.UseCaseCallback.class);

        subject = new LoadRuleExplanationsTask(explanationRepository);

        LoadRuleExplanationsTask.LoadRuleExplanationsRequest inputRequest = new LoadRuleExplanationsTask.LoadRuleExplanationsRequest(ruleId);

        // when
        subject.executeUseCase(inputRequest, callback);

        // Then
        verify(callback).onError(ArgumentMatchers.any());
        verify(callback, never()).onComplete(ArgumentMatchers.any());
    }
}