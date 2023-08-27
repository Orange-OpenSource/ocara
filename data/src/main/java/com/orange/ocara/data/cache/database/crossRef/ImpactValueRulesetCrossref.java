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

@Entity(tableName = "impact_ruleset", primaryKeys = {"rulesetRef", "impactRef", "rulesetVersion"})
public class ImpactValueRulesetCrossref {
    @NonNull
    private String rulesetRef;
    @NonNull
    private String impactRef;
    private int rulesetVersion;

    public ImpactValueRulesetCrossref(@NonNull String rulesetRef, @NonNull String impactRef, int rulesetVersion) {
        this.rulesetRef = rulesetRef;
        this.impactRef = impactRef;
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
    public String getImpactRef() {
        return impactRef;
    }

    public void setImpactRef(@NonNull String impactRef) {
        this.impactRef = impactRef;
    }

    public int getRulesetVersion() {
        return rulesetVersion;
    }

    public void setRulesetVersion(int rulesetVersion) {
        this.rulesetVersion = rulesetVersion;
    }
}

