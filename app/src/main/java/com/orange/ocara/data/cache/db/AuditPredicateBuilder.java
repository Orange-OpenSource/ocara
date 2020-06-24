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
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.tools.StringUtils;

/**
 * a builder for {@link AuditPredicate}s
 */
public class AuditPredicateBuilder implements AuditPredicate {

    private boolean withName = false;
    private String name;

    private boolean withReferenceAndVersion = false;
    private String rulesetReference;
    private Integer rulesetVersion;

    private static final String IS_LIKE = " LIKE ? ";

    public AuditPredicateBuilder name(String value) {
        withName = StringUtils.isNotBlank(value);
        name = value;
        return this;
    }

    public AuditPredicateBuilder rulesetReferenceVersion(String reference, Integer version) {
        withReferenceAndVersion =  StringUtils.isNotBlank(reference) && version > 0;
        rulesetReference = reference;
        rulesetVersion = version;
        return this;
    }

    /**
     * Transforms the given {@link From}
     *
     * @param from a {@link From}
     * @return an updated {@link From}
     */
    public From accept(From from) {
        if (withName) {
            from = from
                    .and(AuditEntity.TABLE_NAME + "." + AuditEntity.COLUMN_NAME + IS_LIKE, "%" + name + "%");
        }

        if (withReferenceAndVersion) {
            from = from
                    .and(AuditEntity.TABLE_NAME + "." + AuditEntity.COLUMN_RULESET_ID + IS_LIKE, "%" + rulesetReference + "%")
                    .and(AuditEntity.TABLE_NAME + "." + AuditEntity.COLUMN_RULESET_VERSION + IS_LIKE, "%" + rulesetVersion + "%");
        }

        return from;
    }
}
