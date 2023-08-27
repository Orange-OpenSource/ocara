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

package com.orange.ocara.data.oldEntities;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orange.ocara.data.network.models.RuleImpactWs;

import java.io.Serializable;

import lombok.EqualsAndHashCode;

/**
 * a model for cached rule impacts
 */
@Table(name = RuleImpactEntity.TABLE_NAME, id = RuleImpactEntity.ID)
@EqualsAndHashCode(callSuper = false)
public class RuleImpactEntity extends Model implements Serializable {

    public static final String TABLE_NAME = "rule_impact";
    public static final String IMPACT_VALUE = "impactValueRef";
    public static final String PROFILE_TYPE = "profileTypeRef";
    public static final String ID = "_id";
    public static final String RULE = "rule";
    public static final String REFERENCE = "reference";
    @SerializedName(REFERENCE)
    @Expose
    @Column(name = REFERENCE)
    public String reference;
    @SerializedName(IMPACT_VALUE)
    @Expose
    @Column(name = IMPACT_VALUE)
    public String impactValueRef;
    @SerializedName(PROFILE_TYPE)
    @Expose
    @Column(name = PROFILE_TYPE)
    public String profileTypeRef;
    @Column(name = RULE)
    private RuleEntity rule;

    public static RuleImpactEntity toEntity(RuleImpactWs in, RuleEntity rule) {

        RuleImpactEntity out = new RuleImpactEntity();
        out.setReference(in.getReference());
        out.setImpactValueRef(in.getImpactValueRef());
        out.setProfileTypeRef(in.getProfileTypeRef());
        out.setRule(rule);
        return out;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getImpactValueRef() {
        return impactValueRef;
    }

    public void setImpactValueRef(String impactValueRef) {
        this.impactValueRef = impactValueRef;
    }

    public String getProfileTypeRef() {
        return profileTypeRef;
    }

    public void setProfileTypeRef(String profileTypeRef) {
        this.profileTypeRef = profileTypeRef;
    }

    public RuleEntity getRule() {
        return rule;
    }

    public void setRule(RuleEntity rule) {
        this.rule = rule;
    }
}
