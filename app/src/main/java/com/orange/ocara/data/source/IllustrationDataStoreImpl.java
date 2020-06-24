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

import com.orange.ocara.data.net.model.Explanation;
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.data.net.model.WithIcon;
import com.orange.ocara.data.source.IllustrationSource.IllustrationDataStore;

import java.util.List;

/**
 * default implementation of {@link IllustrationDataStore}
 */
public class IllustrationDataStoreImpl implements IllustrationDataStore {

    private final RulesetSource.RulesetCache rulesetCache;

    private final IllustrationSource.IllustrationCache illustrationCache;

    private final ImageSource.ImageCache imageCache;

    public IllustrationDataStoreImpl(RulesetSource.RulesetCache rulesetCache, IllustrationSource.IllustrationCache illustrationCache, ImageSource.ImageCache imageCache) {
        this.rulesetCache = rulesetCache;
        this.illustrationCache = illustrationCache;
        this.imageCache = imageCache;
    }

    @Override
    public List<Explanation> findAllByRuleId(Long ruleId) {
        return illustrationCache.findAllByRuleId(ruleId);
    }

    @Override
    public void init() {

        if (rulesetCache.checkDemoRulesetExists()) {
            RulesetEntity ruleset = rulesetCache.findDemoRuleset();

            List<Explanation> items = illustrationCache.findAllByRulesetId(ruleset.getId());
            for (WithIcon item : items) {
                String icon = item.getIcon();
                if (icon != null) {
                    imageCache.createNewFile(icon, "ruleset");
                }
            }
        }
    }
}
