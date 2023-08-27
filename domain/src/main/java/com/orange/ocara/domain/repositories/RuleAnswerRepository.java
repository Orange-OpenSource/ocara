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

import com.orange.ocara.data.cache.database.DAOs.QuestionDAO;
import com.orange.ocara.data.cache.database.DAOs.RuleAnswerDAO;
import com.orange.ocara.data.cache.database.NonTables.QuestionWithRuleAnswer;
import com.orange.ocara.data.cache.database.NonTables.RuleAnswerAndLabel;
import com.orange.ocara.data.cache.database.OcaraDB;
import com.orange.ocara.data.cache.database.Tables.RuleAnswer;
import com.orange.ocara.domain.models.AuditEquipmentModel;
import com.orange.ocara.domain.models.QuestionAnswerModel;
import com.orange.ocara.domain.models.QuestionModel;
import com.orange.ocara.domain.models.QuestionRuleAnswer;
import com.orange.ocara.domain.models.RuleAnswerModel;
import com.orange.ocara.domain.models.RuleModel;
import com.orange.ocara.utils.enums.Answer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import timber.log.Timber;

public class RuleAnswerRepository {
    RuleAnswerDAO ruleAnswerDAO;
    QuestionDAO questionDAO;

    @Inject
    public RuleAnswerRepository(OcaraDB ocaraDB) {
        ruleAnswerDAO = ocaraDB.ruleAnswerDAO();
        questionDAO = ocaraDB.questionDao();
    }

    public Completable insertRuleAnswers(List<RuleAnswerModel> ruleAnswerModels) {
        List<RuleAnswer> ruleAnswers=new ArrayList<>();
        for(RuleAnswerModel ruleAnswerModel:ruleAnswerModels){
            Timber.d(ruleAnswerModel.getQuestionAnswer().getQuestion().getAuditEquipment().getId()+
                    " "+ruleAnswerModel.getAnswer()+ruleAnswerModel.getRule().getRef()+" "+
                    ruleAnswerModel.getQuestionAnswer().getQuestion().getRef());
            ruleAnswers.add(new RuleAnswer.RuleAnswerBuilder()
                    .setAuditEquipmentId(ruleAnswerModel.getQuestionAnswer().getQuestion().getAuditEquipment().getId())
                    .setAnswer(ruleAnswerModel.getAnswer()).setRuleRef(ruleAnswerModel.getRule().getRef())
                    .setQuestionRef(ruleAnswerModel.getQuestionAnswer().getQuestion().getRef()).createRuleAnswer());
        }
        return insert(ruleAnswers);
    }

    public Completable insert(List<RuleAnswer> ruleAnswers) {
        return ruleAnswerDAO.insert(ruleAnswers);
    }

    public Single<List<RuleAnswerAndLabel>> getAnomalies(int auditEqId) {
        return ruleAnswerDAO.getAnomalies(auditEqId);
    }

    public Completable insertQuestionRuleAnswers(List<QuestionRuleAnswer> questionRuleAnswers) {
        List<RuleAnswer> ruleAnswers = new ArrayList<>();
        for (QuestionRuleAnswer questionRuleAnswer : questionRuleAnswers) {
            ruleAnswers.add(new RuleAnswer(questionRuleAnswer.getAuditEquipmentId(), questionRuleAnswer.getAnswer(),
                    questionRuleAnswer.getRuleReference(), questionRuleAnswer.getQuestionReference()));
        }
        return ruleAnswerDAO.insert(ruleAnswers);
    }

    /*
            this method gets the all the rules associated with the equipment
            that is associated with auditEquipmentId and if some of these rules
            are already answered , it gets their answer and if not then their answer will be
            not answered
         */
    public Observable<List<QuestionAnswerModel>> getRulesAnswers(AuditEquipmentModel auditEquipmentModel){
        int auditEquipmentId = auditEquipmentModel.getId();
        return getRulesAnswers(auditEquipmentId)
                .toObservable()
                .concatMap(questionWithRuleAnswers1 -> {
                    List<String> equipmentAndSubEquipmentsRefs = new ArrayList<>();
                    for (AuditEquipmentModel equipmentModel : auditEquipmentModel.getChildren()) {
                        equipmentAndSubEquipmentsRefs.add(equipmentModel.getEquipment().getReference());
                    }
                    equipmentAndSubEquipmentsRefs.add(auditEquipmentModel.getEquipment().getReference());
                    return questionDAO.getQuestionsWithRules(auditEquipmentModel.getAudit().getRulesetRef()
                        ,equipmentAndSubEquipmentsRefs,auditEquipmentModel.getAudit().getRulesetVer())
                            .map(questionWithRuleAnswers2 -> {
                                List<QuestionWithRuleAnswer> allQuestions = combineTwoQuestionsList(questionWithRuleAnswers2,questionWithRuleAnswers1);
                                return createQuestionsAnswersModelsWithTheirRules(auditEquipmentModel,allQuestions);
                            }).toObservable();
                });
    }

    public Single<List<QuestionWithRuleAnswer>> getRulesAnswers(int auditEqId){
        return ruleAnswerDAO.getRulesAnswers(auditEqId);
    }
    /*
        this method combine two QuestionWithRuleAnswer lists and deletes the duplicates
        duplicates are equal of questionRef and ruleRef and objectRef
     */
    private List<QuestionWithRuleAnswer> combineTwoQuestionsList(List<QuestionWithRuleAnswer> questionWithRuleAnswers, List<QuestionWithRuleAnswer> currentQuestionWithRuleAnswers) {
        List<QuestionWithRuleAnswer> result=new ArrayList<>();
        result.addAll(currentQuestionWithRuleAnswers);
        for(QuestionWithRuleAnswer questionWithRuleAnswer:questionWithRuleAnswers){
            if(!exists(result, questionWithRuleAnswer)){
                if(questionWithRuleAnswer.getAnswer()==null){
                    questionWithRuleAnswer.setAnswer(Answer.NO_ANSWER);
                }
                result.add(questionWithRuleAnswer);
            }
        }
        return result;
    }

    private boolean exists(List<QuestionWithRuleAnswer> result, QuestionWithRuleAnswer questionWithRuleAnswer) {
        for(QuestionWithRuleAnswer alreadyAdded:result){
            if(questionWithRuleAnswer.equals(alreadyAdded))return true;
        }
        return false;
    }

    private List<QuestionAnswerModel> createQuestionsAnswersModelsWithTheirRules(AuditEquipmentModel auditEquipmentModel,List<QuestionWithRuleAnswer> questionWithRuleAnswers) {
        List<QuestionAnswerModel> questionAnswers = new ArrayList<>();
        Map<String, QuestionAnswerModel> refToQuestionAnswerModel = new HashMap<>();

        for (QuestionWithRuleAnswer questionWithRuleAnswer : questionWithRuleAnswers) {
            // if this question reference is still haven't been added to the map
            if (!refToQuestionAnswerModel.containsKey(questionWithRuleAnswer.getQuestion().getReference())) {
                QuestionModel questionModel=new QuestionModel(questionWithRuleAnswer.getQuestion());
                questionModel.setAuditEquipment(auditEquipmentModel);
                QuestionAnswerModel questionAnswerModel = new QuestionAnswerModel(
                        // default question answer
                        Answer.DOUBT
                        , questionModel
                        , questionWithRuleAnswer.getObjectReference());
                refToQuestionAnswerModel.put(questionAnswerModel.getQuestion().getRef(), questionAnswerModel);
                questionAnswers.add(questionAnswerModel);
            }
        }

        for (QuestionWithRuleAnswer questionWithRuleAnswer : questionWithRuleAnswers) {
            QuestionAnswerModel questionAnswerModel = refToQuestionAnswerModel.get(questionWithRuleAnswer.getQuestion().getReference());
            RuleAnswerModel ruleAnswerModel = new RuleAnswerModel(new RuleModel(questionWithRuleAnswer.getRule()),
                    questionWithRuleAnswer.getAnswer());
            ruleAnswerModel.setQuestionAnswer(questionAnswerModel);
            questionAnswerModel.addRuleAnswer(ruleAnswerModel);
        }
        return questionAnswers;
    }

    public Completable deleteAuditEquipmentAnswers(int auditEqId){
        return ruleAnswerDAO.deleteRuleAnswersOfAuditEquipment(auditEqId);
    }
}
