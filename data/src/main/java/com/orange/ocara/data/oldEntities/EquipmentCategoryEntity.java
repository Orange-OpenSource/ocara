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
import com.orange.ocara.data.network.models.EquipmentCategoryWs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/** a model for cached categories of equipments (aka Category) */
@Table(name = EquipmentCategoryEntity.TABLE_NAME, id = EquipmentCategoryEntity.ID)
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class EquipmentCategoryEntity extends Model implements Serializable {

    public static final String TABLE_NAME = "objectDescriptions";
    public static final String NAME = "name";
    public static final String ID = "_id";
    private final static long serialVersionUID = 678196231838611480L;

    @SerializedName(NAME)
    @Expose
    @Column(name = NAME)
    public String name;

    private final List<EquipmentEntity> objects = new ArrayList<>();

    static EquipmentCategoryEntity from(EquipmentCategoryWs in) {

        EquipmentCategoryEntity out = null;
        if (in != null) {
            out = new EquipmentCategoryEntity();
            out.setName(in.getName());
        }
        return out;
    }
}
