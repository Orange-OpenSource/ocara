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

package com.orange.ocara.ui.intent.export.docx;

import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.cache.model.ResponseModel;
import com.orange.ocara.data.net.model.ProfileTypeEntity;
import com.orange.ocara.ui.model.AccessibilityStatsUiModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Value;
import timber.log.Timber;

@Value
@EqualsAndHashCode(callSuper = true)
public class AuditStatsPresenter extends Presenter<AuditEntity> {

    /**
     * This class is used to bind audit results with the generated word document
     */

    private final Map<String, AccessibilityStatsUiModel> statsByHandicapId = new LinkedHashMap<>();
    private final Map<String, ProfileTypeEntity> handicapById = new LinkedHashMap<>();

    public AuditStatsPresenter(AuditEntity audit, final Map<String, ProfileTypeEntity> profilTypeFormRuleSet) {
        super(audit);

        aggregateStatsByHandicaps(audit, profilTypeFormRuleSet);
    }

    public List<ProfileTypePresenter> getAccessibleHandicapsWithTotal() {
        return buildHandicaps(ResponseModel.OK, true);
    }

    public List<ProfileTypePresenter> getAccessibleHandicaps() {
        return buildHandicaps(ResponseModel.OK, false);
    }

    public List<ProfileTypePresenter> getAnnoyingHandicapsWithTotal() {
        return buildHandicaps(ResponseModel.ANNOYING, true);
    }

    public List<ProfileTypePresenter> getAnnoyingHandicaps() {
        return buildHandicaps(ResponseModel.ANNOYING, false);
    }

    public List<ProfileTypePresenter> getNotAccessibleHandicapsWithTotal() {
        return buildHandicaps(ResponseModel.BLOCKING, true);
    }

    public List<ProfileTypePresenter> getNotAccessibleHandicaps() {
        return buildHandicaps(ResponseModel.BLOCKING, false);
    }

    public List<ProfileTypePresenter> getUnansweredHandicaps() {
        return buildHandicaps(ResponseModel.NO_ANSWER, false);
    }

    public List<ProfileTypePresenter> getUnansweredHandicapsWithTotal() {
        return buildHandicaps(ResponseModel.NO_ANSWER, true);
    }

    public List<ProfileTypePresenter> getDoubtHandicapsWithTotal() {
        List<ProfileTypePresenter> ret = buildHandicaps(ResponseModel.DOUBT, true);
        if (ret.get(0).getStat() == 0) {
            ret.clear();
        }
        return ret;
    }

    public List<ProfileTypePresenter> getDoubtHandicaps() {
        return buildHandicaps(ResponseModel.DOUBT, false);
    }

    public int getHandicapCount() {
        return handicapById.size();
    }

    private List<ProfileTypePresenter> buildHandicaps(ResponseModel response, boolean withTotal) {

        List<ProfileTypePresenter> profileTypePresenters = new ArrayList<>();

        int index = 0;
        int total = 0;

        ProfileTypePresenter totalProfileTypePresenter = null;

        if (withTotal) {
            ProfileTypeEntity totalHandicap = new ProfileTypeEntity();
            totalHandicap.setName("Tous-profils");
            totalProfileTypePresenter = new ProfileTypePresenter(totalHandicap, total, index++);
            profileTypePresenters.add(totalProfileTypePresenter);
        }

        for (Map.Entry<String, AccessibilityStatsUiModel> entry : statsByHandicapId.entrySet()) {
            ProfileTypeEntity handicap = handicapById.get(entry.getKey());
            AccessibilityStatsUiModel stat = entry.getValue();
            int score = computeScore(response, stat);

            Timber.d("Message=Computing score;Response=%s;HandicapId=%s", response, entry.getKey());
            profileTypePresenters.add(new ProfileTypePresenter(handicap, score, index++));
            total += score;
        }

        if (withTotal) {
            totalProfileTypePresenter.setStat(total);
        }

        return profileTypePresenters;
    }

    private int computeScore(ResponseModel response, AccessibilityStatsUiModel stat) {
        int score = 0;

        switch (response) {
            case OK:
                score = stat.getMControleOkScore();
                break;
            case NOK:
                score = stat.getMBlockingScore() + stat.getMAnnoyingScore();
                break;
            case ANNOYING:
                score = stat.getMAnnoyingScore();
                break;
            case BLOCKING:
                score = stat.getMBlockingScore();
                break;
            case DOUBT:
                score = stat.getMControleDoubtScore();
                break;
            case NO_ANSWER:
                score = stat.getMNoAnswerScore();
                break;
            default:
                break;
        }
        return score;
    }

    private void aggregateStatsByHandicaps(AuditEntity audit, final Map<String, ProfileTypeEntity> profileTypeFormRuleSet) {
        statsByHandicapId.clear();
        handicapById.clear();

        statsByHandicapId.putAll(audit.computeStatsByHandicap(profileTypeFormRuleSet));
        handicapById.putAll(profileTypeFormRuleSet);
    }
}
