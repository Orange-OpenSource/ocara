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

@Entity(tableName = "impact_values")
public class ImpactValue {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "impactRef")
    private String reference;
    private String name;
    private boolean editable;

    public ImpactValue(@NonNull String reference, String name, boolean editable) {
        this.reference = reference;
        this.name = name;
        this.editable = editable;
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

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public static class ImpactValueBuilder {
        private String reference;
        private String name;
        private boolean editable;

        private ImpactValueBuilder() {
        }

        public static ImpactValueBuilder anImpactValue() {
            return new ImpactValueBuilder();
        }

        public ImpactValueBuilder withReference(String reference) {
            this.reference = reference;
            return this;
        }

        public ImpactValueBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ImpactValueBuilder withEditable(boolean editable) {
            this.editable = editable;
            return this;
        }

        public ImpactValue build() {
            return new ImpactValue(reference, name, editable);
        }
    }

}
