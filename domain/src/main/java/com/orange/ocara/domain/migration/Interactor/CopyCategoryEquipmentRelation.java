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

import com.orange.ocara.domain.repositories.CategoryRepository;
import com.orange.ocara.data.oldEntities.QuestionnaireGroupEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class CopyCategoryEquipmentRelation extends Interactor<QuestionnaireGroupEntity>{
    CategoryRepository repository;
    @Inject
    public CopyCategoryEquipmentRelation(CategoryRepository repository){
        this.repository=repository;
    }
    @Override
    public Completable execute() {
        List<Completable> completables=new ArrayList<>();
        List<QuestionnaireGroupEntity> questionnaireGroupEntities=getAll(QuestionnaireGroupEntity.class);
        for(QuestionnaireGroupEntity entity:questionnaireGroupEntities){
            completables.add(repository.insertCategoryEquipmentRelation(entity.getId().intValue(),entity.objectRef));
        }
        return Completable.merge(completables);
    }
}
