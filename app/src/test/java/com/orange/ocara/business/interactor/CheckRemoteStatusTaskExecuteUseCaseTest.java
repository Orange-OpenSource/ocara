/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2020 Orange
 * SPDX-License-Identifier: MPL-2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.business.interactor;

import com.orange.ocara.business.repository.HealthRepository;
import com.orange.ocara.data.common.ConnectorException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static com.orange.ocara.business.interactor.CheckRemoteStatusTask.CheckRemoteStatusRequest;
import static com.orange.ocara.business.interactor.CheckRemoteStatusTask.CheckRemoteStatusResponse;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CheckRemoteStatusTaskExecuteUseCaseTest {

    private CheckRemoteStatusTask subject;

    private HealthRepository repository;

    @Before
    public void setUp() {

        repository = Mockito.mock(HealthRepository.class);

        subject = new CheckRemoteStatusTask(repository);
    }

    @Test
    public void shouldReturnSuccessResponseWhenPingSucceeds() {

        // Given
        CheckRemoteStatusRequest inputRequest = new CheckRemoteStatusRequest();
        UseCase.UseCaseCallback<CheckRemoteStatusResponse> inputCallback = mock(UseCase.UseCaseCallback.class);

        // when
        subject.executeUseCase(inputRequest, inputCallback);

        // then
        verify(inputCallback).onComplete(argThat(argument -> argument.isSuccess()));
    }

    @Test
    public void shouldReturnFailureResponseWhenPingFails() {

        // Given
        Mockito.doThrow(new ConnectorException()).when(repository).ping();
        CheckRemoteStatusRequest inputRequest = new CheckRemoteStatusRequest();
        UseCase.UseCaseCallback<CheckRemoteStatusResponse> inputCallback = mock(UseCase.UseCaseCallback.class);

        // when
        subject.executeUseCase(inputRequest, inputCallback);

        // then
        verify(inputCallback).onComplete(argThat(argument -> !argument.isSuccess()));
    }
}