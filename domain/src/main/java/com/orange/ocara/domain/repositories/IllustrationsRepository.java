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

import android.annotation.SuppressLint;

import com.orange.ocara.data.cache.database.DAOs.IllustrationDAO;
import com.orange.ocara.data.cache.database.DAOs.RuleWithIllustrationDAO;
import com.orange.ocara.data.cache.database.NonTables.IllustrationWithRuleRef;
import com.orange.ocara.data.cache.database.OcaraDB;
import com.orange.ocara.data.cache.database.Tables.Illustration;
import com.orange.ocara.data.cache.database.crossRef.RuleWithIllustrations;
import com.orange.ocara.domain.models.IllustrationModel;
import com.orange.ocara.domain.models.RuleModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import timber.log.Timber;

public class IllustrationsRepository {
    IllustrationDAO illustrationDAO;
    RuleWithIllustrationDAO ruleWithIllustrationDAO;
    @Inject
    public IllustrationsRepository(OcaraDB ocaraDB) {
        this.illustrationDAO = ocaraDB.illustrationDao();
        this.ruleWithIllustrationDAO=ocaraDB.ruleWithIllustrationsDAO();
    }

    /*
        this method returns a completable the finishes when all the illustrations added to the
        rules corresponding to them
     */
    @SuppressLint("CheckResult")
    public Observable<List<IllustrationModel>> addIllustrationsToRules(List<RuleModel> rules, String rulesetRef, int rulesetVer) {
        Map<String, RuleModel> refToRuleModel = new HashMap<>();
        List<String> ruleRefs = new ArrayList<>();
        for (RuleModel ruleModel : rules) {
            refToRuleModel.put(ruleModel.getRef(), ruleModel);
            ruleRefs.add(ruleModel.getRef());
        }
        return illustrationDAO.getIllustrationsForRule(ruleRefs,rulesetRef,rulesetVer)
                .toObservable()
                .map(illustrationWithRuleRefs -> {
                    Timber.d("illustrations size = "+illustrationWithRuleRefs.size());
                    List<IllustrationModel> list=new ArrayList<>();
                    for (IllustrationWithRuleRef illustration : illustrationWithRuleRefs) {
                        IllustrationModel illustrationModel = new IllustrationModel(illustration.getIllustration());
                        refToRuleModel.get(illustration.getRuleRef())
                                .addIllustration(illustrationModel);
                        list.add(illustrationModel);
                    }
                    return list;
                });
    }

    public Single<List<IllustrationModel>> loadIllustrations(String ruleRef, String rulesetRef, int rulesetVersion) {

        return illustrationDAO.getIllustrations(ruleRef, rulesetRef, rulesetVersion)
                .map(this::convertIllustrationsToItsModel);
    }

    public Single<List<IllustrationModel>> loadIllustrations(String ruleRef , String ruleSetRef) {
        return illustrationDAO.getIllustrations(ruleRef,ruleSetRef)
                .map(this::convertIllustrationsToItsModel);
    }

    private List<IllustrationModel> convertIllustrationsToItsModel(List<Illustration> illustrations){
        List<IllustrationModel> modelList = new ArrayList<>();
        for (Illustration item : illustrations) {
            modelList.add(new IllustrationModel(item));
        }
        return modelList;
    }
    public Completable insert(List<Illustration> illustrations) {
        return illustrationDAO.insert(illustrations);
    }

    public Completable insertRuleIllutrations(List<RuleWithIllustrations> list) {
        return ruleWithIllustrationDAO.insert(list);
    }
}
