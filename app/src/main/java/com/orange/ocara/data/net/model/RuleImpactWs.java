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

/** a model for remote rule impacts */
public class RuleImpactWs implements Serializable {

    private static final String IMPACT_VALUE = "impactValueRef";
    private static final String PROFILE_TYPE = "profileTypeRef";
    private static final String REFERENCE = "reference";

    @SerializedName(REFERENCE)
    @Expose
    private String reference;

    @SerializedName(IMPACT_VALUE)
    @Expose
    private String impactValueRef;

    @SerializedName(PROFILE_TYPE)
    @Expose
    private String profileTypeRef;

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
}
