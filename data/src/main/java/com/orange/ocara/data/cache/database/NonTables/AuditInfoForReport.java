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
public class AuditInfoForReport {
    private String name;
    private String rulesetType;
    private String siteName;
    private String auditorName;
    private String auditorLastName;
    private Long date;
    private int level;

    private boolean audit_status;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public String getRulesetType() {
        return rulesetType;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public Long getDate() {
        return date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRulesetType(String rulesetType) {
        this.rulesetType = rulesetType;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public boolean isInProgress() {
        return audit_status;
    }

    public void setAudit_status(boolean inProgress) {
        this.audit_status = inProgress;
    }

    public void setAuditorLastName(String auditorLastName) {
        this.auditorLastName = auditorLastName;
    }

    public String getAuditorLastName() {
        return auditorLastName;
    }
}
