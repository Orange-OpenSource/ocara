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

import com.orange.ocara.utils.models.VersionableModel;
import com.orange.ocara.data.cache.database.Tables.RulesetDetails;
import com.orange.ocara.utils.enums.RuleSetStat;
import com.orange.ocara.data.oldEntities.RulesetEntity;

import java.io.Serializable;

/**
 * Data Transfer object to the view
 */
// todo this is used in the new refactored design
// just delete the RulesetEntity method after finishing the refactoring
public class RulesetModel implements Serializable, VersionableModel {

    private static final long serialVersionUID = 1;

    private Long id;

    private String reference;

    private String type;

    private String version;

    private String date;

    private String author;

    private String comment;

    private String ruleCategoryName;

    private RuleSetStat stat = RuleSetStat.ONLINE;
    private boolean isDemo = false;

    /**
     * default constructor
     */
    public RulesetModel() {
    }

    public RulesetModel(Long id, String reference, String type, String version, String date, String author, String comment, String ruleCategoryName, RuleSetStat stat) {
        this.id = id;
        this.reference = reference;
        this.type = type;
        this.version = version;
        this.date = date;
        this.author = author;
        this.comment = comment;
        this.ruleCategoryName = ruleCategoryName;
        this.stat = stat;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * @return an empty {@link RulesetModel} with the status {@link RuleSetStat#INVALID}
     */
    public static RulesetModel emptyRuleSetInfo() {
        RulesetModel data = new RulesetModel();
        data.setStat(RuleSetStat.INVALID);
        return data;
    }

    public static RulesetModel newRuleSetInfo(RulesetDetails input) {
        RulesetModel out;
        if (input == null) {
            out = emptyRuleSetInfo();
        } else {
            out = RulesetModel
                    .builder()
                    .id(input.getRulesetId())
                    .version(input.getVersion() + "")
                    .reference(input.getReference())
                    .stat(input.getRuleSetStat())
                    .type(input.getType())
                    .date(input.getDate())
                    .comment(input.getComment())
                    .author(input.getAuthorName())
                    .build();
        }
        return out;
    }

    public boolean isSameAs(RulesetModel compared) {
        boolean result = false;
        boolean isValid = compared != null && compared.getReference() != null && compared.getVersion() != null;
        if (isValid) {
            boolean hasSameReference = compared.getReference().compareTo(this.getReference()) == 0;
            boolean hasSameVersion = this.getVersion().compareTo(compared.getVersion()) == 0;
            result = hasSameReference && hasSameVersion;
        }
        return result;
    }

    public boolean isRemoteNewerThanCached(RulesetModel remote) {
        boolean result = false;
        boolean isValid = remote != null && remote.getReference() != null && remote.getVersion() != null;
        if (isValid) {
            boolean hasSameReference = remote.getReference().compareTo(this.getReference()) == 0;
            boolean hasNewerVersion = this.getVersion().compareTo(remote.getVersion()) == 1;
            result = hasSameReference && hasNewerVersion;
        }
        return result;
    }

    /**
     * Creates an instance of {@link RulesetModel} based on a given {@link RulesetEntity}.
     *
     * @param input the source
     * @return an instance of {@link RulesetModel}
     */
    public static RulesetModel newRuleSetInfo(RulesetEntity input) {
        RulesetModel out;
        if (input == null) {
            out = emptyRuleSetInfo();
        } else {
            out = RulesetModel
                    .builder()
                    .id(input.getId())
                    .version(input.getVersion())
                    .reference(input.getReference())
                    .stat(input.getStat())
                    .type(input.getType())
                    .date(input.getDate())
                    .comment(input.getComment())
                    .author(input.getAuthorName())
                    .build();
        }
        return out;
    }

    public Long getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public String getType() {
        return type;
    }

    public String getVersion() {
        return version;
    }

    public String getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public String getComment() {
        return comment;
    }

    public String getRuleCategoryName() {
        return ruleCategoryName;
    }

    public RuleSetStat getStat() {
        return stat;
    }

    public void setStat(RuleSetStat stat) {
        this.stat = stat;
    }

    public String getVersionName() {
        return reference + "_" + version;
    }

    public boolean isLocallyAvailable() {
        return stat == RuleSetStat.OFFLINE || stat == RuleSetStat.OFFLINE_WITH_NEW_VERSION;
    }

    public boolean isRemotelyAvailable() {
        return stat == RuleSetStat.ONLINE;
    }

    public boolean isUpgradable() {

        RuleSetStat stat = getStat();
        return RuleSetStat.OFFLINE_WITH_NEW_VERSION == stat || RuleSetStat.ONLINE == stat;
    }

    public static class Builder {
        private final RulesetModel rulesetModel = new RulesetModel();

        public Builder id(Long id) {
            this.rulesetModel.id = id;
            return this;
        }
        public Builder isDemo(boolean isDemo) {
            this.rulesetModel.isDemo = isDemo;
            return this;
        }
        public Builder reference(String reference) {
            this.rulesetModel.reference = reference;
            return this;
        }

        public Builder type(String type) {
            this.rulesetModel.type = type;
            return this;
        }

        public Builder version(String version) {
            this.rulesetModel.version = version;
            return this;
        }

        public Builder date(String date) {
            this.rulesetModel.date = date;
            return this;
        }

        public Builder author(String author) {
            this.rulesetModel.author = author;
            return this;
        }

        public Builder comment(String comment) {
            this.rulesetModel.comment = comment;
            return this;
        }

        public Builder ruleCategoryName(String ruleCategoryName) {
            this.rulesetModel.ruleCategoryName = ruleCategoryName;
            return this;
        }

        public Builder stat(RuleSetStat stat) {
            this.rulesetModel.stat = stat;
            return this;
        }

        public RulesetModel build() {
            return rulesetModel;
        }

    }

}
