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
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.data.net.model.WithIcon;

import java.util.List;

/** default implementation of {@link ProfileTypeSource.ProfileTypeDataStore} */
public class ProfileTypeDataStoreImpl implements ProfileTypeSource.ProfileTypeDataStore {

    private final RulesetSource.RulesetCache rulesetCache;

    private final ProfileTypeSource.ProfileTypeCache profileTypeCache;

    private final ImageSource.ImageCache imageCache;

    public ProfileTypeDataStoreImpl(RulesetSource.RulesetCache rulesetCache, ProfileTypeSource.ProfileTypeCache profileTypeCache, ImageSource.ImageCache imageCache) {
        this.rulesetCache = rulesetCache;
        this.profileTypeCache = profileTypeCache;
        this.imageCache = imageCache;
    }

    @Override
    public void init() {

        if (rulesetCache.checkDemoRulesetExists()) {
            RulesetEntity ruleset = rulesetCache.findDemoRuleset();

            List<ProfileTypeEntity> items = profileTypeCache.findAllByRulesetId(ruleset.getId());
            for (WithIcon item : items) {
                String icon = item.getIcon();
                if (icon != null) {
                    imageCache.createNewFile(icon, "profile-types");
                }
            }
        }
    }
}
