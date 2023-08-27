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

/*
this model is used in audit list fragment
 */
public class AuditForAuditListModel extends AuditModel{
    private boolean isSelected = false;
    private boolean isCopied = false;

    public AuditForAuditListModel(AuditModel audit) {
        name = audit.getName();
        date = audit.getDate();
        isInProgress = audit.isInProgress();
        level = audit.getLevel();
        id = audit.getId();
        version = audit.getVersion();
        userName = audit.getUserName();
        rulesetRef = audit.getRulesetRef();
        rulesetVer = audit.getRulesetVer();
        site = audit.getSite();
        ruleset = audit.getRuleset();
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isCopied() {
        return isCopied;
    }

    public void setCopied(boolean copied) {
        isCopied = copied;
    }

    @Override
    public AuditForAuditListModel updateAuditVersion() {
        AuditForAuditListModel audit = new AuditForAuditListModel(super.updateAuditVersion());
        audit.setCopied(true);
        return audit;
    }
}
