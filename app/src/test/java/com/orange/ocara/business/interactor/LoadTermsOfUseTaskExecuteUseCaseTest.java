package com.orange.ocara.business.interactor;

import com.orange.ocara.business.binding.BizError;
import com.orange.ocara.business.repository.TermsRepository;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static com.orange.ocara.business.interactor.LoadTermsOfUseTask.LoadTermsOfUseRequest;
import static com.orange.ocara.business.interactor.LoadTermsOfUseTask.LoadTermsOfUseResponse;
import static com.orange.ocara.business.interactor.LoadTermsOfUseTask.UseCaseCallback;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * @see LoadTermsOfUseTask#executeUseCase(LoadTermsOfUseRequest, UseCaseCallback)
 */
public class LoadTermsOfUseTaskExecuteUseCaseTest {

    private LoadTermsOfUseTask subject;

    private TermsRepository service;

    @Before
    public void setUp() {
        service = mock(TermsRepository.class);

        subject = new LoadTermsOfUseTask(service);
    }

    @Test
    public void shouldCallOnErrorWhenExceptionIsThrown() {

        // Given
        doThrow(RuntimeException.class).when(service).markAsAccepted();

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
        Mockito.when(service.find()).thenReturn(RandomStringUtils.randomAlphanumeric(100));

        // When
        when(callback);

        // Then
        verify(callback, never()).onError(ArgumentMatchers.any(BizError.class));

        ArgumentCaptor<LoadTermsOfUseResponse> argumentCaptor = forClass(LoadTermsOfUseResponse.class);
        verify(callback).onComplete(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().isRaw()).isTrue();
    }

    private void when(UseCaseCallback callback) {
        LoadTermsOfUseRequest request = new LoadTermsOfUseRequest();

        // When
        subject.executeUseCase(request, callback);
    }
}