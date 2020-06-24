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
 * Available states of a {@link RulesetModel}
 */
public enum RuleSetStat {
    /**
     * the ruleset is in cache, and no newer version is available on the remote server
     */
    OFFLINE,

    /**
     * the ruleset is in cache, and a newer version is available on the remote server
     */
    OFFLINE_WITH_NEW_VERSION,

    /**
     * the ruleset is not in cache, but a older version is available in cache
     */
    ONLINE_WITH_OLD_VERSION,

    /**
     * the ruleset is not in cache, and no older version is available in cache
     */
    ONLINE,

    /**
     * default status. Helps on avoiding null cases.
     */
    INVALID
}
