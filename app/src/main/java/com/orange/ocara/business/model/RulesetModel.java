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

package com.orange.ocara.business.model;

import com.orange.ocara.data.net.model.RulesetEntity;

import java.io.Serializable;

import lombok.Builder;

/**
 * Data Transfer object to the view
 */
@Builder
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

    @Builder.Default
    private RuleSetStat stat = RuleSetStat.ONLINE;

    /** default constructor */
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

    /**
     *
     * @return an empty {@link RulesetModel} with the status {@link RuleSetStat#INVALID}
     */
    public static RulesetModel emptyRuleSetInfo() {
        RulesetModel data = new RulesetModel();
        data.setStat(RuleSetStat.INVALID);
        return data;
    }

    /**
     * Creates an instance of {@link RulesetModel} based on a given {@link RulesetEntity}.
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
}
