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

package com.orange.ocara.data.cache.db;

import androidx.annotation.NonNull;

import com.orange.ocara.business.model.VersionableModel;
import com.orange.ocara.data.net.model.Ruleset;
import com.orange.ocara.data.net.model.RulesetByReferenceAndVersion;
import com.orange.ocara.data.net.model.RulesetEntity;

import java.util.List;

/**
 * behaviour for {@link RulesetEntity} cache
 */
public interface RulesetDao {

    /**
     * checks the existence of rulesets based on 1 argument
     *
     * @param reference
     * @return true, if at least one ruleset matches the given argument
     */
    boolean exists(String reference);

    /**
     * checks the existence of rulesets based on 2 arguments
     *
     * @param reference an identifier for rulesets
     * @param version   an other identifier for rulesets
     * @return true, if at least one ruleset matches the given arguments
     */
    boolean exists(String reference, Integer version);

    /**
     * @param ruleset a {@link RulesetByReferenceAndVersion}
     * @return true, if at least one ruleset matches the given argument
     */
    boolean exists(VersionableModel ruleset);

    /**
     * retrieves the newest ruleset with the given argument
     *
     * @param reference an identifier for rulesets
     * @return retrieves the ruleset that matches the given reference and the highest version number
     */
    RulesetEntity findLast(String reference);

    /**
     * retrieves the newest ruleset with the given argument
     *
     * @param reference an identifier for rulesets
     * @return retrieves the ruleset that matches the given reference and the highest version number
     */
    RulesetEntity findOne(String reference, Integer version);

    /**
     * @param rulesetId an identifier for rulesets
     * @return retrieves the ruleset with the given identifier
     */
    RulesetEntity findOne(Long rulesetId);

    /**
     * saves the given {@link RulesetEntity}
     *
     * @param entity the data to persist
     * @return the persisted entity
     */
    RulesetEntity save(@NonNull RulesetEntity entity);

    /**
     *
     * @return a bunch of {@link Ruleset}s
     */
    List<RulesetEntity> findAll();
}
