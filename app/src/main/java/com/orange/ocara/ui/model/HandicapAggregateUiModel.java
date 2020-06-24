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

import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.net.model.ProfileTypeEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * a UiModel that aggregates {@link AccessibilityStatsUiModel} and {@link ProfileTypeEntity}s
 */
public class HandicapAggregateUiModel {
    private final List<String> handicapRef = new ArrayList<>();

    private final Map<String, AccessibilityStatsUiModel> statsById = new LinkedHashMap<>();
    private final Map<String, ProfileTypeEntity> handicapById = new LinkedHashMap<>();

    private AccessibilityStatsUiModel totalStat;

    public void update(AuditEntity audit, Map<String, ProfileTypeEntity> handicapById) {
        handicapRef.clear();

        statsById.clear();
        this.handicapById.clear();

        statsById.putAll(audit.computeStatsByHandicap(handicapById));

        this.handicapById.putAll(handicapById);
        handicapRef.addAll(handicapById.keySet());

        computeTotal();
    }

    public void computeTotal() {
        totalStat = new AccessibilityStatsUiModel();

        for (Map.Entry<String, ProfileTypeEntity> entry : handicapById.entrySet()) {
            AccessibilityStatsUiModel stat = statsById.get(entry.getKey());

            if (stat == null) {
                stat = new AccessibilityStatsUiModel();
                statsById.put(entry.getKey(), stat);
            }

            totalStat.plus(stat);
        }
    }

    public List<String> getHandicapRef() {
        return handicapRef;
    }

    public ProfileTypeEntity getHandicap(int position) {
        return getHandicap(handicapRef.get(position));
    }

    public ProfileTypeEntity getHandicap(String handicapId) {
        return handicapById.get(handicapId);
    }

    public AccessibilityStatsUiModel getStat(int position) {
        return getStat(handicapRef.get(position));
    }

    public AccessibilityStatsUiModel getStat(String handicapId) {
        return statsById.get(handicapId);
    }

    public AccessibilityStatsUiModel getTotalStat() {
        return totalStat;
    }
}
