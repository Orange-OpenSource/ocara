package com.orange.ocara.ui.activity;

import com.orange.ocara.ui.activity.IntroActivity.CheckTutorialStatusCallback;

import org.junit.Test;

import static com.orange.ocara.business.interactor.CheckOnboardingStatusTask.CheckOnboardingStatusResponse;
import static com.orange.ocara.business.interactor.CheckOnboardingStatusTask.CheckOnboardingStatusResponse.completed;
import static com.orange.ocara.business.interactor.CheckOnboardingStatusTask.CheckOnboardingStatusResponse.ongoing;
import static com.orange.ocara.ui.contract.IntroContract.IntroView;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/** see {@link CheckTutorialStatusCallback#onComplete(CheckOnboardingStatusResponse)}*/
public class CheckTutorialStatusCallbackOnCompleteTest {

    private CheckTutorialStatusCallback subject;

    private IntroView view;

    @Test
    public void shouldTriggerViewNavigateToHomeWhenResponseIsCompleted() {

        // given
        view = mock(IntroView.class);

        subject = new CheckTutorialStatusCallback(view);

        CheckOnboardingStatusResponse inputResponse = completed();

        // when
        subject.onComplete(inputResponse);

        // then
        verify(view).attemptNextDialog(IntroActivity.LEAVE_INTRO_REQUEST_CODE);
        verifyNoMoreInteractions(view);
    }

    @Test
    public void shouldTriggerViewShowTutorialWhenResponseIsOngoing() {

        // given
        view = mock(IntroView.class);

        subject = new CheckTutorialStatusCallback(view);

        CheckOnboardingStatusResponse inputResponse = ongoing();

        // when
        subject.onComplete(inputResponse);

        // then
        verify(view).showTutorial();
        verifyNoMoreInteractions(view);
    }
}
