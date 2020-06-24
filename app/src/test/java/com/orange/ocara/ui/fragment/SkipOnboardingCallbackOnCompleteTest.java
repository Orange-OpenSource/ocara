package com.orange.ocara.ui.fragment;

import com.orange.ocara.business.interactor.SkipOnboardingTask.SkipOnboardingResponse;
import com.orange.ocara.ui.contract.TutorialDisplayContract.TutorialDisplayView;
import com.orange.ocara.ui.fragment.TutorialDisplayFragment.SkipOnboardingCallback;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/** see {@link SkipOnboardingCallback}*/
public class SkipOnboardingCallbackOnCompleteTest {

    private SkipOnboardingCallback subject;

    @Test
    public void shouldCancelViewWhateverGivenResponse() {

        // given
        TutorialDisplayView view = mock(TutorialDisplayView.class);
        subject = new SkipOnboardingCallback(view);

        SkipOnboardingResponse inputResponse = SkipOnboardingResponse.ok();

        // when
        subject.onComplete(inputResponse);

        // then
        verify(view).cancelView();
        verifyNoMoreInteractions(view);
    }
}
