/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2020 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.data.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;

/** a model for remote questions (aka rules'groups) */
@EqualsAndHashCode(callSuper = false)
public class QuestionWs implements Serializable {

    private static final String REFERENCE = "reference";
    private static final String LABEL = "label";
    private static final String STATE = "state";
    private static final String SUBJECT = "subject";
    private static final String RULES = "rulesRef";
    private static final String CREATION_DATE = "date";

    @SerializedName(REFERENCE)
    @Expose
    private String reference;

    @SerializedName(LABEL)
    @Expose
    private String label;

    @SerializedName(STATE)
    @Expose
    private String state;

    @SerializedName(SUBJECT)
    @Expose
    private SubjectWs subject;

    @SerializedName(RULES)
    @Expose
    private List<String> rulesReferences = new ArrayList<>();

    @SerializedName(CREATION_DATE)
    @Expose
    private String creationDate;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public SubjectWs getSubject() {
        return subject;
    }

    public void setSubject(SubjectWs subject) {
        this.subject = subject;
    }

    public List<String> getRulesReferences() {
        return rulesReferences;
    }

    public void setRulesRef(List<String> rulesRef) {
        this.rulesReferences = rulesRef;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
