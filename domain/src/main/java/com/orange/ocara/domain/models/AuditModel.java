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

import com.orange.ocara.data.cache.database.Tables.Audit;
import com.orange.ocara.utils.enums.AuditLevel;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class AuditModel {
    protected int id;
    protected String name;
    protected RulesetModel ruleset;
    protected Long date;
    protected boolean isInProgress;
    protected String rulesetRef;
    protected int rulesetVer;
    protected AuditLevel level;
    protected int auditorId;

    // should be replaced with auditorId
    @Deprecated
    protected String userName;
    protected int version;
    protected SiteModel site;
    protected AuditorModel auditor;

    protected final List<AuditEquipmentModel> objects = new ArrayList<>();
    protected List<CommentModel> comments = new ArrayList<>();

    public AuditModel() {
    }

    public AuditModel(Audit audit) {
        name = audit.getName();
        date = audit.getDate();
        isInProgress = audit.isInProgress();
        level = audit.getLevel();
        id = audit.getId();
        version = audit.getVersion();
        userName = audit.getUserName();
        rulesetRef = audit.getRulesetRef();
        rulesetVer = audit.getRulesetVer();
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<AuditEquipmentModel> getObjects() {
        return objects;
    }

    public void addObject(AuditEquipmentModel auditEquipment) {
        objects.add(auditEquipment);
    }

    public void setObjects(List<AuditEquipmentModel> auditEquipments) {
        objects.clear();
        objects.addAll(auditEquipments);
    }

    public AuditorModel getAuditor() {
        return auditor;
    }

    public void setAuditor(AuditorModel auditor) {
        this.auditor = auditor;
    }

    public List<CommentModel> getComments() {
        return comments;
    }

    public void setComments(List<CommentModel> comments) {
        this.comments = comments;
    }

    public String getRulesetRef() {
        return rulesetRef;
    }

    public int getRulesetVer() {
        return rulesetVer;
    }

    public RulesetModel getRuleset() {
        return ruleset;
    }

    public void setRuleset(RulesetModel ruleset) {
        this.ruleset = ruleset;
    }

    public SiteModel getSite() {
        return site;
    }

    public void setSite(SiteModel site) {
        this.site = site;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public boolean isInProgress() {
        return isInProgress;
    }

    public void setInProgress(boolean inProgress) {
        isInProgress = inProgress;
    }

    public boolean isTerminated() {
        return !isInProgress;
    }

    public void setTerminated() {
        isInProgress = false;
    }

    public AuditLevel getLevel() {
        return level;
    }

    public void setLevel(AuditLevel level) {
        this.level = level;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(int auditorId) {
        this.auditorId = auditorId;
    }

    /*
    this method updates the version and the date of the audit because they won't be the same as the original
    audit
     */
    public AuditModel updateAuditVersion() {
        AuditModel audit = copyAudit();
        audit.setVersion(getVersion() + 1);
        audit.setDate(System.currentTimeMillis());
        return audit;
    }

    public AuditModel copyAudit() {
        AuditModel audit = new AuditModel();
        audit.id = getId();
        audit.ruleset = getRuleset();
        audit.name = getName();
        audit.date = getDate();
        audit.isInProgress = isInProgress();
        audit.level = getLevel();
        audit.id = getId();
        audit.site = getSite();
        audit.auditorId = getAuditorId();
        audit.version = getVersion();
        audit.userName = getUserName();
        audit.rulesetRef = getRulesetRef();
        audit.rulesetVer = getRulesetVer();
        audit.auditor = getAuditor();
        return audit;
    }

    public static class Builder {
        AuditModel auditModel;

        public Builder() {
            auditModel = new AuditModel();
        }

        public Builder id(int id) {
            auditModel.id = id;
            return this;
        }

        public Builder name(String name) {
            auditModel.name = name;
            return this;
        }

        public Builder ruleset(RulesetModel ruleset) {
            auditModel.ruleset = ruleset;
            return this;
        }

        public Builder date(Long date) {
            auditModel.date = date;
            return this;
        }

        public Builder status(boolean status) {
            auditModel.isInProgress = status;
            return this;
        }

        public Builder level(AuditLevel level) {
            auditModel.level = level;
            return this;
        }

        public Builder auditorId(int id) {
            auditModel.auditorId = id;
            return this;
        }

        public Builder userName(String userName) {
            auditModel.userName = userName;
            return this;
        }

        public Builder version(int version) {
            auditModel.version = version;
            return this;
        }

        public Builder site(SiteModel site) {
            auditModel.site = site;
            return this;
        }

        public AuditModel build() {
            return auditModel;
        }
    }
}
