package com.orange.ocara.business.interactor;

import com.orange.ocara.business.interactor.CheckOnboardingStatusTask.CheckOnboardingStatusRequest;
import com.orange.ocara.business.interactor.UseCase.UseCaseCallback;
import com.orange.ocara.business.repository.OnboardingRepository;

import org.junit.Test;
import org.mockito.ArgumentMatchers;

import static com.orange.ocara.business.interactor.CheckOnboardingStatusTask.CheckOnboardingStatusResponse;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/** see {@link CheckOnboardingStatusTask#executeUseCase(CheckOnboardingStatusRequest, UseCaseCallback)} */
public class CheckOnboardingStatusTaskExecuteUseCaseTest {

    private CheckOnboardingStatusTask subject;

    private OnboardingRepository repository;

    @Test
    public void shouldTriggerCallbackOnCompleteWithCompletedResponseWhenGivenOnboardingStatusIsCompleted() {

        // given
        repository = mock(OnboardingRepository.class);
        when(repository.checkOnboardingIsCompleted()).thenReturn(true);

        subject = new CheckOnboardingStatusTask(repository);

        CheckOnboardingStatusRequest inputRequest = new CheckOnboardingStatusRequest();
        UseCaseCallback<CheckOnboardingStatusResponse > inputCallback = mock(UseCaseCallback.class);

        // when
        subject.executeUseCase(inputRequest, inputCallback);

        // then
        verify(inputCallback).onComplete(argThat(argument -> argument.isCompleted() && !argument.isOngoing()));
        verifyNoMoreInteractions(inputCallback);
    }

    @Test
    public void shouldTriggerCallbackOnCompleteWithOngoingResponseWhenGivenOnboardingStatusIsNotCompleted() {

        // given
        repository = mock(OnboardingRepository.class);
        when(repository.checkOnboardingIsCompleted()).thenReturn(false);

        subject = new CheckOnboardingStatusTask(repository);

        CheckOnboardingStatusRequest inputRequest = new CheckOnboardingStatusRequest();
        UseCaseCallback<CheckOnboardingStatusResponse > inputCallback = mock(UseCaseCallback.class);

        // when
        subject.executeUseCase(inputRequest, inputCallback);

        // then
        verify(inputCallback).onComplete(argThat(argument -> !argument.isCompleted() && argument.isOngoing()));
        verifyNoMoreInteractions(inputCallback);
    }

    @Test
    public void shouldTriggerCallbackOnErrorWhenGivenOnboardingStatusRetrievalFailed() {

        // given
        repository = mock(OnboardingRepository.class);
        when(repository.checkOnboardingIsCompleted()).thenThrow(RuntimeException.class);

        subject = new CheckOnboardingStatusTask(repository);

        CheckOnboardingStatusRequest inputRequest = new CheckOnboardingStatusRequest();
        UseCaseCallback<CheckOnboardingStatusResponse > inputCallback = mock(UseCaseCallback.class);

        // when
        subject.executeUseCase(inputRequest, inputCallback);

        // then
        verify(inputCallback).onError(ArgumentMatchers.any());
        verifyNoMoreInteractions(inputCallback);
    }
}