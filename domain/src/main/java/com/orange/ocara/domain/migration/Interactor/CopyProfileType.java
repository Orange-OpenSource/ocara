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


package com.orange.ocara.domain.migration.Interactor;

import com.orange.ocara.domain.repositories.ProfileTypeRepository;
import com.orange.ocara.data.cache.database.Tables.ProfileType;
import com.orange.ocara.data.oldEntities.ProfileTypeEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class CopyProfileType extends Interactor<ProfileTypeEntity> {
    ProfileTypeRepository repository;

    @Inject
    public CopyProfileType(ProfileTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Completable execute() {
        List<ProfileTypeEntity> profileTypeEntities = getAll(ProfileTypeEntity.class);
        List<ProfileType> profileTypes = new ArrayList<>();
        for (ProfileTypeEntity entity : profileTypeEntities) {
            profileTypes.add(ProfileType.ProfileTypeBuilder.aProfileType()
                    .withIcon(entity.icon)
                    .withName(entity.name)
                    .withReference(entity.reference)
                    .withRulesetRef(entity.mRulsetDetails.getReference())
                    .withRulesetVersion(Integer.parseInt(entity.mRulsetDetails.getVersion()))
                    .build());
        }
        return repository.insert(profileTypes);
    }
}
