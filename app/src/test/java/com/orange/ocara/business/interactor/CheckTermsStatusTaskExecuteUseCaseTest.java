package com.orange.ocara.business.interactor;

import com.orange.ocara.business.binding.BizError;
import com.orange.ocara.business.repository.TermsRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Copyright (C) 2015 Orange
 * <p>
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
public class CheckTermsStatusTaskExecuteUseCaseTest {

    private CheckTermsStatusTask subject;

    private TermsRepository repository;

    @Before
    public void setUp() {
        repository = mock(TermsRepository.class);

        subject = new CheckTermsStatusTask(repository);
    }

    private void when(UseCase.UseCaseCallback callback) {

        subject.executeUseCase(
                new CheckTermsStatusTask.CheckTermsStatusRequest(),
                callback);
    }

    @Test
    public void shouldCallOnErrorWhenExceptionIsThrown() {

        // Given
        doThrow(RuntimeException.class).when(repository).checkTermsAreAccepted();

        UseCase.UseCaseCallback callback = mock(UseCase.UseCaseCallback.class);

        // When
        when(callback);

        // Then
        verify(callback).onError(ArgumentMatchers.any(BizError.class));
        verify(callback, never()).onComplete(ArgumentMatchers.any());
    }

    @Test
    public void shouldCallOnCompleteWithOkResponseWhenTermsAreAccepted() {

        // given
        doReturn(true).when(repository).checkTermsAreAccepted();

        UseCase.UseCaseCallback callback = mock(UseCase.UseCaseCallback.class);

        // when
        when(callback);

        // then
        ArgumentCaptor<CheckTermsStatusTask.CheckTermsStatusResponse> argumentCaptor = forClass(CheckTermsStatusTask.CheckTermsStatusResponse.class);
        verify(callback).onComplete(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().isAccepted()).isTrue();
    }

    @Test
    public void shouldCallOnCompleteWithKoResponseWhenTermsAreNotAccepted() {

        // given
        doReturn(false).when(repository).checkTermsAreAccepted();

        UseCase.UseCaseCallback callback = mock(UseCase.UseCaseCallback.class);

        // when
        when(callback);

        // then
        ArgumentCaptor<CheckTermsStatusTask.CheckTermsStatusResponse> argumentCaptor = forClass(CheckTermsStatusTask.CheckTermsStatusResponse.class);
        verify(callback).onComplete(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().isAccepted()).isFalse();
    }
}