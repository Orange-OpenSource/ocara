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

package com.orange.ocara.business.repository;

import com.orange.ocara.business.model.RulesetLightModel;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.model.VersionableModel;
import com.orange.ocara.data.net.model.Ruleset;

import java.util.List;

/**
 * Behaviour of a repository dedicated to the retrieval of rulesets
 */
public interface RulesetRepository extends InitializableRepository {

    /**
     * check if a ruleset exists
     *
     * @param reference an identifier for rulesets
     * @return true if the repository contains at least one ruleset that matches the argument
     */
    boolean exists(String reference);

    /**
     * check if a ruleset exists
     *
     * @param ruleset a ruleset
     * @return true if the repository contains at least one ruleset that matches the argument
     */
    boolean exists(VersionableModel ruleset);

    /**
     * returns a ruleset
     *
     * @param reference an identifier for rulesets
     * @param version a number
     * @return the {@link RulesetModel} that matches the arguments
     */
    RulesetModel findOne(String reference, Integer version);

    /**
     *
     * @return a bunch of {@link Ruleset}s
     */
    List<RulesetModel> findAll();

    /**
     *
     * @param target data for the new {@link Ruleset}
     * @return the upgraded {@link Ruleset}
     */
    RulesetModel upgradeRuleset(VersionableModel target);

    /**
     * Change the default {@link RulesetModel}
     *
     * @param ruleset the new default value
     */
    void saveDefaultRuleset(RulesetModel ruleset);

}
