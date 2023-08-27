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
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "AuditEquipments")
public class AuditEquipments {
    @ColumnInfo(name = "audit_object_id")
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    @ColumnInfo(name = "objectReference")
    private String objectRef;

    private int lastAnsweredQuestion;

    private String name;
    private int audit_id;
    private int order;


    @Ignore
    public AuditEquipments(int id, @NonNull String objectRef, String name, int audit_id) {
        this(objectRef, name, audit_id);
        this.id = id;
        this.lastAnsweredQuestion = 0;
    }

    public AuditEquipments(@NonNull String objectRef, String name, int audit_id) {
        this.objectRef = objectRef;
        this.name = name;
        this.audit_id = audit_id;
        this.lastAnsweredQuestion = 0;
    }

    @Ignore
    public AuditEquipments(@NonNull String objectRef, String name, int audit_id,int order) {
        this.objectRef = objectRef;
        this.name = name;
        this.audit_id = audit_id;
        this.order = order;
        this.lastAnsweredQuestion = 0;
    }
    @Ignore
    public AuditEquipments(@NonNull String objectRef,int lastAnsweredQuestion, String name, int audit_id) {
        this.objectRef = objectRef;
        this.name = name;
        this.audit_id = audit_id;
        this.lastAnsweredQuestion = lastAnsweredQuestion;
    }

    public int getLastAnsweredQuestion() {
        return lastAnsweredQuestion;
    }

    public void setLastAnsweredQuestion(int lastAnsweredQuestion) {
        this.lastAnsweredQuestion = lastAnsweredQuestion;
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

    public String getObjectRef() {
        return objectRef;
    }

    public void setObjectRef(String objectRef) {
        this.objectRef = objectRef;
    }

    public int getAudit_id() {
        return audit_id;
    }

    public void setAudit_id(int audit_id) {
        this.audit_id = audit_id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public static class AuditEquipmentsBuilder {
        private String objectRef;
        private String name;
        private int audit_id;
        private int id = 0;

        public AuditEquipmentsBuilder setObjectRef(String objectRef) {
            this.objectRef = objectRef;
            return this;
        }

        public AuditEquipmentsBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public AuditEquipmentsBuilder setAudit_id(int audit_id) {
            this.audit_id = audit_id;
            return this;
        }

        public AuditEquipmentsBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public AuditEquipments createAuditEquipments() {
            return new AuditEquipments(id, objectRef, name, audit_id);
        }
    }
}
