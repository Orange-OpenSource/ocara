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

import com.orange.ocara.data.cache.database.Tables.Audit;
import com.orange.ocara.utils.enums.AuditLevel;
import com.orange.ocara.utils.enums.RuleSetStat;

public class AuditWithRulesetWithSite {

    private int auditId;
    private String auditName;
   // private String auditStatus;
    private String auditor;
    private AuditLevel level;
    private String rulesetRef;
   // private String rulesetComment;
    private String rulesetType;
   // private String rulesetAuthorName;
    private int siteId;
    private String siteNoImmo;
    private String siteName;
    private RuleSetStat rulesetStat;
    private String rulesetVresion;

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

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }

    public AuditLevel getLevel() {
        return level;
    }

    public void setLevel(AuditLevel level) {
        this.level = level;
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



}
