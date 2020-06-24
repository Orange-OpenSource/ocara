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
import com.orange.ocara.data.cache.model.SortCriteria;

import java.util.List;

/**
 * Behaviour of the {@link AuditEntity} cache
 */
public interface AuditQueryDao {

    /**
     * Retrieves an {@link AuditEntity}
     *
     * @param auditId an identifier for an {@link AuditEntity}
     * @return an audit that matches the argument. null, if none exists.
     */
    AuditEntity findOne(Long auditId);

    /**
     * Checks that an {@link AuditEntity} exists.
     *
     * @param auditId an identifier for an {@link AuditEntity}
     * @return true if an {@link AuditEntity} matches.
     */
    boolean exists(Long auditId);

    /**
     * retrieves all the {@link AuditEntity}s
     *
     * @return a {@link List} of {@link AuditEntity}s
     */
    List<AuditEntity> findAll();

    /**
     * retrieves all the {@link AuditEntity}s, following the sorting argument
     *
     * @param sort a sorting criterion
     * @return a {@link List} of {@link AuditEntity}s
     */
    List<AuditEntity> findAll(SortCriteria sort);

    /**
     * retrieves all the {@link AuditEntity}s that match the arguments
     *
     * @param predicate a bunch of filters
     * @param sort a sorting criterion
     * @param fromIndex an offset value for the count of elements
     * @param toIndex a limit value for the count of elements
     * @return a {@link List} of {@link AuditEntity}s
     */
    List<AuditEntity> findAll(AuditPredicate predicate, SortCriteria sort, int fromIndex, int toIndex);
}
