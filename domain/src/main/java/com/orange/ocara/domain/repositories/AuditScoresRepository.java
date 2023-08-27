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

import com.orange.ocara.domain.models.RuleModel;
import com.orange.ocara.data.cache.database.DAOs.ReportScoresDAO;
import com.orange.ocara.data.cache.database.NonTables.scoreCalc.AnswerCount;
import com.orange.ocara.data.cache.database.NonTables.scoreCalc.CountImpactName;
import com.orange.ocara.data.cache.database.NonTables.scoreCalc.NoAnsWithProfileAndImpact;
import com.orange.ocara.data.cache.database.NonTables.scoreCalc.YesDoubtNoAnsNAAnsWithprofileAndAns;
import com.orange.ocara.data.cache.database.OcaraDB;
import com.orange.ocara.data.cache.database.Tables.Rule;



import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class AuditScoresRepository {
    ReportScoresDAO reportScoresDAO;

    @Inject
    public AuditScoresRepository(OcaraDB ocaraDB) {
        reportScoresDAO = ocaraDB.reportScoresDAO();
    }

    public Single<List<YesDoubtNoAnsNAAnsWithprofileAndAns>> getNumberOfRulesAnsweredByYesOrDoubtOrNoAnsOrNAGrpByAnsAndProfileForAudit(Long auditId) {
        return reportScoresDAO.getNumberOfRulesAnsweredByYesOrDoubtOrNoAnsOrNAGrpByAnsAndProfileForAudit(auditId);
    }

    public Single<List<NoAnsWithProfileAndImpact>> getNumberOfRulesAnsweredByNoGrpByImpactAndProfileForAudit(Long auditId) {
        return reportScoresDAO.getNumberOfRulesAnsweredByNoGrpByImpactAndProfileForAudit(auditId);
    }

    public Single<List<AnswerCount>> getTotalNumRulesYesOrDoubtHasAnyImpact(Long auditId){
        return reportScoresDAO.getTotalNumRulesYesOrDoubtHasAnyImpact(auditId);
    }

    public Single<List<CountImpactName>> getTotalNumRulesNoHasAnyImpact(Long auditId){
        return reportScoresDAO.getTotalNumRulesNoHasAnyImpact(auditId);
    }

    public Single<List<RuleModel>> getAnomaliesForAuditEquipment(Long equipmentId){
        return reportScoresDAO.getAnomaliesAuditEquipment(equipmentId)
                .map(rules -> {
                    List<RuleModel> ruleModels = new ArrayList<>();
                    for (Rule rule: rules){
                        ruleModels.add(new RuleModel(rule));
                    }
                    return ruleModels;
                });
    }
}
