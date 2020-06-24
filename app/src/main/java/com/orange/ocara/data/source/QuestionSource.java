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

import com.orange.ocara.data.net.model.QuestionEntity;

import java.util.List;

/** contract between a data store that exposes questions (aka groups of rules) and repositories that handle them locally */
public interface QuestionSource {

    /** description of a repository that handles items locally */
    interface QuestionCache {
        /**
         *
         * @param rulesetId an identifier for a {@link com.orange.ocara.data.net.model.Ruleset}
         * @param equipmentId an identifier for a {@link com.orange.ocara.data.net.model.Equipment}
         * @return a list of {@link QuestionEntity}s
         */
        List<QuestionEntity> findAll(Long rulesetId, Long equipmentId);
    }

    /** description of a data store that exposes items to the upper layer */
    interface QuestionDataStore {

        /**
         *
         * @param rulesetId an identifier for a {@link com.orange.ocara.data.net.model.Ruleset}
         * @param equipmentId an identifier for a {@link com.orange.ocara.data.net.model.Equipment}
         * @return a list of {@link QuestionEntity}s
         */
        List<QuestionEntity> findAll(Long rulesetId, Long equipmentId);
    }
}
