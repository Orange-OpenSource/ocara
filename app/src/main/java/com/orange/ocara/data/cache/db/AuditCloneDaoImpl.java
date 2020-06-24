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

package com.orange.ocara.data.cache.db;

import com.orange.ocara.data.cache.model.AuditEntity;

import lombok.RequiredArgsConstructor;

/**
 * default implementation for {@link AuditCloneDao}
 */
@RequiredArgsConstructor
public class AuditCloneDaoImpl implements AuditCloneDao {

    /**
     * a repository
     */
    private final ModelManager modelManager;

    /**
     * Creates a new version of an {@link AuditEntity}
     *
     * The function {@link ModelManager#createAuditWithNewVersion(AuditEntity)} and its sub-functions are
     * too much complicated. So we put it here, until some hardcare developer is interested in their
     * refactoring. Good luck, dude !
     *
     *
     * @param auditId
     * @return the clone
     */
    @Override
    public AuditEntity clone(Long auditId) {
        AuditEntity audit = modelManager.getAudit(auditId);
        return modelManager.createAuditWithNewVersion(audit);
    }
}
