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
import com.orange.ocara.domain.models.AuditModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class LoadAuditForExportTask {
//    AuditRepository auditRepository;

    LoadAuditWithSiteAndAuditorAndRulesetByIdTask loadAuditById;
    GetProfileTypeFromRuleSetAsMap getProfileTypeFormRuleSet;
    LoadAuditEquipments loadAuditEquipments;
    LoadAuditEquipmentComments loadAuditEquipmentComments;
    LoadAuditEquipmentsWithChildren loadAuditEquipmentsWithChildren;
    LoadQuestionsAnswers loadQuestionsAnswers;

    @Inject
    public LoadAuditForExportTask(LoadAuditWithSiteAndAuditorAndRulesetByIdTask loadAuditById,
                                  GetProfileTypeFromRuleSetAsMap getProfileTypeFormRuleSet,
                                  LoadAuditEquipments loadAuditEquipments,
                                  LoadAuditEquipmentComments loadAuditEquipmentComments,
                                  LoadAuditEquipmentsWithChildren loadAuditEquipmentsWithChildren,
                                  LoadQuestionsAnswers loadQuestionsAnswers) {

        this.loadAuditById = loadAuditById;
        this.getProfileTypeFormRuleSet = getProfileTypeFormRuleSet;
        this.loadAuditEquipments = loadAuditEquipments;
        this.loadAuditEquipmentComments = loadAuditEquipmentComments;
        this.loadAuditEquipmentsWithChildren = loadAuditEquipmentsWithChildren;
        this.loadQuestionsAnswers = loadQuestionsAnswers;
    }

    //    public Single<AuditModel> execute(Long auditId) {
//
//        return Single.create(emitter -> {
//            AuditModel audit = loadAuditById.execute(auditId).blockingGet();
//            List<AuditEquipmentModel> auditEquipmentModels = loadAuditEquipments.execute(Long.valueOf(audit.getId())).blockingGet();
//            for (int i = 0; i < auditEquipmentModels.size(); i++) {
//                AuditEquipmentModel model = auditEquipmentModels.get(i);
//                List<CommentModel> comments = loadAuditEquipmentComments.execute(model.getId()).blockingGet();
//                AuditEquipmentModel dbFullModel = loadAuditEquipmentsWithChildren.execute(model.getId()).blockingGet();
//                dbFullModel.setComments(comments);
//                dbFullModel.setAnswer(model.getAnswer());
//                auditEquipmentModels.set(i, dbFullModel);
//            }
//            audit.setObjects(auditEquipmentModels);
//            emitter.onSuccess(audit);
//        });
//    }
    public Observable<AuditModel> execute(Long auditId) {

//        Observable<AuditModel> parentObservable =
        return loadAuditById.execute(auditId.intValue()).toObservable().concatMap(new Function<AuditModel, Observable<AuditModel>>() {
            @Override
            public Observable<AuditModel> apply(@NonNull AuditModel auditModel) throws Exception {
                return loadAuditEquipments.execute(Long.valueOf(auditModel.getId())).toObservable().concatMap(auditEquipmentModels -> {
                    if (auditEquipmentModels.isEmpty())
                        return Observable.just(auditModel);
//                        return loadAuditById.execute(auditId).toObservable();
                    return getAuditEquipmentsRetrievalObservable(auditModel, auditEquipmentModels);
                });
            }
        });
    }

    private Observable<AuditModel> getAuditEquipmentsRetrievalObservable(AuditModel auditModel, List<AuditEquipmentModel> auditEquipmentModels) {
        return Single.zip(getAuditFullEquipmentsSingles(auditEquipmentModels), new Function<Object[], AuditModel>() {
            @Override
            public AuditModel apply(@NonNull Object[] objects) throws Exception {
                auditModel.getObjects().clear();
                for (Object obj : objects) {
                    auditModel.addObject((AuditEquipmentModel) obj);
                }
                return auditModel;
            }
        }).toObservable();

    }

    private List<Single<AuditEquipmentModel>> getAuditFullEquipmentsSingles(List<AuditEquipmentModel> auditEquipmentModels) {
        List<Single<AuditEquipmentModel>> singles = new ArrayList<>();
        for (AuditEquipmentModel model : auditEquipmentModels) {
            singles.add(getSubEquipmentsAndCommentsForAudEq(model));
        }
        return singles;
    }

    private Single<AuditEquipmentModel> getSubEquipmentsAndCommentsForAudEq(AuditEquipmentModel auditEquipmentModel) {

        return loadAuditEquipmentsWithChildren.execute(auditEquipmentModel.getId()).flatMap(auditEquipment -> {
            auditEquipment.setAnswer(auditEquipmentModel.getAnswer());
            return loadQuestionsAnswers.execute(auditEquipment).map(questionAnswerModels -> {
                auditEquipment.setQuestionAnswers(questionAnswerModels);
                return auditEquipment;
            });
        }).flatMap(auditEquipment1 -> {
            return loadAuditEquipmentComments.execute(Long.valueOf(auditEquipment1.getId())).map(commentModels -> {
                auditEquipment1.setComments(commentModels);
                return auditEquipment1;
            });
        });


    }

    List<Single<AuditEquipmentModel>> getEquipmentsQuestAnsSingles(List<AuditEquipmentModel> auditEquipmentModels) {
        List<Single<AuditEquipmentModel>> singles = new ArrayList<>();
        for (AuditEquipmentModel model : auditEquipmentModels) {
            singles.add(getEquipmentQuestionAnsSingle(model));
        }
        return singles;
    }

    Single<AuditEquipmentModel> getEquipmentQuestionAnsSingle(AuditEquipmentModel auditEquipmentModel) {
        return loadQuestionsAnswers.execute(auditEquipmentModel).map(questionAnswerModels -> {
            auditEquipmentModel.setQuestionAnswers(questionAnswerModels);
            return auditEquipmentModel;
        });
    }

}
