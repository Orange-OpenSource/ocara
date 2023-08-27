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


package com.orange.ocara.domain.models;


public class AuditSortConfig {

    private static final String ORDER_ASC = "ASC";
    private static final String ORDER_DESC = "DESC";
    private final Type type;
    private boolean ascending = false;

    public AuditSortConfig(AuditSortConfig.Type type, boolean ascending) {
        this.type = type;
        this.ascending = ascending;
    }

    public Type getType() {
        return type;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public void toggle() {
        this.ascending = !ascending;
    }



    public enum Type {
        NAME, STATUS, SITE, DATE
    }
}
