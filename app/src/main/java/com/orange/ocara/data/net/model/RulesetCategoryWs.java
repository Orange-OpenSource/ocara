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

/** a model for remote RulesetCategories */
public class RulesetCategoryWs implements Serializable {

    private static final String NAME = "name";
    private static final String ACCEPTED_IMPACT_LIST = "acceptedImpactList";
    private static final String DEFAULT_IMPACT = "defaultImpact";

    @SerializedName(NAME)
    @Expose
    private String name;

    @SerializedName(ACCEPTED_IMPACT_LIST)
    @Expose
    private List<String> acceptedImpactList = new ArrayList<>();

    @SerializedName(DEFAULT_IMPACT)
    @Expose
    private String defaultImpactRef;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAcceptedImpactList() {
        return acceptedImpactList;
    }

    public void setAcceptedImpactList(List<String> acceptedImpactList) {
        this.acceptedImpactList = acceptedImpactList;
    }

    public String getDefaultImpactRef() {
        return defaultImpactRef;
    }

    public void setDefaultImpactRef(String defaultImpactRef) {
        this.defaultImpactRef = defaultImpactRef;
    }
}
