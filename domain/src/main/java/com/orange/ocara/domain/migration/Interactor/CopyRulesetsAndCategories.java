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
import com.orange.ocara.domain.repositories.RulesetRepository;
import com.orange.ocara.data.cache.database.Tables.EquipmentCategory;
import com.orange.ocara.data.cache.database.Tables.RulesetDetails;
import com.orange.ocara.data.oldEntities.QuestionnaireGroupEntity;
import com.orange.ocara.data.oldEntities.RulesetEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class CopyRulesetsAndCategories extends Interactor<RulesetEntity> {
    RulesetRepository repository;
    CategoryRepository categoryRepository;
    @Inject
    public CopyRulesetsAndCategories(RulesetRepository repository,CategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Completable execute() {
        List<RulesetEntity> rulesetEntities = getAll(RulesetEntity.class);
        List<RulesetDetails> rulesetDetails = new ArrayList<>();
        List<EquipmentCategory> categories = new ArrayList<>();
        for (RulesetEntity rulesetEntity : rulesetEntities) {
            for(QuestionnaireGroupEntity entity:rulesetEntity.getQuestionnaireGroupDb()){
                categories.add(EquipmentCategory.EquipmentCategoryBuilder.anEquipmentCategory()
                        .withId(entity.getId().intValue())
                        .withName(entity.name)
                        .withRulesetRef(rulesetEntity.getReference())
                        .withRulesetVer(Integer.parseInt(rulesetEntity.getVersion()))
                        .build());
            }
            rulesetDetails.add(new RulesetDetails.Builder()
                    .id(rulesetEntity.getId())
                    .setDate(rulesetEntity.getDate())
                    .setReference(rulesetEntity.getReference())
                    .setAuthorName(rulesetEntity.getAuthorName())
                    .setCategoryName(rulesetEntity.getRuleCategoryName())
                    .setComment(rulesetEntity.getComment())
                    .setLanguage(rulesetEntity.getLanguage())
                    .setRuleSetStat(rulesetEntity.getStat())
                    .setType(rulesetEntity.getType())
                    .setVersion(Integer.parseInt(rulesetEntity.getVersion()))
                    .build());
        }
        return repository.insertRulesets(rulesetDetails).concatWith(categoryRepository.insert(categories));
    }
}
