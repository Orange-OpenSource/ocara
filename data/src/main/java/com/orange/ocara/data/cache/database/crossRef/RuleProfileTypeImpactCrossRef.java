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

@Entity(tableName = "rule_profile_type")
public class RuleProfileTypeImpactCrossRef {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @NonNull
    private String ruleRef;
    @NonNull
    private String ruleImpactRef;
    @NonNull
    private String profileRef;
    private int version;
    @NonNull
    private String rulesetRef;

    public RuleProfileTypeImpactCrossRef(@NonNull String ruleRef, @NonNull String rulesetRef, @NonNull String ruleImpactRef, @NonNull String profileRef, int version) {
        this.ruleRef = ruleRef;
        this.rulesetRef = rulesetRef;
        this.ruleImpactRef = ruleImpactRef;
        this.profileRef = profileRef;
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @NonNull
    public String getRuleRef() {
        return ruleRef;
    }

    public void setRuleRef(@NonNull String ruleRef) {
        this.ruleRef = ruleRef;
    }

    @NonNull
    public String getRuleImpactRef() {
        return ruleImpactRef;
    }

    public void setRuleImpactRef(@NonNull String ruleImpactRef) {
        this.ruleImpactRef = ruleImpactRef;
    }

    @NonNull
    public String getProfileRef() {
        return profileRef;
    }

    public void setProfileRef(@NonNull String profileRef) {
        this.profileRef = profileRef;
    }

    @NonNull
    public String getRulesetRef() {
        return rulesetRef;
    }

    public void setRulesetRef(@NonNull String rulesetRef) {
        this.rulesetRef = rulesetRef;
    }

    public static class Builder {

        private String ruleRef;
        private String ruleImpactRef;
        private String profileRef;
        private int version;
        private String rulesetRef;

        public Builder setRuleRef(String ruleRef) {
            this.ruleRef = ruleRef;
            return this;
        }
        public Builder setRulesetRef(String rulesetRef){
            this.rulesetRef=rulesetRef;
            return this;
        }
        public Builder setRuleImpactRef(String ruleImpactRef) {
            this.ruleImpactRef = ruleImpactRef;
            return this;
        }

        public Builder setProfileRef(String profileRef) {
            this.profileRef = profileRef;
            return this;
        }

        public Builder setVersion(int version) {
            this.version = version;
            return this;
        }

        public RuleProfileTypeImpactCrossRef createRuleProfileTypeImpactCrossRef() {
            return new RuleProfileTypeImpactCrossRef(ruleRef,rulesetRef, ruleImpactRef, profileRef, version);
        }
    }
}
