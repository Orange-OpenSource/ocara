package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.interactor.UseCase;

import org.junit.Test;
import org.mockito.ArgumentMatchers;

import static com.orange.ocara.business.interactor.CheckTermsOfUseTask.CheckTermsOfUseRequest;
import static com.orange.ocara.business.interactor.CheckTermsOfUseTask.CheckTermsOfUseResponse;
import static com.orange.ocara.business.interactor.UseCase.UseCaseCallback;
import static com.orange.ocara.ui.contract.TermsOfUseDisplayContract.TermsOfUseDisplayView;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TermsOfUseCheckingPresenterCheckTermsTest {

    private TermsOfUseCheckingPresenter subject;

    private UseCase<CheckTermsOfUseRequest, CheckTermsOfUseResponse> task;

    @Test
    public void shouldDelegateFunction() {

        // Given
        task = mock(UseCase.class);

        subject = new TermsOfUseCheckingPresenter(mock(TermsOfUseDisplayView.class), task);

        // When
        subject.checkTerms(mock(UseCaseCallback.class));

        // Then
        verify(task).executeUseCase(ArgumentMatchers.any(), ArgumentMatchers.any(UseCaseCallback.class));
    }

}