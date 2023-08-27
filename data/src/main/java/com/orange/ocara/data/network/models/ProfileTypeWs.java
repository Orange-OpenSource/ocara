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
import java.util.List;

/** a model for remote profile types */
public class ProfileTypeWs implements Serializable, WithIcon {

    private static final String NAME = "name";
    private static final String REFERENCE = "reference";
    private static final String ICON = "icon";
    private static final String RULES_CATEGORY = "rulesCategories";

    @SerializedName(NAME)
    @Expose
    private String name;

    @SerializedName(REFERENCE)
    @Expose
    private String reference;

    @SerializedName(ICON)
    @Expose
    private String icon;

    @SerializedName(RULES_CATEGORY)
    @Expose
    private List<RulesetCategoryWs> rulesCategories;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public boolean hasIcon() {
        return getIcon() != null && !getIcon().isEmpty();
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<RulesetCategoryWs> getRulesCategories() {
        return rulesCategories;
    }

    public void setRulesCategories(List<RulesetCategoryWs> rulesCategories) {
        this.rulesCategories = rulesCategories;
    }
}
