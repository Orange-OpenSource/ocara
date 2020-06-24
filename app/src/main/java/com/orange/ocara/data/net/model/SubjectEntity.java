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

package com.orange.ocara.data.net.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** amodel for cached subjects */
@Table(name = SubjectEntity.TABLE_NAME, id = SubjectEntity.ID)
@Data
@EqualsAndHashCode(callSuper = false)
public class SubjectEntity extends Model implements Serializable {

    public static final String TABLE_NAME = "subject";
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String QUESTION = "question";

    @SerializedName(NAME)
    @Expose
    @Column(name = NAME)
    public String name;

    @Column(name = QUESTION)
    public QuestionEntity question;

    public static SubjectEntity toEntity(SubjectWs in) {

        SubjectEntity out = new SubjectEntity();
        out.setName(in.getName());
        return out;
    }
}
