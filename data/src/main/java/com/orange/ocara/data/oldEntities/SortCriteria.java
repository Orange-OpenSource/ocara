/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.data.oldEntities;

import com.activeandroid.query.From;

import lombok.Data;

/** utility class that helps on building sorting criteria */
@Data
public abstract class SortCriteria {

    private static final String ORDER_ASC = "ASC";
    private static final String ORDER_DESC = "DESC";
    private final Type type;
    private boolean ascending = false;

    /**
     * Constructor with default sort order.
     *
     * @param type      Type
     * @param ascending true for ascending, false for descending
     */
    private SortCriteria(Type type, boolean ascending) {
        this.type = type;
        this.ascending = ascending;
    }

    /**
     * @return true if sort order is descending
     */
    public boolean isDescending() {
        return !isAscending();
    }

    /**
     * To toggle the sort order.
     */
    public void toggleOrder() {
        ascending = !ascending;
    }

    public abstract From upgrade(From from);

    private String formatOrder() {
        return ascending ? ORDER_ASC : ORDER_DESC;
    }

    String formatOrderBy(String column) {
        return formatOrderBy(column, formatOrder());
    }

    String formatOrderBy(String column, String order) {
        return String.format("%s %s", column, order);
    }

    public enum Type {

        NAME {
            @Override
            public SortCriteria build() {
                return new SortCriteria(NAME, true) {
                    @Override
                    public From upgrade(From from) {
                        return from.orderBy(AuditEntity.COLUMN_NAME + ", " + formatOrderBy(AuditEntity.COLUMN_DATE, ORDER_DESC));
                    }
                };
            }
        },


        STATUS {
            @Override
            public SortCriteria build() {
                return new SortCriteria(STATUS, true) {
                    @Override
                    public From upgrade(From from) {
                        return from.orderBy(formatOrderBy(AuditEntity.COLUMN_STATUS) + ", " + formatOrderBy(AuditEntity.COLUMN_DATE, ORDER_DESC));
                    }
                };
            }
        },

        SITE {
            @Override
            public SortCriteria build() {
                return new SortCriteria(SITE, true) {
                    @Override
                    public From upgrade(From from) {
                        return from.join(SiteEntity.class).on(AuditEntity.TABLE_NAME + "." + AuditEntity.COLUMN_SITE + "=" + SiteEntity.TABLE_NAME + ".Id").
                                orderBy(formatOrderBy(SiteEntity.TABLE_NAME + "." + SiteEntity.COLUMN_NAME));
                    }
                };
            }
        },

        DATE {
            @Override
            public SortCriteria build() {
                return new SortCriteria(DATE, false) {
                    @Override
                    public From upgrade(From from) {
                        return from.orderBy(formatOrderBy(AuditEntity.COLUMN_DATE));
                    }
                };
            }
        };

        public abstract SortCriteria build();
    }
}