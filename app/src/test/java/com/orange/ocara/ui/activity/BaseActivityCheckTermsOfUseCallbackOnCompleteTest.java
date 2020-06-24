package com.orange.ocara.ui.activity;

import com.orange.ocara.business.interactor.CheckTermsOfUseTask.CheckTermsOfUseResponse;

import org.junit.Before;
import org.junit.Test;

import static com.orange.ocara.ui.contract.TermsOfUseDisplayContract.TermsOfUseDisplayView;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * @see BaseActivity.BaseActivityCheckTermsOfUseCallback#onComplete(CheckTermsOfUseResponse)
 */
public class BaseActivityCheckTermsOfUseCallbackOnCompleteTest {

    private BaseActivity.BaseActivityCheckTermsOfUseCallback subject;

    private TermsOfUseDisplayView view;

    @Before
    public void setUp() {

        view = mock(TermsOfUseDisplayView.class);
        subject = new BaseActivity.BaseActivityCheckTermsOfUseCallback(view);
    }

    @Test
    public void shouldNotShowTermsWhenTermsAreAccepted() {

        // When


        // When
        subject.onComplete(CheckTermsOfUseResponse.accepted());

        // Then
        verify(view, never()).showTerms();
    }

    @Test
    public void shouldShowTermsWhenTermsAreRefused() {

        // When

        // When
        subject.onComplete(CheckTermsOfUseResponse.declined());

        // Then
        verify(view).showTerms();
    }

    @Test
    public void shouldShowTermsWhenTermsHaveNotBeenChecked() {

        // When

        // When
        subject.onComplete(CheckTermsOfUseResponse.notDefined());

        // Then
        verify(view).showTerms();
    }
}