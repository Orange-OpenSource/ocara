/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model.export.docx;

import com.orange.ocara.model.AccessibilityStats;
import com.orange.ocara.model.Audit;
import com.orange.ocara.modelStatic.Handicap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class AuditStatsPresenter extends Presenter<Audit> {

    private final Map<String, AccessibilityStats> statsByHandicapId = new LinkedHashMap<String, AccessibilityStats>();
    private final Map<String, Handicap> handicapById = new LinkedHashMap<String, Handicap>();


    public AuditStatsPresenter(Audit audit) {
        super(audit);

        aggregateStatsByHandicaps(audit);
    }


    public List<HandicapPresenter> getAccessibleHandicapsWithTotal() {
        return buildHandicaps(AccessibilityStats.Type.ACCESSIBLE, true);
    }

    public List<HandicapPresenter> getAccessibleHandicaps() {
        return buildHandicaps(AccessibilityStats.Type.ACCESSIBLE, false);
    }

    public List<HandicapPresenter> getAnnoyingHandicapsWithTotal() {
        return buildHandicaps(AccessibilityStats.Type.ANNOYING, true);
    }

    public List<HandicapPresenter> getAnnoyingHandicaps() {
        return buildHandicaps(AccessibilityStats.Type.ANNOYING, false);
    }

    public List<HandicapPresenter> getBlockingHandicapsWithTotal() {
        return buildHandicaps(AccessibilityStats.Type.BLOCKING, true);
    }

    public List<HandicapPresenter> getBlockingHandicaps() {
        return buildHandicaps(AccessibilityStats.Type.BLOCKING, false);
    }

    public List<HandicapPresenter> getDoubtHandicapsWithTotal() {
        List<HandicapPresenter> ret = buildHandicaps(AccessibilityStats.Type.DOUBT, true);
        if (ret.get(0).getStat() == 0) {
            ret.clear();
        }
        return ret;
    }

    public List<HandicapPresenter> getDoubtHandicaps() {
        return buildHandicaps(AccessibilityStats.Type.DOUBT, false);
    }

    public int getHandicapCount() {
        return handicapById.size();
    }

    private List<HandicapPresenter> buildHandicaps(AccessibilityStats.Type type, boolean withTotal) {

        List<HandicapPresenter> ret = new ArrayList<>();

        int index = 0;
        int total = 0;
        HandicapPresenter totalHandicapPresenter = null;
        if (withTotal) {
            Handicap totalHandicap = new Handicap();
            totalHandicap.setName("total");
            totalHandicapPresenter = new HandicapPresenter(totalHandicap, total, index++);
            ret.add(totalHandicapPresenter);
        }

        for (Map.Entry<String, AccessibilityStats> entry : statsByHandicapId.entrySet()) {

            Handicap handicap = handicapById.get(entry.getKey());
            AccessibilityStats stat = entry.getValue();

            ret.add(new HandicapPresenter(handicap, stat.getCounter(type), index++));
            total = total + stat.getCounter(type);
        }

        if (withTotal) {
            totalHandicapPresenter.setStat(total);
        }

        return ret;
    }

    private void aggregateStatsByHandicaps(Audit audit) {
        statsByHandicapId.clear();
        handicapById.clear();

        statsByHandicapId.putAll(audit.computeStatsByHandicap());
        handicapById.putAll(audit.getRuleSet().getHandicapsById());


    }


}
