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



public class ImpactValueModel {
    private String reference;
    private String name;
    private boolean editable;
    private RulesetModel ruleset;

    public ImpactValueModel(String reference, String name, boolean editable) {
        this.reference = reference;
        this.name = name;
        this.editable = editable;

    }

    public RulesetModel getRuleset() {
        return ruleset;
    }

    public void setRuleset(RulesetModel ruleset) {
        this.ruleset = ruleset;
    }

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

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
