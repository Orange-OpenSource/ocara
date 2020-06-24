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
package com.orange.ocara.data.cache;

import com.orange.ocara.data.cache.db.ProfileTypeDao;
import com.orange.ocara.data.net.model.ProfileTypeEntity;
import com.orange.ocara.data.source.ProfileTypeSource;

import java.util.List;

/** default implementation of {@link com.orange.ocara.data.source.ProfileTypeSource.ProfileTypeCache} */
public class ProfileTypeCacheImpl implements ProfileTypeSource.ProfileTypeCache {

    private final ProfileTypeDao dao;

    public ProfileTypeCacheImpl(ProfileTypeDao dao) {
        this.dao = dao;
    }

    @Override
    public List<ProfileTypeEntity> findAllByRulesetId(Long rulesetId) {
        return dao.findAll(rulesetId);
    }
}
