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

import androidx.annotation.NonNull;

import com.orange.ocara.data.cache.database.OcaraDB;
import com.orange.ocara.data.cache.database.Tables.Equipment;
import com.orange.ocara.data.cache.database.Tables.EquipmentCategory;
import com.orange.ocara.data.cache.database.Tables.Illustration;
import com.orange.ocara.data.cache.database.Tables.ImpactValue;
import com.orange.ocara.data.cache.database.Tables.ProfileType;
import com.orange.ocara.data.cache.database.Tables.Question;
import com.orange.ocara.data.cache.database.Tables.Rule;
import com.orange.ocara.data.cache.database.Tables.RulesetDetails;
import com.orange.ocara.data.cache.database.crossRef.EquipmentAndCategoryCrossRef;
import com.orange.ocara.data.cache.database.crossRef.EquipmentRulesetVersion;
import com.orange.ocara.data.cache.database.crossRef.EquipmentSubEquipmentCrossRef;
import com.orange.ocara.data.cache.database.crossRef.EquipmentWithIllustrations;
import com.orange.ocara.data.cache.database.crossRef.ImpactValueRulesetCrossref;
import com.orange.ocara.data.cache.database.crossRef.ProfileTypeRulesetCrossref;
import com.orange.ocara.data.cache.database.crossRef.QuestionsEquipmentsCrossRef;
import com.orange.ocara.data.cache.database.crossRef.QuestionsRulesCrossRef;
import com.orange.ocara.data.cache.database.crossRef.RuleProfileTypeImpactCrossRef;
import com.orange.ocara.data.cache.database.crossRef.RuleWithIllustrations;
import com.orange.ocara.data.network.client.NewImageRemote;
import com.orange.ocara.data.network.client.NewRulesetRemote;
import com.orange.ocara.data.network.models.ChainWs;
import com.orange.ocara.data.network.models.EquipmentWs;
import com.orange.ocara.data.network.models.IllustrationWs;
import com.orange.ocara.data.network.models.ImpactValueWs;
import com.orange.ocara.data.network.models.ProfileTypeWs;
import com.orange.ocara.data.network.models.QuestionWs;
import com.orange.ocara.data.network.models.QuestionnaireGroupWs;
import com.orange.ocara.data.network.models.QuestionnaireWs;
import com.orange.ocara.data.network.models.RuleImpactWs;
import com.orange.ocara.data.network.models.RuleWs;
import com.orange.ocara.data.network.models.RulesetLightWs;
import com.orange.ocara.data.network.models.RulesetWs;
import com.orange.ocara.data.network.models.WithIcon;
import com.orange.ocara.data.network.remoteContracts.ImageRemoteApi;
import com.orange.ocara.data.network.remoteContracts.RulesetWebServiceRx;
import com.orange.ocara.data.source.ImageSource;
import com.orange.ocara.data.source.NetworkInfoSource;
import com.orange.ocara.domain.RulesetModelFactory;
import com.orange.ocara.domain.cache.prefs.DefaultRulesetPreferences;
import com.orange.ocara.domain.models.RulesetModel;
import com.orange.ocara.domain.tools.RulesetComparator;
import com.orange.ocara.utils.CollectionUtils;
import com.orange.ocara.utils.ListUtils;
import com.orange.ocara.utils.enums.RuleSetStat;
import com.orange.ocara.utils.models.VersionableModel;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Function;
import timber.log.Timber;

public class RulesetRepository {
    private final OcaraDB ocaraDB;
    private final NewImageRemote imageRemote;
    private final ImageSource.ImageCache imageCache;
    private final NewRulesetRemote rulesetRemote;
    private final DefaultRulesetPreferences defaultRulesetPreferences;
    private final NetworkInfoSource.NetworkInfoCache networkCache;
    private final RulesetWebServiceRx rulesetWebServiceRx;
    private final ImageRemoteApi imageRemoteApi;

    @Inject
    public RulesetRepository(OcaraDB ocaraDB
            , NetworkInfoSource.NetworkInfoCache networkInfoCache
            , NewRulesetRemote rulesetRemote
            , NewImageRemote imageRemote
            , ImageSource.ImageCache imageCache
            , DefaultRulesetPreferences defaultRulesetPreferences
            , RulesetWebServiceRx rulesetWebServiceRx, ImageRemoteApi imageRemoteApi) {
        this.ocaraDB = ocaraDB;
        this.defaultRulesetPreferences = defaultRulesetPreferences;
        this.imageRemote = imageRemote;
        this.imageCache = imageCache;
        this.networkCache = networkInfoCache;
        this.rulesetRemote = rulesetRemote;
        this.rulesetWebServiceRx = rulesetWebServiceRx;
        this.imageRemoteApi = imageRemoteApi;
    }

    public Single<Integer> checkIfDemoRulesetIsInserted() {
        return ocaraDB.rulesetDAO()
                .getDefualtRuleset();
    }

    private static boolean allDifferent(List<RulesetModel> source, RulesetModel reference) {
        boolean allDiff = true;
        for (RulesetModel element : source) {
            if (element.isSameAs(reference)) {
                allDiff = false;
                break;
            }
        }
        return allDiff;
    }

    public Single<Integer> getNumberOfAudits(int id) {
        return ocaraDB.rulesetDAO().getNumberOfRelatedAudits(id);
    }

    private static Map<String, RulesetModel> wsToMap(List<RulesetLightWs> input) {
        Map<String, RulesetModel> map = new HashMap<>();
        for (RulesetLightWs item : input) {
            map.put(item.getReference(), RulesetModelFactory.makeRulesetModel(item));
        }
        return map;
    }

    private static Map<String, RulesetModel> cachedToMap(List<RulesetDetails> input) {
        Map<String, RulesetModel> map = new HashMap<>();
        for (RulesetDetails item : input) {
            map.put(item.getReference(), RulesetModel.newRuleSetInfo(item));
        }
        return map;
    }

    /*public Single<Boolean> checkRulesetIsUpgradable(RulesetModel rulesetModel){
        return ocaraDB.rulesetDAO().getLatestRulesetByReference(rulesetModel.getReference())
                .map(rulesetDetails -> {
                    return
                });
    }*/
    public Completable saveInRoom(RulesetWs input) {

        Completable res = insertRulesetDetailsInRoom(input);
        res = res.concatWith(insertEquipmentsInRoom(input));
        res = res.concatWith(insertEquipmentsVersions(input));
        res = res.concatWith(insertObjectCategoriesInRoom(input));
        res = res.concatWith(insertEquipmentSubEquipmentInRoom(input));
        res = res.concatWith(insertIllustrationsInRoom(input));
        res = res.concatWith(insertObjectIllustrations(input));
        res = res.concatWith(insertRulesInRoom(input));
        res = res.concatWith(insertRuleIllustrations(input));
        res = res.concatWith(insertQuestionsInRoom(input));
        res = res.concatWith(insertRelationBetweenObjectAndQuestions(input));
        res = res.concatWith(insertRelationBetweenQuestionsAndRules(input));
        res = res.concatWith(insertProfileTypesInRoom(input));
        res = res.concatWith(insertImpactValueInRoom(input));
        res = res.concatWith(insertRelationBetweenRulesAndImpactValueAndProfileType(input));
        return res;
    }

    public Completable deleteRuleset(int rulesetId) {
        // todo edit this to delete all versions of a ruleset and its relations
        //  it's currently deleting the ruleset only from its table
//        ocaraDB.ruleProfileTypeImpactDAO().delete(rulesetId);
//        ocaraDB.impactValueRulesetDao().delete(rulesetId);
//        ocaraDB.equipmentRulesetVersionDAO().delete(rulesetId);
//        ocaraDB.equipmentAndCategoryRelationDAO().delete(rulesetId);
//
//        ocaraDB.categoryDao().delete(rulesetId);
//        ocaraDB.illustrationDao().deleteRulesetIllustrations(rulesetId);
//        ocaraDB.objectWithIllustrationsDAO().deleteEquipmentWithIllustrations(rulesetId);
//        Completable res = ocaraDB.rulesetDAO().delete(rulesetId);

        Completable res = ocaraDB.ruleProfileTypeImpactDAO().delete(rulesetId)
                .andThen(ocaraDB.impactValueRulesetDao().delete(rulesetId))
                .andThen(ocaraDB.equipmentRulesetVersionDAO().delete(rulesetId))
                .andThen(ocaraDB.equipmentAndCategoryRelationDAO().delete(rulesetId))
                .andThen(ocaraDB.categoryDao().delete(rulesetId))
                .andThen(ocaraDB.illustrationDao().deleteRulesetIllustrations(rulesetId))
                .andThen(ocaraDB.objectWithIllustrationsDAO().deleteEquipmentWithIllustrations(rulesetId))
                .andThen(ocaraDB.rulesetDAO().delete(rulesetId));

        return res;
    }

    public void saveDefaultRuleset(RulesetModel ruleset) {
        if (ruleset == null) {
            defaultRulesetPreferences.saveRuleset(null);
            return;
        }
        RulesetModel current = defaultRulesetPreferences.retrieveRuleset();
        boolean isDifferent = current == null || !current.getVersionName().equals(ruleset.getVersionName());
        if (isDifferent) {
            Timber.i("CacheMessage=Saving new default ruleset;OldRuleset=%s;NewRuleset=%s", current == null ? "<null>" : current.getVersionName(), ruleset.getVersionName());
            defaultRulesetPreferences.saveRuleset(ruleset);
        } else {
            Timber.d("CacheMessage=No need to change default ruleset = %s", ruleset.getReference());
        }
    }


    public Completable insertRulesets(List<RulesetDetails> rulesetDetails) {
        return ocaraDB.rulesetDAO().insert(rulesetDetails);
    }

    public Single<List<RulesetModel>> findAll() {
        boolean networkAvailable = networkCache.isNetworkAvailable();
        return ocaraDB.rulesetDAO().getRulesets()
//                .map(rulesetDetails -> {
//                    List<RulesetModel> rulesetModels = new ArrayList<>();
//                    for(RulesetDetails ruleset:rulesetDetails){
//                        rulesetModels.add(RulesetModel.newRuleSetInfo(ruleset));
//                    }
//                    return rulesetModels;
//                })
                .toObservable()
                .concatMap((Function<List<RulesetDetails>, ObservableSource<List<RulesetModel>>>) rulesetDetails -> {
                            Timber.d("test rulesets cached = " + rulesetDetails.size() + " ");
                            Map<String, RulesetModel> cachedRulesets = cachedToMap(rulesetDetails);
                            List<RulesetModel> res = new ArrayList<>(cachedRulesets.values());
                            if (networkAvailable) {
                                Timber.d("nnnn network available");
                                return loadRemoteRulesets(cachedRulesets, res).toObservable();
                            } else {
                                Timber.d("nnnn no network available");
                                return Single.create((SingleOnSubscribe<List<RulesetModel>>) emitter -> emitter.onSuccess(res)).toObservable();
                            }
                        }

                ).map(rulesetModels -> {
                    // sorting
                    RulesetComparator comparator;
                    RulesetModel defaultRuleset = findDefaultRuleset();
                    if (defaultRuleset != null) {
                        comparator = new RulesetComparator(defaultRuleset);
                        if (allDifferent(rulesetModels, defaultRuleset)) {
                            rulesetModels.add(0, defaultRuleset);
                        }
                    } else {
                        comparator = new RulesetComparator();
                    }
                    Collections.sort(rulesetModels, comparator);
                    return rulesetModels;
                })
                .firstOrError();


    }

    private Single<List<RulesetModel>> loadRemoteRulesets(Map<String, RulesetModel> cachedRulesets, List<RulesetModel> res) {


        return rulesetWebServiceRx.getRuleSetList()
                .map(rulesetLightWs -> {
                    Map<String, RulesetModel> remoteRulesets = wsToMap(rulesetLightWs);
                    Set<String> cachedRulesetsReferences = cachedRulesets.keySet();
                    Set<String> remoteRulesetsReferences = remoteRulesets.keySet();

                    Set<String> intersection = CollectionUtils.intersection(cachedRulesetsReferences, remoteRulesetsReferences);
                    for (String ref : intersection) {
                        if (remoteRulesets.get(ref).isSameAs(cachedRulesets.get(ref))) {
                            remoteRulesets.remove(ref);
                        } else if (remoteRulesets.get(ref).isRemoteNewerThanCached(cachedRulesets.get(ref))) {
                            remoteRulesets.get(ref).setStat(RuleSetStat.OFFLINE_WITH_NEW_VERSION);
                        }
                    }

                    res.addAll(remoteRulesets.values());
                    return res;
                });

    }

    public RulesetModel findDefaultRuleset() {
        RulesetModel output = null;
        if (defaultRulesetPreferences.checkRulesetExists()) {
            output = defaultRulesetPreferences.retrieveRuleset();
        }
        return output;
    }

    public Single<RulesetModel> getRulesetByReference(String reference) {
        return ocaraDB.rulesetDAO().getLatestRulesetByReference(reference)
                .map(rulesetDetails -> {
                    return RulesetModel.newRuleSetInfo(rulesetDetails);
                });
    }


    public void saveIcons(RulesetWs newRuleset) {
        List<WithIcon> illustrableItems = ListUtils.newArrayList();
        illustrableItems.addAll(newRuleset.getEquipments());
        illustrableItems.addAll(newRuleset.getProfileTypes());
        illustrableItems.addAll(newRuleset.getIllustrations());

        for (WithIcon item : illustrableItems) {
            String filename = item.getIcon();
            if (filename != null && !filename.isEmpty() && !imageCache.fileExists(filename)) {
                imageRemote.get(filename, new okhttp3.Callback() {
                    @Override
                    public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                        imageCache.createNewFile(filename, "");
                    }

                    @Override
                    public void onResponse(@NotNull okhttp3.Call call, @NotNull okhttp3.Response response) throws IOException {
                        if (response.body().byteStream().available() <= 0) {
                            imageCache.createNewFile(filename, "");
                        } else {
                            imageCache.write(response.body().byteStream(), filename);
                            Timber.d("image saved");
                        }
                    }
                });
            }
        }
    }

    public Completable saveImage(RulesetWs rulesetWs) {
        List<WithIcon> illustrableItems = ListUtils.newArrayList();
        illustrableItems.addAll(rulesetWs.getEquipments());
        illustrableItems.addAll(rulesetWs.getProfileTypes());
        illustrableItems.addAll(rulesetWs.getIllustrations());
        /*
            removing from the list while iterating it causes a concurrent modification exception
            so we add the elements to be removed to other list and then we remove the new list
            from the original list
         */
        List<WithIcon> itemsToRemove = ListUtils.newArrayList();
        for (WithIcon item : illustrableItems) {
            String filename = item.getIcon();
            if (filename == null || filename.isEmpty() || imageCache.fileExists(filename)) {
                itemsToRemove.add(item);
            }
        }
        illustrableItems.removeAll(itemsToRemove);
        return Observable.fromIterable(illustrableItems)
                .flatMap(withIcon -> {
                    String filename = withIcon.getIcon();
                    return imageRemoteApi.downloadImage(filename)
                            .map(responseBody -> {
                                if (responseBody.isSuccessful() && responseBody.body().byteStream() != null) {
                                    Timber.d("image saved");
                                    imageCache.write(responseBody.body().byteStream(), filename);
                                }
                                return withIcon;
                            }).doOnError(Throwable::printStackTrace);
                }).ignoreElements();
    }

    public Single<List<RulesetModel>> findDownloadedWithRxJava() {
        return ocaraDB.rulesetDAO().getRulesets().map(rulesetDetails -> {
            // now we all have all the rulesets in the DB
            // but we want to take the duplicates with different version
            HashMap<String, RulesetDetails> referenceToRuleset = new HashMap<>();
            for (RulesetDetails rulset : rulesetDetails) {
                if (referenceToRuleset.containsKey(rulset.getReference())) {
                    RulesetDetails curRulesetInMap = referenceToRuleset.get(rulset.getReference());
                    if (rulset.getVersion() > curRulesetInMap.getVersion()) {
                        referenceToRuleset.put(rulset.getReference(), rulset);
                    }
                } else {
                    referenceToRuleset.put(rulset.getReference(), rulset);
                }
            }
            List<RulesetModel> rulesetModels = new ArrayList<>();
            for (RulesetDetails rulesetDetail : referenceToRuleset.values()) {
                rulesetModels.add(RulesetModel.newRuleSetInfo(rulesetDetail));
            }
            return rulesetModels;
        });
    }

    public Completable saveInRoom(VersionableModel target) {
        return Single.create((SingleOnSubscribe<RulesetWs>) emitter -> {
            Timber.d("info = " + target.getReference() + " " + target.getVersion());
            RulesetWs rulesetWs = rulesetRemote.findOne(target.getReference(), Integer.parseInt(target.getVersion()));
            emitter.onSuccess(rulesetWs);
        }).toObservable()
                .concatMap(rulesetWs -> {
                    saveIcons(rulesetWs);
                    return saveInRoom(rulesetWs).toObservable();
                }).ignoreElements();
    }

    public Completable saveInRoomWithRx(VersionableModel target) {

//        Observable<RulesetWs> rulesetWsObservable =
        return rulesetWebServiceRx.getRulsetDetail(target.getReference(), Integer.parseInt(target.getVersion()))
                .doOnSuccess(rulesetWs ->
                        Timber.d("info = " + rulesetWs.getReference() + " " + rulesetWs.getVersion()))
                .doOnError(e -> Timber.e(e, "ErrorMessage=Error encountered while retrieving Rulesets from remote server."))
                .toObservable()


                .concatMapCompletableDelayError(rulesetWs -> {
                    Completable completable = saveImage(rulesetWs);
                    completable = completable.concatWith(saveInRoom(rulesetWs));
                    return completable;
                });


    }


    private Completable insertRelationBetweenRulesAndImpactValueAndProfileType(RulesetWs input) {
        List<RuleWs> rules = input.getRules();
        List<RuleProfileTypeImpactCrossRef> ruleProfileTypeImpactList = new ArrayList<>();
        for (RuleWs ruleWs : rules) {
            for (RuleImpactWs ruleImpactWs : ruleWs.getRuleImpacts()) {
                RuleProfileTypeImpactCrossRef ruleProfileTypeImpactCrossRef =
                        new RuleProfileTypeImpactCrossRef.Builder().setRulesetRef(input.getReference()).setRuleRef(ruleWs.getReference()).setRuleImpactRef(ruleImpactWs.getImpactValueRef()).setProfileRef(ruleImpactWs.getProfileTypeRef()).setVersion(input.getVersion()).createRuleProfileTypeImpactCrossRef();
                ruleProfileTypeImpactList.add(ruleProfileTypeImpactCrossRef);
            }
        }
        return ocaraDB.ruleProfileTypeImpactDAO().insert(ruleProfileTypeImpactList);
    }

    private Completable insertImpactValueInRoom(RulesetWs input) {
        List<ImpactValueWs> impactValues = input.getImpactValues();
        List<ImpactValue> impactValuesList = new ArrayList<>();
        List<ImpactValueRulesetCrossref> impactValuesRulesetList = new ArrayList<>();
        for (ImpactValueWs impactValueWs : impactValues) {
            ImpactValue impactValue = new ImpactValue(impactValueWs.getReference(),
                    impactValueWs.getName(),
                    impactValueWs.getEditable());
            impactValuesList.add(impactValue);
            impactValuesRulesetList.add(new ImpactValueRulesetCrossref(input.getReference(), impactValue.getReference(), input.getVersion()));
        }
        return ocaraDB.impactValueDao().insert(impactValuesList).concatWith(ocaraDB.impactValueRulesetDao().insert(impactValuesRulesetList));
    }

    private Completable insertProfileTypesInRoom(RulesetWs input) {
        List<ProfileTypeWs> profileTypes = input.getProfileTypes();
        List<ProfileType> profileTypeList = new ArrayList<>();

        List<ProfileTypeRulesetCrossref> profileTypeRulesetList = new ArrayList<>();

        for (ProfileTypeWs profileTypeWs : profileTypes) {
            ProfileType profileType = new ProfileType(
                    profileTypeWs.getName(),
                    profileTypeWs.getReference(),
                    profileTypeWs.getIcon()/*,
                    input.getReference(),
                    input.getVersion()*/);
            profileTypeList.add(profileType);

            profileTypeRulesetList.add(new ProfileTypeRulesetCrossref(input.getReference(), profileTypeWs.getReference(), input.getVersion()));
        }
        return ocaraDB.profileTypeDao().insert(profileTypeList).concatWith(ocaraDB.profileTypeRulesetDao().insert(profileTypeRulesetList));
    }

    private Completable insertRelationBetweenQuestionsAndRules(RulesetWs input) {
        List<QuestionWs> questions = input.getQuestions();
        List<QuestionsRulesCrossRef> questionsRulesList = new ArrayList<>();
        for (QuestionWs question : questions) {
            for (String ruleRef : question.getRulesReferences()) {
                QuestionsRulesCrossRef questionsRulesCrossRef =
                        new QuestionsRulesCrossRef(question.getReference(),
                                ruleRef, input.getReference(), input.getVersion());
                questionsRulesList.add(questionsRulesCrossRef);
            }
        }
        return ocaraDB.questionRulesDAO().insert(questionsRulesList);
    }

    private Completable insertRelationBetweenObjectAndQuestions(RulesetWs input) {
        HashMap<String, QuestionnaireWs> refToQuestionnaireWs = new HashMap<>();
        for (QuestionnaireWs questionnaireWs : input.getQuestionnaires()) {
            refToQuestionnaireWs.put(questionnaireWs.getReference(), questionnaireWs);
        }
        List<QuestionsEquipmentsCrossRef> questionsEquipmentsCrossRefs = new ArrayList<>();
        for (QuestionnaireWs questionnaireWs : input.getQuestionnaires()) {
            String equipmentRef = questionnaireWs.getObjectDescriptionRef();
            for (ChainWs chainWs : questionnaireWs.getChains()) {
                for (String questionRef : chainWs.getQuestionsRef()) {
                    QuestionsEquipmentsCrossRef questionsEquipmentsCrossRef =
                            new QuestionsEquipmentsCrossRef.QuestionsEquipmentsBuilder().setQuestionRef(questionRef).setObjectReference(equipmentRef).setRulesetRef(input.getReference()).setRulesetVer(input.getVersion()).createQuestionsEquipmentsCrossRef();
                    questionsEquipmentsCrossRefs.add(questionsEquipmentsCrossRef);
                }
            }
        }
        return ocaraDB.questionEquipmentDAO().insert(questionsEquipmentsCrossRefs);
    }

    private Completable insertQuestionsInRoom(RulesetWs input) {
        List<QuestionWs> questions = input.getQuestions();
        List<Question> questionList = new ArrayList<>();
        for (QuestionWs questionWs : questions) {
            Question question = new Question(questionWs.getReference(),
                    questionWs.getLabel(),
                    questionWs.getState(),
                    questionWs.getCreationDate(),
                    questionWs.getSubject().getName());
            questionList.add(question);
        }
        return ocaraDB.questionDao().insert(questionList);
    }

    private Completable insertRuleIllustrations(RulesetWs input) {
        List<RuleWs> rules = input.getRules();
        List<RuleWithIllustrations> ruleWithIllustrations = new ArrayList<>();
        for (RuleWs ruleWs : rules) {
            for (String ill : ruleWs.getIllustration()) {
                RuleWithIllustrations ruleWithIllustration = new RuleWithIllustrations.RuleWithIllustrationBuilder().setRuleRef(ruleWs.getReference()).setIllustRef(ill).setVersion(input.getVersion()).setRulesetRef(input.getReference()).createRuleWithIllustrations();
                ruleWithIllustrations.add(ruleWithIllustration);
            }
        }
        return ocaraDB.ruleWithIllustrationsDAO().insert(ruleWithIllustrations);
    }

    private Completable insertRulesInRoom(RulesetWs input) {
        List<RuleWs> rules = input.getRules();
        List<Rule> rulesRoom = new ArrayList<>();
        for (RuleWs ruleWs : rules) {
            Rule rule = new Rule(ruleWs.getReference()
                    , ruleWs.getLabel()
                    , ruleWs.getOrigin()
                    , ruleWs.getDate());
            rulesRoom.add(rule);
        }
        return ocaraDB.ruleDao().insert(rulesRoom);
    }

    private Completable insertObjectIllustrations(RulesetWs input) {
        List<EquipmentWs> equipmentWs = input.getEquipments();
        List<EquipmentWithIllustrations> equipmentWithIllustrationsList = new ArrayList<>();
        for (EquipmentWs equipment : equipmentWs) {
            for (String ill : equipment.getIllustration()) {
                EquipmentWithIllustrations equipmentWithIllustrations = new
                        EquipmentWithIllustrations(equipment.getReference(), ill
                        , input.getVersion(), input.getReference());
                equipmentWithIllustrationsList.add(equipmentWithIllustrations);
            }
        }
        return ocaraDB.objectWithIllustrationsDAO().insert(equipmentWithIllustrationsList);
    }

    private Completable insertIllustrationsInRoom(RulesetWs input) {
        List<IllustrationWs> illustrations = input.getIllustrations();
        List<Illustration> illustrationsRoom = new ArrayList<>();
        for (IllustrationWs illustration : illustrations) {
            Illustration illustrationRoom = new Illustration(illustration.reference,
                    illustration.image, illustration.comment, illustration.date);
            illustrationsRoom.add(illustrationRoom);
        }
        return ocaraDB.illustrationDao().insert(illustrationsRoom);
    }

    private Completable insertEquipmentsInRoom(RulesetWs input) {
        List<EquipmentWs> equipments = input.getEquipments();
        List<Equipment> equipmentsRoom = new ArrayList<>();
        for (EquipmentWs equipment : equipments) {
            Equipment equipmentRoom = new Equipment(equipment.getReference()
                    , equipment.getName(), equipment.getIcon()
                    , equipment.getType(), equipment.getDefinition()
                    , equipment.getDate());
            equipmentsRoom.add(equipmentRoom);
        }
        return ocaraDB.equipmentDao().insert(equipmentsRoom);
    }

    private Completable insertEquipmentsVersions(RulesetWs input) {
        List<EquipmentRulesetVersion> equipmentRulesetVersions = new ArrayList<>();
        for (EquipmentWs equipment : input.getEquipments()) {
            equipmentRulesetVersions.add(new EquipmentRulesetVersion.Builder().setObjRef(equipment.getReference()).setRulesetRef(input.getReference()).setVersion(input.getVersion()).createEquipmentRulesetVersion());
        }
        return ocaraDB.equipmentRulesetVersionDAO().insert(equipmentRulesetVersions);
    }

    private Completable insertEquipmentSubEquipmentInRoom(RulesetWs input) {
        List<EquipmentWs> equipments = input.getEquipments();
        List<EquipmentSubEquipmentCrossRef> equipmentSubEquipmentCrossRefs = new ArrayList<>();
        for (EquipmentWs equipment : equipments) {
            for (String subobject : equipment.getSubObject()) {
                equipmentSubEquipmentCrossRefs
                        .add(new EquipmentSubEquipmentCrossRef
                                (equipment.getReference(), subobject
                                        , input.getReference()
                                        , input.getVersion()));
            }
        }
        return ocaraDB.equipmentSubEquipmentDAO().insert(equipmentSubEquipmentCrossRefs);
    }

    private Completable insertObjectCategoriesInRoom(RulesetWs input) {
        List<QuestionnaireGroupWs> questionnaireGroupWsList = input.getQuestionnaireGroups();
        List<EquipmentCategory> equipmentCategories = new ArrayList<>();
        for (QuestionnaireGroupWs questionnaireGroupWs : questionnaireGroupWsList) {
            EquipmentCategory equipmentCategory = new EquipmentCategory(questionnaireGroupWs.getName(), input.getReference(), input.getVersion());
            equipmentCategories.add(equipmentCategory);
        }
        return ocaraDB.categoryDao().insert(equipmentCategories)
                .toObservable()
                .concatMap((Function<List<Long>, ObservableSource<List<EquipmentCategory>>>) ids -> {
                    return ocaraDB.categoryDao().getCategories(ids).toObservable();
                })
                .concatMap(categories -> {
                    Timber.d("categories size = " + categories.size());
                    HashMap<String, Integer> nameToCategory = getNameToCategory(categories);
                    List<EquipmentAndCategoryCrossRef> equipmentAndCategoryList = getEquipmentAndCategoryCrossRefs(input, nameToCategory);
                    return ocaraDB.equipmentAndCategoryRelationDAO().insert(equipmentAndCategoryList).toObservable();
                })
                .ignoreElements();
    }

    @NotNull
    private List<EquipmentAndCategoryCrossRef> getEquipmentAndCategoryCrossRefs(RulesetWs input, HashMap<String, Integer> nameToCategory) {
        List<EquipmentAndCategoryCrossRef> equipmentAndCategoryList = new ArrayList<>();
        for (QuestionnaireGroupWs questionnaireGroupWs : input.getQuestionnaireGroups()) {
            int id = nameToCategory.get(questionnaireGroupWs.getName());
            for (String obj : questionnaireGroupWs.getObjectRef()) {
                equipmentAndCategoryList.add(new
                        EquipmentAndCategoryCrossRef(obj, id));
            }
        }
        return equipmentAndCategoryList;
    }

    @NotNull
    private HashMap<String, Integer> getNameToCategory(List<EquipmentCategory> equipmentCategories) {
        HashMap<String, Integer> nameToCategory = new HashMap<>();
        for (EquipmentCategory equipmentCategory : equipmentCategories) {
            nameToCategory.put(equipmentCategory.getName(), equipmentCategory.getId());
        }
        return nameToCategory;
    }

    private Completable insertRulesetDetailsInRoom(RulesetWs input) {
//        RulesetDetails rulesetDetails = new RulesetDetails.Builder()
//                .setAuthorName(input.getUserCredentials().getUsername())
//                .setComment(input.getComment())
//                .setDate(input.getDate())
//                .setLanguage(input.getLanguage())
//                .setRuleSetStat(RuleSetStat.OFFLINE)
//                .setType(input.getType())
//                .setReference(input.getReference())
//                .setVersion(input.getVersion())
//                .build();
        RulesetDetails rulesetDetails = new RulesetDetails();
        rulesetDetails.setAuthorName(input.getUserCredentials().getUsername());
        rulesetDetails.setComment(input.getComment());
        rulesetDetails.setDate(input.getDate());
        rulesetDetails.setLanguage(input.getLanguage());
        rulesetDetails.setRuleSetStat(RuleSetStat.OFFLINE);
        rulesetDetails.setType(input.getType());
        rulesetDetails.setReference(input.getReference());
        rulesetDetails.setVersion(input.getVersion());
        rulesetDetails.setIsDemo(input.getIsDemo() ? 1 : 0);

        return ocaraDB.rulesetDAO().insert(rulesetDetails);
    }
}
