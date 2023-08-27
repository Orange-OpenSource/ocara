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

import com.orange.ocara.data.cache.database.NonTables.EquipmentNameAndIcon;

import org.jetbrains.annotations.NotNull;

public class EquipmentNameAndIconModel {

    private String icon;
    private String equipment_name;


    public EquipmentNameAndIconModel(String icon, String equipment_name) {
        this.icon = icon;
        this.equipment_name = equipment_name;
    }

    public EquipmentNameAndIconModel(@NotNull EquipmentNameAndIcon equipmentNameAndIcon) {
        this.icon = equipmentNameAndIcon.getIcon();
        this.equipment_name = equipmentNameAndIcon.getEquipment_name();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getEquipment_name() {
        return equipment_name;
    }

    public void setEquipment_name(String equipment_name) {
        this.equipment_name = equipment_name;
    }
}
