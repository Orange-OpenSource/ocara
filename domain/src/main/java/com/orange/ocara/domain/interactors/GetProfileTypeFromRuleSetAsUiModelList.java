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

import android.content.Context;

import com.orange.ocara.utils.R;
import com.orange.ocara.domain.models.ImpactValueModel;
import com.orange.ocara.domain.models.ProfileAnswersUIModel;
import com.orange.ocara.domain.models.ProfileTypeModel;
import com.orange.ocara.domain.repositories.ProfileTypeRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class GetProfileTypeFromRuleSetAsUiModelList {

    public static final String ALL_DISABILITIES = "ALL_DISABILITIES";
    GetImpactValueFormRuleSetAsList getImpactValueFormRuleSetAsList;
    ProfileTypeRepository profileTypeRepository;
    private final Context context;

    @Inject
    public GetProfileTypeFromRuleSetAsUiModelList(@ApplicationContext Context context, ProfileTypeRepository profileTypeRepository, GetImpactValueFormRuleSetAsList getImpactValueFormRuleSetAsList) {
        this.profileTypeRepository = profileTypeRepository;
        this.getImpactValueFormRuleSetAsList = getImpactValueFormRuleSetAsList;
        this.context = context;
    }

    public Observable<List<ProfileAnswersUIModel>> execute(String ruleSetRef, int ruleSetVer) {

        return profileTypeRepository.getProfileTypesForRulesetAsList(ruleSetRef, ruleSetVer)
                .map(profileTypeModels -> {
                    List<ProfileAnswersUIModel> profileAnswersUIModels = new ArrayList<>();
                    for (ProfileTypeModel profile : profileTypeModels) {
                        ProfileAnswersUIModel model = new ProfileAnswersUIModel();
                        model.setProfileTypeModel(profile);
                        profileAnswersUIModels.add(model);
                    }

                    ProfileAnswersUIModel allDisabilityModel = new ProfileAnswersUIModel();
                    allDisabilityModel.getProfileTypeModel().setReference(ALL_DISABILITIES);

                    allDisabilityModel.getProfileTypeModel().setName(context.getString(R.string.audit_results_table_all_handicap));
                    profileAnswersUIModels.add(allDisabilityModel);
                    return profileAnswersUIModels;
                }).toObservable()
                .concatMap((Function<List<ProfileAnswersUIModel>, Observable<List<ProfileAnswersUIModel>>>)
                        profileAnswersUIModels -> {
                            return getImpactValueFormRuleSetAsList.execute(ruleSetRef, ruleSetVer)
                                    .toObservable()
                                    .map(impactValueModels -> {
                                        for (ProfileAnswersUIModel profileAnswer : profileAnswersUIModels) {
                                            for (ImpactValueModel impactValue : impactValueModels) {
                                                if (!impactValue.getReference().equals("1"))
                                                    profileAnswer.getNumberOfNo().put(impactValue.getName(), 0);
                                            }
                                        }
                                        return profileAnswersUIModels;
                                    });
//                return null;
                        });
    }
}
