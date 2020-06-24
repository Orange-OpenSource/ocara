package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.interactor.ChangeOnboardingStepTask.ChangeOnboardingStepRequest;
import com.orange.ocara.business.interactor.ChangeOnboardingStepTask.ChangeOnboardingStepResponse;
import com.orange.ocara.business.interactor.UseCase;
import com.orange.ocara.business.interactor.UseCaseHandler;

import org.junit.Test;

import static com.orange.ocara.business.interactor.CompleteOnboardingTask.CompleteOnboardingRequest;
import static com.orange.ocara.business.interactor.CompleteOnboardingTask.CompleteOnboardingResponse;
import static com.orange.ocara.business.interactor.CompleteOnboardingTask.UseCaseCallback;
import static com.orange.ocara.business.interactor.LoadOnboardingItemsTask.LoadOnboardingItemsRequest;
import static com.orange.ocara.business.interactor.LoadOnboardingItemsTask.LoadOnboardingItemsResponse;
import static com.orange.ocara.business.interactor.SkipOnboardingTask.SkipOnboardingRequest;
import static com.orange.ocara.business.interactor.SkipOnboardingTask.SkipOnboardingResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/** see {@link TutorialDisplayPresenter#openNextStep(UseCaseCallback)} */
public class TutorialDisplayPresenterOpenNextStepTest {

    private TutorialDisplayPresenter subject;

    @Test
    public void shouldDelegateTaskExecutionToHandler() {

        // given
        UseCaseHandler useCaseHandler = mock(UseCaseHandler.class);
        UseCase<CompleteOnboardingRequest, CompleteOnboardingResponse> completeUseCase = mock(UseCase.class);
        UseCase<LoadOnboardingItemsRequest, LoadOnboardingItemsResponse> loadUseCase = mock(UseCase.class);
        UseCase<SkipOnboardingRequest, SkipOnboardingResponse> skipUseCase = mock(UseCase.class);
        UseCase<ChangeOnboardingStepRequest, ChangeOnboardingStepResponse> changeStepUseCase = mock(UseCase.class);

        subject = new TutorialDisplayPresenter(useCaseHandler, completeUseCase, loadUseCase, skipUseCase, changeStepUseCase);

        UseCaseCallback<ChangeOnboardingStepResponse> inputCallback = mock(UseCaseCallback.class);

        // when
        subject.openNextStep(inputCallback);

        // then
        verify(useCaseHandler).execute(eq(changeStepUseCase), any(ChangeOnboardingStepRequest.class), any(UseCaseCallback.class));
    }

}