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

import com.orange.ocara.data.cache.database.NonTables.AuditWithRulesetWithSite;
import com.orange.ocara.data.cache.database.Tables.Audit;
import com.orange.ocara.utils.enums.AuditLevel;
import com.orange.ocara.utils.enums.RuleSetStat;

public class AuditWithRulesetWithSiteModel {

    private int auditId;
    private String auditName;
   // private String auditStatus;
    private String auditor;
    private AuditLevel level;
    private String rulesetRef;
   // private String rulesetComment;
    private String rulesetType;
   // private String rulesetAuthorName;
    private RuleSetStat rulesetStat;
    private String rulesetVresion;
    private int siteId;
    private String siteNoImmo;
    private String siteName;


    public AuditWithRulesetWithSiteModel() {
    }

    public static AuditWithRulesetWithSiteModel getInstance(AuditWithRulesetWithSite audit) {
        return new AuditWithRulesetWithSiteModel(
                audit.getAuditName(),
                audit.getAuditor(),
                audit.getLevel(),
                audit.getRulesetRef(),
                audit.getRulesetType(),
                audit.getRulesetStat(),
                audit.getRulesetVresion(),
                audit.getSiteId(),
                audit.getSiteNoImmo(),
                audit.getSiteName());
    }

    public AuditWithRulesetWithSiteModel(String auditName,
                                         String auditor,
                                         AuditLevel level,
                                         String rulesetRef,
                                         String rulesetType,
                                         RuleSetStat rulesetStat,
                                         String rulesetVresion,
                                         int siteId,
                                         String siteNoImmo,
                                         String siteName) {
        this.auditName = auditName;
       // this.auditStatus = auditStatus;
        this.auditor = auditor;
        this.level = level;
        this.rulesetRef = rulesetRef;
       // this.rulesetComment = rulesetComment;
        this.rulesetType = rulesetType;
       // this.rulesetAuthorName = rulesetAuthorName;
        this.siteId = siteId;
        this.siteNoImmo = siteNoImmo;
        this.siteName = siteName;
        this.rulesetStat = rulesetStat;
        this.rulesetVresion = rulesetVresion;
    }

    private RulesetModel getRuleset() {

        return RulesetModel.builder()
                .type(rulesetType)
                .stat(rulesetStat)
                .version(rulesetVresion)
                .reference(rulesetRef)
                .build();
    }

    private SiteModel getSite() {
        return SiteModel.builder()
                .name(siteName)
                .noImmo(siteNoImmo)
                .id(siteId)
                .build();
    }

    public AuditModel getAuditModel() {
        return AuditModel.builder()
                .id(auditId)
                .userName(auditor)
                .name(auditName)
                .ruleset(getRuleset())
                .site(getSite())
                .level(getLevel())
                .build();
    }

    public int getAuditId() {
        return auditId;
    }

    public void setAuditId(int auditId) {
        this.auditId = auditId;
    }

    public String getRulesetRef() {
        return rulesetRef;
    }

    public void setRulesetRef(String rulesetRef) {
        this.rulesetRef = rulesetRef;
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }


    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }


    public String getRulesetType() {
        return rulesetType;
    }

    public void setRulesetType(String rulesetType) {
        this.rulesetType = rulesetType;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public String getSiteNoImmo() {
        return siteNoImmo;
    }

    public void setSiteNoImmo(String siteNoImmo) {
        this.siteNoImmo = siteNoImmo;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public AuditLevel getLevel() {
        return level;
    }

    public void setLevel(AuditLevel level) {
        this.level = level;
    }

    public RuleSetStat getRulesetStat() {
        return rulesetStat;
    }

    public void setRulesetStat(RuleSetStat rulesetStat) {
        this.rulesetStat = rulesetStat;
    }

    public String getRulesetVresion() {
        return rulesetVresion;
    }

    public void setRulesetVresion(String rulesetVresion) {
        this.rulesetVresion = rulesetVresion;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final AuditWithRulesetWithSiteModel model;

        public Builder() {
            this.model = new AuditWithRulesetWithSiteModel();
        }

        public Builder auditName(String auditName) {
            model.setAuditName(auditName);
            return this;
        }

        public Builder auditor(String auditor) {
            model.setAuditor(auditor);
            return this;
        }

        public Builder rulesetType(String rulesetType) {
            model.setRulesetType(rulesetType);
            return this;
        }
        public Builder siteId(int siteId) {
            model.setSiteId(siteId);
            return this;
        }

        public Builder siteNoImmo(String siteNoImmo) {
            model.setSiteNoImmo(siteNoImmo);
            return this;
        }

        public Builder siteName(String siteName) {
            model.setSiteName(siteName);
            return this;
        }

        public AuditWithRulesetWithSiteModel build() {
            return model;
        }

    }

}
