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

import com.orange.ocara.data.net.model.Explanation;
import com.orange.ocara.data.net.model.RuleEntity;

import java.util.List;

/** contract between a data store that exposes illustrations / explanations and the repositories that handle them locally */
public interface IllustrationSource {

    /** behaviour of a local repository that handles illustrations */
    interface IllustrationCache {

        /**
         *
         * @param ruleId an identifier for a {@link RuleEntity}
         * @return a bunch of {@link Explanation}s
         */
        List<Explanation> findAllByRuleId(Long ruleId);

        /**
         *
         * @param rulesetId an identifier for a {@link com.orange.ocara.data.net.model.Ruleset}
         * @return a bunch of {@link Explanation}s
         */
        List<Explanation> findAllByRulesetId(Long rulesetId);
    }

    /** behaviour of a data store that handles illustrations */
    interface IllustrationDataStore {

        /**
         *
         * @param ruleId an identifier for a {@link RuleEntity}
         * @return
         */
        List<Explanation> findAllByRuleId(Long ruleId);

        /**
         * initializes the data store
         */
        void init();
    }
}
