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

import com.orange.ocara.data.net.model.Equipment;

import java.util.List;

/** contract between a data store that exposes Equipments and repositories that handle them locally */
public interface EquipmentSource {

    /** behaviour of a local repository that handles Equipments */
    interface EquipmentCache {

        /**
         *
         * @param rulesetId an identifier for a {@link com.orange.ocara.data.net.model.Equipment}
         * @return a bunch of {@link Equipment}s
         */
        List<Equipment> findAllByRulesetId(Long rulesetId);
    }

    /** behaviour of a data store that handles Equipments */
    interface EquipmentDataStore {

        /**
         * initializes the data store
         */
        void init();
    }
}
