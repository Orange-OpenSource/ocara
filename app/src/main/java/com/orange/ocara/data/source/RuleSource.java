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

package com.orange.ocara.data.source;

import com.orange.ocara.data.net.model.RuleEntity;

import java.util.List;

/** contract between a data store that exposes rules and repositories that handle them locally */
public interface RuleSource {

    /** description of the repository that manage rules in a local database */
    interface RuleCache {

        /**
         *
         * @param rulesetId an identifier for a {@link com.orange.ocara.data.net.model.Ruleset}
         * @return a list of {@link RuleEntity}s
         */
        List<RuleEntity> findAll(Long rulesetId);
    }

    /** description of a data store that exposes rules */
    interface RuleDataStore {

        /**
         *
         * @param rulesetId an identifier for a {@link com.orange.ocara.data.net.model.Ruleset}
         * @return a list of {@link RuleEntity}s
         */
        List<RuleEntity> findAll(Long rulesetId);

    }
}
