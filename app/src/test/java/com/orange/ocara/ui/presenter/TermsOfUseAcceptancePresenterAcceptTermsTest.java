package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.binding.ErrorBundle;
import com.orange.ocara.business.interactor.UseCase;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static com.orange.ocara.business.interactor.AcceptTermsOfUseTask.AcceptTermsOfUseRequest;
import static com.orange.ocara.business.interactor.AcceptTermsOfUseTask.AcceptTermsOfUseResponse;
import static com.orange.ocara.ui.contract.TermsOfUseAcceptanceContract.TermsOfUseAcceptanceView;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @see TermsOfUseAcceptancePresenter#acceptTerms() ()
 */
public class TermsOfUseAcceptancePresenterAcceptTermsTest {

    private TermsOfUseAcceptancePresenter subject;

    private TermsOfUseAcceptanceView view;

    @Before
    public void setUp() {

        view = mock(TermsOfUseAcceptanceView.class);
    }

    @Test
    public void shouldShowErrorWhenAcceptanceFails() {

        // Given
        String expectedMessage = RandomStringUtils.randomAlphabetic(5);
        prepareSubjectWithError(expectedMessage);

        // When
        subject.acceptTerms();

        // Then
        verify(view).showError(expectedMessage);
    }

    @Test
    public void shouldHideTermsWhenAccepted() {

        // When
        prepareSubjectWithSuccess();

        // When
        subject.acceptTerms();

        // Then
        verify(view).showTermsAccepted();
    }

    private void prepareSubjectWithError(String errorMessage) {
        ErrorBundle errors = mock(ErrorBundle.class);
        when(errors.getMessage()).thenReturn(errorMessage);
        prepareSubject((request, callback) -> callback.onError(errors));
    }

    private void prepareSubjectWithSuccess() {
        prepareSubject((request, callback) -> callback.onComplete(AcceptTermsOfUseResponse.success()));
    }

    private void prepareSubject(UseCase<AcceptTermsOfUseRequest, AcceptTermsOfUseResponse> task) {

        subject = new TermsOfUseAcceptancePresenter(view, task, mock(UseCase.class), mock(UseCase.class));
    }
}