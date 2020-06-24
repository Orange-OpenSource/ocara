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

package com.orange.ocara.data.net.model;

import com.orange.ocara.business.model.RuleSetStat;

/**
 * Behaviour of a model that has a state
 */
public interface WithState {

    RuleSetStat getStat();

    void setStat(RuleSetStat state);

    boolean isRemotelyAvailable();

    boolean isLocallyAvailable();

    boolean isCached();

    boolean isDeprecated();

    boolean isRemote();

    boolean isInvalid();

    boolean isUpgradable();

}
