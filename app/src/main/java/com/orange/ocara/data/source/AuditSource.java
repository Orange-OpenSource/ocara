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

/** Contract about audits between the cache and the repository */
public interface AuditSource {

    /**
     * description of the access to the local database
     */
    interface AuditCache {

        /**
         * retrieves one {@link AuditEntity}
         *
         * @param auditId an identifier for an {@link AuditEntity}
         * @return the found item. null, if none exists.
         */
        AuditEntity findOne(Long auditId);

        /**
         * retrieves a copy of the audit that matches the argument
         *
         * @param auditId an identifier for an {@link AuditEntity}
         * @return the copy
         */
        AuditEntity cloneOne(Long auditId);
    }

    /**
     * description of the repository
     */
    interface AuditDataStore {

        /**
         * retrieves one {@link AuditEntity}
         *
         * @param auditId an identifier for an {@link AuditEntity}
         * @return the found item. null, if none exists.
         */
        AuditEntity findOne(Long auditId);

        /**
         * retrieves a copy of the audit that matches the argument
         *
         * @param auditId an identifier for an {@link AuditEntity}
         * @return the copy
         */
        AuditEntity cloneOne(Long auditId);
    }
}
