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

package com.orange.ocara.data.cache;

import com.orange.ocara.data.cache.db.EquipmentDao;
import com.orange.ocara.data.net.model.Equipment;
import com.orange.ocara.data.source.EquipmentSource;
import com.orange.ocara.tools.ListUtils;

import java.util.List;

/** default implementation of {@link com.orange.ocara.data.source.EquipmentSource.EquipmentCache} */
public class EquipmentCacheImpl implements EquipmentSource.EquipmentCache {

    private final EquipmentDao dao;

    public EquipmentCacheImpl(EquipmentDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Equipment> findAllByRulesetId(Long rulesetId) {
        return ListUtils.newArrayList(dao.findAll(rulesetId));
    }
}
