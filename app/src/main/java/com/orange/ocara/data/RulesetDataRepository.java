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

import com.orange.ocara.business.model.RulesetLightModel;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.model.VersionableModel;
import com.orange.ocara.business.repository.RulesetRepository;
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.data.source.EquipmentSource;
import com.orange.ocara.data.source.IllustrationSource;
import com.orange.ocara.data.source.NetworkInfoSource;
import com.orange.ocara.data.source.ProfileTypeSource;
import com.orange.ocara.data.source.RulesetSource;

import java.util.List;

/**
 * default implementation of {@link RulesetRepository} for retrieving rulesets
 */
public class RulesetDataRepository implements RulesetRepository {

    private final RulesetSource.RulesetDataStore rulesetDataStore;

    private final EquipmentSource.EquipmentDataStore equipmentDataStore;

    private final IllustrationSource.IllustrationDataStore illustrationDataStore;

    private final ProfileTypeSource.ProfileTypeDataStore profileTypeDataStore;

    private final NetworkInfoSource.NetworkInfoCache networkCache;

    RulesetDataRepository(RulesetSource.RulesetDataStore rulesetDataStore, EquipmentSource.EquipmentDataStore equipmentDataStore, IllustrationSource.IllustrationDataStore illustrationDataStore, ProfileTypeSource.ProfileTypeDataStore profileTypeDataStore, NetworkInfoSource.NetworkInfoCache networkCache) {
        this.rulesetDataStore = rulesetDataStore;
        this.equipmentDataStore = equipmentDataStore;
        this.illustrationDataStore = illustrationDataStore;
        this.profileTypeDataStore = profileTypeDataStore;
        this.networkCache = networkCache;
    }

    @Override
    public boolean exists(String reference) {
        return rulesetDataStore.exists(reference);
    }

    @Override
    public boolean exists(VersionableModel ruleset) {
        return rulesetDataStore.exists(ruleset);
    }

    @Override
    public RulesetModel findOne(String reference, Integer version) {
        return rulesetDataStore.findOne(reference, version);
    }

    @Override
    public List<RulesetModel> findAll() {

        return rulesetDataStore.findAll(!networkCache.isNetworkAvailable());
    }

    @Override
    public RulesetModel upgradeRuleset(VersionableModel target) {
        return rulesetDataStore.upgrade(target);
    }


    @Override
    public void init() {

        if (rulesetDataStore.requiresInitialization()) {
            rulesetDataStore.init();
            equipmentDataStore.init();
            profileTypeDataStore.init();
            illustrationDataStore.init();
        }
    }

    @Override
    public void saveDefaultRuleset(RulesetModel ruleset) {
        rulesetDataStore.saveDefaultRuleset(ruleset);
    }
}
