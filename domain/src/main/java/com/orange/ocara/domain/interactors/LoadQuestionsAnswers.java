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

import com.orange.ocara.domain.models.AuditEquipmentModel;
import com.orange.ocara.domain.models.QuestionAnswerModel;
import com.orange.ocara.domain.models.RuleAnswerModel;
import com.orange.ocara.domain.repositories.RuleAnswerRepository;
import com.orange.ocara.utils.enums.Answer;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class LoadQuestionsAnswers {
    RuleAnswerRepository repository;

    @Inject
    public LoadQuestionsAnswers(RuleAnswerRepository repository) {
        this.repository = repository;
    }

    public Single<List<QuestionAnswerModel>> execute(AuditEquipmentModel auditEquipment) {
        return Single.fromObservable(repository.getRulesAnswers(auditEquipment).map(questionAnswerModels -> {
            for(QuestionAnswerModel questionAnswerModel:questionAnswerModels){
                computeQuestionsAnswer(questionAnswerModel);
            }
            return questionAnswerModels;
        }));
    }

    private void computeQuestionsAnswer(QuestionAnswerModel questionAnswer) {
        questionAnswer.setAnswer(Answer.NO_ANSWER);
        for (RuleAnswerModel ruleAnswer : questionAnswer.getRuleAnswers()){
            if(ruleAnswer.getAnswer().ordinal() > questionAnswer.getAnswer().ordinal()){
                questionAnswer.setAnswer(ruleAnswer.getAnswer());
            }
        }
    }
}
