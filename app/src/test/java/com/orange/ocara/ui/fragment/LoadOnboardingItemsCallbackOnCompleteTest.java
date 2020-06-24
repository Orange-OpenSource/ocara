package com.orange.ocara.ui.fragment;

import com.google.common.collect.Lists;
import com.orange.ocara.business.interactor.LoadOnboardingItemsTask.LoadOnboardingItemsResponse;
import com.orange.ocara.business.model.OnboardingItemModel;
import com.orange.ocara.ui.fragment.TutorialDisplayFragment.LoadOnboardingItemsCallback;

import org.junit.Test;

import java.util.List;

import static com.orange.ocara.ui.contract.TutorialDisplayContract.TutorialDisplayView;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/** see {@link LoadOnboardingItemsCallback}*/
public class LoadOnboardingItemsCallbackOnCompleteTest {

    private LoadOnboardingItemsCallback subject;

    @Test
    public void shouldShowItemsWhateverGivenResponse() {

        // given
        TutorialDisplayView view = mock(TutorialDisplayView.class);
        subject = new LoadOnboardingItemsCallback(view);

        List<OnboardingItemModel> expectedItems = Lists.newArrayList(mock(OnboardingItemModel.class));
        LoadOnboardingItemsResponse inputResponse = LoadOnboardingItemsResponse.ok(expectedItems);

        // when
        subject.onComplete(inputResponse);

        // then
        verify(view).showItems(expectedItems);
    }
}
