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
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orange.ocara.data.network.models.AuthorWs;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** a model for cached authors (aka UserCredentials) */
@Table(name = AuthorEntity.TABLE_NAME, id = AuthorEntity.ID)
@Data
@EqualsAndHashCode(callSuper = false)
public class AuthorEntity extends Model implements Serializable {

    public static final String TABLE_NAME = "usercred";
    public static final String ID = "_id";
    public static final String USERNAME = "username";

    @SerializedName(USERNAME)
    @Expose
    @Column(name = USERNAME)
    public String username;

    public AuthorEntity() {
    }

    static AuthorEntity toEntity(AuthorWs input) {

        AuthorEntity output = new AuthorEntity();
        output.setUsername(input.getUsername());
        return output;
    }
}
