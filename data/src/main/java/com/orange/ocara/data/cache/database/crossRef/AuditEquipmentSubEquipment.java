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
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "audit_equipment_subequipment")
public class AuditEquipmentSubEquipment {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int auditEquipmentId;
    private final int audit_id;
    @NonNull
    // the child won't be save as an audit equipment
    // we can retrieve its data by the its reference
    private String childRef;
    // obj1 (obj2 - obj3)

    public AuditEquipmentSubEquipment(int audit_id, int auditEquipmentId, @NonNull String childRef) {
        this.auditEquipmentId = auditEquipmentId;
        this.childRef = childRef;
        this.audit_id = audit_id;
    }

    public int getAudit_id() {
        return audit_id;
    }

    public int getAuditEquipmentId() {
        return auditEquipmentId;
    }

    public void setAuditEquipmentId(int auditEquipmentId) {
        this.auditEquipmentId = auditEquipmentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getChildRef() {
        return childRef;
    }

    public void setChildRef(@NonNull String childRef) {
        this.childRef = childRef;
    }

    public static class AuditEquipmentSubEquipmentBuilder {
        private int audit_id;
        private int auditEquipmentId;
        private String childRef;

        public AuditEquipmentSubEquipmentBuilder setAudit_id(int audit_id) {
            this.audit_id = audit_id;
            return this;
        }

        public AuditEquipmentSubEquipmentBuilder setAuditEquipmentId(int auditEquipmentId) {
            this.auditEquipmentId = auditEquipmentId;
            return this;
        }

        public AuditEquipmentSubEquipmentBuilder setChildRef(String childRef) {
            this.childRef = childRef;
            return this;
        }

        public AuditEquipmentSubEquipment createAuditEquipmentSubEquipment() {
            return new AuditEquipmentSubEquipment(audit_id, auditEquipmentId, childRef);
        }
    }
}
