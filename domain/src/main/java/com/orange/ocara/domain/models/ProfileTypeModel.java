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

import com.orange.ocara.data.cache.database.Tables.ProfileType;

public class ProfileTypeModel {
//    private int id;
    private String reference;
    private String name;
    private String icon;
    private RulesetModel ruleset;


    public ProfileTypeModel(ProfileType profileType) {
        this.name = profileType.getName();
        this.icon = profileType.getIcon();
//        this.id = profileType.getIcon();
        this.reference = profileType.getReference();
    }


    public ProfileTypeModel() {
        this.name = "";
        this.icon = "";
    }

    public ProfileTypeModel(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public RulesetModel getRuleset() {
        return ruleset;
    }

    public void setRuleset(RulesetModel ruleset) {
        this.ruleset = ruleset;
    }

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
