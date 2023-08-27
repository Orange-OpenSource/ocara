package com.orange.ocara.data.cache.database.NonTables;
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
import androidx.room.Embedded;
import androidx.room.Relation;

import com.orange.ocara.data.cache.database.Tables.ImpactValue;
import com.orange.ocara.data.cache.database.Tables.ProfileType;
import com.orange.ocara.data.cache.database.Tables.Rule;
import com.orange.ocara.data.cache.database.crossRef.RuleProfileTypeImpactCrossRef;

public class RuleWithProfiletypeImpact {
    @Embedded
    public RuleProfileTypeImpactCrossRef ruleProfileTypeImpactCrossRef;

    @Relation(parentColumn = "ruleRef", entityColumn = "ruleRef")
    private Rule rule;

    @Relation(parentColumn = "ruleImpactRef", entityColumn = "impactRef")
    private ImpactValue impactValue;

    @Relation(parentColumn = "profileRef", entityColumn = "profileRef")
    private ProfileType profileType;

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public ImpactValue getImpactValue() {
        return impactValue;
    }

    public void setImpactValue(ImpactValue impactValue) {
        this.impactValue = impactValue;
    }

    public ProfileType getProfileType() {
        return profileType;
    }

    public void setProfileType(ProfileType profileType) {
        this.profileType = profileType;
    }
}
