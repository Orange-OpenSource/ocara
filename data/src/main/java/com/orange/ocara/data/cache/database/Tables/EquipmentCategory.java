package com.orange.ocara.data.cache.database.Tables;
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
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "EquipmentCategory")
public class EquipmentCategory {
    private String rulesetRef;
    private int rulesetVer;
    private String name;
    @ColumnInfo(name = "category_id")
    @PrimaryKey(autoGenerate = true)
    private int id;
    public EquipmentCategory(String name, String rulesetRef, int rulesetVer) {
        this.rulesetRef = rulesetRef;
        this.name = name;
        this.rulesetVer = rulesetVer;
    }

    public int getRulesetVer() {
        return rulesetVer;
    }

    public void setRulesetVer(int rulesetVer) {
        this.rulesetVer = rulesetVer;
    }

    public String getRulesetRef() {
        return rulesetRef;
    }

    public void setRulesetRef(String rulesetRef) {
        this.rulesetRef = rulesetRef;
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

    public static class EquipmentCategoryBuilder {
        private int id;
        private String rulesetRef;
        private int rulesetVer;
        private String name;

        private EquipmentCategoryBuilder() {
        }

        public static EquipmentCategoryBuilder anEquipmentCategory() {
            return new EquipmentCategoryBuilder();
        }

        public EquipmentCategoryBuilder withId(int id) {
            this.id = id;
            return this;
        }

        public EquipmentCategoryBuilder withRulesetRef(String rulesetRef) {
            this.rulesetRef = rulesetRef;
            return this;
        }

        public EquipmentCategoryBuilder withRulesetVer(int rulesetVer) {
            this.rulesetVer = rulesetVer;
            return this;
        }

        public EquipmentCategoryBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public EquipmentCategory build() {
            EquipmentCategory equipmentCategory = new EquipmentCategory(name, rulesetRef, rulesetVer);
            equipmentCategory.setId(id);
            return equipmentCategory;
        }
    }

}
