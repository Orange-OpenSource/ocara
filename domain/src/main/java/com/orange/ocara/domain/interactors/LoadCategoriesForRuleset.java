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


package com.orange.ocara.domain.interactors;

import com.orange.ocara.domain.models.EquipmentCategoryModel;
import com.orange.ocara.domain.repositories.CategoryRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class LoadCategoriesForRuleset {
    CategoryRepository categoryRepository;
    @Inject
    public LoadCategoriesForRuleset(CategoryRepository categoryRepository){
        this.categoryRepository=categoryRepository;
    }
    public Single<List<EquipmentCategoryModel>> execute(String rulesetRef, int rulesetVer){
        return Single.fromObservable(categoryRepository.getCategories(rulesetRef, rulesetVer));
    }
}
