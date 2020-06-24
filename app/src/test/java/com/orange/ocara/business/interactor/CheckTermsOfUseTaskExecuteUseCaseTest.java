package com.orange.ocara.business.interactor;

import com.orange.ocara.business.binding.BizError;
import com.orange.ocara.business.interactor.CheckTermsOfUseTask.CheckTermsOfUseResponse;
import com.orange.ocara.business.repository.TermsRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;

import static org.apache.commons.lang3.RandomUtils.nextBoolean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * @see CheckTermsOfUseTask#executeUseCase(CheckTermsOfUseTask.CheckTermsOfUseRequest, UseCase.UseCaseCallback)
 */
public class CheckTermsOfUseTaskExecuteUseCaseTest {

    private CheckTermsOfUseTask subject;

    private TermsRepository repository;

    @Before
    public void setUp() {
        repository = mock(TermsRepository.class);

        subject = new CheckTermsOfUseTask(repository);
    }

    @Test
    public void shouldCallOnErrorWhenExceptionIsThrown() {

        // Given
        doThrow(RuntimeException.class).when(repository).checkTermsAreDefined();

        UseCase.UseCaseCallback callback = mock(UseCase.UseCaseCallback.class);

        // When
        when(callback);

        // Then
        verify(callback).onError(ArgumentMatchers.any(BizError.class));
        verify(callback, never()).onComplete(ArgumentMatchers.any());
    }

    @Test
    public void shouldCallOnCompleteWhenResponseIsOk() {

        // Given
        doReturn(true).when(repository).checkTermsAreDefined();
        doReturn(true).when(repository).checkTermsAreAccepted();

        UseCase.UseCaseCallback callback = mock(UseCase.UseCaseCallback.class);

        // When
        when(callback);

        // Then
        verify(callback, never()).onError(ArgumentMatchers.any(BizError.class));

        ArgumentCaptor<CheckTermsOfUseResponse> argumentCaptor = forClass(CheckTermsOfUseResponse.class);
        verify(callback).onComplete(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().isDefined()).isTrue();
        assertThat(argumentCaptor.getValue().isAccepted()).isTrue();
        assertThat(argumentCaptor.getValue().isDeclined()).isFalse();
    }

    @Test
    public void shouldCallOnCompleteWhenResponseIsKo() {

        // Given
        doReturn(true).when(repository).checkTermsAreDefined();
        doReturn(false).when(repository).checkTermsAreAccepted();

        UseCase.UseCaseCallback callback = mock(UseCase.UseCaseCallback.class);

        // When
        when(callback);

        // Then
        verify(callback, never()).onError(ArgumentMatchers.any(BizError.class));

        ArgumentCaptor<CheckTermsOfUseResponse> argumentCaptor = forClass(CheckTermsOfUseResponse.class);
        verify(callback).onComplete(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().isDefined()).isTrue();
        assertThat(argumentCaptor.getValue().isAccepted()).isFalse();
        assertThat(argumentCaptor.getValue().isDeclined()).isTrue();
    }

    @Test
    public void shouldCallOnCompleteWhenResponseIsNotDefined() {

        // Given
        doReturn(false).when(repository).checkTermsAreDefined();
        doReturn(nextBoolean()).when(repository).checkTermsAreAccepted();

        UseCase.UseCaseCallback callback = mock(UseCase.UseCaseCallback.class);

        // When
        when(callback);

        // Then
        verify(callback, never()).onError(ArgumentMatchers.any(BizError.class));

        ArgumentCaptor<CheckTermsOfUseResponse> argumentCaptor = forClass(CheckTermsOfUseResponse.class);
        verify(callback).onComplete(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().isDefined()).isFalse();
        assertThat(argumentCaptor.getValue().isAccepted()).isFalse();
        assertThat(argumentCaptor.getValue().isDeclined()).isFalse();
    }

    private void when(UseCase.UseCaseCallback callback) {

        subject.executeUseCase(
                new CheckTermsOfUseTask.CheckTermsOfUseRequest(),
                callback);
    }

}