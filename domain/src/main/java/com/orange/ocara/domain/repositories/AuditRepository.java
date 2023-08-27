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

import android.content.Context;

import com.orange.ocara.data.cache.database.DAOs.AuditDAO;
import com.orange.ocara.data.cache.database.DAOs.AuditEquipmentDAO;
import com.orange.ocara.data.cache.database.DAOs.AuditEquipmentSubEquipmentDAO;
import com.orange.ocara.data.cache.database.DAOs.EquipmentDAO;
import com.orange.ocara.data.cache.database.DAOs.RuleAnswerDAO;
import com.orange.ocara.data.cache.database.NonTables.AuditInfoForReport;
import com.orange.ocara.data.cache.database.NonTables.AuditWithRulesetAndSiteAndComments;
import com.orange.ocara.data.cache.database.NonTables.AuditWithSite;
import com.orange.ocara.data.cache.database.NonTables.PairOfOldAndNewAuditEquipmentId;
import com.orange.ocara.data.cache.database.NonTables.RulesetRefAndVersion;
import com.orange.ocara.data.cache.database.OcaraDB;
import com.orange.ocara.data.cache.database.Tables.Audit;
import com.orange.ocara.data.cache.database.Tables.RuleAnswer;
import com.orange.ocara.data.cache.database.crossRef.AuditEquipmentSubEquipment;
import com.orange.ocara.data.cache.database.crossRef.AuditEquipments;
import com.orange.ocara.data.oldEntities.AuditEntity;
import com.orange.ocara.data.oldEntities.RulesetEntity;
import com.orange.ocara.domain.models.AuditEquipmentModel;
import com.orange.ocara.domain.models.AuditInfoForReportModel;
import com.orange.ocara.domain.models.AuditModel;
import com.orange.ocara.domain.models.AuditWithRulesetWithSiteModel;
import com.orange.ocara.domain.models.AuditorModel;
import com.orange.ocara.domain.models.RulesetModel;
import com.orange.ocara.domain.models.SiteModel;
import com.orange.ocara.utils.enums.AuditLevel;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import timber.log.Timber;

public class AuditRepository {
    private static final String AUDITS_DIRECTORY = "audits";
    private final AuditDAO auditDAO;
    private final AuditEquipmentDAO auditEquipmentDAO;
    private final AuditEquipmentSubEquipmentDAO auditEquipmentSubEquipmentDAO;
    private final RuleAnswerDAO ruleAnswerDAO;

    private final Context context;
    private final EquipmentDAO equipmentDAO;

    @Inject
    public AuditRepository(@ApplicationContext Context context, OcaraDB ocaraDB) {
        this.auditDAO = ocaraDB.auditDao();
        this.context = context;
        equipmentDAO = ocaraDB.equipmentDao();
        auditEquipmentDAO = ocaraDB.auditObjectDao();
        auditEquipmentSubEquipmentDAO = ocaraDB.auditEquipmentSubEquipmentDAO();
        ruleAnswerDAO = ocaraDB.ruleAnswerDAO();
    }

    public Single<Integer> getNumberOfAudits() {
        return auditDAO.getTheNumberOfAudits();
    }

    public Single<RulesetRefAndVersion> getRulesetInfo(int auditId) {
        return auditDAO.getRulesetInfo(auditId);
    }

    public Single<Integer> getRulesetId(int auditId) {
        return auditDAO.getRulesetIdForAudit(auditId);
    }

    public Single<AuditInfoForReportModel> getAuditInfoForReport(int auditId) {
        return auditDAO.getAuditInfoForReport(auditId)
                .map(this::mapEntityToModel);
    }

    private AuditInfoForReportModel mapEntityToModel(AuditInfoForReport audit) {
        return new AuditInfoForReportModel(audit);
    }

    public Completable deleteAudits(List<Integer> ids) {
        Completable result = deleteAuditWithEquipmentsAndAnswers(ids.get(0));
        for (int i = 1; i < ids.size(); i++) {
            result = result.concatWith(deleteAuditWithEquipmentsAndAnswers(ids.get(i)));
        }
        return result;
    }

    public Single<AuditLevel> getAuditLevel(int auditEqId) {
        return auditDAO.getAuditLevel(auditEqId);
    }

    public Single<List<String>> getAuthors() {
        return auditDAO.getAuthors();
    }

    public Single<Boolean> checkAuditExistenceByNameAndVersion(String name, int version, int siteId) {
        return auditDAO.getAudit(name, version, siteId).map(audit -> {
            return audit != null;
        });
    }

    public Completable insertAudits(List<AuditEntity> auditEntities) {
        List<Audit> audits = new ArrayList<>();
        for (AuditEntity auditEntity : auditEntities) {
            Audit audit = mapModelToEntity(auditEntity);
            audit.setId(auditEntity.getId().intValue());
            audits.add(audit);
        }
        return auditDAO.insert(audits);
    }

    public Single<Long> insertAudit(AuditModel auditModel) {
        Audit audit = mapModelToEntity(auditModel);
        return auditDAO.insert(audit);
    }

    private Audit mapModelToEntity(AuditModel auditModel) {
        return Audit.Builder().setLevel(auditModel.getLevel())
                .setName(auditModel.getName())
                .setDate(auditModel.getDate())
                .setVersion(auditModel.getVersion())
                .setRulesetRef(auditModel.getRuleset().getReference())
                .setInProgress(auditModel.isInProgress())
                .setRulesetVer(Integer.parseInt(auditModel.getRuleset().getVersion()))
                .setSiteId(auditModel.getSite().getId())
                .setUserName(auditModel.getUserName())
                .setAuditorId(auditModel.getAuditorId())
                .build();
    }

    private Audit mapModelToEntity(AuditEntity auditModel) {
        RulesetEntity rulesetEntity = auditModel.getRuleSet();
        boolean isInProgress = auditModel.getStatus().equals(AuditEntity.Status.IN_PROGRESS);
        AuditLevel level = auditModel.getLevel().equals(AuditEntity.Level.BEGINNER) ? AuditLevel.BEGINNER : AuditLevel.EXPERT;
        return Audit.Builder().setLevel(level)
                .setName(auditModel.getName())
                .setDate(auditModel.getDate().getTime())
                .setVersion(auditModel.getVersion())
                .setRulesetRef(rulesetEntity.getReference())
                .setInProgress(isInProgress)
                .setRulesetVer(Integer.parseInt(rulesetEntity.getVersion()))
                .setSiteId(auditModel.getSite().getId().intValue())
                .setUserName(auditModel.getAuthorName())
                .build();
    }

    public Observable<List<Long>> copyAudit(final AuditModel auditModel, final boolean copyAnswers) {
        int oldAuditId = auditModel.getId();
        Audit auditEntity = mapModelToEntity(auditModel);
        auditEntity.setInProgress(true);
        return auditDAO.insert(auditEntity)
                .toObservable()
                .concatMap(new Function<Long, ObservableSource<List<AuditEquipments>>>() {
                    @Override
                    public ObservableSource<List<AuditEquipments>> apply(Long newAuditId) throws Exception {
                        Timber.d("inserted audit copy with id = " + newAuditId);
                        auditModel.setId(newAuditId.intValue());
                        return auditEquipmentDAO.getAuditEquipments(oldAuditId).toObservable();
                    }
                }).concatMap(new Function<List<AuditEquipments>, ObservableSource<List<Long>>>() {
                    @Override
                    public ObservableSource<List<Long>> apply(List<AuditEquipments> auditEquipments) throws Exception {
                        Timber.d("inserting audit equipments");
                        for (AuditEquipments auditEquipment : auditEquipments) {
                            // this is to allow room to set the id
                            auditEquipment.setId(0);
                            auditEquipment.setAudit_id(auditModel.getId());
                        }
                        return auditEquipmentDAO.insert(auditEquipments).toObservable();
                    }
                })
                .concatMap(new Function<List<Long>, ObservableSource<List<PairOfOldAndNewAuditEquipmentId>>>() {
                    @Override
                    public ObservableSource<List<PairOfOldAndNewAuditEquipmentId>> apply(List<Long> longs) throws Exception {
                        Timber.d("loading ------ mapOldAuditEquipmentIdToNewAuditEquipmentId");
                        return auditEquipmentDAO.mapOldAuditEquipmentIdToNewAuditEquipmentId(oldAuditId, auditModel.getId()).toObservable();
                    }
                }).concatMap(pairs -> {
                    List<Completable> completables = new ArrayList<>();
                    if (copyAnswers)
                        for (PairOfOldAndNewAuditEquipmentId singePair : pairs) {
                            completables.add(copyAuditAnswers(singePair));
                        }
                    return Completable.merge(completables).toObservable();
                })
                .concatMap(new Function<Object, ObservableSource<List<PairOfOldAndNewAuditEquipmentId>>>() {
                    @Override
                    public ObservableSource<List<PairOfOldAndNewAuditEquipmentId>> apply(Object ids) throws Exception {
                        Timber.d("loading mapOldAuditEquipmentIdToNewAuditEquipmentId");
                        return auditEquipmentDAO.mapOldAuditEquipmentIdToNewAuditEquipmentId(oldAuditId, auditModel.getId()).toObservable();
                    }
                }).concatMap(new Function<List<PairOfOldAndNewAuditEquipmentId>, ObservableSource<List<AuditEquipmentSubEquipment>>>() {
                    @Override
                    public ObservableSource<List<AuditEquipmentSubEquipment>> apply(List<PairOfOldAndNewAuditEquipmentId> pairOfOldAndNewAuditEquipmentIds) throws Exception {
                        HashMap<Integer, Integer> oldAuditEquipmentIdToNewAuditEquipmentId = convertListToHashMap(pairOfOldAndNewAuditEquipmentIds);
                        return auditEquipmentSubEquipmentDAO.getSubEquipmentsOfAudit(oldAuditId)
                                .map(oldSubEquipments -> {
                                    Timber.d("before1");
                                    for (int k : oldAuditEquipmentIdToNewAuditEquipmentId.keySet()) {
                                        Timber.d(k + " " + oldAuditEquipmentIdToNewAuditEquipmentId.get(k));
                                    }
                                    Timber.d("before2");
                                    for (AuditEquipmentSubEquipment subEquipment : oldSubEquipments) {
                                        // this is to allow room to set the id
                                        subEquipment.setId(0);
                                        subEquipment.setAuditEquipmentId(
                                                oldAuditEquipmentIdToNewAuditEquipmentId
                                                        .get(subEquipment.getAuditEquipmentId()));
                                    }
                                    Timber.d("after");
                                    return oldSubEquipments;
                                }).toObservable();
                    }
                }).concatMap(new Function<List<AuditEquipmentSubEquipment>, ObservableSource<List<Long>>>() {
                    @Override
                    public ObservableSource<List<Long>> apply(List<AuditEquipmentSubEquipment> auditEquipmentSubEquipments) throws Exception {
                        Timber.d("insert ---  mapOldAuditEquipmentIdToNewAuditEquipmentId");

                        return auditEquipmentSubEquipmentDAO.insert(auditEquipmentSubEquipments).toObservable();
                    }
                });
    }

    public Completable copyAuditAnswers(PairOfOldAndNewAuditEquipmentId pair) {
        return ruleAnswerDAO.getRuleAnswersList(pair.getOldAuditEquipmentId()).flatMapCompletable(
                ruleAnswers -> {
                    for (RuleAnswer answer : ruleAnswers) {
                        answer.setAuditEquipmentId(pair.getNewAuditEquipmentId());
                    }
                    return ruleAnswerDAO.insert(ruleAnswers);
                }
        );
    }

    public Completable copyAuditAnswers(int oldAuditId, int newAuditId) {
        List<Completable> completables = new ArrayList<>();

        return auditEquipmentDAO.mapOldAuditEquipmentIdToNewAuditEquipmentId(oldAuditId, newAuditId).flatMapCompletable(
                new Function<List<PairOfOldAndNewAuditEquipmentId>, Completable>() {
                    @Override
                    public Completable apply(List<PairOfOldAndNewAuditEquipmentId> pairOfOldAndNewAuditEquipmentIds) throws Exception {
                        for (PairOfOldAndNewAuditEquipmentId pair : pairOfOldAndNewAuditEquipmentIds) {
                            ruleAnswerDAO.getRuleAnswersList(pair.getOldAuditEquipmentId()).map(
                                    ruleAnswers -> {
                                        for (RuleAnswer answer : ruleAnswers) {
                                            answer.setAuditEquipmentId(pair.getNewAuditEquipmentId());
                                        }
                                        return completables.add(ruleAnswerDAO.insert(ruleAnswers));
                                    }
                            );
                        }
                        return Completable.merge(completables);
                    }
                });


    }

    @NotNull
    private HashMap<Integer, Integer> convertListToHashMap(List<PairOfOldAndNewAuditEquipmentId> pairOfOldAndNewAuditEquipmentIds) {
        HashMap<Integer, Integer> oldAuditEquipmentIdToNewAuditEquipmentId = new HashMap<>();
        for (PairOfOldAndNewAuditEquipmentId pairOfOldAndNewAuditEquipmentId : pairOfOldAndNewAuditEquipmentIds) {
            oldAuditEquipmentIdToNewAuditEquipmentId.put(
                    pairOfOldAndNewAuditEquipmentId.getOldAuditEquipmentId()
                    , pairOfOldAndNewAuditEquipmentId.getNewAuditEquipmentId());
        }
        return oldAuditEquipmentIdToNewAuditEquipmentId;
    }

    // this method loads the audit with its ruleset and site
    public Single<AuditModel> getAuditById(Long id) {
        return auditDAO.getAudit(id).map(audit -> {
            AuditModel auditModel = new AuditModel(audit.getAudit());
            auditModel.setId(id.intValue());
            auditModel.setRuleset(RulesetModel
                    .newRuleSetInfo(audit.getRulesetDetails()));
            auditModel.setSite(new SiteModel(audit.getSite()));
            return auditModel;
        });
    }

    // this method loads the audit with its ruleset , site and auditor
    public Single<AuditModel> getAuditWithSiteAndAuditorAndRulesetById(Long id) {
        return auditDAO.getAuditWithRulesetAndSiteAndAuditor(id).map(audit -> {
            AuditModel auditModel = new AuditModel(audit.getAudit());
            auditModel.setId(id.intValue());
            auditModel.setRuleset(RulesetModel
                    .newRuleSetInfo(audit.getRulesetDetails()));
            auditModel.setSite(new SiteModel(audit.getSite()));
            auditModel.setAuditor(new AuditorModel(audit.getAuditor()));
            auditModel.setAuditorId(audit.getAuditor().getId());
            return auditModel;
        });
    }

    public Single<List<AuditModel>> getAuditsWithRulesetAndSite(String filter) {
        return auditDAO.getAuditsWithRulesetAndSiteSortedByDateDESC(filter)
                .map(this::mapAuditListWithRulesetToAuditModel);
    }

    public List<AuditModel> mapAuditListWithRulesetToAuditModel(List<AuditWithRulesetAndSiteAndComments> audits) {
        List<AuditModel> result = new ArrayList<>();
        for (AuditWithRulesetAndSiteAndComments audit : audits) {
            AuditModel auditModel = new AuditModel(audit.getAudit());
            auditModel = addRulesetToAuditModel(audit, auditModel);
            auditModel = addSiteToAuditModel(audit, auditModel);
            result.add(auditModel);
        }
        return result;
    }

    private AuditModel addSiteToAuditModel(AuditWithRulesetAndSiteAndComments audit, AuditModel auditModel) {
        SiteModel site = new SiteModel(audit.getSite());
        auditModel.setSite(site);
        return auditModel;
    }

    private AuditModel addRulesetToAuditModel(AuditWithRulesetAndSiteAndComments audit, AuditModel auditModel) {
        RulesetModel ruleset = RulesetModel.newRuleSetInfo(audit.getRulesetDetails());
        auditModel.setRuleset(ruleset);
        return auditModel;
    }

    public Flowable<List<AuditModel>> getAuditsOrderedByDateASC(String key) {
        if (key == null || key.isEmpty())
            return getAuditsWithSiteFromSource(auditDAO.getAuditsWithSiteOrderedByDateASC());
        key = "%" + key + "%";
        return getAuditsWithSiteFromSource(auditDAO.getAuditsWithSiteOrderedByDateASC(key));
    }

    public Flowable<List<AuditModel>> getAuditsOrderedByDateDESC(String key) {
        if (key == null || key.isEmpty())
            return getAuditsWithSiteFromSource(auditDAO.getAuditsWithSiteOrderedByDateDESC());
        key = "%" + key + "%";
        return getAuditsWithSiteFromSource(auditDAO.getAuditsWithSiteOrderedByDateDESC(key));
    }

    public Flowable<List<AuditModel>> getAuditsOrderedBySiteAsc(String key) {
        if (key == null || key.isEmpty())
            return getAuditsWithSiteFromSource(auditDAO.getAuditsWithSiteOrderedBySiteASC());
        key = "%" + key + "%";
        return getAuditsWithSiteFromSource(auditDAO.getAuditsWithSiteOrderedBySiteASC(key));
    }

    public Flowable<List<AuditModel>> getAuditsOrderedBySiteDESC(String key) {
        if (key == null || key.isEmpty())
            return getAuditsWithSiteFromSource(auditDAO.getAuditsWithSiteOrderedBySiteDESC());
        key = "%" + key + "%";
        return getAuditsWithSiteFromSource(auditDAO.getAuditsWithSiteOrderedBySiteDESC(key));
    }


    public Flowable<List<AuditModel>> getAuditsOrderedByStatusASC(String key) {
        if (key == null || key.isEmpty())
            return getAuditsWithSiteFromSource(auditDAO.getAuditsWithSiteOrderedByStatusASC());
        key = "%" + key + "%";
        return getAuditsWithSiteFromSource(auditDAO.getAuditsWithSiteOrderedByStatusASC(key));
    }


    public Flowable<List<AuditModel>> getAuditsOrderedByStatusDESC(String key) {
        if (key == null || key.isEmpty())
            return getAuditsWithSiteFromSource(auditDAO.getAuditsWithSiteOrderedByStatusDESC());
        key = "%" + key + "%";
        return getAuditsWithSiteFromSource(auditDAO.getAuditsWithSiteOrderedByStatusDESC(key));
    }


    public Flowable<List<AuditModel>> getAuditsOrderedByNameASC(String key) {
        if (key == null || key.isEmpty())
            return getAuditsWithSiteFromSource(auditDAO.getAuditsWithSiteOrderedByNameASC());
        key = "%" + key + "%";
        return getAuditsWithSiteFromSource(auditDAO.getAuditsWithSiteOrderedByNameASC(key));
    }


    public Flowable<List<AuditModel>> getAuditsOrderedByNameDESC(String key) {
        if (key == null || key.isEmpty())
            return getAuditsWithSiteFromSource(auditDAO.getAuditsWithSiteOrderedByNameDESC());
        key = "%" + key + "%";
        return getAuditsWithSiteFromSource(auditDAO.getAuditsWithSiteOrderedByNameDESC(key));
    }
//--------------------------------


    private Flowable<List<AuditModel>> getAuditsWithSiteFromSource(Flowable<List<AuditWithSite>> source) {
        return source.flatMap(auditList ->
                Flowable.fromIterable(auditList)
                        .map(audit -> {
                            AuditModel auditModel = new AuditModel(audit.getAudit());
                            auditModel.setSite(new SiteModel(audit.getSite()));
                            return auditModel;
                        })
                        .toList().toFlowable());
    }

    // todo refactor
    public Single<Long> insertAuditEquipmentAndSubEquipments(AuditEquipmentModel equipmentModel) {
        AuditEquipments auditEquipment = getAuditEquipment(equipmentModel);
        Observable<Integer> getNumberOfAuditEquipmentsObservable = auditEquipmentDAO.getNumberOfAuditEquipmentsForAudit(auditEquipment.getAudit_id())
                .toObservable();
        return Single.fromObservable(getNumberOfAuditEquipmentsObservable
                .concatMap(addOrderToAuditEquipmentAndSaveItInDB(auditEquipment))
                .concatMap(insertSubEquipments(equipmentModel)));
    }

    @NotNull
    private Function<Long, ObservableSource<Long>> insertSubEquipments(AuditEquipmentModel equipmentModel) {
        return id -> {
            List<String> subRefs = equipmentModel.getChildrenReferences();
            return insertSubEquipments(equipmentModel.getAudit_id(), id.intValue(), subRefs).toObservable()
                    .map(o -> id);
        };
    }

    @NotNull
    private Function<Integer, ObservableSource<Long>> addOrderToAuditEquipmentAndSaveItInDB(AuditEquipments auditEquipment) {
        return numberOfEquipmentsBeforeCurrent -> {
            auditEquipment.setOrder(numberOfEquipmentsBeforeCurrent + 1);
            return auditEquipmentDAO.insert(auditEquipment).toObservable();
        };
    }

    private AuditEquipments getAuditEquipment(AuditEquipmentModel equipmentModel) {
        return new AuditEquipments.AuditEquipmentsBuilder()
                .setObjectRef(equipmentModel.getEquipment().getReference())
                .setName(equipmentModel.getEquipment().getName())
                .setAudit_id(equipmentModel.getAudit_id())
                .createAuditEquipments();
    }


    public File getAttachmentDirectory(Long auditId) {
        File auditsDirectory = getAuditsDirectory();
        File result = new File(auditsDirectory, "" + auditId);
        if (!result.exists()) {
            result.mkdirs();
        }
        return result;
    }

    public Completable deleteAuditObject(int auditId, int objRef) {
        return auditEquipmentDAO.deleteAuditObject(auditId, objRef)
                .concatWith(auditEquipmentSubEquipmentDAO.deleteSubEquipments(objRef));
    }

    private File getAuditsDirectory() {
        return context.getDir(AUDITS_DIRECTORY, Context.MODE_PRIVATE);
    }

    public Completable deleteAllAuditObjects(int auditId) {
        return auditEquipmentDAO.deleteAllAuditObjects(auditId)
                .concatWith(auditEquipmentSubEquipmentDAO.deleteSubEquipments(auditId));
    }

    public Completable deleteSubEquipments(int auditId, int auditEquipmentId, List<String> children) {
        return auditEquipmentSubEquipmentDAO.deleteChildren(auditId, auditEquipmentId, children);
    }

    public Single<List<Long>> insertSubEquipments(int auditId, int auditEquipmentId, List<String> children) {
        List<AuditEquipmentSubEquipment> auditEquipmentSubEquipments = new ArrayList<>();
        for (String child : children) {
            auditEquipmentSubEquipments.add(new AuditEquipmentSubEquipment.AuditEquipmentSubEquipmentBuilder().setAudit_id(auditId).setAuditEquipmentId(auditEquipmentId).setChildRef(child).createAuditEquipmentSubEquipment());
        }
        return auditEquipmentSubEquipmentDAO.insertAndReturnSingle(auditEquipmentSubEquipments);
    }

    public Completable updateAudit(AuditModel auditModel) {
        return auditDAO.update(auditModel.getName(), auditModel.getUserName(), auditModel.getLevel(), auditModel.getId());
    }

    /*
        this update method is used for the mobile version where we have a separate table of auditors in the DB
     */
    public Completable updateAuditV2(AuditModel auditModel) {
        return auditDAO.updateV2(auditModel.getName(), auditModel.getSite().getId(), auditModel.getAuditorId(), auditModel.getLevel(), auditModel.getId());
    }

    public Single<AuditWithRulesetWithSiteModel> getAuditWithRuleWithSite(Long auditId) {
        return auditDAO.getAuditWithRulesetWithSite(auditId)
                .map(AuditWithRulesetWithSiteModel::getInstance);
    }


//    public Completable deleteAudit(long auditId) {
//        return auditDAO.deleteAudit(auditId);
//    }

    public Completable deleteAuditWithEquipmentsAndAnswers(long auditId) {
        int audId = ((int) auditId);
        Timber.d("deleting " + audId);
        return ruleAnswerDAO.deleteRuleAnswersOfAudit(auditId)
                .concatWith(deleteAllAuditObjects(audId))
                .concatWith(auditDAO.deleteAudit(auditId));
    }


    public Completable setAuditToLocked(long auditId) {
        return auditDAO.setAuditToLocked(auditId);
    }

    public Single<List<AuditModel>> getLastThreeAudits() {
        return auditDAO.getTheLastThreeAudits()
                .map(audits -> {
                    List<AuditModel> models = new ArrayList<>();
                    for (Audit audit :
                            audits) {
                        models.add(new AuditModel(audit));
                    }
                    return models;
                });
    }
}
