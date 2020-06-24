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

import com.orange.ocara.data.net.model.IllustrationEntity;

import java.util.List;

/**
 * description of a DAO dedicated to {@link IllustrationEntity}s
 */
public interface IllustrationDao {

    IllustrationEntity findOne(Long id);

    IllustrationEntity findOne(Long rulesetId, String illustrationReference);

    boolean exists(Long id);

    boolean exists(Long rulesetId, String illustrationReference);

    List<IllustrationEntity> findAllByRulesetId(Long rulesetId);

    List<IllustrationEntity> findAllByRuleId(Long ruleId);
}
