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

/** a model for remote impact values */
public class ImpactValueWs implements Serializable {

    private static final String NAME = "name";
    private static final String EDITABLE = "editable";
    private static final String REFERENCE = "reference";

    @SerializedName(REFERENCE)
    @Expose
    private String reference;

    @SerializedName(NAME)
    @Expose
    private String name;

    @SerializedName(EDITABLE)
    @Expose
    private Boolean editable;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }
}
