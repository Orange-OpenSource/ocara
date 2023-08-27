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
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "EquipmentWithIllustrations")
public class EquipmentWithIllustrations {
    @PrimaryKey(autoGenerate = true)
    int id;
    @NonNull
    private String objectRef;
    @NonNull
    private String illustRef;
    private int version;
    private String rulesetRef;

    public EquipmentWithIllustrations(@NonNull String objectRef, @NonNull String illustRef, int version, String rulesetRef) {
        this.objectRef = objectRef;
        this.illustRef = illustRef;
        this.version = version;
        this.rulesetRef = rulesetRef;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getRulesetRef() {
        return rulesetRef;
    }

    public void setRulesetRef(String rulesetRef) {
        this.rulesetRef = rulesetRef;
    }

    @NonNull
    public String getObjectRef() {
        return objectRef;
    }

    public void setObjectRef(@NonNull String objectRef) {
        this.objectRef = objectRef;
    }

    @NonNull
    public String getIllustRef() {
        return illustRef;
    }

    public void setIllustRef(@NonNull String illustRef) {
        this.illustRef = illustRef;
    }
}
