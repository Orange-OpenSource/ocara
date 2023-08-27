package com.orange.ocara.data.cache.database.crossRef;
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
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "equipment_ruleset_version")
public class EquipmentRulesetVersion {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String objRef;
    private String rulesetRef;
    private int version;

    public EquipmentRulesetVersion(String objRef, String rulesetRef, int version) {
        this.objRef = objRef;
        this.rulesetRef = rulesetRef;
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObjRef() {
        return objRef;
    }

    public void setObjRef(String objRef) {
        this.objRef = objRef;
    }

    public String getRulesetRef() {
        return rulesetRef;
    }

    public void setRulesetRef(String rulesetRef) {
        this.rulesetRef = rulesetRef;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public static class Builder{
        private String objRef;
        private String rulesetRef;
        private int version;

        public Builder setObjRef(String objRef) {
            this.objRef = objRef;
            return this;
        }

        public Builder setRulesetRef(String rulesetRef) {
            this.rulesetRef = rulesetRef;
            return this;
        }

        public Builder setVersion(int version) {
            this.version = version;
            return this;
        }

        public EquipmentRulesetVersion createEquipmentRulesetVersion() {
            return new EquipmentRulesetVersion(objRef, rulesetRef, version);
        }
    }
}
