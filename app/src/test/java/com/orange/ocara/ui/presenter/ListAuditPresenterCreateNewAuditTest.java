package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.binding.ErrorBundle;
import com.orange.ocara.business.interactor.RedoAuditTask;
import com.orange.ocara.business.interactor.UseCase;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.ui.contract.ListAuditContract;

import org.junit.Test;
import org.mockito.ArgumentMatchers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * @see ListAuditPresenter#createNewAudit(AuditEntity)
 */
public class ListAuditPresenterCreateNewAuditTest {

    private ListAuditPresenter subject;

    private ListAuditContract.ListAuditView view;

    private UseCase<RedoAuditTask.RedoAuditRequest, RedoAuditTask.RedoAuditResponse> task;

    @Test
    public void shouldShowSetupWhenTaskIsSuccessful() {
        // Given
        view = mock(ListAuditContract.ListAuditView.class);
        task = (request, callback) -> callback.onComplete(new RedoAuditTask.RedoAuditResponse("", mock(AuditEntity.class)));
        subject = new ListAuditPresenter(view, task);

        // When
        subject.createNewAudit(mock(AuditEntity.class));

        // Then
        verify(view).showSetupPath(ArgumentMatchers.any(AuditEntity.class));
    }

    @Test
    public void shouldShowDoNothingWhenTaskFails() {
        // Given
        view = mock(ListAuditContract.ListAuditView.class);
        task = (request, callback) -> callback.onError(mock(ErrorBundle.class));
        subject = new ListAuditPresenter(view, task);

        // When
        subject.createNewAudit(mock(AuditEntity.class));

        // Then
        verify(view, never()).showSetupPath(ArgumentMatchers.any(AuditEntity.class));
    }
}