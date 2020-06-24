package com.orange.ocara.ui.fragment;

import com.orange.ocara.ui.contract.TutorialDisplayContract.TutorialDisplayView;
import com.orange.ocara.ui.fragment.TutorialDisplayFragment.CompleteOnboardingCallback;

import org.junit.Test;

import static com.orange.ocara.business.interactor.CompleteOnboardingTask.CompleteOnboardingResponse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/** see {@link CompleteOnboardingCallback}*/
public class CompleteOnboardingCallbackOnCompleteTest {

    private CompleteOnboardingCallback subject;

    @Test
    public void shouldCloseViewWhateverGivenResponse() {

        // given
        TutorialDisplayView view = mock(TutorialDisplayView.class);
        subject = new CompleteOnboardingCallback(view);

        CompleteOnboardingResponse inputResponse = CompleteOnboardingResponse.ok();

        // when
        subject.onComplete(inputResponse);

        // then
        verify(view).finishView();
    }
}
