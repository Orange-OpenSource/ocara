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

public class EquipmentForAddEquipmentModel extends EquipmentModel {
    private boolean isSelected = false;

    public EquipmentForAddEquipmentModel(Equipment equipment, RulesetModel rulesetModel) {
        super(equipment, rulesetModel);
    }

    public EquipmentForAddEquipmentModel(Equipment equipment) {
        super(equipment);
    }

    public EquipmentForAddEquipmentModel(EquipmentModel equipment) {
        super();
        this.reference = equipment.getReference();
        this.name = equipment.getName();
        this.icon = equipment.getIcon();
        this.type = equipment.getType();
        this.definition = equipment.getDefinition();
        this.date = equipment.getDate();
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
