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

/**
 * Behaviour of cache that can produce some clones of audits
 */
public interface AuditCloneDao {

    /**
     * Creates a new version of an {@link AuditEntity}
     *
     * @param auditId an identifier for an audit
     * @return the clone
     */
    AuditEntity clone(Long auditId);
}
