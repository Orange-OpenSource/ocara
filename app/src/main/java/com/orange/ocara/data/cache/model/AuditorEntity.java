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

package com.orange.ocara.data.cache.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.orange.ocara.business.model.UserModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@Data
@Table(name = AuditorEntity.TABLE_NAME)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class AuditorEntity extends Model implements UserModel {

    static final String TABLE_NAME = "auditors";

    public static final String COLUMN_USER_NAME = "userName";

    @Column(name = COLUMN_USER_NAME, notNull = true, unique = true)
    private String userName;

    /**
     * Default constructor. Needed by ORM.
     */
    public AuditorEntity() {
        super();
    }
}