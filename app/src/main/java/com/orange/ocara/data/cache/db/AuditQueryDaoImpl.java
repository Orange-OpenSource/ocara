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

import com.activeandroid.annotation.Table;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.cache.model.SortCriteria;

import java.util.List;

import static com.orange.ocara.tools.ListUtils.emptyList;
import static com.orange.ocara.tools.ListUtils.newArrayList;
import static timber.log.Timber.v;

/**
 * default implementation for {@link AuditQueryDao}
 */
public class AuditQueryDaoImpl implements AuditQueryDao {

    private final static String WHERE_CLAUSE = Table.DEFAULT_ID_NAME + "=?";

    @Override
    public AuditEntity findOne(Long auditId) {
        return new Select()
                .from(AuditEntity.class)
                .where(WHERE_CLAUSE, auditId)
                .executeSingle();
    }

    @Override
    public boolean exists(Long auditId) {
        return new Select()
                .from(AuditEntity.class)
                .where(WHERE_CLAUSE, auditId)
                .exists();
    }

    @Override
    public List<AuditEntity> findAll() {
        return findAll(null, SortCriteria.Type.NAME.build(), 0, 0);
    }

    @Override
    public List<AuditEntity> findAll(SortCriteria sort) {

        return findAll(null, sort, 0, 0);
    }

    @Override
    public List<AuditEntity> findAll(AuditPredicate predicate, SortCriteria sort, int fromIndex, int toIndex) {
        long now = System.currentTimeMillis();
        From from = new Select()
                .all()
                .from(AuditEntity.class);

        if (fromIndex > 0) {
            from = from
                    .offset(fromIndex);
        }

        if (toIndex > fromIndex) {
            from = from
                .limit(toIndex);
        }

        if (predicate != null) {
            from = predicate.accept(from);
        }

        if (sort != null) {
            from = sort.upgrade(from);
        }

        List<AuditEntity> result = from.execute();
        List<AuditEntity> audits;
        if (result == null) {
            audits = emptyList();
        } else {
            audits = newArrayList(result);
        }

        v("select all audits done in %d", System.currentTimeMillis() - now);
        return audits;
    }
}
