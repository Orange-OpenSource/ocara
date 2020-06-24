package com.orange.ocara.business.interactor;

import com.orange.ocara.business.interactor.UseCase.UseCaseCallback;

import org.junit.Test;

import static com.orange.ocara.business.interactor.SkipOnboardingTask.SkipOnboardingRequest;
import static com.orange.ocara.business.interactor.SkipOnboardingTask.SkipOnboardingResponse;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/** see {@link SkipOnboardingTask#executeUseCase(SkipOnboardingRequest, UseCaseCallback)}*/
public class SkipOnboardingTaskExecuteUseCaseTest {

    private SkipOnboardingTask subject = new SkipOnboardingTask();

    @Test
    public void shouldtriggerCallbackOnCompleteWhateverGivenRequest() {

        // given
        SkipOnboardingRequest inputRequest = new SkipOnboardingRequest();
        UseCaseCallback<SkipOnboardingResponse > inputCallback = mock(UseCaseCallback.class);

        // when
        subject.executeUseCase(inputRequest, inputCallback);

        // then
        verify(inputCallback).onComplete(argThat(argument -> argument.isOk()));
        verifyNoMoreInteractions(inputCallback);
    }
}