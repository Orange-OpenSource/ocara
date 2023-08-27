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

import com.orange.ocara.data.cache.database.NonTables.RuleWithObjName;
import com.orange.ocara.domain.models.ImpactValueModel;
import com.orange.ocara.domain.models.ProfileTypeModel;
import com.orange.ocara.domain.models.RuleImpactModel;
import com.orange.ocara.domain.models.RuleModel;
import com.orange.ocara.data.cache.database.DAOs.RuleAnswerDAO;
import com.orange.ocara.data.cache.database.DAOs.RuleDAO;
import com.orange.ocara.data.cache.database.DAOs.RuleProfileTypeImpactDAO;
import com.orange.ocara.data.cache.database.NonTables.RuleWithIllustrationsCount;
import com.orange.ocara.data.cache.database.NonTables.RuleWithProfiletypeImpact;
import com.orange.ocara.data.cache.database.OcaraDB;
import com.orange.ocara.data.cache.database.Tables.Rule;
import com.orange.ocara.data.cache.database.crossRef.RuleProfileTypeImpactCrossRef;
import com.orange.ocara.domain.models.RuleWithEquipmentModel;

import org.apache.commons.collections4.map.HashedMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import timber.log.Timber;

public class RuleRepository {
    RuleDAO ruleDAO;
    RuleAnswerDAO ruleAnswerDAO;
    RuleProfileTypeImpactDAO ruleProfileTypeImpactDAO;

    @Inject
    public RuleRepository(OcaraDB ocaraDB) {
        this.ruleDAO = ocaraDB.ruleDao();
        this.ruleAnswerDAO = ocaraDB.ruleAnswerDAO();
        ruleProfileTypeImpactDAO = ocaraDB.ruleProfileTypeImpactDAO();
    }

    public Single<List<RuleModel>> findRulesByQuestion(String questionRef, String rulesetRef, int rulesetVersion) {
        return ruleDAO.getRulesWithQuestionAndRuleset(questionRef, rulesetRef, rulesetVersion).map(rules -> {
            Timber.d(questionRef + " " + rulesetRef + " " + rulesetVersion + " rules = " + rules.size());
            List<RuleModel> ruleModels = new ArrayList<>();
            int i = 0;
            for (RuleWithIllustrationsCount rule : rules) {
                ruleModels.add(new RuleModel(rule.getRule().getReference()
                        , rule.getRule().getLabel()
                        , questionRef, i++, rule.getIllustrations() > 0));
            }
            return ruleModels;
        });
    }

    public Single<List<RuleModel>> findRulesByQuestions(List<String> questionsRef, String rulesetRef, int rulesetVersion) {
        return ruleDAO.getRulesWithQuestionAndRuleset(questionsRef, rulesetRef, rulesetVersion).map(rules -> {
            Timber.d(questionsRef + " " + rulesetRef + " " + rulesetVersion + " rules = " + rules.size());
            List<RuleModel> ruleModels = new ArrayList<>();
            int i = 0;
            for (RuleWithIllustrationsCount rule : rules) {
                ruleModels.add(new RuleModel(rule.getRule().getReference()
                        , rule.getRule().getLabel()
                        , rule.getQuestionRef(), i++, rule.getIllustrations() > 0));
            }
            return ruleModels;
        });
    }

    public Single<List <RuleWithEquipmentModel>> findRulesWithEquipments( String rulesetRef, String rulesetVersion) {
        return ruleDAO.getRulesWithObjNames( rulesetRef,rulesetVersion).map(rules -> {
            Map <String , RuleWithEquipmentModel> ruleModelsMap = new HashedMap();
//            List<RuleWithEquipmentModel> ruleModels = new ArrayList<>();
            int i = 0;
            for (RuleWithObjName rule : rules) {
                String ruleRef= rule.getRule().getReference();
                if (ruleModelsMap.get(ruleRef) != null ){
                    ruleModelsMap.get(ruleRef).addEquipmentName(rule.getEquipment_name());
                }else{
                    RuleModel ruleModel = new RuleModel(rule.getRule());
                    ruleModel.setIllustrated(rule.getIllustrationsCount()>0);
                    ruleModelsMap.put( ruleRef , (new RuleWithEquipmentModel(ruleModel , rule.getEquipment_name())));
                }
            }
            return new ArrayList(ruleModelsMap.values());
        });
    }

    public Single<List<RuleImpactModel>> getRuleWithImpactValueForRule(String rulesetRef,int rulesetVersion, String ruleRef) {
        return ruleDAO.getRuleWithImpactValueForRule(rulesetRef,rulesetVersion, ruleRef).map(rules -> {
            List<RuleImpactModel> ruleModels = new ArrayList<>();
            for (RuleWithProfiletypeImpact rule : rules) {
                RuleModel ruleModel = new RuleModel(rule.getRule());
                ImpactValueModel impactValueModel = new ImpactValueModel(rule.getImpactValue().getReference(), rule.getImpactValue().getName(), rule.getImpactValue().isEditable());
                ProfileTypeModel profileTypeModel = new ProfileTypeModel(rule.getProfileType());

                ruleModels.add(new RuleImpactModel(impactValueModel, profileTypeModel, ruleModel));
            }
            return ruleModels;
        });
    }
    public Completable insertRules(List<Rule> rules){
        return ruleDAO.insert(rules);
    }
    public Completable insertRuleImpact(List<RuleProfileTypeImpactCrossRef> list){
        return ruleProfileTypeImpactDAO.insert(list);
    }
}
