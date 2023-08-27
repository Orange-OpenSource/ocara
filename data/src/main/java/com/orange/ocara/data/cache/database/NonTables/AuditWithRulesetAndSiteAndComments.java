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
import androidx.room.Embedded;
import androidx.room.Relation;

import com.orange.ocara.data.cache.database.Tables.Audit;
import com.orange.ocara.data.cache.database.Tables.Comment;
import com.orange.ocara.data.cache.database.Tables.RulesetDetails;
import com.orange.ocara.data.cache.database.Tables.Site;

import java.util.List;

public class AuditWithRulesetAndSiteAndComments {
    @Embedded
    private Audit audit;
    @Embedded
    private RulesetDetails rulesetDetails;
    @Embedded
    private Site site;

    @Relation(
            entity = Comment.class,
            parentColumn = "audit_id",
            entityColumn = "audit_id"
    )
    private List<Comment> comments;


    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    public RulesetDetails getRulesetDetails() {
        return rulesetDetails;
    }

    public void setRulesetDetails(RulesetDetails rulesetDetails) {
        this.rulesetDetails = rulesetDetails;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
