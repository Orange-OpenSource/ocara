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

import com.orange.ocara.domain.models.ProfileTypeModel;
import com.orange.ocara.domain.models.RulesetModel;

import com.orange.ocara.data.cache.database.DAOs.ProfileTypeDAO;
import com.orange.ocara.data.cache.database.NonTables.ProfileTypeWithRuleset;
import com.orange.ocara.data.cache.database.OcaraDB;
import com.orange.ocara.data.cache.database.Tables.ProfileType;





import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class ProfileTypeRepository {
    ProfileTypeDAO profileTypeDAO;

    @Inject
    public ProfileTypeRepository(OcaraDB ocaraDB) {
        profileTypeDAO = ocaraDB.profileTypeDao();
    }

    public Single<Map<String, ProfileTypeModel>> getProfileTypesForRulesetAsMap(String ruleSetRef, int ruleSetVer) {

        return profileTypeDAO.getProfileTypesForRuleset(ruleSetRef, ruleSetVer).map(types -> {
                    Map<String, ProfileTypeModel> modelsMap = new HashMap<>();
//                    List<ProfileTypeModel> models = new ArrayList<>();
                    for (ProfileTypeWithRuleset type : types) {
                        ProfileTypeModel model = new ProfileTypeModel();
                        model.setIcon(type.getProfileType().getIcon());
                        model.setName(type.getProfileType().getName());
                        model.setReference(type.getProfileType().getReference());
                        model.setRuleset(RulesetModel.newRuleSetInfo(type.getRulesetDetails()));
                        modelsMap.put(type.getProfileType().getReference(), model);
                    }
                    return modelsMap;
                }
        );
    }

    public Single<List<ProfileTypeModel>> getProfileTypesForRulesetAsList(String ruleSetRef, int ruleSetVer) {

        return profileTypeDAO.getProfileTypesForRuleset(ruleSetRef, ruleSetVer).map(types -> {
                    List<ProfileTypeModel> modelsList = new ArrayList<>();
//                    List<ProfileTypeModel> models = new ArrayList<>();
                    for (ProfileTypeWithRuleset type : types) {
                        ProfileTypeModel model = new ProfileTypeModel();
                        model.setIcon(type.getProfileType().getIcon());
                        model.setName(type.getProfileType().getName());
                        model.setReference(type.getProfileType().getReference());
                        model.setRuleset(RulesetModel.newRuleSetInfo(type.getRulesetDetails()));
                        modelsList.add(model);
                    }
                    return modelsList;
                }
        );
    }

    public Completable insert(List<ProfileType> profileTypes) {
        return profileTypeDAO.insert(profileTypes);
    }
}
