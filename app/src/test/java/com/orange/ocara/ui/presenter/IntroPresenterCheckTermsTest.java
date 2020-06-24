package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.interactor.InitTask;
import com.orange.ocara.business.interactor.UseCase;
import com.orange.ocara.business.interactor.UseCaseHandler;

import org.junit.Test;

import static com.orange.ocara.business.interactor.CheckOnboardingStatusTask.CheckOnboardingStatusRequest;
import static com.orange.ocara.business.interactor.CheckOnboardingStatusTask.CheckOnboardingStatusResponse;
import static com.orange.ocara.business.interactor.CheckRemoteStatusTask.CheckRemoteStatusRequest;
import static com.orange.ocara.business.interactor.CheckRemoteStatusTask.CheckRemoteStatusResponse;
import static com.orange.ocara.business.interactor.CheckTermsStatusTask.CheckTermsStatusRequest;
import static com.orange.ocara.business.interactor.CheckTermsStatusTask.CheckTermsStatusResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/** see {@link IntroPresenter#checkTerms(UseCase.UseCaseCallback)} */
public class IntroPresenterCheckTermsTest {

    private IntroPresenter subject;

    @Test
    public void shouldDelegateToCheckTermsTask() {

        // given
        UseCaseHandler useCaseHandler = mock(UseCaseHandler.class);
        UseCase<InitTask.InitRequest, InitTask.InitResponse> initTask = mock(UseCase.class);
        UseCase<CheckTermsStatusRequest, CheckTermsStatusResponse> checkTermsStatusTask = mock(UseCase.class);
        UseCase<CheckOnboardingStatusRequest, CheckOnboardingStatusResponse> checkOnboardingStatusTask = mock(UseCase.class);
        UseCase<CheckRemoteStatusRequest, CheckRemoteStatusResponse> checkRemoteStatusTask = mock(UseCase.class);

        subject = new IntroPresenter(useCaseHandler, initTask, checkTermsStatusTask, checkOnboardingStatusTask, checkRemoteStatusTask);

        UseCase.UseCaseCallback<CheckTermsStatusResponse> inputCallback = mock(UseCase.UseCaseCallback.class);

        // when
        subject.checkTerms(inputCallback);

        // then
        verify(useCaseHandler).execute(eq(checkTermsStatusTask), any(CheckTermsStatusRequest.class), eq(inputCallback));
    }
}