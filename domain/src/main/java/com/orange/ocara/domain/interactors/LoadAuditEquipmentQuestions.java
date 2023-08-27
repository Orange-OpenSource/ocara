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

import com.orange.ocara.data.cache.database.NonTables.QuestionWithRuleAnswer;
import com.orange.ocara.domain.models.AuditEquipmentModel;
import com.orange.ocara.domain.models.EquipmentModel;
import com.orange.ocara.domain.models.QuestionAnswerModel;
import com.orange.ocara.domain.models.QuestionModel;
import com.orange.ocara.domain.models.RuleAnswerModel;
import com.orange.ocara.domain.models.RuleModel;
import com.orange.ocara.domain.repositories.RuleAnswerRepository;
import com.orange.ocara.utils.enums.Answer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class LoadAuditEquipmentQuestions {
    LoadAuditEquipmentByIdForNoviceMode loadAuditEquipmentByIdForNoviceMode;
    RuleAnswerRepository repository;

    @Inject
    public LoadAuditEquipmentQuestions(LoadAuditEquipmentByIdForNoviceMode loadAuditEquipmentByIdForNoviceMode
            , RuleAnswerRepository repository) {
        this.repository = repository;
        this.loadAuditEquipmentByIdForNoviceMode = loadAuditEquipmentByIdForNoviceMode;
    }

    public Single<List<QuestionAnswerModel>> execute(int auditEqId) {
        // this will get me all the questions with rules but without the answers
        Single<AuditEquipmentModel> auditEquipmentWithQuestions = loadAuditEquipmentByIdForNoviceMode.execute(auditEqId);
        Observable<List<QuestionAnswerModel>> observable = auditEquipmentWithQuestions
                .toObservable()
                .concatMap(convertAuditEquipmentToListOfQuestionsWithAnswers(auditEqId));
        return Single.fromObservable(observable);
    }
    private Function<AuditEquipmentModel, ObservableSource<List<QuestionAnswerModel>>> convertAuditEquipmentToListOfQuestionsWithAnswers(int auditEqId){
        return new Function<AuditEquipmentModel, ObservableSource<List<QuestionAnswerModel>>>() {
            @Override
            public ObservableSource<List<QuestionAnswerModel>> apply(@NonNull AuditEquipmentModel auditEquipmentModel){
                return repository.getRulesAnswers(auditEqId)
                        .map(questionWithRuleAnswers -> {
                            return getQuestionsList(auditEquipmentModel,questionWithRuleAnswers);
                        }).toObservable();
            }
        };
    }
    /*
    this method takes an audit equipment with all the questions in its equipment attribute
    and a list of questions that was answered , and combine both in the questionAnswersList in the
    audit equipment directly , we didn't return the list of questions directly because it's the list of
    answered questions
     */
    private List<QuestionAnswerModel> getQuestionsList(AuditEquipmentModel auditEquipment, List<QuestionWithRuleAnswer> questionWithRuleAnswers) {
        EquipmentModel equipment = auditEquipment.getEquipment();
        List<QuestionModel> questions = equipment.getQuestions();
        List<QuestionAnswerModel> questionAnswers = new ArrayList<>();
        for (QuestionModel question : questions) {
            QuestionAnswerModel questionAnswer = new QuestionAnswerModel(Answer.NO_ANSWER, question, auditEquipment.getEquipment().getReference());
            addRuleAnswersToQuestionAnswer(questionAnswer, questionWithRuleAnswers);
            questionAnswer.updateAnswer();
            questionAnswers.add(questionAnswer);
        }
        return questionAnswers;
    }

    private void addRuleAnswersToQuestionAnswer(QuestionAnswerModel questionAnswer, List<QuestionWithRuleAnswer> questionWithRuleAnswers) {
        QuestionModel question = questionAnswer.getQuestion();
        for (RuleModel rule : question.getRules()) {
            Answer answer = getAnswerForThisRuleWithThisQuestion
                    (questionWithRuleAnswers, rule.getRef(), question.getRef());
            RuleAnswerModel ruleAnswer = new RuleAnswerModel(rule, answer);
            ruleAnswer.setQuestionAnswer(questionAnswer);
            questionAnswer.addRuleAnswer(ruleAnswer);
        }
    }

    private Answer getAnswerForThisRuleWithThisQuestion(List<QuestionWithRuleAnswer> questionWithRuleAnswers
            , String ruleRef, String questionRef) {
        for (QuestionWithRuleAnswer rule : questionWithRuleAnswers) {
            if (checkIfRuleEqualsOtherRule(ruleRef, questionRef, rule)) {
                return rule.getAnswer();
            }
        }
        return Answer.NO_ANSWER;
    }

    /*
    the answer for a question is the maximum rule answer
     */
    private Answer getAnswerForThisQuestion(List<QuestionWithRuleAnswer> questionWithRuleAnswers,String questionRef){
        Answer result=Answer.NO_ANSWER;
        for(QuestionWithRuleAnswer rule:questionWithRuleAnswers){
            if(rule.getQuestion().getReference().equals(questionRef)){
                result = max(result,rule.getAnswer());
            }
        }
        return result;
    }
    private Answer max(Answer a1,Answer a2){
        return a1.ordinal() > a2.ordinal() ? a1 : a2;
    }
    private boolean checkIfRuleEqualsOtherRule(String ruleRef, String questionRef, QuestionWithRuleAnswer rule) {
        return rule.getRule().getReference().equals(ruleRef) && rule.getQuestion().getReference().equals(questionRef);
    }
}
