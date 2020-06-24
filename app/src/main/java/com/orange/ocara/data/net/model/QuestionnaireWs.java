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

/** a model for remote questionnaires (aka chapters) */
public class QuestionnaireWs implements Serializable {

    private static final String REFERENCE = "reference";
    private static final String STATE = "state";
    private static final String OBJECT_DESCRIPTION = "objectDescriptionRef";
    private static final String CHAINS = "chains";
    private static final String DATE = "date";
    private static final String COMPLETE = "complete";
    private static final String PARENT_OBJECTS_REF = "parentObjectRefs";

    @SerializedName(REFERENCE)
    @Expose
    private String reference;

    @SerializedName(STATE)
    @Expose
    private String state;

    @SerializedName(COMPLETE)
    @Expose
    private Boolean complete;

    @SerializedName(OBJECT_DESCRIPTION)
    @Expose
    private String objectDescriptionRef;

    @SerializedName(CHAINS)
    @Expose
    private List<ChainWs> chains = null;

    @SerializedName(PARENT_OBJECTS_REF)
    @Expose
    private List<String> parentObjectRefs = new ArrayList<>();

    @SerializedName(DATE)
    @Expose
    private String date;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public String getObjectDescriptionRef() {
        return objectDescriptionRef;
    }

    public void setObjectDescriptionRef(String objectDescriptionRef) {
        this.objectDescriptionRef = objectDescriptionRef;
    }

    public List<ChainWs> getChains() {
        return chains;
    }

    public void setChains(List<ChainWs> chains) {
        this.chains = chains;
    }

    public List<String> getParentObjectRefs() {
        return parentObjectRefs;
    }

    public void setParentObjectRefs(List<String> parentObjectRefs) {
        this.parentObjectRefs = parentObjectRefs;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
