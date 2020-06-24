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

import com.orange.ocara.data.net.model.ProfileTypeEntity;

import java.util.List;

/** contract between a data store that exposes ProfileTypes and repositories that handle them locally */
public interface ProfileTypeSource {

    /** behaivour of a repository that handles ProfileTypes */
    interface ProfileTypeCache {

        /**
         *
         * @param rulesetId an identifier for a {@link ProfileTypeEntity}
         * @return a bunch of {@link ProfileTypeEntity}s
         */
        List<ProfileTypeEntity> findAllByRulesetId(Long rulesetId);
    }

    /** behaviour of a data store that handles ProfileTypes */
    interface ProfileTypeDataStore {

        /**
         * initializes the data store
         */
        void init();
    }
}
