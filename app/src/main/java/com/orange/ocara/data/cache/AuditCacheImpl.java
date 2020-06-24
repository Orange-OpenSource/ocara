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

package com.orange.ocara.data.cache;

import com.orange.ocara.data.cache.db.AuditCloneDao;
import com.orange.ocara.data.cache.db.AuditQueryDao;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.source.AuditSource;

/** default implementation of {@link com.orange.ocara.data.source.AuditSource.AuditCache} */
public class AuditCacheImpl implements AuditSource.AuditCache {

    private final AuditQueryDao queryDao;

    private final AuditCloneDao cloneDao;

    AuditCacheImpl(AuditQueryDao queryDao, AuditCloneDao cloneDao) {
        this.queryDao = queryDao;
        this.cloneDao = cloneDao;
    }

    @Override
    public AuditEntity findOne(Long auditId) {
        return queryDao.findOne(auditId);
    }

    @Override
    public AuditEntity cloneOne(Long auditId) {
        return cloneDao.clone(auditId);
    }
}
