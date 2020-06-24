package com.orange.ocara.business.interactor;

import com.orange.ocara.business.BizConfig;
import com.orange.ocara.business.interactor.UseCase.UseCaseCallback;

import org.junit.Test;
import org.mockito.ArgumentMatchers;

import static com.orange.ocara.business.interactor.ChangeOnboardingStepTask.ChangeOnboardingStepRequest;
import static com.orange.ocara.business.interactor.ChangeOnboardingStepTask.ChangeOnboardingStepResponse;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/** see {@link ChangeOnboardingStepTask#executeUseCase(ChangeOnboardingStepRequest, UseCaseCallback)}*/
public class ChangeOnboardingStepTaskExecuteUseCaseTest {

    private final BizConfig config = new BizConfig();

    private UseCase<ChangeOnboardingStepRequest, ChangeOnboardingStepTask.ChangeOnboardingStepResponse> subject = config.changeOnboardingItemTask();

    @Test
    public void shouldTriggerCallbackOnCompleteWithNotLastStepResponseWhenExpectedPositionIsLowerThanMaxPosition() {

        // given
        boolean expectedLastStep = false;
        int expectedPosition = nextInt(0, 10);
        int maxPosition = nextInt(11, 20);
        ChangeOnboardingStepRequest inputRequest = new ChangeOnboardingStepRequest(nextInt(), expectedPosition, maxPosition);
        UseCaseCallback<ChangeOnboardingStepResponse > inputCallback = mock(UseCaseCallback.class);

        // when
        subject.executeUseCase(inputRequest, inputCallback);

        // then
        verify(inputCallback).onComplete(argThat(argument -> argument.isLastStep() == expectedLastStep && argument.getTargetPosition() == expectedPosition));
        verifyNoMoreInteractions(inputCallback);
    }

    @Test
    public void shouldTriggerCallbackOnCompleteWithLastStepResponseWhenExpectedPositionEqualsMaxPosition() {

        // given
        boolean expectedLastStep = true;
        int expectedPosition = nextInt(0, 10);
        ChangeOnboardingStepRequest inputRequest = new ChangeOnboardingStepRequest(nextInt(), expectedPosition, expectedPosition);
        UseCaseCallback<ChangeOnboardingStepResponse > inputCallback = mock(UseCaseCallback.class);

        // when
        subject.executeUseCase(inputRequest, inputCallback);

        // then
        verify(inputCallback).onComplete(argThat(argument -> argument.isLastStep() == expectedLastStep && argument.getTargetPosition() == expectedPosition));
        verifyNoMoreInteractions(inputCallback);
    }

    @Test
    public void shouldTriggerCallbackOnErrorWhenExpectedPositionisGreatherThanMaxPosition() {

        // given
        int expectedPosition = nextInt(11, 20);
        int maxPosition = nextInt(0, 10);
        ChangeOnboardingStepRequest inputRequest = new ChangeOnboardingStepRequest(nextInt(), expectedPosition, maxPosition);
        UseCaseCallback<ChangeOnboardingStepResponse > inputCallback = mock(UseCaseCallback.class);

        // when
        subject.executeUseCase(inputRequest, inputCallback);

        // then
        verify(inputCallback).onError(ArgumentMatchers.any());
        verifyNoMoreInteractions(inputCallback);
    }
}