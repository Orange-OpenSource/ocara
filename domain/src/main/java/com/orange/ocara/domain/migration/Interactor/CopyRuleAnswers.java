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

import com.orange.ocara.domain.repositories.RuleAnswerRepository;
import com.orange.ocara.data.cache.database.Tables.RuleAnswer;
import com.orange.ocara.data.oldEntities.AuditObjectEntity;
import com.orange.ocara.data.oldEntities.QuestionAnswerEntity;
import com.orange.ocara.data.oldEntities.RuleAnswerEntity;
import com.orange.ocara.utils.enums.Answer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class CopyRuleAnswers extends Interactor<AuditObjectEntity>{
    RuleAnswerRepository repository;
    @Inject
    public CopyRuleAnswers(RuleAnswerRepository repository){
        this.repository=repository;
    }
    @Override
    public Completable execute() {
        List<AuditObjectEntity> auditObjectEntities=getAll(AuditObjectEntity.class);
        List<RuleAnswer> ruleAnswers=new ArrayList<>();
        for(AuditObjectEntity auditObjectEntity:auditObjectEntities){
            for(QuestionAnswerEntity questionAnswerEntity:auditObjectEntity.getQuestionAnswers()){
                for(RuleAnswerEntity ruleAnswerEntity:questionAnswerEntity.getRuleAnswers()){
                    ruleAnswers.add(new RuleAnswer.RuleAnswerBuilder()
                            .setAnswer(Answer.valueOf(ruleAnswerEntity.getResponse().toString()))
                            .setAuditEquipmentId(auditObjectEntity.getId().intValue())
                            .setQuestionRef(questionAnswerEntity.getQuestionId())
                            .setRuleRef(ruleAnswerEntity.getRuleId())
                            .createRuleAnswer());
                }
            }
        }
        return repository.insert(ruleAnswers);
    }
}
