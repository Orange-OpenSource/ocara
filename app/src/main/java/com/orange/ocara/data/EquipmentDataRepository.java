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

package com.orange.ocara.data;

import com.orange.ocara.business.repository.EquipmentRepository;
import com.orange.ocara.data.cache.db.EquipmentDao;
import com.orange.ocara.data.net.model.EquipmentEntity;

import java.util.List;

import lombok.RequiredArgsConstructor;

import static com.orange.ocara.tools.ListUtils.newArrayList;

/**
 * Implementation of {@link EquipmentRepository}
 */
@RequiredArgsConstructor
public class EquipmentDataRepository implements EquipmentRepository {

    /**
     * a local repository for {@link EquipmentEntity}s
     */
    private final EquipmentDao dao;

    @Override
    public List<EquipmentEntity> findAll(Long rulesetId) {
        List<EquipmentEntity> items = newArrayList();
        if (dao.exists(rulesetId)) {
            items.addAll(dao.findAll(rulesetId));
        }
        return items;
    }
}
