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

import com.orange.ocara.domain.models.ProfileAnswersUIModel;
import com.orange.ocara.domain.models.QuestionAnswerModel;
import com.orange.ocara.domain.repositories.AuditScoresRepository;
import com.orange.ocara.data.cache.database.NonTables.scoreCalc.AnswerCount;
import com.orange.ocara.data.cache.database.NonTables.scoreCalc.CountImpactName;
import com.orange.ocara.data.cache.database.NonTables.scoreCalc.NoAnsWithProfileAndImpact;
import com.orange.ocara.data.cache.database.NonTables.scoreCalc.YesDoubtNoAnsNAAnsWithprofileAndAns;
import com.orange.ocara.utils.enums.Answer;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class GetAuditScoresTask {
    public static final String ALL_DISABILITIES = "ALL_DISABILITIES";
    AuditScoresRepository auditRepository;
    GetProfileTypeFromRuleSetAsUiModelList getProfileTypeFormRuleSetAsUiModelList;

    @Inject
    public GetAuditScoresTask(AuditScoresRepository auditRepository, GetProfileTypeFromRuleSetAsUiModelList getProfileTypeFormRuleSetAsUiModelList) {
        this.auditRepository = auditRepository;
        this.getProfileTypeFormRuleSetAsUiModelList = getProfileTypeFormRuleSetAsUiModelList;
    }

    public Observable<List<ProfileAnswersUIModel>> execute(Long auditId, String ruleSetRef, int ruleSetVer) {

        return getProfileTypeFormRuleSetAsUiModelList.execute(ruleSetRef, ruleSetVer)

                .concatMap((Function<List<ProfileAnswersUIModel>, Observable<List<ProfileAnswersUIModel>>>)
                        profileAnswersUIModels -> {
                            //get yes and doubt answers | first 2 cols for all profiles
                            return auditRepository.getNumberOfRulesAnsweredByYesOrDoubtOrNoAnsOrNAGrpByAnsAndProfileForAudit(auditId)
                                    .map(yesDoubtAnsWithprofileAndAns -> {
                                        for (YesDoubtNoAnsNAAnsWithprofileAndAns answer : yesDoubtAnsWithprofileAndAns) {
                                            int objIdx = getProfile(profileAnswersUIModels, answer.getProfileRef());
                                            setYesDoubtAnswerForProfile(answer, profileAnswersUIModels.get(objIdx));
                                        }
                                        return profileAnswersUIModels;
                                    }).toObservable();
                        }).concatMap(
                        profileAnswersUIModels -> {
                            //get No answers | last cols for all profiles
                            return auditRepository.getNumberOfRulesAnsweredByNoGrpByImpactAndProfileForAudit(auditId)
                                    .map(noAnsWithProfileAndImpacts -> {
                                        for (NoAnsWithProfileAndImpact answer : noAnsWithProfileAndImpacts) {
                                            int objIdx = getProfile(profileAnswersUIModels, answer.getProfileRef());
                                            setNoAnswerForProfile(answer, profileAnswersUIModels.get(objIdx));
                                        }
                                        return profileAnswersUIModels;
                                    }).toObservable();
                        }).concatMap(
                        profileAnswersUIModels -> {
                            //get total yes and total D | first 2 col in last row
                            return auditRepository.getTotalNumRulesYesOrDoubtHasAnyImpact(auditId).map(answerCounts -> {
                                for (AnswerCount answer : answerCounts) {
                                    int objIdx = profileAnswersUIModels.size()-1;
                                    setYesDoubtAnswerForProfile(answer, profileAnswersUIModels.get(objIdx));
                                }
                                return profileAnswersUIModels;
                            }).toObservable();
                        }).concatMap(
                        profileAnswersUIModels -> auditRepository.getTotalNumRulesNoHasAnyImpact(auditId)
                                .map(countImpactNames -> {
                                    //get total No for all disabilitoes | last cols in last row
                                    for (CountImpactName answer:countImpactNames) {
                                        int objIdx = profileAnswersUIModels.size()-1;
                                        setNoAnswerForProfile(answer, profileAnswersUIModels.get(objIdx));
                                    }
                                    return profileAnswersUIModels;
                                }).toObservable());

    }

    private void setNoAnswerForProfile(CountImpactName answer, ProfileAnswersUIModel profile) {
        profile.getNumberOfNo().put(answer.getImpactName(), answer.getCount());
    }

    private void setYesDoubtAnswerForProfile(AnswerCount answer, ProfileAnswersUIModel profile) {
        if (answer.getAnswer() == Answer.OK) {
            profile.setNumberOfOk(answer.getCount());
        } else if (answer.getAnswer() == Answer.DOUBT) {
            profile.setNumberOfDoubt(answer.getCount());
        }else if (answer.getAnswer() == Answer.NO_ANSWER){
            profile.setNumberOfNoAns(answer.getCount());
        }else if (answer.getAnswer() == Answer.NOT_APPLICABLE){
            profile.setNumberOfNA(answer.getCount());
        }
    }

    private int getProfile(List<ProfileAnswersUIModel> list, String profileRef) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getProfileTypeModel().getReference().equals(profileRef))
                return i;
        }
        return -1;
    }
}
