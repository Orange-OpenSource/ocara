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

import com.orange.ocara.data.cache.database.NonTables.AuditInfoForReport;
import com.orange.ocara.utils.enums.AuditLevel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AuditInfoForReportModel {
    private String name;
    private String rulesetType;
    private String siteName;
    private String auditorName;
    private String date;
    private String fullDate;
    private AuditLevel level;
    private boolean audit_status;


    public AuditInfoForReportModel(AuditInfoForReport audit) {
        this.name = audit.getName();
        this.rulesetType = audit.getRulesetType();
        this.siteName = audit.getSiteName();
        this.auditorName = audit.getAuditorName() + " " + audit.getAuditorLastName();
        this.date = new SimpleDateFormat("EEE d, MMM  y").format(new Date(audit.getDate()));
        this.fullDate = DateFormat.getDateInstance(DateFormat.FULL).format(audit.getDate());
        this.level = AuditLevel.values()[audit.getLevel()];
        this.audit_status = audit.isInProgress();
    }

    public AuditLevel getLevel() {
        return level;
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

    public String getDate() {
        return date;
    }

    public String getFullDate() {
        return fullDate;
    }

    public boolean isInProgress() {
        return audit_status;
    }

    public void setInProgress(boolean inProgress) {
        this.audit_status = inProgress;
    }
}
