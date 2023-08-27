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

public class LoadQuestionsAndRulesByRulesetRefAndEquipmentRefWithRxJava {
    QuestionRepository questionRepository;
    RuleRepository ruleRepository;

    @Inject
    public LoadQuestionsAndRulesByRulesetRefAndEquipmentRefWithRxJava(QuestionRepository questionRepository, RuleRepository ruleRepository) {
        this.questionRepository = questionRepository;
        this.ruleRepository = ruleRepository;
    }

    public Single<Pair<List<QuestionModel> , List<RuleModel>>> execute(String rulesetRef, String objRef, int version) {

        return questionRepository.findAll(rulesetRef, objRef, version).flatMap(new Function<List<QuestionModel>, SingleSource<Pair<List<QuestionModel> , List<RuleModel>>>>() {
            @Override
            public SingleSource<Pair<List<QuestionModel> , List<RuleModel>>> apply(@NonNull List<QuestionModel> questionModels) throws Exception {
                List<String> refs = new ArrayList<>();
                for (QuestionModel questionModel : questionModels) {
                    refs.add(questionModel.getRef());
                }
                return ruleRepository.findRulesByQuestions(refs, rulesetRef, version).map(new Function<List<RuleModel>, Pair<List<QuestionModel> , List<RuleModel>>>() {
                    @Override
                    public Pair<List<QuestionModel> , List<RuleModel>> apply(@NonNull List<RuleModel> ruleModels) throws Exception {
                        return new Pair(questionModels , ruleModels);
                    }
                });
            }
        });
    }
}
