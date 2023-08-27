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

@Entity(tableName = "profile_type_ruleset", primaryKeys = {"rulesetRef", "profileRef", "rulesetVersion"})
public class ProfileTypeRulesetCrossref {
    @NonNull
    private String rulesetRef;
    @NonNull
    private String profileRef;
    private int rulesetVersion;

    public ProfileTypeRulesetCrossref(@NonNull String rulesetRef, @NonNull String profileRef, int rulesetVersion) {
        this.rulesetRef = rulesetRef;
        this.profileRef = profileRef;
        this.rulesetVersion = rulesetVersion;
    }

    @NonNull
    public String getRulesetRef() {
        return rulesetRef;
    }

    public void setRulesetRef(@NonNull String rulesetRef) {
        this.rulesetRef = rulesetRef;
    }

    @NonNull
    public String getProfileRef() {
        return profileRef;
    }

    public void setProfileRef(@NonNull String profileRef) {
        this.profileRef = profileRef;
    }

    public int getRulesetVersion() {
        return rulesetVersion;
    }

    public void setRulesetVersion(int rulesetVersion) {
        this.rulesetVersion = rulesetVersion;
    }
}

