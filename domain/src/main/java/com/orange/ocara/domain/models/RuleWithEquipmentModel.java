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

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class RuleWithEquipmentModel {

    RuleModel rule;
    List<String> equipmentNames;

    public RuleWithEquipmentModel(RuleModel rule, List<String> equipmentNames) {
        this.rule = rule;
        this.equipmentNames = new ArrayList<>();
        this.equipmentNames.addAll(equipmentNames);
    }

    public RuleWithEquipmentModel(RuleModel rule) {
        this.rule = rule;
        this.equipmentNames = new ArrayList<>();
    }

    public RuleWithEquipmentModel(RuleModel rule , String equipmentName) {
        this.rule = rule;
        this.equipmentNames = new ArrayList<>();
        this.equipmentNames.add(equipmentName);

    }


    public RuleModel getRule() {
        return rule;
    }

    public void setRule(RuleModel rule) {
        this.rule = rule;
    }

    public List<String> getEquipmentNames() {
        return equipmentNames;
    }

    public String getEquipmentNamesString() {
        return TextUtils.join(", ", equipmentNames);
    }

    public void addEquipmentName(String equipmentName) {
        this.equipmentNames.add(equipmentName);
    }
}
