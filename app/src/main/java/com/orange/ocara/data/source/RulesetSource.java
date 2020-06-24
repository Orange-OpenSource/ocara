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

import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.model.VersionableModel;
import com.orange.ocara.data.net.model.Ruleset;
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.data.net.model.RulesetLightWs;
import com.orange.ocara.data.net.model.RulesetWs;

import java.util.List;

/** contract between a data store that exposes rulesets and repositories that handle them locally and remotely */
public interface RulesetSource {

    /**
     * description of the repository that manage rulesets in a local database
     */
    interface RulesetCache {

        /**
         * the default ruleset is the ruleset that was last selected by the user
         *
         * @param ruleset the {@link VersionableModel} to register
         */
        void saveDefaultRuleset(RulesetModel ruleset);

        /**
         * the default ruleset is the ruleset that was last selected by the user
         *
         * @return a ruleset|null if none exists
         */
        RulesetModel findDefaultRuleset();

        /**
         *
         * @return true if a default ruleset exists
         */
        boolean checkDefaultRulesetExists();

        /**
         * a demo ruleset is a ruleset that is embedded in the app, and should help on initializing
         * the database
         *
         * @return a ruleset|null if none exists
         */
        RulesetEntity findDemoRuleset();

        /**
         * a demo ruleset is a ruleset that is embedded in the app, and should help on initializing
         * the database
         *
         * @return true if a demo ruleset exists
         */
        boolean checkDemoRulesetExists();

        /**
         *
         * @return all the registered {@link Ruleset}s
         */
        List<RulesetModel> findAll();

        /**
         *
         * @param ruleset the entity to save
         * @return the entity after the saving operation
         */
        RulesetEntity save(RulesetEntity ruleset);

        /**
         *
         * @param input the object to save as a {@link RulesetEntity}
         * @return the entity after the saving operation
         */
        RulesetEntity save(RulesetWs input);

        /**
         *
         * @param rulesetId an identifier for a ruleset
         * @return the ruleset that matches the given argument|null if no such ruleset exists
         */
        RulesetEntity findOne(Long rulesetId);

        /**
         *
         * @param reference an identifier for a ruleset
         * @param version an integer
         * @return the ruleset that matches the given arguments|null if no such ruleset exists
         */
        RulesetEntity findOne(String reference, Integer version);

        /**
         *
         * @param reference an identifier for a ruleset
         * @return the ruleset with the latest version|null if no such ruleset exists
         */
        RulesetEntity findLast(String reference);

        /**
         *
         * @param reference an identifier for a ruleset
         * @param version a version for the ruleset
         * @return true if a ruleset matches the given arguments
         */
        boolean exists(String reference, Integer version);

        /**
         *
         * @param ruleset a {@link VersionableModel}
         * @return true if a ruleset matches the given argument
         */
        boolean exists(VersionableModel ruleset);

        /**
         * checks the existence of rulesets based on 1 argument
         *
         * @param reference an identifier for a {@link Ruleset}
         * @return true, if at least one ruleset matches the given argument
         */
        boolean exists(String reference);

        /**
         * initializes the local repository
         */
        void init();

        /**
         *
         * @return true if data has been stored in cache
         */
        boolean isCached();

        /**
         *
         * @return true if the stored data is deprecated
         */
        boolean isExpired();

        /**
         * resets the stored last cache time
         */
        void resetLastCacheTime();
    }

    /**
     * description of the repository that manage rulesets through webservices
     */
    interface RulesetRemote {

        List<RulesetLightWs> findAll();

        RulesetWs findOne(String reference, Integer version);

        RulesetLightWs findLast(String reference);
    }

    /**
     * description of a repository that manages rulesets between both local and remote repositories
     */
    interface RulesetDataStore {

        /**
         *
         * @param ruleset an item to mark as default
         */
        void saveDefaultRuleset(RulesetModel ruleset);

        /**
         * returns a ruleset
         *
         * @param reference an identifier for rulesets
         * @param version a number
         * @return the {@link Ruleset} that matches the arguments
         */
        RulesetModel findOne(String reference, Integer version);

        /**
         *
         * @param id an identifier for rulesets
         * @return an entity
         */
        RulesetModel findOne(Long id);

        /**
         *
         * @param localOnly when true, returns only from cache, else result is a merge from cache and remote.
         * @return a bunch of {@link Ruleset}s
         */
        List<RulesetModel> findAll(boolean localOnly);

        /**
         *
         * @param target a version to upgrade the {@link Ruleset} to
         * @return the upgraded {@link RulesetModel}
         */
        RulesetModel upgrade(VersionableModel target);

        /**
         * check if a ruleset exists
         *
         * @param reference an identifier for rulesets
         * @return true if the repository contains at least one ruleset that matches the argument
         */
        boolean exists(String reference);

        /**
         * check if a ruleset exists
         *
         * @param ruleset a ruleset
         * @return true if the repository contains at least one ruleset that matches the argument
         */
        boolean exists(VersionableModel ruleset);

        /**
         * retrieves a ruleset
         *
         * @param reference an attribute for rulesets
         * @return the latest version of the ruleset that matches the given reference
         */
        RulesetEntity findLast(String reference);

        /**
         * initializes the data store
         */
        void init();

        /**
         *
         * @return true if init is required
         */
        boolean requiresInitialization();
    }
}
