/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table(name = Site.TABLE_NAME)
@EqualsAndHashCode(callSuper=true)
public class Site extends Model {

    static final String TABLE_NAME = "sites";

    static final String COLUMN_NOIMMO = "NOIMMO";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_UGI = "UGI";
    static final String COLUMN_LABEL_UGI = "labelUGI";
    static final String COLUMN_ADDR_STREET = "addr_street";
    static final String COLUMN_ADDR_CITY = "addr_city";
    static final String COLUMN_ADDR_CODE = "addr_code";

    @Column(name = COLUMN_NOIMMO, notNull = false, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String noImmo; // must be a string because some number could contain letters (for Corse)
    @Column(name = COLUMN_NAME, notNull = true)
    private String name;
    @Column(name = COLUMN_UGI, notNull = false)
    private String ugi;
    @Column(name = COLUMN_LABEL_UGI, notNull = false)
    private String labelUgi;
    @Column(name = COLUMN_ADDR_STREET, notNull = false)
    private String street;
    @Column(name = COLUMN_ADDR_CODE, notNull = false)
    private Integer code;
    @Column(name = COLUMN_ADDR_CITY, notNull = false)
    private String city;

    /**
     * Default constructor. Needed by ORM.
     */
    public Site() {
        super();
    }
}