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

package com.orange.ocara.data.cache.db;

import com.orange.ocara.data.net.model.ProfileTypeEntity;

import java.util.List;

/**
 * behaviour for {@link ProfileTypeEntity} repository
 */
public interface ProfileTypeDao {

    /**
     * check if the local storage has elements
     *
     * @param rulesetId an identifier
     * @return true, if at least one element exists. false, if none.
     */
    boolean exists(Long rulesetId);

    /**
     * retrieves a {@link List} of {@link ProfileTypeEntity}s
     *
     * @param rulesetId an identifier for rulesets
     * @return retrieves the list
     */
    List<ProfileTypeEntity> findAll(Long rulesetId);
}
