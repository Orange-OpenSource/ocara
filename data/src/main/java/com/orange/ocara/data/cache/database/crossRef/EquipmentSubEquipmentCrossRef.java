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

/*
an equipment might have a child in one ruleset but
doesn't have it in another ruleset or in another version
 */
@Entity(tableName = "equipment_subequipments", primaryKeys = {"parentRef"
        , "childRef", "rulesetRef", "version"})
public class EquipmentSubEquipmentCrossRef {
    @NonNull
    private String parentRef;
    @NonNull
    private String childRef;
    @NonNull
    private String rulesetRef;
    private int version;

    public EquipmentSubEquipmentCrossRef(@NonNull String parentRef, @NonNull String childRef, @NonNull String rulesetRef, int version) {
        this.parentRef = parentRef;
        this.childRef = childRef;
        this.rulesetRef = rulesetRef;
        this.version = version;
    }

    @NonNull
    public String getParentRef() {
        return parentRef;
    }

    public void setParentRef(@NonNull String parentRef) {
        this.parentRef = parentRef;
    }

    @NonNull
    public String getChildRef() {
        return childRef;
    }

    public void setChildRef(@NonNull String childRef) {
        this.childRef = childRef;
    }

    @NonNull
    public String getRulesetRef() {
        return rulesetRef;
    }

    public void setRulesetRef(@NonNull String rulesetRef) {
        this.rulesetRef = rulesetRef;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public static class EquipmentSubEquipmentCrossRefBuilder {
        private String parentRef;
        private String childRef;
        private String rulesetRef;
        private int version;

        private EquipmentSubEquipmentCrossRefBuilder() {
        }

        public static EquipmentSubEquipmentCrossRefBuilder anEquipmentSubEquipmentCrossRef() {
            return new EquipmentSubEquipmentCrossRefBuilder();
        }

        public EquipmentSubEquipmentCrossRefBuilder withParentRef(String parentRef) {
            this.parentRef = parentRef;
            return this;
        }

        public EquipmentSubEquipmentCrossRefBuilder withChildRef(String childRef) {
            this.childRef = childRef;
            return this;
        }

        public EquipmentSubEquipmentCrossRefBuilder withRulesetRef(String rulesetRef) {
            this.rulesetRef = rulesetRef;
            return this;
        }

        public EquipmentSubEquipmentCrossRefBuilder withVersion(int version) {
            this.version = version;
            return this;
        }

        public EquipmentSubEquipmentCrossRef build() {
            return new EquipmentSubEquipmentCrossRef(parentRef, childRef, rulesetRef, version);
        }
    }

}
