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

import com.orange.ocara.domain.docexport.models.AccessibilityStatsUiModel;
import com.orange.ocara.domain.models.AuditEquipmentModel;
import com.orange.ocara.domain.models.AuditModel;
import com.orange.ocara.domain.models.ProfileTypeModel;
import com.orange.ocara.domain.models.QuestionAnswerModel;
import com.orange.ocara.domain.models.RuleAnswerModel;
import com.orange.ocara.domain.models.RuleImpactModel;
import com.orange.ocara.domain.models.RuleModel;
import com.orange.ocara.domain.repositories.RuleRepository;
import com.orange.ocara.utils.enums.Answer;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


//OLD TASK TO BE DEPRECATED
public class ComputeAuditStatsByHandicapTask {

    RuleRepository ruleRepository;

    @Inject
    public ComputeAuditStatsByHandicapTask(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    public Map<String, AccessibilityStatsUiModel> execute(AuditModel audit, Map<String, ProfileTypeModel> handicapById) {

        Map<String, AccessibilityStatsUiModel> result = new LinkedHashMap<>();

        // Initialize
        for (String handicapId : handicapById.keySet()) {
//            Timber.i("Message=Creating AccessibilityStats for audit;AuditId=%d", getId());
            result.put(handicapId, new AccessibilityStatsUiModel());
        }

        // Compute
        for (AuditEquipmentModel object : audit.getObjects()) {
//            Timber.i("Message=Updating AccessibilityStats for audit;AuditId=%d", getId());
            computeAuditObjectStatsByHandicap(audit, object, result);
        }

        return result;
    }

    private void computeAuditObjectStatsByHandicap(AuditModel audit, AuditEquipmentModel object, Map<String, AccessibilityStatsUiModel> result) {
//        Timber.i("Message=Updating AccessibilityStats for auditObject;AuditObjectId=%d", getId());
        for (QuestionAnswerModel qa : object.getQuestionsAnswers()) {
            computeQAStatsByHandicap(audit, qa, result);
        }

        for (AuditEquipmentModel characteristic : object.getChildren()) {
            computeAuditObjectStatsByHandicap(audit, characteristic, result);
        }

    }

    private void computeQAStatsByHandicap(AuditModel audit, QuestionAnswerModel qa, Map<String, AccessibilityStatsUiModel> result) {
//        Timber.i("Message=Updating AccessibilityStats for QuestionAnswer;QuestionAnswerId=%d", getId());
        final List<RuleAnswerModel> list = qa.getRuleAnswers();
        for (RuleAnswerModel ruleAnswer : list) {
            computeRuleAnsStatsByHandicap(audit, ruleAnswer, result);
        }
    }

    private void computeRuleAnsStatsByHandicap(AuditModel audit, RuleAnswerModel ruleAnswer, Map<String, AccessibilityStatsUiModel> result) {

        RuleModel ruleFromRef = ruleAnswer.getRule();
        List<RuleImpactModel> ruleImpactDb = ruleRepository.getRuleWithImpactValueForRule(audit.getRulesetRef(),audit.getRulesetVer(), ruleFromRef.getRef()).blockingGet();
        Map<String, List<RuleImpactModel>> ruleImpactMap = new HashMap<>();

        for (RuleImpactModel ruleImpact : ruleImpactDb) {
            String profileTypeRef = ruleImpact.getProfileType().getReference();
            if (!AccessibilityStatsUiModel.NA_IMPACT_REF.equals(ruleImpact.getImpactValue().getReference())) {
                if (ruleImpactMap.containsKey(profileTypeRef)) {
                    List<RuleImpactModel> profileTypeImpacts = ruleImpactMap.get(profileTypeRef);
                    profileTypeImpacts.add(ruleImpact);
                    ruleImpactMap.put(profileTypeRef, profileTypeImpacts);
                } else {
                    List<RuleImpactModel> profileTypeImpacts = new ArrayList<>();
                    profileTypeImpacts.add(ruleImpact);
                    ruleImpactMap.put(profileTypeRef, profileTypeImpacts);
                }
            }
        }

        final Answer currentResponse = ruleAnswer.getAnswer();
        List<RuleImpactModel> ruleImpacts;
        for (String handicapId : result.keySet()) {
            ruleImpacts = ruleImpactMap.get(handicapId);
            if (result.containsKey(handicapId)) {
//                result.get(handicapId).compute(currentResponse, ruleImpacts);
            }
        }
    }
}
