package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.binding.ErrorBundle;
import com.orange.ocara.business.interactor.UseCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.net.MalformedURLException;
import java.net.URL;

import static com.orange.ocara.business.interactor.LoadTermsOfUseTask.LoadTermsOfUseRequest;
import static com.orange.ocara.business.interactor.LoadTermsOfUseTask.LoadTermsOfUseResponse;
import static com.orange.ocara.business.interactor.LoadTermsOfUseTask.LoadTermsOfUseResponse.ok;
import static com.orange.ocara.ui.contract.TermsOfUseReadingContract.TermsOfUseReadingView;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @see TermsOfUseReadingPresenter#loadTerms()
 */
public class TermsOfUseReadingPresenterLoadTermsTest {

    private TermsOfUseReadingPresenter subject;

    private TermsOfUseReadingView view;

    @Before
    public void setUp() {

        view = mock(TermsOfUseReadingView.class);
    }

    @Test
    public void shouldShowErrorWhenRefusalFails() {

        // Given
        String expectedMessage = randomAlphabetic(5);
        prepareSubjectWithError(expectedMessage);

        // When
        subject.loadTerms();

        // Then
        verify(view).showError(expectedMessage);
    }

    @Test
    public void shouldShowTermsWhenRawDataLoadSucceeds() {

        // When
        prepareSubjectWithTextSuccess();

        // When
        subject.loadTerms();

        // Then
        verify(view).showTerms(ArgumentMatchers.anyString());
    }

    @Test
    public void shouldShowTermsWhenUrlLoadSucceeds() throws MalformedURLException {

        // When
        URL expected = new URL("http://example.com/");
        prepareSubjectWithUrlSuccess(expected);

        // When
        subject.loadTerms();

        // Then
        verify(view).showTerms(expected);
    }

    private void prepareSubjectWithError(String errorMessage) {
        ErrorBundle errors = mock(ErrorBundle.class);
        when(errors.getMessage()).thenReturn(errorMessage);
        prepareSubject((request, callback) -> callback.onError(errors));
    }

    private void prepareSubjectWithTextSuccess() {
        prepareSubject((request, callback) -> callback.onComplete(ok(randomAlphabetic(1))));
    }

    private void prepareSubjectWithUrlSuccess(URL url) {
        prepareSubject((request, callback) -> callback.onComplete(ok(url)));
    }

    private void prepareSubject(UseCase<LoadTermsOfUseRequest, LoadTermsOfUseResponse> task) {

        subject = new TermsOfUseReadingPresenter(view, task);
    }
}