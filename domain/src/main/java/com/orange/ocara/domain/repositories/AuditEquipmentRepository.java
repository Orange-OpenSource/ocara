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

import com.orange.ocara.data.cache.database.DAOs.AuditDAO;
import com.orange.ocara.data.cache.database.DAOs.AuditEquipmentDAO;
import com.orange.ocara.data.cache.database.DAOs.AuditEquipmentSubEquipmentDAO;
import com.orange.ocara.data.cache.database.DAOs.AuditEquipmentUpdateOrderDAO;
import com.orange.ocara.data.cache.database.DAOs.CommentDAO;
import com.orange.ocara.data.cache.database.DAOs.EquipmentDAO;
import com.orange.ocara.data.cache.database.DAOs.EquipmentSubEquipmentDAO;
import com.orange.ocara.data.cache.database.DAOs.IllustrationDAO;
import com.orange.ocara.data.cache.database.DAOs.QuestionDAO;
import com.orange.ocara.data.cache.database.DAOs.RuleDAO;
import com.orange.ocara.data.cache.database.NonTables.AuditEquipmentForReport;
import com.orange.ocara.data.cache.database.NonTables.AuditEquipmentNameAndIcon;
import com.orange.ocara.data.cache.database.NonTables.AuditEquipmentWithAuditAndEquipment;
import com.orange.ocara.data.cache.database.NonTables.AuditEquipmentWithNumberOfCommentsAndOrder;
import com.orange.ocara.data.cache.database.NonTables.EquipmentWithSubEquipment;
import com.orange.ocara.data.cache.database.NonTables.IllustrationWithRuleRef;
import com.orange.ocara.data.cache.database.NonTables.QuestionWithRuleAnswer;
import com.orange.ocara.data.cache.database.NonTables.RulesetRefAndVersion;
import com.orange.ocara.data.cache.database.OcaraDB;
import com.orange.ocara.data.cache.database.Tables.Comment;
import com.orange.ocara.data.cache.database.Tables.Equipment;
import com.orange.ocara.data.cache.database.crossRef.AuditEquipmentSubEquipment;
import com.orange.ocara.data.cache.database.crossRef.AuditEquipments;
import com.orange.ocara.domain.models.AuditEquipmentForCurrentRouteModel;
import com.orange.ocara.domain.models.AuditEquipmentModel;
import com.orange.ocara.domain.models.AuditEquipmentWithNumberOfCommentAndOrderModel;
import com.orange.ocara.domain.models.AuditModel;
import com.orange.ocara.domain.models.CommentModel;
import com.orange.ocara.domain.models.EquipmentModel;
import com.orange.ocara.domain.models.EquipmentNameAndIconModel;
import com.orange.ocara.domain.models.IllustrationModel;
import com.orange.ocara.domain.models.QuestionModel;
import com.orange.ocara.domain.models.RuleModel;
import com.orange.ocara.utils.enums.Answer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class AuditEquipmentRepository {
    AuditEquipmentDAO auditEquipmentDAO;
    AuditEquipmentUpdateOrderDAO auditEquipmentUpdateOrderDAO;

    AuditDAO auditDAO;
    EquipmentDAO equipmentDAO;
    QuestionDAO questionDAO;
    RuleDAO ruleDAO;
    IllustrationDAO illustrationDAO;
    CommentDAO commentDAO;
    EquipmentSubEquipmentDAO equipmentSubEquipmentDAO;
    AuditEquipmentSubEquipmentDAO auditEquipmentSubEquipmentDAO;
    OcaraDB ocaraDB;

    @Inject
    public AuditEquipmentRepository(OcaraDB ocaraDB) {
        this.ocaraDB = ocaraDB;
        auditEquipmentDAO = ocaraDB.auditObjectDao();
        auditEquipmentUpdateOrderDAO = ocaraDB.auditObjectUpdateOrderDao();
        auditDAO = ocaraDB.auditDao();
        commentDAO = ocaraDB.commentDao();
        auditEquipmentSubEquipmentDAO = ocaraDB.auditEquipmentSubEquipmentDAO();
        equipmentDAO = ocaraDB.equipmentDao();
        questionDAO = ocaraDB.questionDao();
        ruleDAO = ocaraDB.ruleDao();
        illustrationDAO = ocaraDB.illustrationDao();
        equipmentSubEquipmentDAO = ocaraDB.equipmentSubEquipmentDAO();
    }

    /*
        this method deletes the audit equipment with its ruleAnswers
        and comments and subObjects
     */
    public Completable deleteAuditEquipmentAndItsData(int auditEquipmentId) {
        return auditEquipmentDAO.deleteAuditObject(auditEquipmentId)
                .andThen(commentDAO.deleteAllEquipmentComments(auditEquipmentId))
                .andThen(auditEquipmentSubEquipmentDAO.deleteSubEquipments(auditEquipmentId))
                .andThen(ocaraDB.ruleAnswerDAO().deleteRuleAnswersOfAuditEquipment(auditEquipmentId));
    }

    public Completable deleteAuditEquipmentsAndThereData(List<Integer> auditEquipmentId) {
        return auditEquipmentDAO.deleteAuditObjects(auditEquipmentId)
                .andThen(commentDAO.deleteAllEquipmentsComments(auditEquipmentId))
                .andThen(auditEquipmentSubEquipmentDAO.deleteSubEquipments(auditEquipmentId))
                .andThen(ocaraDB.ruleAnswerDAO().deleteRuleAnswersOfAuditEquipments(auditEquipmentId));
    }

    public Single<AuditEquipmentNameAndIcon> getAuditEquipmentInfo(int equipmentId) {
        return auditEquipmentDAO.getAuditEquipmentInfo(equipmentId);
    }

    public Single<String> loadAuditEquipmentName(int auditEquipmentId) {
        return auditEquipmentDAO.getAuditObjectName(auditEquipmentId);
    }

    public Single<Integer> loadAuditId(int auditEquipmentId) {
        return auditEquipmentDAO.loadAuditId(auditEquipmentId);
    }
    public Single<RulesetRefAndVersion> getRuleSetAndVersion(int auditEquipmentId) {
        return auditEquipmentDAO.getRuleSetAndVersion(auditEquipmentId);
    }

    public Completable deleteAuditSubObjects(int auditEqId) {
        return auditEquipmentSubEquipmentDAO.deleteSubEquipments(auditEqId);
    }

    private List<AuditEquipmentSubEquipment> getSubEquipments(AuditEquipmentModel auditEquipment) {
        List<AuditEquipmentSubEquipment> subEquipments = new ArrayList<>();
        for (AuditEquipmentModel subEquipment : auditEquipment.getChildren()) {
            subEquipments.add(getAuditEquipmentSubEquipment(auditEquipment, subEquipment));
        }
        return subEquipments;
    }

    private AuditEquipmentSubEquipment getAuditEquipmentSubEquipment(AuditEquipmentModel parent, AuditEquipmentModel subEquipment) {
        return new AuditEquipmentSubEquipment(parent.getAudit_id()
                , parent.getId(), subEquipment.getEquipment().getReference());
    }

    public Completable addAuditSubObjects(AuditEquipmentModel auditEquipment) {
        return auditEquipmentSubEquipmentDAO.insert(getSubEquipments(auditEquipment));
    }

    public Completable updateAuditObjectName(int auditEqId, String name) {
        return auditEquipmentDAO.updateAuditObjectName(auditEqId, name);
    }

    public Single<List<AuditEquipmentForCurrentRouteModel>> loadAuditEquipmentsWithOrder(Long auditId) {
        return auditEquipmentDAO.loadAuditEquipmentsWithOrderAndNumberOfComments(auditId)
                .map(this::convertResultToAuditEquipmentWithOrderList);
    }

    private List<AuditEquipmentForCurrentRouteModel> convertResultToAuditEquipmentWithOrderList(List<AuditEquipmentWithNumberOfCommentsAndOrder> auditEquipmentWithAuditAndEquipments) {
        List<AuditEquipmentForCurrentRouteModel> auditEquipments = new ArrayList<>();
        for (AuditEquipmentWithNumberOfCommentsAndOrder equipment : auditEquipmentWithAuditAndEquipments) {
            auditEquipments.add(convertItemToAuditEquipment(equipment));
        }
        return auditEquipments;
    }

    private AuditEquipmentForCurrentRouteModel convertItemToAuditEquipment(AuditEquipmentWithNumberOfCommentsAndOrder equipment) {
        AuditModel auditModel = new AuditModel(equipment.getAudit());
        EquipmentModel equipmentModel = new EquipmentModel(equipment.getEquipment(), null);
        return new AuditEquipmentForCurrentRouteModel.Builder()
                .setId(equipment.getId())
                .setAnswer(equipment.getAnswer())
                .setAudit(auditModel)
                .setEquipment(equipmentModel)
                .setName(equipment.getName())
                .setNumberOfComments(equipment.getNumberOfComments())
                .setOrder(equipment.getOrder())
                .build();
    }

    public Single<List<AuditEquipmentForReport>> getAuditEquipmentsForReport(int auditId) {
        return ocaraDB.auditObjectDao().getAuditEquipmentsForReport(auditId);
    }

    public Single<List<AuditEquipmentModel>> loadAuditEquipments(Long auditId) {
        return auditEquipmentDAO.loadAuditEquipments(auditId)
                .map(this::convertResultToAuditEquipmentList);
    }

    private List<AuditEquipmentModel> convertResultToAuditEquipmentList(List<AuditEquipmentWithAuditAndEquipment> auditEquipmentWithAuditAndEquipments) {
        List<AuditEquipmentModel> auditEquipments = new ArrayList<>();
        for (AuditEquipmentWithAuditAndEquipment equipment : auditEquipmentWithAuditAndEquipments) {
            auditEquipments.add(convertItemToAuditEquipment(equipment));
        }
        return auditEquipments;
    }

    private AuditEquipmentModel convertItemToAuditEquipment(AuditEquipmentWithAuditAndEquipment equipment) {
        AuditModel auditModel = new AuditModel(equipment.getAudit());
        EquipmentModel equipmentModel = new EquipmentModel(equipment.getEquipment(), null);
        return new AuditEquipmentModel.Builder()
                .setEquipment(equipmentModel)
                .setAudit(auditModel)
                .setAnswer(equipment.getAnswer())
                .setName(equipment.getName())
                .setId(equipment.getId())
                .build();
    }

    public Completable updateOrder(int auditEquipmentId, int order) {
        return auditEquipmentDAO.updateOrder(auditEquipmentId, order);
    }

    public Completable updateLastAnsweredQuestion(int auditEquipmentId, int questionIdx) {
        return auditEquipmentDAO.updateLastAnsweredQuestion(auditEquipmentId, questionIdx);
    }

    public Completable updateOrders(List<AuditEquipmentWithNumberOfCommentAndOrderModel> eqs) {
        List<Integer> eqIds = new ArrayList<>();
        List<Integer> eqOrders = new ArrayList<>();

        for (AuditEquipmentWithNumberOfCommentAndOrderModel eq : eqs) {
            eqIds.add(eq.getId());
            eqOrders.add(eq.getOrder());
        }
        return auditEquipmentUpdateOrderDAO.updateOrders(eqIds, eqOrders);
    }

    public Observable<AuditEquipmentModel> newLoadAuditEquipmentById(int objectId) {
        return Single.zip(
                auditEquipmentDAO.loadAuditEquipmentById(objectId),
                auditEquipmentDAO.getSubEquipments(objectId),
                commentDAO.getAuditObjectComments((long) objectId),

                ((auditEquipmentWithAuditAndEquipment, childEquipments, comments) -> {
                    AuditEquipmentModel auditEquipmentModel = new AuditEquipmentModel.Builder()
                            .setAnswer(auditEquipmentWithAuditAndEquipment.getAnswer())
                            .setAudit(new AuditModel(auditEquipmentWithAuditAndEquipment.getAudit()))
                            .setEquipment(new EquipmentModel(auditEquipmentWithAuditAndEquipment.getEquipment()))
                            .setName(auditEquipmentWithAuditAndEquipment.getName())
                            .build();
                    auditEquipmentModel.setId(auditEquipmentWithAuditAndEquipment.getId());
                    addAuditSubEquipmentsInParent(auditEquipmentModel, childEquipments);
                    addCommentsToAuditEquipment(auditEquipmentModel, comments);

                    return auditEquipmentModel;

                }))
                .toObservable()
                .concatMap(auditEquipmentModel ->
                        equipmentSubEquipmentDAO.loadSubEquipments(
                                auditEquipmentModel.getEquipment().getReference(),
                                auditEquipmentModel.getAudit().getRulesetRef(),
                                auditEquipmentModel.getAudit().getRulesetVer())
                                .toObservable()
                                .map(equipmentWithSubEquipments -> {

                                    //Add Total subEquipmentToParent
                                    List<EquipmentModel> subEquipments = new ArrayList<>();

                                    for (EquipmentWithSubEquipment equipment : equipmentWithSubEquipments) {

                                        subEquipments.add(new EquipmentModel(equipment.getSubequipment()));
                                    }
                                    auditEquipmentModel.getEquipment().setChildren(subEquipments);
                                    return auditEquipmentModel;
                                }));


    }

    private void addCommentsToAuditEquipment(AuditEquipmentModel auditEquipmentModel, List<Comment> comments) {

        List<CommentModel> commentModels = new ArrayList<>();
        for (Comment comment : comments) {

            commentModels.add(new CommentModel(comment));
        }
        auditEquipmentModel.setComments(commentModels);
    }

    public Observable<AuditEquipmentWithNumberOfCommentAndOrderModel> getAuditEquipmentWithCharacteristics(int auditEquipmentId) {
        return getAuditEquipmentById(auditEquipmentId)
                .toObservable()
                .concatMap((Function<AuditEquipmentWithNumberOfCommentAndOrderModel, ObservableSource<AuditEquipmentWithNumberOfCommentAndOrderModel>>)
                        this::addSubAuditEquipments)
                .concatMap((Function<AuditEquipmentWithNumberOfCommentAndOrderModel, ObservableSource<AuditEquipmentWithNumberOfCommentAndOrderModel>>)
                        this::addSubEquipments);
    }

    private Observable<AuditEquipmentWithNumberOfCommentAndOrderModel> addSubEquipments(AuditEquipmentWithNumberOfCommentAndOrderModel auditEquipment) {
        return equipmentDAO.getChildrenOfEquipment(auditEquipment.getAudit().getRulesetRef(), auditEquipment.getAudit().getRulesetVer()
                , auditEquipment.getEquipment().getReference())
                .map(equipments -> addSubEquipmentsToAuditEquipment(auditEquipment, equipments))
                .toObservable();
    }

    private AuditEquipmentWithNumberOfCommentAndOrderModel addSubEquipmentsToAuditEquipment(AuditEquipmentWithNumberOfCommentAndOrderModel auditEquipment
            , List<Equipment> equipments) {
        for (Equipment equipment : equipments) {
            auditEquipment.getEquipment().addChild(new EquipmentModel(equipment));
        }
        return auditEquipment;
    }

    private Observable<AuditEquipmentWithNumberOfCommentAndOrderModel> addSubAuditEquipments(@NonNull AuditEquipmentWithNumberOfCommentAndOrderModel auditEquipment) {
        return auditEquipmentDAO.getSubEquipments(auditEquipment.getId())
                .map(equipments -> addSubAuditEquipmentsToAuditEquipment(auditEquipment, equipments)).toObservable();
    }

    private AuditEquipmentWithNumberOfCommentAndOrderModel addSubAuditEquipmentsToAuditEquipment(AuditEquipmentWithNumberOfCommentAndOrderModel auditEquipment
            , List<Equipment> equipments) {
        for (Equipment equipment : equipments) {
            EquipmentModel equipmentModel = new EquipmentModel(equipment);
            auditEquipment.addChild(getAuditEquipmentModel(auditEquipment.getAudit(), equipmentModel));
        }
        return auditEquipment;
    }

    private AuditEquipmentModel getAuditEquipmentModel(AuditModel audit, EquipmentModel equipment) {
        return new AuditEquipmentModel.Builder()
                .setEquipment(equipment)
                .setName(equipment.getName())
                .setAudit(audit)
                .build();
    }

    public Single<AuditEquipmentWithNumberOfCommentAndOrderModel> getAuditEquipmentById(int auditEquipmentId) {
        return auditEquipmentDAO.getAuditEquipment(auditEquipmentId)
                .map(this::getAuditEquipmentModelFromAuditEquipment);
    }

    private AuditEquipmentWithNumberOfCommentAndOrderModel getAuditEquipmentModelFromAuditEquipment(AuditEquipmentWithNumberOfCommentsAndOrder auditEquipment) {
        return new AuditEquipmentWithNumberOfCommentAndOrderModel.Builder()
                .setId(auditEquipment.getId())
                .setAudit(getAuditModelFromAuditEquipment(auditEquipment))
                .setEquipment(getEquipmentModelFromAuditEquipment(auditEquipment))
                .setName(auditEquipment.getName())
                .setNumberOfComments(auditEquipment.getNumberOfComments())
                .setOrder(auditEquipment.getOrder()).build();
    }

    private AuditModel getAuditModelFromAuditEquipment(AuditEquipmentWithNumberOfCommentsAndOrder auditEquipment) {
        return new AuditModel(auditEquipment.getAudit());
    }

    private EquipmentModel getEquipmentModelFromAuditEquipment(AuditEquipmentWithNumberOfCommentsAndOrder auditEquipment) {
        return new EquipmentModel(auditEquipment.getEquipment());
    }

    /*
        this method will take an audit equipment id and return
        an object of AuditEquipmentModel with its children and
        each audit equipment (parent or child) will have its equipment
        data in it and from the equipment you get the questions
        and from the question you can get the rules and from the rules
        you can get its illustrations
     */
    public Observable<AuditEquipmentModel> loadAuditEqById(int objectId) {
        return Single.zip(
                auditEquipmentDAO.loadAuditEquipmentById(objectId),
                auditEquipmentDAO.getSubEquipments(objectId),

                ((auditEquipmentWithAuditAndEquipment, childEquipments) -> {
                    AuditEquipmentModel auditEquipmentModel = new AuditEquipmentModel.Builder()
                            .setAnswer(auditEquipmentWithAuditAndEquipment.getAnswer())
                            .setAudit(new AuditModel(auditEquipmentWithAuditAndEquipment.getAudit()))
                            .setEquipment(new EquipmentModel(auditEquipmentWithAuditAndEquipment.getEquipment()))
                            .setName(auditEquipmentWithAuditAndEquipment.getName())
                            .build();
                    auditEquipmentModel.setId(auditEquipmentWithAuditAndEquipment.getId());
                    addAuditSubEquipmentsInParent(auditEquipmentModel, childEquipments);

                    return auditEquipmentModel;

                }))
                .toObservable()
                .concatMap(auditEquipmentModel ->
                        equipmentSubEquipmentDAO.loadSubEquipments(
                                auditEquipmentModel.getEquipment().getReference(),
                                auditEquipmentModel.getAudit().getRulesetRef(),
                                auditEquipmentModel.getAudit().getRulesetVer())
                                .toObservable()
                                .map(equipmentWithSubEquipments -> {

                                    //Add Total subEquipmentToParent
                                    List<EquipmentModel> subEquipments = new ArrayList<>();

                                    for (EquipmentWithSubEquipment equipment : equipmentWithSubEquipments) {

                                        subEquipments.add(new EquipmentModel(equipment.getSubequipment()));
                                    }
                                    auditEquipmentModel.getEquipment().setChildren(subEquipments);
                                    return auditEquipmentModel;
                                }))
                .concatMap(auditEquipmentModel -> {
                    Map<String, AuditEquipmentModel> refToAuditEq = new HashMap<>();
                    refToAuditEq.put(auditEquipmentModel.getEquipment().getReference(), auditEquipmentModel);

                    for (AuditEquipmentModel subEq : auditEquipmentModel.getChildren()) {
                        refToAuditEq.put(subEq.getEquipment().getReference(), subEq);
                    }
                    List<String> objRef = new ArrayList<>(refToAuditEq.keySet());
                    return questionDAO.getQuestionsWithRules(auditEquipmentModel.getAudit().getRulesetRef()
                            , objRef, auditEquipmentModel.getAudit().getRulesetVer())
                            .map(questionWithRuleAnswers -> {
                                Map<String, QuestionModel> refToQuestion = new HashMap<>();
                                for (QuestionWithRuleAnswer questionWithRuleAnswer : questionWithRuleAnswers) {
                                    String qRef = questionWithRuleAnswer.getQuestion().getReference();

                                    if (!refToQuestion.containsKey(qRef)) {
                                        QuestionModel questionModel = new QuestionModel(questionWithRuleAnswer.getQuestion());
                                        refToQuestion.put(qRef, questionModel);

                                        refToAuditEq.get(questionWithRuleAnswer.getObjectReference())
                                                .getEquipment().addQuestion(questionModel);
                                    }
                                    RuleModel ruleModel = new RuleModel(questionWithRuleAnswer.getRule());
                                    refToQuestion.get(qRef).addRule(ruleModel);
                                }
                                return auditEquipmentModel;
                            })
                            .toObservable();
                }).concatMap((Function<AuditEquipmentModel, ObservableSource<AuditEquipmentModel>>) auditEquipmentModel -> {
                    Map<String, RuleModel> ruleRefs = new HashMap<>();
                    for (QuestionModel questionModel : auditEquipmentModel.getEquipment().getQuestions()) {
                        for (RuleModel ruleModel : questionModel.getRules()) {
                            ruleRefs.put(ruleModel.getRef(), ruleModel);
                        }
                    }
                    for (AuditEquipmentModel sub : auditEquipmentModel.getChildren()) {
                        for (QuestionModel questionModel : sub.getEquipment().getQuestions()) {
                            for (RuleModel ruleModel : questionModel.getRules()) {
                                ruleRefs.put(ruleModel.getRef(), ruleModel);
                            }
                        }
                    }
                    List<String> rules = new ArrayList<>();
                    rules.addAll(ruleRefs.keySet());
                    return illustrationDAO.getIllustrationsForRule(rules, auditEquipmentModel.getAudit().getRulesetRef(), auditEquipmentModel.getAudit().getRulesetVer())
                            .map(illustrationWithRuleRefs -> {
                                for (IllustrationWithRuleRef illustration : illustrationWithRuleRefs) {
                                    IllustrationModel illustrationModel = new IllustrationModel(illustration.getIllustration());
                                    ruleRefs.get(illustration.getRuleRef()).addIllustration(illustrationModel);
                                }
                                return auditEquipmentModel;
                            }).toObservable();
                })
                ;
    }

    private void addAuditSubEquipmentsInParent(AuditEquipmentModel auditEquipment, List<Equipment> objects) {
        for (Equipment equipment : objects) {
            auditEquipment.addChild(getChildAuditEquipmen(auditEquipment, equipment));
        }
    }

    @NotNull
    private AuditEquipmentModel getChildAuditEquipmen(AuditEquipmentModel parent, Equipment equipment) {
        AuditEquipmentModel childAuditEquipment = new AuditEquipmentModel.Builder()
                .setAudit(parent.getAudit())
                .setName(equipment.getName())
                .setEquipment(new EquipmentModel(equipment))
                .setAnswer(parent.getAnswer())
                .build();
        childAuditEquipment.setParent(parent);
        return childAuditEquipment;
    }

    public Single<EquipmentNameAndIconModel> getAuditEquipmentName(int equipmentId) {
        return auditEquipmentDAO.getAuditEquipmentNameAndIcon(equipmentId)
                .map(EquipmentNameAndIconModel::new);
    }

    public Completable insertAuditEquipments(List<AuditEquipments> auditEquipments) {
        return auditEquipmentDAO.insertAndReturnCompletable(auditEquipments);
    }

    public Completable insertAuditSubEquipments(List<AuditEquipmentSubEquipment> auditEquipments) {
        return auditEquipmentSubEquipmentDAO.insert(auditEquipments);
    }
}
