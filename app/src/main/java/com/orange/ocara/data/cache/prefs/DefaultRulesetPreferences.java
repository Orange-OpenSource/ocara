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

package com.orange.ocara.data.cache.prefs;

import android.content.Context;

import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.model.VersionableModel;

import java.util.List;

/**
 * Behaviour of a service that stores and reads the default value for a ruleset
 *
 *
 */
public interface DefaultRulesetPreferences {

    /**
     * Change the default {@link VersionableModel}
     *
     * @param ruleset the new default value
     */
    void saveRuleset(RulesetModel ruleset);

    List<RulesetModel> findAll();

    /**
     * Retrieves the default {@link VersionableModel}
     *
     * @return the {@link VersionableModel} defined as default, if exists. Returns null, if none.
     */
    RulesetModel retrieveRuleset();

    /**
     * Checks that a default {@link VersionableModel} exists
     *
     * @return true, if the default value exists in the given {@link Context}
     */
    boolean checkRulesetExists();

    /**
     * @return the last time data was cached
     */
    Long getLastCacheTime();

    /**
     * Store the last time data was cached
     */
    void setLastCacheTime(long time);
}
