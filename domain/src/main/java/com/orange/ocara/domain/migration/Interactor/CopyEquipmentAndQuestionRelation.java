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

import com.orange.ocara.domain.repositories.QuestionRepository;
import com.orange.ocara.data.cache.database.crossRef.QuestionsEquipmentsCrossRef;
import com.orange.ocara.data.oldEntities.ChainEntity;
import com.orange.ocara.data.oldEntities.QuestionnaireEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class CopyEquipmentAndQuestionRelation extends Interactor<QuestionnaireEntity>{
    QuestionRepository repository;
    @Inject
    public CopyEquipmentAndQuestionRelation(QuestionRepository repository){
        this.repository=repository;
    }

    @Override
    public Completable execute() {
        List<QuestionnaireEntity> questionnaireEntities=getAll(QuestionnaireEntity.class);
        List<QuestionsEquipmentsCrossRef> res=new ArrayList<>();
        for(QuestionnaireEntity questionnaireEntity:questionnaireEntities){
            for(ChainEntity chainEntity:questionnaireEntity.getChaineDb()){
                for(String questionRef:chainEntity.questionsRef){
                    res.add(new QuestionsEquipmentsCrossRef.QuestionsEquipmentsBuilder()
                            .setObjectReference(questionnaireEntity.objectDescriptionRef)
                            .setQuestionRef(questionRef)
                            .setRulesetRef(questionnaireEntity.getRuleSetDetail().getReference())
                            .setRulesetVer(Integer.parseInt(questionnaireEntity.getRuleSetDetail().getVersion()))
                            .createQuestionsEquipmentsCrossRef());
                }
            }
        }
        return repository.insertEquipmentQuestions(res);
    }

}
