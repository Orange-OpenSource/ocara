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


package com.orange.ocara.domain.models;

public class RuleImpactModel {

    ImpactValueModel impactValue;
    ProfileTypeModel profileType;
    RuleModel rule;

    public RuleImpactModel(ImpactValueModel impactValue, ProfileTypeModel profileType, RuleModel rule) {
        this.impactValue = impactValue;
        this.profileType = profileType;
        this.rule = rule;
    }

    public ImpactValueModel getImpactValue() {
        return impactValue;
    }

    public void setImpactValue(ImpactValueModel impactValue) {
        this.impactValue = impactValue;
    }

    public ProfileTypeModel getProfileType() {
        return profileType;
    }

    public void setProfileType(ProfileTypeModel profileType) {
        this.profileType = profileType;
    }

    public RuleModel getRule() {
        return rule;
    }

    public void setRule(RuleModel rule) {
        this.rule = rule;
    }
}
