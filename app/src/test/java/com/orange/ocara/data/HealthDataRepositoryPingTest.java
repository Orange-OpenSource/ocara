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

package com.orange.ocara.data;

import com.orange.ocara.data.common.ConnectorException;

import org.junit.Test;

public class HealthDataRepositoryPingTest {

    private HealthDataRepository subject;

    @Test
    public void shouldDoNothingWhenInputIsTrue() {

        // given
        subject = new HealthDataRepository(true);

        // when
        subject.ping();

        // then
    }

    @Test(expected = ConnectorException.class)
    public void shouldThrowExceptionWhenInputIsFalse() {

        subject = new HealthDataRepository(false);

        // when
        subject.ping();

    }
}