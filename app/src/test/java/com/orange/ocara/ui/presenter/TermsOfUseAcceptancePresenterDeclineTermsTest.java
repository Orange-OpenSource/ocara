package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.binding.ErrorBundle;
import com.orange.ocara.business.interactor.UseCase;

import org.junit.Before;
import org.junit.Test;

import static com.orange.ocara.business.interactor.DeclineTermsOfUseTask.DeclineTermsOfUseRequest;
import static com.orange.ocara.business.interactor.DeclineTermsOfUseTask.DeclineTermsOfUseResponse;
import static com.orange.ocara.ui.contract.TermsOfUseAcceptanceContract.TermsOfUseAcceptanceView;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @see TermsOfUseAcceptancePresenter#declineTerms() () ()
 */
public class TermsOfUseAcceptancePresenterDeclineTermsTest {

    private TermsOfUseAcceptancePresenter subject;

    private TermsOfUseAcceptanceView view;

    @Before
    public void setUp() {

        view = mock(TermsOfUseAcceptanceView.class);
    }

    @Test
    public void shouldShowErrorWhenRefusalFails() {

        // Given
        String expectedMessage = randomAlphabetic(5);
        prepareSubjectWithError(expectedMessage);

        // When
        subject.declineTerms();

        // Then
        verify(view).showError(expectedMessage);
    }

    @Test
    public void shouldHideTermsWhenDeclined() {

        // When
        prepareSubjectWithSuccess();

        // When
        subject.declineTerms();

        // Then
        verify(view).showTermsDeclined();
    }

    private void prepareSubjectWithError(String errorMessage) {
        ErrorBundle errors = mock(ErrorBundle.class);
        when(errors.getMessage()).thenReturn(errorMessage);
        prepareSubject((request, callback) -> callback.onError(errors));
    }

    private void prepareSubjectWithSuccess() {
        prepareSubject((request, callback) -> callback.onComplete(new DeclineTermsOfUseResponse(randomAlphabetic(1))));
    }

    private void prepareSubject(UseCase<DeclineTermsOfUseRequest, DeclineTermsOfUseResponse> task) {

        subject = new TermsOfUseAcceptancePresenter(view, mock(UseCase.class), task, mock(UseCase.class));
    }
}