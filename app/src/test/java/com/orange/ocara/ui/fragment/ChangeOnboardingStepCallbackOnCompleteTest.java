package com.orange.ocara.ui.fragment;

import com.orange.ocara.ui.fragment.TutorialDisplayFragment.ChangeOnboardingStepCallback;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import static com.orange.ocara.business.interactor.ChangeOnboardingStepTask.ChangeOnboardingStepResponse;
import static com.orange.ocara.ui.contract.TutorialDisplayContract.TutorialDisplayView;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/** see {@link ChangeOnboardingStepCallback}*/
public class ChangeOnboardingStepCallbackOnCompleteTest {

    private ChangeOnboardingStepCallback subject;

    @Test
    public void shouldMakeViewShowLastStepWhenResponseIsLastStep() {

        // given
        Integer expectedPosition = nextInt();
        TutorialDisplayView view = mock(TutorialDisplayView.class);
        subject = new ChangeOnboardingStepCallback(view);

        ChangeOnboardingStepResponse inputResponse = new ChangeOnboardingStepResponse(RandomUtils.nextInt(), expectedPosition, true);

        // when
        subject.onComplete(inputResponse);

        // then
        verify(view).showStartButton();
        verify(view).showStep(expectedPosition);
        verifyNoMoreInteractions(view);
    }

    @Test
    public void shouldMakeViewShowNextButtonWhenResponseIsNotLastStep() {

        // given
        Integer expectedPosition = nextInt();

        TutorialDisplayView view = mock(TutorialDisplayView.class);
        subject = new ChangeOnboardingStepCallback(view);

        ChangeOnboardingStepResponse inputResponse = new ChangeOnboardingStepResponse(RandomUtils.nextInt(), expectedPosition, false);

        // when
        subject.onComplete(inputResponse);

        // then
        verify(view).showNextButton();
        verify(view).showStep(expectedPosition);
        verifyNoMoreInteractions(view);
    }

}
