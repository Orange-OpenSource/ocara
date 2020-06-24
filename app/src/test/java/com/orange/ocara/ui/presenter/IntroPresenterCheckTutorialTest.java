package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.interactor.CheckTermsStatusTask.CheckTermsStatusResponse;
import com.orange.ocara.business.interactor.InitTask;
import com.orange.ocara.business.interactor.InitTask.InitResponse;
import com.orange.ocara.business.interactor.UseCase;
import com.orange.ocara.business.interactor.UseCaseHandler;

import org.junit.Test;

import static com.orange.ocara.business.interactor.CheckOnboardingStatusTask.CheckOnboardingStatusRequest;
import static com.orange.ocara.business.interactor.CheckOnboardingStatusTask.CheckOnboardingStatusResponse;
import static com.orange.ocara.business.interactor.CheckOnboardingStatusTask.UseCaseCallback;
import static com.orange.ocara.business.interactor.CheckRemoteStatusTask.CheckRemoteStatusRequest;
import static com.orange.ocara.business.interactor.CheckRemoteStatusTask.CheckRemoteStatusResponse;
import static com.orange.ocara.business.interactor.CheckTermsStatusTask.CheckTermsStatusRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * see {@link IntroPresenter#checkTutorial(UseCase.UseCaseCallback)}
 */
public class IntroPresenterCheckTutorialTest {

    private IntroPresenter subject;

    @Test
    public void shouldDelegateToCheckTutorialTask() {

        // given
        UseCaseHandler useCaseHandler = mock(UseCaseHandler.class);
        UseCase<InitTask.InitRequest, InitResponse> initTask = mock(UseCase.class);
        UseCase<CheckTermsStatusRequest, CheckTermsStatusResponse> checkTermsStatusTask = mock(UseCase.class);
        UseCase<CheckOnboardingStatusRequest, CheckOnboardingStatusResponse> checkOnboardingStatusTask = mock(UseCase.class);
        UseCase<CheckRemoteStatusRequest, CheckRemoteStatusResponse> checkRemoteStatusTask = mock(UseCase.class);

        subject = new IntroPresenter(useCaseHandler, initTask, checkTermsStatusTask, checkOnboardingStatusTask, checkRemoteStatusTask);

        UseCaseCallback<CheckOnboardingStatusResponse> inputCallback = mock(UseCaseCallback.class);

        // when
        subject.checkTutorial(inputCallback);

        // then
        verify(useCaseHandler).execute(eq(checkOnboardingStatusTask), any(CheckOnboardingStatusRequest.class), eq(inputCallback));
    }
}