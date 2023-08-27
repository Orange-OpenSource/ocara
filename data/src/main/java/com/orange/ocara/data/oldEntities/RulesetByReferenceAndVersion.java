/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.data.oldEntities;


import com.orange.ocara.data.network.models.WithState;
import com.orange.ocara.utils.models.VersionableModel;

/**
 * Behaviour of a model that can be identified by a reference and a version
 */
public interface RulesetByReferenceAndVersion extends RulesetById, VersionableModel, WithState {

    /**
     *
     * @return a title for the ruleset
     */
    String getType();

    /**
     *
     * @return true if the ruleset is available on the remote server
     */
    boolean isRemotelyAvailable();

    /**
     *
     * @return true if the ruleset is available in the local cache
     */
    boolean isLocallyAvailable();

}
