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

package com.orange.ocara.business.service.impl;

import com.orange.ocara.business.repository.EquipmentRepository;
import com.orange.ocara.business.service.EquipmentService;
import com.orange.ocara.data.EquipmentDataRepository;
import com.orange.ocara.data.cache.db.EquipmentDaoImpl;
import com.orange.ocara.data.net.model.Equipment;
import com.orange.ocara.tools.ListUtils;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;

import java.util.List;

/**
 * Default implementation of {@link EquipmentService}
 */
@EBean
public class EquipmentServiceImpl implements EquipmentService {

    /**
     * Target repository
     */
    private EquipmentRepository objectDescriptionRepository;

    @AfterInject
    void afterInjection() {
        objectDescriptionRepository = new EquipmentDataRepository(new EquipmentDaoImpl());
    }

    @Override
    public List<Equipment> retrieveObjectDescriptionsByRulesetId(Long rulesetId) {
        return ListUtils.newArrayList(objectDescriptionRepository.findAll(rulesetId));
    }
}
