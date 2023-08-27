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

import com.orange.ocara.domain.repositories.ImpactValueRepository;
import com.orange.ocara.data.cache.database.Tables.ImpactValue;
import com.orange.ocara.data.oldEntities.ImpactValueEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class CopyImpactValue extends Interactor<ImpactValueEntity>{
    ImpactValueRepository repository;
    @Inject
    public CopyImpactValue(ImpactValueRepository repository){
        this.repository=repository;
    }

    @Override
    public Completable execute() {
        List<ImpactValueEntity> impactValueEntities = getAll(ImpactValueEntity.class);
        List<ImpactValue> impactValues = new ArrayList<>();
        for(ImpactValueEntity entity:impactValueEntities){
            impactValues.add(ImpactValue.ImpactValueBuilder.anImpactValue()
                    .withEditable(entity.editable)
                    .withName(entity.name)
                    .withReference(entity.reference)
                    .build());
        }
        return repository.insert(impactValues);
    }
}
