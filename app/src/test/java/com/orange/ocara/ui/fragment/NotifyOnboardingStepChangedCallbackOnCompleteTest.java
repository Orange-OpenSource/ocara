package com.orange.ocara.ui.fragment;

import com.orange.ocara.ui.contract.TutorialDisplayContract.TutorialDisplayView;
import com.orange.ocara.ui.fragment.TutorialDisplayFragment.NotifyOnboardingStepChangedCallback;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import static com.orange.ocara.business.interactor.ChangeOnboardingStepTask.ChangeOnboardingStepResponse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * see {@link NotifyOnboardingStepChangedCallback}
 */
public class NotifyOnboardingStepChangedCallbackOnCompleteTest {

    private NotifyOnboardingStepChangedCallback subject;

    @Test
    public void shouldTriggerViewShowStartButtonWhenResponseIsLastStep() {

        // given
        TutorialDisplayView view = mock(TutorialDisplayView.class);
        subject = new NotifyOnboardingStepChangedCallback(view);

        ChangeOnboardingStepResponse inputResponse = ChangeOnboardingStepResponse.ok(RandomUtils.nextInt(), RandomUtils.nextInt(), true);

        // when
        subject.onComplete(inputResponse);

        // then
        verify(view).showStartButton();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void shouldTriggerViewShowNextButtonWhenResponseIsNotLastStep() {

        // given
        TutorialDisplayView view = mock(TutorialDisplayView.class);
        subject = new NotifyOnboardingStepChangedCallback(view);

        ChangeOnboardingStepResponse inputResponse = ChangeOnboardingStepResponse.ok(RandomUtils.nextInt(), RandomUtils.nextInt(), false);

        // when
        subject.onComplete(inputResponse);

        // then
        verify(view).showNextButton();
        verifyNoMoreInteractions(view);
    }
}
