/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2020 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.ui.model;

import com.orange.ocara.data.cache.model.ResponseModel;
import com.orange.ocara.data.net.model.ImpactValueEntity;
import com.orange.ocara.data.net.model.RuleImpactEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import timber.log.Timber;

@Data
public class AccessibilityStatsUiModel {

    public static final String NA_IMPACT_REF = "1";

    private Map<String, Integer> mImpactRefAndScoreMap = new HashMap<>();
    private Integer mControleOkScore = 0;
    private Integer mControleDoubtScore = 0;
    private Integer mNoAnswerScore = 0;
    private Integer mAnnoyingScore = 0;
    private Integer mBlockingScore = 0;

    public void compute(ResponseModel response, List<RuleImpactEntity> ruleImpacts) {
        if (ruleImpacts != null) {

            for (RuleImpactEntity ruleImpact : ruleImpacts) {
                Timber.i(
                        "Message=Updating AccessibilityStats for RuleAnswer;CurrentResponse=%s;HandicapId=%s;RuleImpactId=%s",
                        response, ruleImpact.getProfileTypeRef(), ruleImpact.getImpactValueRef());
                String impactValueRef = ruleImpact.getImpactValueRef();

                if (!NA_IMPACT_REF.equals(impactValueRef)) {
                    if (ResponseModel.OK.equals(response)) {
                        Timber.i(
                                "Message=Adding OK to AccessibilityStats;CurrentResponse=%s;HandicapId=%s;RuleImpactId=%s",
                                response, ruleImpact.getProfileTypeRef(), ruleImpact.getImpactValueRef());
                        mControleOkScore++;
                    } else if (ResponseModel.DOUBT.equals(response)) {
                        Timber.i(
                                "Message=Adding DOUBT to AccessibilityStats;CurrentResponse=%s;HandicapId=%s;RuleImpactId=%s",
                                response, ruleImpact.getProfileTypeRef(), ruleImpact.getImpactValueRef());
                        mControleDoubtScore++;
                    } else if (ResponseModel.NO_ANSWER.equals(response)) {
                        Timber.i(
                                "Message=Adding NO_ANSWER to AccessibilityStats;CurrentResponse=%s;HandicapId=%s;RuleImpactId=%s",
                                response, ruleImpact.getProfileTypeRef(), ruleImpact.getImpactValueRef());
                        mNoAnswerScore++;
                    } else if (!ResponseModel.NOT_APPLICABLE.equals(response)) {

                        if (mImpactRefAndScoreMap.containsKey(impactValueRef)) {
                            int currentScore = mImpactRefAndScoreMap.get(impactValueRef);
                            mImpactRefAndScoreMap.put(impactValueRef, currentScore + 1);
                        } else {
                            mImpactRefAndScoreMap.put(impactValueRef, 1);
                        }

                        Timber.i(
                                "Message=Adding OTHERS to AccessibilityStats;CurrentResponse=%s;HandicapId=%s;RuleImpactId=%s",
                                response, ruleImpact.getProfileTypeRef(), ruleImpact.getImpactValueRef());

                        if ("2".equals(impactValueRef)) {
                            mAnnoyingScore++;
                        } else if ("3".equals(impactValueRef)) {
                            mBlockingScore++;
                        }
                    }
                } else {
                    Timber.i(
                            "Message=Updating AccessibilityStats for RuleAnswer is not applicable here;CurrentResponse=%s;HandicapId=%s;RuleImpactId=%s",
                            response, ruleImpact.getProfileTypeRef(), ruleImpact.getImpactValueRef());
                }
            }
        }
    }

    public void plus(AccessibilityStatsUiModel stats) {
        mControleOkScore += stats.getMControleOkScore();
        mControleDoubtScore += stats.getMControleDoubtScore();
        mNoAnswerScore += stats.getMNoAnswerScore();
        mAnnoyingScore += stats.getMAnnoyingScore();
        mBlockingScore += stats.getMBlockingScore();

        for (String reference : stats.getMImpactRefAndScoreMap().keySet()) {
            if (mImpactRefAndScoreMap.containsKey(reference)) {
                int currentScore = mImpactRefAndScoreMap.get(reference);
                mImpactRefAndScoreMap.put(reference, currentScore + stats.getMImpactRefAndScoreMap().get(reference));
            } else {
                mImpactRefAndScoreMap.put(reference, stats.getMImpactRefAndScoreMap().get(reference));
            }
        }
    }

    public int getCounter(ImpactValueEntity impactValue) {
        String reference = impactValue.getReference();
        if (mImpactRefAndScoreMap.containsKey(reference)) {
            return mImpactRefAndScoreMap.get(reference);
        } else {
            return 0;
        }
    }
}
