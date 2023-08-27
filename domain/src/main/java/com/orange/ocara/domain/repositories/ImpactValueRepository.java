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

import com.orange.ocara.domain.models.RulesetModel;
import com.orange.ocara.data.cache.database.DAOs.ImpactValueDAO;
import com.orange.ocara.data.cache.database.NonTables.ImpactValueWithRuleset;
import com.orange.ocara.data.cache.database.OcaraDB;
import com.orange.ocara.data.cache.database.Tables.ImpactValue;

import com.orange.ocara.domain.models.ImpactValueModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class ImpactValueRepository {
    ImpactValueDAO impactValueDAO;

    @Inject
    public ImpactValueRepository(OcaraDB ocaraDB) {
        this.impactValueDAO = ocaraDB.impactValueDao();
    }

    public Completable insert(List<ImpactValue> impactValues) {
        return impactValueDAO.insert(impactValues);
    }


    public Single<Map<String, ImpactValueModel>> getImpactValuesForRulesetAsMap(String ruleSetRef, int ruleSetVer) {

        return impactValueDAO.getImpactValuesForRuleset(ruleSetRef, ruleSetVer).map(types -> {
                    Map<String, ImpactValueModel> modelsMap = new HashMap<>();
//                    List<ProfileTypeModel> models = new ArrayList<>();
                    for (ImpactValueWithRuleset impact : types) {
                        ImpactValueModel model = new ImpactValueModel(impact.getImpactValue().getReference(),
                                impact.getImpactValue().getName(), impact.getImpactValue().isEditable());
                        model.setRuleset(RulesetModel.newRuleSetInfo(impact.getRulesetDetails()));
                        modelsMap.put(impact.getImpactValue().getReference(), model);
                    }
                    return modelsMap;
                }
        );
    }

    public Single<List<ImpactValueModel>> getImpactValuesForRulesetAsList(String ruleSetRef, int ruleSetVer) {

        return impactValueDAO.getImpactValuesForRuleset(ruleSetRef, ruleSetVer).map(types -> {
                    List<ImpactValueModel> modelsMap = new ArrayList<>();
//                    List<ProfileTypeModel> models = new ArrayList<>();
                    for (ImpactValueWithRuleset impact : types) {
                        ImpactValueModel model = new ImpactValueModel(impact.getImpactValue().getReference(),
                                impact.getImpactValue().getName(), impact.getImpactValue().isEditable());
                        model.setRuleset(RulesetModel.newRuleSetInfo(impact.getRulesetDetails()));
                        modelsMap.add(model);
                    }
                    return modelsMap;
                }
        );
    }
}
