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

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import lombok.Builder;
import lombok.EqualsAndHashCode;

@Builder
@Table(name = AuditorEntity.TABLE_NAME)
@EqualsAndHashCode(callSuper = true)
public class AuditorEntity extends Model {

    public static final String COLUMN_USER_NAME = "userName";
    static final String TABLE_NAME = "auditors";
    @Column(name = COLUMN_USER_NAME, notNull = true, unique = true)
    private String userName;

    public AuditorEntity(String userName) {
        this.userName = userName;
    }

    /**
     * Default constructor. Needed by ORM.
     */
    public AuditorEntity() {
        super();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}