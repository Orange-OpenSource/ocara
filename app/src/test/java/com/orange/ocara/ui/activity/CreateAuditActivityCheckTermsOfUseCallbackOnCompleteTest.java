package com.orange.ocara.ui.activity;

import com.orange.ocara.business.interactor.CheckTermsOfUseTask.CheckTermsOfUseResponse;

import org.junit.Before;
import org.junit.Test;

import static com.orange.ocara.business.interactor.CheckTermsOfUseTask.CheckTermsOfUseResponse.accepted;
import static com.orange.ocara.business.interactor.CheckTermsOfUseTask.CheckTermsOfUseResponse.declined;
import static com.orange.ocara.business.interactor.CheckTermsOfUseTask.CheckTermsOfUseResponse.notDefined;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * @see CreateAuditActivity.CreateAuditActivityCheckTermsOfUseCallback#onComplete(CheckTermsOfUseResponse)
 */
public class CreateAuditActivityCheckTermsOfUseCallbackOnCompleteTest {

    private CreateAuditActivity.CreateAuditActivityCheckTermsOfUseCallback subject;

    private CreateAuditActivity view;

    @Before
    public void setUp() {

        view = mock(CreateAuditActivity.class);
        subject = new CreateAuditActivity.CreateAuditActivityCheckTermsOfUseCallback(view);
    }

    @Test
    public void shouldShowAcceptedTermsWhenTermsAreAccepted() {

        // When


        // When
        subject.onComplete(accepted());

        // Then
        verify(view).acceptTerms();
        verify(view, never()).showTerms();
    }

    @Test
    public void shouldShowTermsWhenTermsAreRefused() {

        // When

        // When
        subject.onComplete(declined());

        // Then
        verify(view).showTerms();
        verify(view, never()).acceptTerms();
    }

    @Test
    public void shouldShowTermsWhenTermsHaveNotBeenChecked() {

        // When

        // When
        subject.onComplete(notDefined());

        // Then
        verify(view).showTerms();
    }
}