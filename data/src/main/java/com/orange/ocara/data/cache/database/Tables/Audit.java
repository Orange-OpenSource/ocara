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

import com.orange.ocara.utils.enums.AuditLevel;

@Entity(tableName = "audit")
public class Audit {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "audit_id")
    private int id;
    @ColumnInfo(name = "audit_name")
    private String name;
    @ColumnInfo(name = "audit_ruleset")
    private String rulesetRef;
    @ColumnInfo(name = "audit_date")
    private Long date;

    @ColumnInfo(name = "audit_status")
    private boolean inProgress = true;
    private AuditLevel level;
    private int auditorId;

    // should be replaced with auditorId
    @Deprecated
    private String userName="";
    private int rulesetVer;
    @ColumnInfo(name = "audit_version")
    private int version;
    private int auditSiteId;

    public static Builder Builder() {
        return new Builder();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAuditSiteId() {
        return auditSiteId;
    }

    public void setAuditSiteId(int siteId) {
        this.auditSiteId = siteId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRulesetRef() {
        return rulesetRef;
    }

    public void setRulesetRef(String rulesetRef) {
        this.rulesetRef = rulesetRef;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public AuditLevel getLevel() {
        return level;
    }

    public void setLevel(AuditLevel level) {
        this.level = level;
    }

    public int getRulesetVer() {
        return rulesetVer;
    }

    public void setRulesetVer(int rulesetVer) {
        this.rulesetVer = rulesetVer;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(int auditorId) {
        this.auditorId = auditorId;
    }

//    public enum Level {
//        /**
//         * Audit level for beginner.
//         */
//        BEGINNER,
//        /**
//         * Audit level for expert.
//         */
//        EXPERT
//    }

    public static class Builder {
        Audit audit = new Audit();

        public Builder setName(String name) {
            audit.name = name;
            return this;
        }

        public Builder setRulesetRef(String rulesetRef) {
            audit.rulesetRef = rulesetRef;
            return this;
        }

        public Builder setDate(Long date) {
            audit.date = date;
            return this;
        }

        public Builder setInProgress(boolean status) {
            audit.inProgress = status;
            return this;
        }

        public Builder setLevel(AuditLevel level) {
            audit.level = level;
            return this;
        }

        public Builder setUserName(String userName) {
            audit.userName = userName;
            return this;
        }

        public Builder setRulesetVer(int rulesetVer) {
            audit.rulesetVer = rulesetVer;
            return this;
        }

        public Builder setVersion(int version) {
            audit.version = version;
            return this;
        }

        public Builder setSiteId(int siteId) {
            audit.auditSiteId = siteId;
            return this;
        }
        public Builder setAuditorId(int auditorId) {
            audit.auditorId = auditorId;
            return this;
        }
        public Audit build() {
            return audit;
        }

    }
}
