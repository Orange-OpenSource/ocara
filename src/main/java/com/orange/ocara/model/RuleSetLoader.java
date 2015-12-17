/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model;

import com.orange.ocara.modelStatic.RuleSet;

import java.util.Set;

public interface RuleSetLoader {

    /**
     * To initialize the component.
     */
    void initialize();

    /**
     * To terminate the component.
     */
    void terminate();


    /**
     * returns the set of ruleSets installed on the device
     * @return the set of ruleSets installed on the device
     */
    Set<String> getInstalledRuleSetIds();


    /**
     * Loads the given ruleSet
     * @param ruleSetId the id of the ruleSet to be loaded
     * @return the ruleSet
     */
    RuleSet loadInstalledRuleSet(String ruleSetId);


}
