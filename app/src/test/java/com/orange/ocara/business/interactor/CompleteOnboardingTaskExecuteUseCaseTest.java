package com.orange.ocara.business.interactor;

import com.orange.ocara.business.interactor.CompleteOnboardingTask.CompleteOnboardingRequest;
import com.orange.ocara.business.interactor.CompleteOnboardingTask.CompleteOnboardingResponse;
import com.orange.ocara.business.repository.OnboardingRepository;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;

import static com.orange.ocara.business.interactor.UseCase.UseCaseCallback;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/** see {@link CompleteOnboardingTask} */
public class CompleteOnboardingTaskExecuteUseCaseTest {

    private CompleteOnboardingTask subject;

    private OnboardingRepository repository;

    @Test
    public void shouldTriggerCallbackOnCompleteWithOkResponseWhenSavingSucceeds() {

        // given
        repository = mock(OnboardingRepository.class);

        subject = new CompleteOnboardingTask(repository);

        CompleteOnboardingRequest inputRequest = new CompleteOnboardingRequest();
        UseCaseCallback<CompleteOnboardingResponse> inputCallback = mock(UseCaseCallback.class);

        // when
        subject.executeUseCase(inputRequest, inputCallback);

        // then
        verify(inputCallback).onComplete(argThat(new ArgumentMatcher<CompleteOnboardingResponse>() {
            @Override
            public boolean matches(CompleteOnboardingResponse argument) {
                return argument.isOk();
            }
        }));
        verifyNoMoreInteractions(inputCallback);
    }

    @Test
    public void shouldTriggerCallbackOnErrorWhenSavingFails() {

        // given
        repository = mock(OnboardingRepository.class);
        doThrow(RuntimeException.class).when(repository).saveCompletedOnboarding();

        subject = new CompleteOnboardingTask(repository);

        CompleteOnboardingRequest inputRequest = new CompleteOnboardingRequest();
        UseCaseCallback<CompleteOnboardingResponse> inputCallback = mock(UseCaseCallback.class);

        // when
        subject.executeUseCase(inputRequest, inputCallback);

        // then
        verify(inputCallback).onError(ArgumentMatchers.any());
        verifyNoMoreInteractions(inputCallback);
    }
}