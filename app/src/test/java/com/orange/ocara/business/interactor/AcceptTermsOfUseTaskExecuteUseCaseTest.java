package com.orange.ocara.business.interactor;

import com.orange.ocara.business.binding.BizError;
import com.orange.ocara.business.common.TaskStatus;
import com.orange.ocara.business.interactor.AcceptTermsOfUseTask.AcceptTermsOfUseRequest;
import com.orange.ocara.business.interactor.AcceptTermsOfUseTask.AcceptTermsOfUseResponse;
import com.orange.ocara.business.interactor.UseCase.UseCaseCallback;
import com.orange.ocara.business.repository.TermsRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * @see AcceptTermsOfUseTask#executeUseCase(AcceptTermsOfUseRequest, UseCaseCallback)
 */
public class AcceptTermsOfUseTaskExecuteUseCaseTest {

    private AcceptTermsOfUseTask subject;

    private TermsRepository repository;

    @Before
    public void setUp() {
        repository = mock(TermsRepository.class);

        subject = new AcceptTermsOfUseTask(repository);
    }

    @Test
    public void shouldCallOnErrorWhenExceptionIsThrown() {

        // Given
        doThrow(RuntimeException.class).when(repository).markAsAccepted();

        UseCaseCallback callback = mock(UseCaseCallback.class);

        // When
        when(callback);

        // Then
        verify(callback).onError(ArgumentMatchers.any(BizError.class));
        verify(callback, never()).onComplete(ArgumentMatchers.any());
    }

    @Test
    public void shouldCallOnCompleteWhenResponseIsValid() {

        // Given
        UseCaseCallback callback = mock(UseCaseCallback.class);

        // When
        when(callback);

        // Then
        verify(callback, never()).onError(ArgumentMatchers.any(BizError.class));

        ArgumentCaptor<AcceptTermsOfUseResponse> argumentCaptor = forClass(AcceptTermsOfUseResponse.class);
        verify(callback).onComplete(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getCode()).isEqualTo(TaskStatus.SUCCESS.name());
    }

    private void when(UseCaseCallback callback) {
        AcceptTermsOfUseRequest request = new AcceptTermsOfUseRequest();

        // When
        subject.executeUseCase(request, callback);
    }
}