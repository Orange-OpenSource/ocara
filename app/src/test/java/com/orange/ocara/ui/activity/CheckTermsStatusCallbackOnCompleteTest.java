package com.orange.ocara.ui.activity;

import com.orange.ocara.ui.activity.IntroActivity.CheckTermsStatusCallback;
import com.orange.ocara.ui.contract.IntroContract.IntroView;

import org.junit.Test;

import static com.orange.ocara.business.interactor.CheckTermsStatusTask.CheckTermsStatusResponse;
import static com.orange.ocara.business.interactor.CheckTermsStatusTask.CheckTermsStatusResponse.accepted;
import static com.orange.ocara.business.interactor.CheckTermsStatusTask.CheckTermsStatusResponse.notAccepted;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/** see {@link CheckTermsStatusCallback#onComplete(CheckTermsStatusResponse)}*/
public class CheckTermsStatusCallbackOnCompleteTest {

    private CheckTermsStatusCallback subject;

    private IntroView view;

    @Test
    public void shouldTriggerViewShowTermsWhenResponseIsNotAccepted() {

        // given
        view = mock(IntroView.class);

        subject = new CheckTermsStatusCallback(view);

        CheckTermsStatusResponse inputResponse = notAccepted();

        // when
        subject.onComplete(inputResponse);

        // then
        verify(view).showTerms();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void shouldTriggerViewShowTutorialWhenResponseIsAccepted() {

        // given
        view = mock(IntroView.class);

        subject = new CheckTermsStatusCallback(view);

        CheckTermsStatusResponse inputResponse = accepted();

        // when
        subject.onComplete(inputResponse);

        // then
        verify(view).attemptNextDialog(IntroActivity.SHOW_TUTORIAL_REQUEST_CODE);
        verifyNoMoreInteractions(view);
    }
}
