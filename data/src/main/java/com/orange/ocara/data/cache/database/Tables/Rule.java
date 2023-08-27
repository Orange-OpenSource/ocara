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

@Entity(tableName = "rule")
public class Rule {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "ruleRef")
    private String reference;

    private String label;
    private String origin;
    private String date;

    public Rule(@NonNull String reference, String label, String origin, String date) {
        this.reference = reference;
        this.label = label;
        this.origin = origin;
        this.date = date;
    }

    @NonNull
    public String getReference() {
        return reference;
    }

    public void setReference(@NonNull String reference) {
        this.reference = reference;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static class RuleBuilder {
        private String reference;
        private String label;
        private String origin;
        private String date;

        private RuleBuilder() {
        }

        public static RuleBuilder aRule() {
            return new RuleBuilder();
        }

        public RuleBuilder withReference(String reference) {
            this.reference = reference;
            return this;
        }

        public RuleBuilder withLabel(String label) {
            this.label = label;
            return this;
        }

        public RuleBuilder withOrigin(String origin) {
            this.origin = origin;
            return this;
        }

        public RuleBuilder withDate(String date) {
            this.date = date;
            return this;
        }

        public Rule build() {
            return new Rule(reference, label, origin, date);
        }
    }

}
