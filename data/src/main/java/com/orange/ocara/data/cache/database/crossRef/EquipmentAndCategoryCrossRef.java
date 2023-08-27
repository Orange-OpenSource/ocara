package com.orange.ocara.data.cache.database.crossRef;
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
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "objects_categories", primaryKeys = {"objectRef", "category_id"})
public class EquipmentAndCategoryCrossRef {
    @NonNull
    private String objectRef;
    @ColumnInfo(name = "category_id")
    private int categoryRef;

    public EquipmentAndCategoryCrossRef(@NonNull String objectRef, int categoryRef) {
        this.objectRef = objectRef;
        this.categoryRef = categoryRef;
    }

    @NonNull
    public String getObjectRef() {
        return objectRef;
    }

    public void setObjectRef(@NonNull String objectRef) {
        this.objectRef = objectRef;
    }

    public int getCategoryRef() {
        return categoryRef;
    }

    public void setCategoryRef(int categoryRef) {
        this.categoryRef = categoryRef;
    }


}
