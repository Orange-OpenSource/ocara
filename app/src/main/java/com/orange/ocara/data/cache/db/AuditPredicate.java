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

import com.activeandroid.query.From;

/**
 * Behaviour of predicates for ORM querying
 */
public interface AuditPredicate {

    /**
     * Helper function for {@link AuditPredicateBuilder}
     *
     * @param from a {@link From}
     * @return another {@link From}
     */
    From accept(From from);
}
