package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.interactor.UseCase;
import com.orange.ocara.business.interactor.UseCaseHandler;

import org.junit.Test;

import static com.orange.ocara.business.interactor.ChangeOnboardingStepTask.ChangeOnboardingStepRequest;
import static com.orange.ocara.business.interactor.ChangeOnboardingStepTask.ChangeOnboardingStepResponse;
import static com.orange.ocara.business.interactor.ChangeOnboardingStepTask.UseCaseCallback;
import static com.orange.ocara.business.interactor.CompleteOnboardingTask.CompleteOnboardingRequest;
import static com.orange.ocara.business.interactor.CompleteOnboardingTask.CompleteOnboardingResponse;
import static com.orange.ocara.business.interactor.LoadOnboardingItemsTask.LoadOnboardingItemsRequest;
import static com.orange.ocara.business.interactor.LoadOnboardingItemsTask.LoadOnboardingItemsResponse;
import static com.orange.ocara.business.interactor.SkipOnboardingTask.SkipOnboardingRequest;
import static com.orange.ocara.business.interactor.SkipOnboardingTask.SkipOnboardingResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/** see {@link TutorialDisplayPresenter#skipOnboarding(UseCase.UseCaseCallback)} */
public class TutorialDisplayPresenterSkipOnboardingTest {

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

        UseCaseCallback<SkipOnboardingResponse> inputCallback = mock(UseCaseCallback.class);

        // when
        subject.skipOnboarding(inputCallback);

        // then
        verify(useCaseHandler).execute(eq(skipUseCase), any(SkipOnboardingRequest.class), eq(inputCallback));
    }
}