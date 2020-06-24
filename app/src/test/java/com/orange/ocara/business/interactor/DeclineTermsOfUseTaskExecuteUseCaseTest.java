package com.orange.ocara.business.interactor;

import com.orange.ocara.business.binding.BizError;
import com.orange.ocara.business.interactor.DeclineTermsOfUseTask.DeclineTermsOfUseRequest;
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
 * @see DeclineTermsOfUseTask#executeUseCase(DeclineTermsOfUseRequest, UseCaseCallback)
 */
public class DeclineTermsOfUseTaskExecuteUseCaseTest {

    private DeclineTermsOfUseTask subject;

    private TermsRepository repository;

    @Before
    public void setUp() {
        repository = mock(TermsRepository.class);

        subject = new DeclineTermsOfUseTask(repository);
    }

    @Test
    public void shouldCallOnErrorWhenExceptionIsThrown() {

        // Given
        doThrow(RuntimeException.class).when(repository).markAsRefused();

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
        ArgumentCaptor<DeclineTermsOfUseTask.DeclineTermsOfUseResponse> argumentCaptor = forClass(DeclineTermsOfUseTask.DeclineTermsOfUseResponse.class);
        verify(callback).onComplete(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().isOk()).isTrue();
    }

    private void when(UseCaseCallback callback) {
        DeclineTermsOfUseRequest request = new DeclineTermsOfUseRequest();

        // When
        subject.executeUseCase(request, callback);
    }
}