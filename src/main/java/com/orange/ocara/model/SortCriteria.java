/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model;

import com.activeandroid.query.From;

import lombok.Data;

@Data
public abstract class SortCriteria {

    public enum Type {

        STATUS {
            @Override
            public SortCriteria build() {
                return new SortCriteria(STATUS, true) {
                    @Override
                    public From upgrade(From from) {
                        return from.orderBy(formatOrderBy(Audit.COLUMN_STATUS) + ", " + formatOrderBy(Audit.COLUMN_DATE, ORDER_DESC));
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
                        return from.join(Site.class).on(Audit.TABLE_NAME + "." + Audit.COLUMN_SITE + "=" + Site.TABLE_NAME + ".Id").
                                orderBy(formatOrderBy(Site.TABLE_NAME + "." + Site.COLUMN_NAME));
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
                        return from.orderBy(formatOrderBy(Audit.COLUMN_DATE));
                    }
                };
            }
        };

        public abstract SortCriteria build();
    }

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

    protected String formatOrder() {
        return ascending ? ORDER_ASC : ORDER_DESC;
    }

    protected String formatOrderBy(String column) {
        return formatOrderBy(column, formatOrder());
    }

    protected String formatOrderBy(String column, String order) {
        return String.format("%s %s", column, order);
    }
}