package com.orange.ocara.ui.activity;

import com.orange.ocara.business.binding.ErrorBundle;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static com.orange.ocara.ui.contract.TermsOfUseDisplayContract.TermsOfUseDisplayView;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @see BaseActivity.BaseActivityCheckTermsOfUseCallback#onError(ErrorBundle)
 */
public class BaseActivityCheckTermsOfUseCallbackOnErrorTest {

    private BaseActivity.BaseActivityCheckTermsOfUseCallback subject;

    private TermsOfUseDisplayView view;

    @Before
    public void setUp() {

        view = mock(TermsOfUseDisplayView.class);
        subject = new BaseActivity.BaseActivityCheckTermsOfUseCallback(view);
    }

    @Test
    public void shouldShowErrorWhenCheckingTermsFails() {

        // Given
        ErrorBundle errors = mock(ErrorBundle.class);
        String expectedMessage = RandomStringUtils.randomAlphabetic(5);
        when(errors.getMessage()).thenReturn(expectedMessage);

        // When
        subject.onError(errors);

        // Then
        verify(view).showError(expectedMessage);
    }
}