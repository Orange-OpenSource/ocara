/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2020 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.data.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orange.ocara.business.model.RuleSetStat;

/**
 * model for remote rulesets
 */
public class RulesetLightWs {

    private static final String VERSION = "version";
    private static final String COMMENT = "comment";
    private static final String REFERENCE = "reference";
    private static final String TYPE = "type";
    private static final String AUTHOR = "author";
    private static final String DATE = "date";
    private static final String RULE_CATEGORY_NAME = "ruleCategoryName";

    @SerializedName(REFERENCE)
    @Expose
    private String reference;

    @SerializedName(VERSION)
    @Expose
    private String version;

    @SerializedName(COMMENT)
    @Expose
    private String comment;

    @SerializedName(TYPE)
    @Expose
    private String type;

    @SerializedName(AUTHOR)
    @Expose
    private String author;

    @SerializedName(DATE)
    @Expose
    private String date;

    @SerializedName(RULE_CATEGORY_NAME)
    @Expose
    private String ruleCategoryName;

    private RuleSetStat stat = RuleSetStat.ONLINE;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public RuleSetStat getStat() {
        return stat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRuleCategoryName() {
        return ruleCategoryName;
    }

    public void setRuleCategoryName(String ruleCategoryName) {
        this.ruleCategoryName = ruleCategoryName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isLocallyAvailable() {
        return stat == RuleSetStat.OFFLINE || stat == RuleSetStat.OFFLINE_WITH_NEW_VERSION;
    }

    public boolean isRemotelyAvailable() {
        return stat == RuleSetStat.ONLINE;
    }

    public boolean isCached() {
        return getStat() == RuleSetStat.OFFLINE || getStat() == RuleSetStat.OFFLINE_WITH_NEW_VERSION;
    }

    public boolean isDeprecated() {
        return getStat() == RuleSetStat.OFFLINE_WITH_NEW_VERSION;
    }

    public boolean isRemote() {
        return getStat() == RuleSetStat.ONLINE;
    }

    /** @return true if the ruleset is available, neither in the cache nor in the remote server */
    public boolean isInvalid() {
        return getStat() == RuleSetStat.INVALID;
    }

    public boolean isUpgradable() {

        RuleSetStat stat = getStat();
        return RuleSetStat.OFFLINE_WITH_NEW_VERSION == stat || RuleSetStat.ONLINE == stat;
    }
}
