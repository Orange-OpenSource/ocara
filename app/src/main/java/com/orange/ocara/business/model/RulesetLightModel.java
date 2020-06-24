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

/**
 * Data Transfer object to the view
 */
public class RulesetLightModel implements VersionableModel  {

    private Long id;

    private String reference;

    private String version;

    private RuleSetStat stat = RuleSetStat.INVALID;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getVersionName() {
        return reference + "_" + version;
    }

    public RuleSetStat getStat() {
        return stat;
    }

    public void setStat(RuleSetStat stat) {
        this.stat = stat;
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

}
