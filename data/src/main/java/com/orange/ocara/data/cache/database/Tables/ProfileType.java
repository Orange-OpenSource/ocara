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

@Entity(tableName = "profile_type")
public class ProfileType {
    private String name;
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "profileRef")
    private String reference;
    private String icon;


//    @ColumnInfo(name = "profile_type_ruleset")
//    private String rulesetRef;
//    @ColumnInfo(name = "profile_type_ruleset_ver")
//    private int rulesetVer;

    public ProfileType(String name, @NonNull String reference, String icon) {
        this.name = name;
        this.reference = reference;
        this.icon = icon;
    }

//    public int getRulesetVer() {
//        return rulesetVer;
//    }
//
//    public void setRulesetVer(int rulesetVer) {
//        this.rulesetVer = rulesetVer;
//    }
//
//    public String getRulesetRef() {
//        return rulesetRef;
//    }
//
//    public void setRulesetRef(String rulesetRef) {
//        this.rulesetRef = rulesetRef;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    public String getReference() {
        return reference;
    }

    public void setReference(@NonNull String reference) {
        this.reference = reference;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public static class ProfileTypeBuilder {
        private String name;
        private String reference;
        private String icon;
        private int rulesetVer;
        private String rulesetRef;

        private ProfileTypeBuilder() {
        }

        public static ProfileTypeBuilder aProfileType() {
            return new ProfileTypeBuilder();
        }

        public ProfileTypeBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ProfileTypeBuilder withReference(String reference) {
            this.reference = reference;
            return this;
        }

        public ProfileTypeBuilder withIcon(String icon) {
            this.icon = icon;
            return this;
        }
        public ProfileTypeBuilder withRulesetVersion(int version) {
            this.rulesetVer = version;
            return this;
        }
        public ProfileTypeBuilder withRulesetRef(String ref) {
            this.rulesetRef = ref;
            return this;
        }
        public ProfileType build() {
            return new ProfileType(name, reference, icon /*, rulesetRef , rulesetVer*/);
        }
    }

}
