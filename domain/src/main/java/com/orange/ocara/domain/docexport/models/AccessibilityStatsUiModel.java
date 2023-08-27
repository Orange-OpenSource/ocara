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

package com.orange.ocara.domain.docexport.models;


import com.orange.ocara.domain.models.ImpactValueModel;
import com.orange.ocara.domain.models.ProfileAnswersUIModel;
import com.orange.ocara.domain.models.QuestionAnswerModel;
import com.orange.ocara.domain.models.RuleImpactModel;
import com.orange.ocara.utils.enums.Answer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import timber.log.Timber;
//TODO
@Data
public class AccessibilityStatsUiModel {

    public static final String NA_IMPACT_REF = "1";

    private final Map<String, Integer> mImpactRefAndScoreMap = new HashMap<>();
    private Integer mControleOkScore = 0;
    private Integer mControleDoubtScore = 0;
    private Integer mNoAnswerScore = 0;
    private Integer mAnnoyingScore = 0;
    private Integer mBlockingScore = 0;


//    public void compute(Answer response, List<RuleImpactModel> ruleImpacts) {
//        if (ruleImpacts != null) {
//
//            for (RuleImpactModel ruleImpact : ruleImpacts) {
////                Timber.i(
////                        "Message=Updating AccessibilityStats for RuleAnswer;CurrentResponse=%s;HandicapId=%s;RuleImpactId=%s",
////                        response, ruleImpact.getProfileTypeRef(), ruleImpact.getImpactValueRef());
//                String impactValueRef = ruleImpact.getImpactValue().getReference();
//
//                if (!NA_IMPACT_REF.equals(impactValueRef)) {
//                    if (Answer.OK.equals(response)) {
//                        Timber.i(
//                                "Message=Adding OK to AccessibilityStats;CurrentResponse=%s;HandicapId=%s;RuleImpactId=%s",
//                                response, ruleImpact.getProfileType().getReference(), ruleImpact.getImpactValue().getReference());
//                        mControleOkScore++;
//                    } else if (Answer.DOUBT.equals(response)) {
//                        Timber.i(
//                                "Message=Adding DOUBT to AccessibilityStats;CurrentResponse=%s;HandicapId=%s;RuleImpactId=%s",
//                                response, ruleImpact.getProfileType().getReference(), ruleImpact.getImpactValue().getReference());
//                        mControleDoubtScore++;
//                    } else if (Answer.NO_ANSWER.equals(response)) {
//                        Timber.i(
//                                "Message=Adding NO_ANSWER to AccessibilityStats;CurrentResponse=%s;HandicapId=%s;RuleImpactId=%s",
//                                response, ruleImpact.getProfileType().getReference(), ruleImpact.getImpactValue().getReference());
//                        mNoAnswerScore++;
//                    } else if (!Answer.NOT_APPLICABLE.equals(response)) {
//
//                        if (mImpactRefAndScoreMap.containsKey(impactValueRef)) {
//                            int currentScore = mImpactRefAndScoreMap.get(impactValueRef);
//                            mImpactRefAndScoreMap.put(impactValueRef, currentScore + 1);
//                        } else {
//                            mImpactRefAndScoreMap.put(impactValueRef, 1);
//                        }
//
//                        Timber.i(
//                                "Message=Adding OTHERS to AccessibilityStats;CurrentResponse=%s;HandicapId=%s;RuleImpactId=%s",
//                                response, ruleImpact.getProfileType().getReference(), ruleImpact.getImpactValue().getReference());
//
//                        if ("2".equals(impactValueRef)) {
//                            mAnnoyingScore++;
//                        } else if ("3".equals(impactValueRef)) {
//                            mBlockingScore++;
//                        }
//                    }
//                } else {
//                    Timber.i(
//                            "Message=Updating AccessibilityStats for RuleAnswer is not applicable here;CurrentResponse=%s;HandicapId=%s;RuleImpactId=%s",
//                            response, ruleImpact.getProfileType().getReference(), ruleImpact.getImpactValue().getReference());
//                }
//            }
//        }
//    }

//    public void plus(AccessibilityStatsUiModel stats) {
//        mControleOkScore += stats.getControleOkScore();
//        mControleDoubtScore += stats.getControleDoubtScore();
//        mNoAnswerScore += stats.getNoAnswerScore();
//        mAnnoyingScore += stats.getAnnoyingScore();
//        mBlockingScore += stats.getBlockingScore();
//
//        for (String reference : stats.getImpactRefAndScoreMap().keySet()) {
//            if (mImpactRefAndScoreMap.containsKey(reference)) {
//                int currentScore = mImpactRefAndScoreMap.get(reference);
//                mImpactRefAndScoreMap.put(reference, currentScore + stats.getImpactRefAndScoreMap().get(reference));
//            } else {
//                mImpactRefAndScoreMap.put(reference, stats.getImpactRefAndScoreMap().get(reference));
//            }
//        }
//    }

//    public int getCounter(ImpactValueModel impactValue) {
//        String reference = impactValue.getReference();
//        if (mImpactRefAndScoreMap.containsKey(reference)) {
//            return mImpactRefAndScoreMap.get(reference);
//        } else {
//            return 0;
//        }
//    }

    public void setData(ProfileAnswersUIModel model){
        mControleOkScore = model.getNumberOfOk();
        mControleDoubtScore = model.getNumberOfDoubt();
        mNoAnswerScore = model.getNumberOfNoAns();
        mAnnoyingScore = model.getNumberOfNo().get("Gênant");
        mBlockingScore = model.getNumberOfNo().get("Bloquant");
    }
    public Integer getControleOkScore() {
        return mControleOkScore;
    }


    public Integer getControleDoubtScore() {
        return mControleDoubtScore;
    }


    public Integer getNoAnswerScore() {
        return mNoAnswerScore;
    }

    public Integer getAnnoyingScore() {
        return mAnnoyingScore;
    }


    public Integer getBlockingScore() {
        return mBlockingScore;
    }

//    public Map<String, Integer> getImpactRefAndScoreMap() {
//        return mImpactRefAndScoreMap;
//    }
}
