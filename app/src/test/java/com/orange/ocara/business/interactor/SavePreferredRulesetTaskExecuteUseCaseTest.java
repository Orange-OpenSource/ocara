package com.orange.ocara.business.interactor;

import com.orange.ocara.business.binding.ErrorBundle;
import com.orange.ocara.business.interactor.SavePreferredRulesetTask.SavePreferredRulesetRequest;
import com.orange.ocara.business.interactor.SavePreferredRulesetTask.SavePreferredRulesetResponse;
import com.orange.ocara.business.interactor.UseCase.UseCaseCallback;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.repository.RulesetRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * see {@link SavePreferredRulesetTask#executeUseCase(SavePreferredRulesetRequest, UseCaseCallback)}
 */
public class SavePreferredRulesetTaskExecuteUseCaseTest {

    private SavePreferredRulesetTask subject;

    private RulesetRepository repository;

    @Before
    public void setUp() {
        repository = mock(RulesetRepository.class);

        subject = new SavePreferredRulesetTask(repository);
    }

    @Test
    public void shouldDelegateWhenProcessSuccessful() {
        // Given
        UseCaseCallback output = mock(UseCaseCallback.class);
        RulesetModel expectedRuleset = mock(RulesetModel.class);
        SavePreferredRulesetRequest request = new SavePreferredRulesetRequest(expectedRuleset);

        // When
        subject.executeUseCase(request, output);

        // Then
        verify(repository).saveDefaultRuleset(expectedRuleset);
        verify(output).onComplete(any(SavePreferredRulesetResponse.class));
        verify(output, never()).onError(any(ErrorBundle.class));
    }

    @Test
    public void shouldRetrieveExceptionWhenProcessFails() {
        // Given
        doThrow(RuntimeException.class).when(repository).saveDefaultRuleset(Mockito.any(RulesetModel.class));

        UseCaseCallback output = mock(UseCaseCallback.class);
        SavePreferredRulesetRequest request = new SavePreferredRulesetRequest(mock(RulesetModel.class));

        // When
        subject.executeUseCase(request, output);

        // Then
        verify(output, never()).onComplete(any(SavePreferredRulesetResponse.class));
        verify(output).onError(any(ErrorBundle.class));
    }
}