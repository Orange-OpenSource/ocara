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

import com.orange.ocara.data.cache.database.Tables.EquipmentCategory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EquipmentCategoryModel implements Serializable {
    private String rulesetRef;
    private int rulesetVer;
    private String name;
    private int id;
    private final List<EquipmentModel> equipments;

    public EquipmentCategoryModel(EquipmentCategory category) {
        this(category.getRulesetRef(), category.getRulesetVer(), category.getName(), category.getId());
    }

    public EquipmentCategoryModel(String rulesetRef, int rulesetVer, String name, int id) {
        this.rulesetRef = rulesetRef;
        this.rulesetVer = rulesetVer;
        this.name = name;
        this.id = id;
        equipments = new ArrayList<>();
    }

    public void addEquipment(EquipmentModel equipmentModel) {
        equipments.add(equipmentModel);
    }

    public List<EquipmentModel> getEquipments() {
        return equipments;
    }

    public String getRulesetRef() {
        return rulesetRef;
    }

    public void setRulesetRef(String rulesetRef) {
        this.rulesetRef = rulesetRef;
    }

    public int getRulesetVer() {
        return rulesetVer;
    }

    public void setRulesetVer(int rulesetVer) {
        this.rulesetVer = rulesetVer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
