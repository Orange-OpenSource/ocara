/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2020 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.data;

import com.orange.ocara.business.repository.AuditRepository;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.source.AuditSource;

import lombok.RequiredArgsConstructor;

/**
 *
 * default implementation of {@link AuditRepository}
 */
@RequiredArgsConstructor
public class AuditDataRepository implements AuditRepository {

    private final AuditSource.AuditDataStore dataStore;

    @Override
    public AuditEntity findOne(Long auditId) {
        return dataStore.findOne(auditId);
    }

    @Override
    public AuditEntity cloneOne(Long auditId) {
        return dataStore.cloneOne(auditId);
    }
}
