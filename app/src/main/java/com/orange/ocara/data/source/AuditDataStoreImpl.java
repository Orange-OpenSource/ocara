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

package com.orange.ocara.data.source;

import com.orange.ocara.data.cache.model.AuditEntity;

public class AuditDataStoreImpl implements AuditSource.AuditDataStore {

    private final AuditSource.AuditCache auditCache;

    public AuditDataStoreImpl(AuditSource.AuditCache auditCache) {
        this.auditCache = auditCache;
    }

    @Override
    public AuditEntity findOne(Long auditId) {
        return auditCache.findOne(auditId);
    }

    @Override
    public AuditEntity cloneOne(Long auditId) {
        return auditCache.cloneOne(auditId);
    }
}
