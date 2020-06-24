package com.orange.ocara.business.interactor;

import com.orange.ocara.business.binding.BizError;
import com.orange.ocara.business.repository.InitializableRepository;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.mockito.ArgumentCaptor.forClass;
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
public class InitTaskExecuteUseCaseTest {

    private InitTask subject;

    private void when(UseCase.UseCaseCallback callback) {
        InitTask.InitRequest request = new InitTask.InitRequest();

        // When
        subject.executeUseCase(request, callback);
    }

    @Test
    public void shouldCallOnCompleteWhenSubjectHasNoRepositoryToInitialize() {

        // given
        UseCase.UseCaseCallback callback = mock(UseCase.UseCaseCallback.class);

        subject = new InitTask();

        // When
        when(callback);

        // Then
        verify(callback, never()).onError(ArgumentMatchers.any(BizError.class));
        ArgumentCaptor<InitTask.InitRequest> argumentCaptor = forClass(InitTask.InitRequest.class);
        verify(callback).onComplete(argumentCaptor.capture());
    }

    @Test
    public void shouldCallOnCompleteWhenSubjectHasOneRepositoryToInitialize() {

        // given
        UseCase.UseCaseCallback callback = mock(UseCase.UseCaseCallback.class);

        InitializableRepository repository = Mockito.mock(InitializableRepository.class);

        subject = new InitTask(repository);

        // When
        when(callback);

        // Then
        verify(repository).init();
        verify(callback, never()).onError(ArgumentMatchers.any(BizError.class));
        ArgumentCaptor<InitTask.InitRequest> argumentCaptor = forClass(InitTask.InitRequest.class);
        verify(callback).onComplete(argumentCaptor.capture());
    }

    @Test
    public void shouldCallOnCompleteWhenSubjectHasTwoRepositoryThatSucceedToInitialize() {

        // given
        UseCase.UseCaseCallback callback = mock(UseCase.UseCaseCallback.class);

        InitializableRepository repository = Mockito.mock(InitializableRepository.class);
        InitializableRepository otherRepository = Mockito.mock(InitializableRepository.class);

        subject = new InitTask(repository, otherRepository);

        // When
        when(callback);

        // Then
        verify(repository).init();
        verify(otherRepository).init();
        verify(callback, never()).onError(ArgumentMatchers.any(BizError.class));
        ArgumentCaptor<InitTask.InitRequest> argumentCaptor = forClass(InitTask.InitRequest.class);
        verify(callback).onComplete(argumentCaptor.capture());
    }

    @Test
    public void shouldCallOnErrorWhenSubjectHasAtLeastOneRepositoryThatFailsToInitialize() {

        // given
        UseCase.UseCaseCallback callback = mock(UseCase.UseCaseCallback.class);

        InitializableRepository repository = Mockito.mock(InitializableRepository.class);
        doThrow(RuntimeException.class).when(repository).init();

        InitializableRepository otherRepository = Mockito.mock(InitializableRepository.class);

        subject = new InitTask(repository, otherRepository);

        // When
        when(callback);

        // Then
        verify(callback).onError(ArgumentMatchers.any());
        verify(callback, never()).onComplete(ArgumentMatchers.any());
    }
}