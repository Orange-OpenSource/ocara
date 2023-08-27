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

import com.orange.ocara.data.cache.database.DAOs.EquipmentDAO;
import com.orange.ocara.data.cache.database.DAOs.EquipmentRulesetVersionDAO;
import com.orange.ocara.data.cache.database.DAOs.EquipmentSubEquipmentDAO;
import com.orange.ocara.data.cache.database.DAOs.RulesetDAO;
import com.orange.ocara.data.cache.database.OcaraDB;
import com.orange.ocara.data.cache.database.Tables.Equipment;
import com.orange.ocara.data.cache.database.crossRef.EquipmentRulesetVersion;
import com.orange.ocara.data.cache.database.crossRef.EquipmentSubEquipmentCrossRef;
import com.orange.ocara.domain.models.EquipmentModel;
import com.orange.ocara.domain.models.RulesetModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class EquipmentRepository {
    EquipmentRulesetVersionDAO equipmentRulesetVersionDAO;
    RulesetDAO rulesetDao;
    EquipmentDAO equipmentDAO;
    EquipmentSubEquipmentDAO equipmentSubEquipmentDAO;

    @Inject
    public EquipmentRepository(OcaraDB ocaraDB) {
        equipmentRulesetVersionDAO = ocaraDB.equipmentRulesetVersionDAO();
        rulesetDao = ocaraDB.rulesetDAO();
        equipmentDAO = ocaraDB.equipmentDao();
        equipmentSubEquipmentDAO = ocaraDB.equipmentSubEquipmentDAO();
    }

    public Completable insert(List<Equipment> equipments) {
        return equipmentDAO.insert(equipments);
    }

    public Single<Integer> getNumberOfSubObjects(int auditEquipmentId) {
        return equipmentDAO.getNumberOfChildren(auditEquipmentId);
    }

    /*
    this method returns all the equipments related to that ruleset
    but we need to retrieve the ruleset first to connect its model
    to the equipments model
     */
    public Observable<List<EquipmentModel>> findAll(String rulesetRef, int version) {
        return rulesetDao.getRuleset(rulesetRef, version)
                .toObservable()
                .concatMap(rulesetDetails -> equipmentRulesetVersionDAO.getEquipmentsByVersion(rulesetRef, version)
                        .map(equipmentList -> {
                            List<EquipmentModel> equipmentModels = new ArrayList<>();
                            for (Equipment equipment : equipmentList) {
                                equipmentModels.add(new EquipmentModel(equipment
                                        , RulesetModel.newRuleSetInfo(rulesetDetails)));
                            }
                            return equipmentModels;
                        }).toObservable());
    }
    /*
        this method returns all the equipments related to that ruleset
        but we need to retrieve the ruleset first to connect its model
        to the equipments model
         */
    public Observable<List<EquipmentModel>> findAllByRulesetId(long rulesetId) {
        return rulesetDao.getRulesetById(rulesetId)
                .toObservable()
                .concatMap(rulesetDetails -> equipmentRulesetVersionDAO.getEquipmentsById(rulesetId)
                        .map(equipmentList -> {
                            List<EquipmentModel> equipmentModels = new ArrayList<>();
                            for (Equipment equipment : equipmentList) {
                                equipmentModels.add(new EquipmentModel(equipment
                                        , RulesetModel.newRuleSetInfo(rulesetDetails)));
                            }
                            return equipmentModels;
                        }).toObservable());
    }

    public Single<EquipmentModel> loadEquipmentInfoWithChildren(Long auditId, String equipmentRef) {

        return Single.zip(equipmentDAO.getEquipmentByRef(equipmentRef),
                equipmentDAO.getAllChildrenOfEquipment(auditId.intValue(), equipmentRef),
                (parentEquipment, childrenEquipments) -> {

                    EquipmentModel parentEquipmentModel = new EquipmentModel(parentEquipment);
                    addChildrenEquipmentToParent(parentEquipmentModel, childrenEquipments);

                    return parentEquipmentModel;
                });
    }

    private void addChildrenEquipmentToParent(EquipmentModel parentEquipmentModel, List<Equipment> childrenEquipments) {
        List<EquipmentModel> childrenModel = new ArrayList<>();

        for (Equipment equipment : childrenEquipments) {

            childrenModel.add(new EquipmentModel(equipment));
        }
        parentEquipmentModel.setChildren(childrenModel);
    }

    public Completable insertEquipmentSubEquipment(List<EquipmentSubEquipmentCrossRef> lst) {
        return equipmentSubEquipmentDAO.insert(lst);
    }

    public Completable insertEquipmentRulesetRelation(List<EquipmentRulesetVersion> list) {
        return equipmentRulesetVersionDAO.insert(list);
    }
}