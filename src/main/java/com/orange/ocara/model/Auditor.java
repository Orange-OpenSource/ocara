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
@Table(name = Auditor.TABLE_NAME)
@EqualsAndHashCode(callSuper=true)
public class Auditor extends Model {

    static final String TABLE_NAME = "auditors";

    static final String COLUMN_USER_NAME = "userName";

    @Column(name = COLUMN_USER_NAME, notNull = true, unique = true)
    private String userName;

    /**
     * Default constructor. Needed by ORM.
     */
    public Auditor() {
        super();
    }
}