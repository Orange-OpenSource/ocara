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

import com.orange.ocara.business.model.RuleSetStat;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.model.VersionableModel;
import com.orange.ocara.data.RulesetModelFactory;
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.data.net.model.RulesetLightWs;
import com.orange.ocara.data.net.model.RulesetWs;
import com.orange.ocara.data.net.model.WithIcon;
import com.orange.ocara.tools.CollectionUtils;
import com.orange.ocara.tools.ListUtils;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.orange.ocara.data.RulesetModelFactory.makeRulesetModel;
import static com.orange.ocara.data.source.RulesetSource.RulesetCache;
import static com.orange.ocara.data.source.RulesetSource.RulesetDataStore;
import static com.orange.ocara.data.source.RulesetSource.RulesetRemote;

/** default implementation of {@link RulesetDataStore} */
public class RulesetDataStoreImpl implements RulesetDataStore {

    private final RulesetRemote rulesetRemote;

    private final RulesetCache rulesetCache;

    private final ImageSource.ImageRemote imageRemote;

    private final ImageSource.ImageCache imageCache;

    /**
     * instantiates.
     *
     * @param rulesetCache a local repository for rulesets
     * @param rulesetRemote a remote repository for rulesets
     */
    public RulesetDataStoreImpl(RulesetCache rulesetCache, RulesetRemote rulesetRemote, ImageSource.ImageCache imageCache, ImageSource.ImageRemote imageRemote) {
        this.rulesetRemote = rulesetRemote;
        this.rulesetCache = rulesetCache;
        this.imageRemote = imageRemote;
        this.imageCache = imageCache;
    }

    @Override
    public void saveDefaultRuleset(RulesetModel ruleset) {

        rulesetCache.saveDefaultRuleset(ruleset);
    }

    @Override
    public List<RulesetModel> findAll(boolean localOnly) {

        List<RulesetModel> output = ListUtils.newArrayList();

        // merging cache and remote
        Map<String, RulesetModel> cachedRulesets = cachedToMap(rulesetCache.findAll());
        output.addAll(cachedRulesets.values());

        if (!localOnly) {
            Map<String, RulesetModel> remoteRulesets = wsToMap(rulesetRemote.findAll());

            Set<String> cachedRulesetsReferences = cachedRulesets.keySet();
            Set<String> remoteRulesetsReferences = remoteRulesets.keySet();

            Set<String> intersection = CollectionUtils.intersection(cachedRulesetsReferences, remoteRulesetsReferences);
            for (String ref : intersection) {
                if (isSameAs(remoteRulesets.get(ref), cachedRulesets.get(ref))) {
                    remoteRulesets.remove(ref);
                } else if (isRemoteNewerThanCached(remoteRulesets.get(ref), cachedRulesets.get(ref))) {
                    remoteRulesets.get(ref).setStat(RuleSetStat.OFFLINE_WITH_NEW_VERSION);
                }
            }

            output.addAll(remoteRulesets.values());
        }

        // sorting
        RulesetComparator comparator;
        if (rulesetCache.checkDefaultRulesetExists()) {
            RulesetModel defaultRuleset =  rulesetCache.findDefaultRuleset();
            comparator = new RulesetComparator(defaultRuleset);

            if (allDifferent(output, defaultRuleset)) {

                output.add(0, defaultRuleset);
            }
        } else {
            comparator = new RulesetComparator();
        }
        Collections.sort(output, comparator);

        return output;
    }

    /**
     * Upgrading a ruleset is about retrieving a new version, saving it, and then copying all its
     * illustrations from remote to cache.
     *
     * @param target metadata about the ruleset to download and save
     * @return a {@link RulesetModel} of the upgraded ruleset
     */
    @Override
    public RulesetModel upgrade(VersionableModel target) {

        RulesetWs newRuleset = rulesetRemote.findOne(target.getReference(), Integer.parseInt(target.getVersion()));

        RulesetEntity entity = rulesetCache.save(newRuleset);

        InputStream is;
        String filename;
        List<WithIcon> illustrableItems = ListUtils.newArrayList();
        illustrableItems.addAll(newRuleset.getEquipments());
        illustrableItems.addAll(newRuleset.getProfileTypes());
        illustrableItems.addAll(newRuleset.getIllustrations());

        for (WithIcon item : illustrableItems) {
            filename = item.getIcon();
            if (filename != null && !filename.isEmpty() && !imageCache.fileExists(filename)) {
                is = imageRemote.get(filename);
                imageCache.write(is, filename);
            }
        }

        entity.setStat(RuleSetStat.OFFLINE);

        return makeRulesetModel(entity);
    }

    @Override
    public boolean exists(String reference) {
        return rulesetCache.exists(reference) || rulesetRemote.findLast(reference) != null;
    }

    @Override
    public boolean exists(VersionableModel ruleset) {
        return rulesetCache.exists(ruleset);
    }

    @Override
    public RulesetModel findOne(String reference, Integer version) {
        RulesetEntity entity;
        RulesetModel output = null;
        if (rulesetCache.exists(reference, version)) {
            entity = rulesetCache.findOne(reference, version);
        } else {
            entity = RulesetEntity.toEntity(rulesetRemote.findOne(reference, version));
        }
        if (entity != null) {
            output = makeRulesetModel(entity);
        }
        return output;
    }

    @Override
    public RulesetModel findOne(Long id) {
        RulesetModel output = null;
        RulesetEntity entity = rulesetCache.findOne(id);
        if (entity != null) {
            output = makeRulesetModel(entity);
        }
        return output;
    }

    @Override
    public RulesetEntity findLast(String reference) {

        return rulesetCache.findLast(reference);
    }

    @Override
    public void init() {

        rulesetCache.init();
    }

    @Override
    public boolean requiresInitialization() {
        RulesetEntity demo = rulesetCache.findDemoRuleset();

        return demo == null;
    }

    private static Map<String, RulesetModel> cachedToMap(List<RulesetModel> input) {
        Map<String, RulesetModel> map = new HashMap<>();
        for (RulesetModel item : input) {
            map.put(item.getReference(), item);
        }
        return map;
    }

    private static Map<String, RulesetModel> wsToMap(List<RulesetLightWs> input) {
        Map<String, RulesetModel> map = new HashMap<>();
        for (RulesetLightWs item : input) {
            map.put(item.getReference(), RulesetModelFactory.makeRulesetModel(item));
        }
        return map;
    }

    private static boolean isRemoteNewerThanCached(RulesetModel actual, RulesetModel initial) {

        boolean result = false;
        boolean isValid = initial != null && initial.getReference() != null && initial.getVersion() != null;
        if (isValid) {
            boolean hasSameReference = initial.getReference().compareTo(actual.getReference()) == 0;
            boolean hasNewerVersion = actual.getVersion().compareTo(initial.getVersion()) == 1;
            result = hasSameReference && hasNewerVersion;
        }
        return result;
    }


    private static boolean isSameAs(RulesetModel actual, RulesetModel compared) {
        boolean result = false;
        boolean isValid = compared != null && compared.getReference() != null && compared.getVersion() != null;
        if (isValid) {
            boolean hasSameReference = compared.getReference().compareTo(actual.getReference()) == 0;
            boolean hasSameVersion = actual.getVersion().compareTo(compared.getVersion()) == 0;
            result = hasSameReference && hasSameVersion;
        }

        return result;
    }

    /**
     * check if an element is NOT present in a list
     *
     * @param source    collection of elements where the reference is looked for
     * @param reference the {@link RulesetModel} to find
     * @return true if all the elements of the collection are different from the given reference
     */
    private static boolean allDifferent(List<RulesetModel> source, RulesetModel reference) {
        boolean allMatch = true;
        for (RulesetModel element : source) {
            if (isSameAs(element, reference)) {
                allMatch = false;
            }
        }
        return allMatch;
    }

}
