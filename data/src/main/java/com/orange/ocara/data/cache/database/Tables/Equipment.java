package com.orange.ocara.data.cache.database.Tables;
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
import androidx.room.PrimaryKey;

@Entity(tableName = "objects")
public class Equipment {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "objectReference")
    private String reference;
    @ColumnInfo(name = "equipment_name")
    private String name;
    private String icon;
    private String type;
    private String definition;
    @ColumnInfo(name = "equipment_date")
    private String date;
    public Equipment(@NonNull String reference, String name, String icon, String type, String definition, String date) {
        this.reference = reference;
        this.name = name;
        this.icon = icon;
        this.type = type;
        this.definition = definition;
        this.date = date;
    }

    @NonNull
    public String getReference() {
        return reference;
    }

    public void setReference(@NonNull String reference) {
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static class EquipmentBuilder {
        private String reference;
        private String name;
        private String icon;
        private String type;
        private String definition;
        private String date;

        private EquipmentBuilder() {
        }

        public static EquipmentBuilder anEquipment() {
            return new EquipmentBuilder();
        }

        public EquipmentBuilder withReference(String reference) {
            this.reference = reference;
            return this;
        }

        public EquipmentBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public EquipmentBuilder withIcon(String icon) {
            this.icon = icon;
            return this;
        }

        public EquipmentBuilder withType(String type) {
            this.type = type;
            return this;
        }

        public EquipmentBuilder withDefinition(String definition) {
            this.definition = definition;
            return this;
        }

        public EquipmentBuilder withDate(String date) {
            this.date = date;
            return this;
        }

        public Equipment build() {
            return new Equipment(reference, name, icon, type, definition, date);
        }
    }

}
