package com.orange.ocara.data.cache.database.NonTables;
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
import androidx.room.Embedded;

import com.orange.ocara.data.cache.database.Tables.Rule;

public class RuleWithObjName {
    @Embedded
    private Rule rule;
    private int illustrationsCount;
    private String equipment_name;

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public String getEquipment_name() {
        return equipment_name;
    }

    public void setEquipment_name(String equipment_name) {
        this.equipment_name = equipment_name;
    }

    public int getIllustrationsCount() {
        return illustrationsCount;
    }

    public void setIllustrationsCount(int illustrationsCount) {
        this.illustrationsCount = illustrationsCount;
    }

}
