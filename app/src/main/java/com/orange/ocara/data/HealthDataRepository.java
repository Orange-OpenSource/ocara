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

import com.orange.ocara.business.repository.HealthRepository;
import com.orange.ocara.data.common.ConnectorException;


/**
 * default implementation of {@link HealthRepository}
 */
public class HealthDataRepository implements HealthRepository {

    private final boolean connected;

    HealthDataRepository(boolean connected) {
        this.connected = connected;
    }

    @Override
    public void ping() {
        if (!connected) {
            throw new ConnectorException("offline");
        }
    }
}
