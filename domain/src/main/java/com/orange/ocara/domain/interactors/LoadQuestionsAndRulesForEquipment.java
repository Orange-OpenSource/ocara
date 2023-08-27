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

import android.util.Pair;

import com.orange.ocara.domain.models.QuestionModel;
import com.orange.ocara.domain.models.RuleModel;
import com.orange.ocara.domain.repositories.QuestionRepository;
import com.orange.ocara.domain.repositories.RuleRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class LoadQuestionsAndRulesForEquipment {
    //with illustrations
    QuestionRepository questionRepository;
    RuleRepository ruleRepository;

    @Inject
    public LoadQuestionsAndRulesForEquipment(QuestionRepository questionRepository, RuleRepository ruleRepository) {
        this.questionRepository = questionRepository;
        this.ruleRepository = ruleRepository;
    }

    public Single<List<QuestionModel>> execute(String rulesetRef, String objRef, String version) {

        return questionRepository.findAll(rulesetRef, objRef, Integer.valueOf(version)).flatMap(new Function<List<QuestionModel>, SingleSource<List<QuestionModel> >>() {
            @Override
            public SingleSource<List<QuestionModel>> apply(@NonNull List<QuestionModel> questionModels) throws Exception {
                List<String> refs = new ArrayList<>();
                for (QuestionModel questionModel : questionModels) {
                    refs.add(questionModel.getRef());
                }
                return ruleRepository.findRulesByQuestions(refs, rulesetRef, Integer.valueOf(version)).map(new Function<List<RuleModel>, List<QuestionModel> >() {
                    @Override
                    public List<QuestionModel>  apply(@NonNull List<RuleModel> ruleModels) throws Exception {
                        for (int i = 0; i < ruleModels.size(); i++) {
                            for (int j = 0; j < questionModels.size(); j++) {
                                if (ruleModels.get(i).getQuestionRef().equals(questionModels.get(j).getRef())){
                                    questionModels.get(j).addRule(ruleModels.get(i));
                                }
                            }
                        }
                        return  questionModels;
                    }
                });
            }
        });
    }
}
