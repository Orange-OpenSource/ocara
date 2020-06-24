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

import com.activeandroid.query.Select;
import com.orange.ocara.data.net.model.EquipmentEntity;

import java.util.List;

/**
 * Implementation of {@link EquipmentDao}
 */
public class EquipmentDaoImpl implements EquipmentDao {

    private static final String WHERE_CLAUSE = EquipmentEntity.RULESSET_DETAILS + "=?";
    @Override
    public boolean exists(Long rulesetId) {
        return new Select()
                .from(EquipmentEntity.class)
                .where(WHERE_CLAUSE, rulesetId)
                .exists();
    }

    @Override
    public List<EquipmentEntity> findAll(Long rulesetId) {
        return new Select()
                .from(EquipmentEntity.class)
                .where(WHERE_CLAUSE, rulesetId)
                .execute();
    }
}
