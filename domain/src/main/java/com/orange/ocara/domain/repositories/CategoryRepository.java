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


package com.orange.ocara.domain.repositories;

import com.orange.ocara.domain.models.EquipmentCategoryModel;
import com.orange.ocara.domain.models.EquipmentModel;
import com.orange.ocara.data.cache.database.DAOs.CategoryDAO;
import com.orange.ocara.data.cache.database.DAOs.EquipmentAndCategoryRelationDAO;
import com.orange.ocara.data.cache.database.DAOs.EquipmentSubEquipmentDAO;
import com.orange.ocara.data.cache.database.NonTables.CategoryWithEquipments;
import com.orange.ocara.data.cache.database.NonTables.EquipmentWithSubEquipment;
import com.orange.ocara.data.cache.database.OcaraDB;
import com.orange.ocara.data.cache.database.Tables.EquipmentCategory;
import com.orange.ocara.data.cache.database.crossRef.EquipmentAndCategoryCrossRef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import timber.log.Timber;

public class CategoryRepository {
    private final CategoryDAO categoryDAO;
    private final EquipmentSubEquipmentDAO equipmentSubEquipmentDAO;
    private final EquipmentAndCategoryRelationDAO equipmentAndCategoryRelationDAO;

    @Inject
    public CategoryRepository(OcaraDB ocaraDB) {
        categoryDAO = ocaraDB.categoryDao();
        equipmentSubEquipmentDAO = ocaraDB.equipmentSubEquipmentDAO();
        equipmentAndCategoryRelationDAO = ocaraDB.equipmentAndCategoryRelationDAO();
    }

    public Completable insert(List<EquipmentCategory> categories) {
        return categoryDAO.insertAndReturnCompletable(categories);
    }
    public Observable<List<EquipmentCategoryModel>> getCategories(String ruleset,int version){
        return categoryDAO.getCategories(ruleset, version)
                .toObservable()
                .concatMap((Function<List<CategoryWithEquipments>, ObservableSource<List<EquipmentCategoryModel>>>) equipmentCategories -> {
                    List<EquipmentCategoryModel> result = new ArrayList<>();
                    HashMap<Integer, EquipmentCategoryModel> idToCategory = new HashMap<>();
                    for (CategoryWithEquipments category : equipmentCategories) {
                        EquipmentCategoryModel categoryModel = new EquipmentCategoryModel(category.getEquipmentCategory());
                        idToCategory.put(categoryModel.getId(), categoryModel);
                    }
                    for (Integer id : idToCategory.keySet()) {
                        result.add(idToCategory.get(id));
                    }
                    List<EquipmentModel> equipments = new ArrayList<>();
                    Map<String, EquipmentModel> refToEquipment = new HashMap<>();
                    for (CategoryWithEquipments category : equipmentCategories) {
                        if (!refToEquipment.containsKey(category.getEquipment().getReference())) {
                            EquipmentModel equipmentModel = new EquipmentModel(category.getEquipment(), null);
                            refToEquipment.put(equipmentModel.getReference(), equipmentModel);
                            equipments.add(equipmentModel);
                        }
                    }
                    for (CategoryWithEquipments category : equipmentCategories) {
                        EquipmentCategoryModel categoryModel = idToCategory.get(category.getEquipmentCategory().getId());
                        if (categoryModel != null) {
                            EquipmentModel equipmentModel = refToEquipment.get(category.getEquipment().getReference());
                            categoryModel.addEquipment(equipmentModel);
                        }
                    }
                    List<String> equipmentRefs = new ArrayList<>();
                    equipmentRefs.addAll(refToEquipment.keySet());
                    return equipmentSubEquipmentDAO.loadSubEquipments(equipmentRefs,ruleset,version)
                            .map(equipmentWithSubEquipments -> {
                                for (EquipmentWithSubEquipment equipmentWithSubEquipment : equipmentWithSubEquipments) {
                                    EquipmentModel equipmentModel = refToEquipment.get(equipmentWithSubEquipment
                                            .getParentRef());
                                    Timber.d("parent ref = " + equipmentModel.getReference());
                                    equipmentModel.addChild(new EquipmentModel(equipmentWithSubEquipment.getSubequipment()));
                                }
                                return result;
                            }).toObservable();
                });
    }


    public Completable insertCategoryEquipmentRelation(int categoryId, List<String> objRefs) {
        List<EquipmentAndCategoryCrossRef> equipmentAndCategoryCrossRefs=new ArrayList<>();
        for(String obj:objRefs){
            equipmentAndCategoryCrossRefs.add(new EquipmentAndCategoryCrossRef(obj,categoryId));
        }
        return equipmentAndCategoryRelationDAO.insert(equipmentAndCategoryCrossRefs);
    }
}

