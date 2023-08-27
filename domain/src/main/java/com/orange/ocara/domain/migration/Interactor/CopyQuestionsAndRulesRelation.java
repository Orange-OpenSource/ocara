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


package com.orange.ocara.domain.migration.Interactor;

import com.orange.ocara.domain.repositories.QuestionRuleRepository;
import com.orange.ocara.data.cache.database.crossRef.QuestionsRulesCrossRef;
import com.orange.ocara.data.oldEntities.QuestionEntity;
import com.orange.ocara.data.oldEntities.RulesetEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class CopyQuestionsAndRulesRelation extends Interactor<RulesetEntity>{
    QuestionRuleRepository repository;
    @Inject
    public CopyQuestionsAndRulesRelation(QuestionRuleRepository repository){
        this.repository=repository;
    }

    @Override
    public Completable execute() {
        List<RulesetEntity> entities=getAll(RulesetEntity.class);
        List<QuestionsRulesCrossRef> questionsRulesCrossRefs=new ArrayList<>();
        for(RulesetEntity rulesetEntity:entities){
            for(QuestionEntity questionEntity:rulesetEntity.getQuestionsDb()){
                for(String ruleRef:questionEntity.getRulesRef()){
                    questionsRulesCrossRefs.add(QuestionsRulesCrossRef.QuestionsRulesCrossRefBuilder.aQuestionsRulesCrossRef()
                            .withQuestionRef(questionEntity.reference)
                            .withRuleRef(ruleRef)
                            .withRulesetRef(rulesetEntity.getReference())
                            .withRulesetVersion(Integer.parseInt(rulesetEntity.getVersion()))
                            .build());
                }
            }
        }
        return repository.insert(questionsRulesCrossRefs);
    }
}
