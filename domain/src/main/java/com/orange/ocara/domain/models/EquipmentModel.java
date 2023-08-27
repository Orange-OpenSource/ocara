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

import com.orange.ocara.data.cache.database.Tables.Equipment;

import java.util.ArrayList;
import java.util.List;


public class EquipmentModel {
    protected String reference;
    protected String name;
    protected String icon;
    protected String type;
    protected String definition;
    protected String date;
    private RulesetModel rulesetModel;
    private List<EquipmentModel> children = new ArrayList<>();
    private final List<QuestionModel> questions = new ArrayList<>();

    public EquipmentModel() {
    }

    public EquipmentModel(Equipment equipment, RulesetModel rulesetModel) {
        this(equipment);
        this.rulesetModel = rulesetModel;
    }

    public EquipmentModel(Equipment equipment) {
        this.reference = equipment.getReference();
        this.name = equipment.getName();
        this.icon = equipment.getIcon();
        this.type = equipment.getType();
        this.definition = equipment.getDefinition();
        this.date = equipment.getDate();
    }


    public List<QuestionModel> getQuestions() {
        return questions;
    }

    public void addQuestion(QuestionModel question) {
        questions.add(question);
    }

    public RulesetModel getRulesetModel() {
        return rulesetModel;
    }

    public void setChildren(List<EquipmentModel> children) {
        this.children = children;
    }

    public List<EquipmentModel> getChildren() {
        return children;
    }

    public void addChild(EquipmentModel equipmentModel) {
        children.add(equipmentModel);
    }

    public String getReference() {
        return reference;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String getType() {
        return type;
    }

    public String getDefinition() {
        return definition;
    }

    public String getDate() {
        return date;
    }

    public boolean isCharacteristic() {
        return "subobject".equals(type);
    }

}
