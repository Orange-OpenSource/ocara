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


import com.orange.ocara.domain.interactors.ComputeAuditStatsByHandicapForExportTask;
import com.orange.ocara.domain.interactors.ComputeAuditStatsByHandicapTask;
import com.orange.ocara.domain.interactors.GetAuditScoresTask;
import com.orange.ocara.domain.models.AuditModel;
import com.orange.ocara.domain.models.ProfileTypeModel;
import com.orange.ocara.domain.models.QuestionAnswerModel;
import com.orange.ocara.utils.enums.Answer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Value;
import timber.log.Timber;

@Value
@EqualsAndHashCode(callSuper = true)
public class AuditStatsPresenter extends Presenter<AuditModel> {

    /**
     * This class is used to bind audit results with the generated word document
     */

    private final Map<String, AccessibilityStatsUiModel> statsByHandicapId = new LinkedHashMap<>();
    private final Map<String, ProfileTypeModel> handicapById = new LinkedHashMap<>();

    ComputeAuditStatsByHandicapForExportTask computeAuditStatsByHandicapTask;

    public AuditStatsPresenter(ComputeAuditStatsByHandicapForExportTask computeAuditStatsByHandicapTask, AuditModel audit, final Map<String, ProfileTypeModel> profilTypeFormRuleSet) {
        super(audit);
        this.computeAuditStatsByHandicapTask = computeAuditStatsByHandicapTask;
        aggregateStatsByHandicaps(audit, profilTypeFormRuleSet);
    }

    public List<ProfileTypePresenter> getAccessibleHandicapsWithTotal() {
        return buildHandicaps(Answer.OK, true);
    }

    public List<ProfileTypePresenter> getAccessibleHandicaps() {
        return buildHandicaps(Answer.OK, false);
    }

    public List<ProfileTypePresenter> getAnnoyingHandicapsWithTotal() {
        return buildHandicaps(Answer.ANNOYING, true);
    }

    public List<ProfileTypePresenter> getAnnoyingHandicaps() {
        return buildHandicaps(Answer.ANNOYING, false);
    }

    public List<ProfileTypePresenter> getNotAccessibleHandicapsWithTotal() {
        return buildHandicaps(Answer.BLOCKING, true);
    }

    public List<ProfileTypePresenter> getNotAccessibleHandicaps() {
        return buildHandicaps(Answer.BLOCKING, false);
    }

    public List<ProfileTypePresenter> getUnansweredHandicaps() {
        return buildHandicaps(Answer.NO_ANSWER, false);
    }

    public List<ProfileTypePresenter> getUnansweredHandicapsWithTotal() {
        return buildHandicaps(Answer.NO_ANSWER, true);
    }

    public List<ProfileTypePresenter> getDoubtHandicapsWithTotal() {
        List<ProfileTypePresenter> ret = buildHandicaps(Answer.DOUBT, true);
//        if (ret.get(0).getStat() == 0) {
//            ret.clear();
//        }
        return ret;
    }

    public List<ProfileTypePresenter> getDoubtHandicaps() {
        return buildHandicaps(Answer.DOUBT, false);
    }

    public int getHandicapCount() {
        return handicapById.size();
    }

    private List<ProfileTypePresenter> buildHandicaps(Answer response, boolean withTotal) {

        List<ProfileTypePresenter> profileTypePresenters = new ArrayList<>();

        int index = 0;
        int total = 0;

        ProfileTypePresenter totalProfileTypePresenter = null;

        if (withTotal) {
            ProfileTypeModel totalHandicap = new ProfileTypeModel();
            totalHandicap.setName("Tous les profils");
            totalProfileTypePresenter = new ProfileTypePresenter(totalHandicap, total, index++);
            profileTypePresenters.add(totalProfileTypePresenter);
        }

        for (Map.Entry<String, AccessibilityStatsUiModel> entry : statsByHandicapId.entrySet()) {
            if (entry.getKey().equals(GetAuditScoresTask.ALL_DISABILITIES))
                continue;
            ProfileTypeModel handicap = handicapById.get(entry.getKey());
            AccessibilityStatsUiModel stat = entry.getValue();
            int score = computeScore(response, stat);

            Timber.d("Message=Computing score;Response=%s;HandicapId=%s", response, entry.getKey());
            profileTypePresenters.add(new ProfileTypePresenter(handicap, score, index++));
            total += score;
        }

        if (withTotal) {
            int tot = computeScore(response , statsByHandicapId.get(GetAuditScoresTask.ALL_DISABILITIES));
            totalProfileTypePresenter.setStat(tot);
        }

        return profileTypePresenters;
    }

    private int computeScore(Answer response, AccessibilityStatsUiModel stat) {
        int score = 0;

        switch (response) {
            case OK:
                score = stat.getControleOkScore();
                break;
            case NOK:
                score = stat.getBlockingScore() + stat.getAnnoyingScore();
                break;
            case ANNOYING:
                score = stat.getAnnoyingScore();
                break;
            case BLOCKING:
                score = stat.getBlockingScore();
                break;
            case DOUBT:
                score = stat.getControleDoubtScore();
                break;
            case NO_ANSWER:
                score = stat.getNoAnswerScore();
                break;
            default:
                break;
        }
        return score;
    }

    private void aggregateStatsByHandicaps(AuditModel audit, final Map<String, ProfileTypeModel> profileTypeFormRuleSet) {
        statsByHandicapId.clear();
        handicapById.clear();
//TODO audit.computeStatsByHandicap(profileTypeFormRuleSet)
        statsByHandicapId.putAll(computeAuditStatsByHandicapTask.execute(audit,profileTypeFormRuleSet));
        handicapById.putAll(profileTypeFormRuleSet);
    }
}
