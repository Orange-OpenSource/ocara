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
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.data.net.model.WithIcon;

import java.util.List;

/** default implementation of {@link com.orange.ocara.data.source.EquipmentSource.EquipmentDataStore} */
public class EquipmentDataStoreImpl implements EquipmentSource.EquipmentDataStore {

    private final RulesetSource.RulesetCache rulesetCache;

    private final EquipmentSource.EquipmentCache equipmentCache;

    private final ImageSource.ImageCache imageCache;

    public EquipmentDataStoreImpl(RulesetSource.RulesetCache rulesetCache, EquipmentSource.EquipmentCache equipmentCache, ImageSource.ImageCache imageCache) {
        this.rulesetCache = rulesetCache;
        this.equipmentCache = equipmentCache;
        this.imageCache = imageCache;
    }

    @Override
    public void init() {

        if (rulesetCache.checkDemoRulesetExists()) {
            RulesetEntity ruleset = rulesetCache.findDemoRuleset();

            List<Equipment> items = equipmentCache.findAllByRulesetId(ruleset.getId());
            for (WithIcon item : items) {
                String icon = item.getIcon();
                if (icon != null) {
                    imageCache.createNewFile(icon, "objects");
                }
            }
        }
    }
}
