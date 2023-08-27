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

package com.orange.ocara.data.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * a model for remote rules
 */
public class RuleWs implements Serializable {

    private static final String REFERENCE = "reference";
    private static final String LABEL = "label";
    private static final String ORIGIN = "origin";
    private static final String USER_CREDENTIALS = "userCredentials";
    private static final String ILLUSTRATION = "illustration";
    private static final String DATE = "date";

    private static final String ASSOCIATED_OBJECT_REF = "associatedObjectRef";
    private static final String CONCERNED_OBJECT_REF = "concernedObjectRef";
    private static final String RULE_IMPACTS = "ruleImpacts";


    @SerializedName(REFERENCE)
    @Expose
    private String reference;

    @SerializedName(ORIGIN)
    @Expose
    private String origin;

    @SerializedName(LABEL)
    @Expose
    private String label;

    @SerializedName(USER_CREDENTIALS)
    @Expose
    private AuthorWs userCredentials;

    @SerializedName(ILLUSTRATION)
    @Expose
    private List<String> illustration = new ArrayList<>();

    @SerializedName(RULE_IMPACTS)
    @Expose
    private List<RuleImpactWs> ruleImpacts = new ArrayList<>();

    @SerializedName(DATE)
    @Expose
    private String date;

    @SerializedName(ASSOCIATED_OBJECT_REF)
    @Expose
    private List<String> associatedObjectRef = new ArrayList<>();

    @SerializedName(CONCERNED_OBJECT_REF)
    @Expose
    private List<String> concernedObjectRef = new ArrayList<>();

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

    public AuthorWs getUserCredentials() {
        return userCredentials;
    }

    public void setUserCredentials(AuthorWs userCredentials) {
        this.userCredentials = userCredentials;
    }

    public List<String> getIllustration() {
        return illustration;
    }

    public void setIllustration(List<String> illustration) {
        this.illustration = illustration;
    }

    public List<RuleImpactWs> getRuleImpacts() {
        return ruleImpacts;
    }

    public void setRuleImpacts(List<RuleImpactWs> ruleImpacts) {
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
}
