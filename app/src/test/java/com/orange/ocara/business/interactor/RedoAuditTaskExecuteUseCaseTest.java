package com.orange.ocara.business.interactor;

import com.orange.ocara.business.binding.BizError;
import com.orange.ocara.business.repository.AuditRepository;
import com.orange.ocara.data.cache.model.AuditEntity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @see RedoAuditTask#executeUseCase(RedoAuditTask.RedoAuditRequest, UseCase.UseCaseCallback)
 */
public class RedoAuditTaskExecuteUseCaseTest {

    private RedoAuditTask subject;

    private AuditRepository auditRepository;

    @Before
    public void setUp() {
        auditRepository = mock(AuditRepository.class);

        subject = new RedoAuditTask(auditRepository);
    }

    @Test
    public void shouldRetrieveErrorWhenExceptionIsThrownFromCloningService() {
        // Given
        when(auditRepository.cloneOne(Mockito.any())).thenThrow(RuntimeException.class);

        RedoAuditTask.RedoAuditRequest request = new RedoAuditTask.RedoAuditRequest(mock(AuditEntity.class));

        UseCase.UseCaseCallback callback = mock(UseCase.UseCaseCallback.class);

        // When
        subject.executeUseCase(request, callback);

        // Then
        verify(callback, never()).onComplete(ArgumentMatchers.any());
        verify(callback).onError(ArgumentMatchers.any(BizError.class));
    }

    @Test
    public void shouldRetrieveOkWhenServiceCanCloneAudit() {
        // Given
        when(auditRepository.cloneOne(Mockito.any())).thenReturn(mock(AuditEntity.class));

        RedoAuditTask.RedoAuditRequest request = new RedoAuditTask.RedoAuditRequest(mock(AuditEntity.class));

        UseCase.UseCaseCallback callback = mock(UseCase.UseCaseCallback.class);

        // When
        subject.executeUseCase(request, callback);

        // Then
        verify(callback).onComplete(ArgumentMatchers.any());
        verify(callback, never()).onError(ArgumentMatchers.any(BizError.class));
    }
}