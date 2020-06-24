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

package com.orange.ocara.business.service;

import com.orange.ocara.data.net.model.Equipment;
import com.orange.ocara.data.net.model.EquipmentEntity;

import java.util.List;

/**
 * Behaviour of a service for {@link Equipment}s, also known as {@link EquipmentEntity}s
 */
public interface EquipmentService {

    /**
     * retrieves a bunch of equipments
     *
     * @param rulesetId an identifier for a {@link com.orange.ocara.data.net.model.Ruleset}
     * @return a {@link List} of {@link Equipment}s
     */
    List<Equipment> retrieveObjectDescriptionsByRulesetId(Long rulesetId);
}
