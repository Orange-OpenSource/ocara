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
import com.orange.ocara.data.network.models.RuleWs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * a model for cached rules
 */
@Table(name = RuleEntity.TABLE_NAME, id = RuleEntity.ID)
@Data
@EqualsAndHashCode(callSuper = false)
public class RuleEntity extends Model implements Serializable {

    public static final String TABLE_NAME = "rule";
    public static final String ID = "_id";
    public static final String REFERENCE = "reference";
    public static final String LABEL = "label";
    public static final String ORIGIN = "origin";
    public static final String USER_CREDENTIALS = "userCredentials";
    public static final String ILLUSTRATION = "illustration";
    public static final String PROFILE_TYPES = "profileTypes";
    public static final String DATE = "date";

    public static final String QUESTION = "question";
    public static final String ASSOCIATED_OBJECT_REF = "associatedObjectRef";
    public static final String CONCERNED_OBJECT_REF = "concernedObjectRef";
    public static final String RULE_IMPACTS = "ruleImpacts";
    public static final String RULESET_DETAILS = "details";


    @SerializedName(REFERENCE)
    @Expose
    @Column(name = REFERENCE)
    public String reference;

    @SerializedName(ORIGIN)
    @Expose
    @Column(name = ORIGIN)
    public String origin;

    @SerializedName(LABEL)
    @Expose
    @Column(name = LABEL)
    public String label;

    @SerializedName(USER_CREDENTIALS)
    @Expose
    @Column(name = USER_CREDENTIALS)
    public AuthorEntity userCredentials;

    @SerializedName(ILLUSTRATION)
    @Expose
    @Column(name = ILLUSTRATION)
    public List<String> illustration = new ArrayList<>();

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public AuthorEntity getUserCredentials() {
        return userCredentials;
    }

    public void setUserCredentials(AuthorEntity userCredentials) {
        this.userCredentials = userCredentials;
    }

    public List<String> getIllustration() {
        return illustration;
    }

    public void setIllustration(List<String> illustration) {
        this.illustration = illustration;
    }

    public List<RuleImpactEntity> getRuleImpacts() {
        return ruleImpacts;
    }

    public void setRuleImpacts(List<RuleImpactEntity> ruleImpacts) {
        this.ruleImpacts = ruleImpacts;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getAssociatedObjectRef() {
        return associatedObjectRef;
    }

    public void setAssociatedObjectRef(List<String> associatedObjectRef) {
        this.associatedObjectRef = associatedObjectRef;
    }

    public List<String> getConcernedObjectRef() {
        return concernedObjectRef;
    }

    public void setConcernedObjectRef(List<String> concernedObjectRef) {
        this.concernedObjectRef = concernedObjectRef;
    }

    public RulesetEntity getRuleSeDetail() {
        return ruleSeDetail;
    }

    public void setRuleSeDetail(RulesetEntity ruleSeDetail) {
        this.ruleSeDetail = ruleSeDetail;
    }

    @SerializedName(RULE_IMPACTS)
    @Expose
    public List<RuleImpactEntity> ruleImpacts = null;

    @SerializedName(DATE)
    @Expose
    @Column(name = DATE)
    public String date;

    @SerializedName(ASSOCIATED_OBJECT_REF)
    @Expose
    @Column(name = ASSOCIATED_OBJECT_REF)
    public List<String> associatedObjectRef = new ArrayList<>();

    @SerializedName(CONCERNED_OBJECT_REF)
    @Expose
    @Column(name = CONCERNED_OBJECT_REF)
    public List<String> concernedObjectRef = new ArrayList<>();

    @Column(name = RULESET_DETAILS)
    private RulesetEntity ruleSeDetail;

    public List<RuleImpactEntity> getRuleImpactDb() {
        return getMany(RuleImpactEntity.class, RuleImpactEntity.RULE);
    }

    public boolean withIllustrations() {
        return !illustration.isEmpty();
    }

    public static RuleEntity toEntity(RuleWs in, RulesetEntity ruleset) {

        RuleEntity out = new RuleEntity();
        out.setReference(in.getReference());
        out.setAssociatedObjectRef(in.getAssociatedObjectRef());
        out.setConcernedObjectRef(in.getConcernedObjectRef());
        out.setIllustration(in.getIllustration());
        out.setLabel(in.getLabel());
        out.setDate(in.getDate());
        out.setOrigin(in.getOrigin());
        out.setUserCredentials(AuthorEntity.toEntity(in.getUserCredentials()));
        out.setRuleSeDetail(ruleset);

        return out;
    }
}