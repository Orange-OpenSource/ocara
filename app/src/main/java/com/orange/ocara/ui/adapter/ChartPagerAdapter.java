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

package com.orange.ocara.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.orange.ocara.data.net.model.ProfileTypeEntity;
import com.orange.ocara.ui.activity.ResultAuditActivity;
import com.orange.ocara.ui.fragment.HandicapPieChartFragment_;
import com.orange.ocara.ui.model.AccessibilityStatsUiModel;
import com.orange.ocara.ui.model.HandicapAggregateUiModel;

import timber.log.Timber;

/**
 * Categories pager adapter.
 */
public class ChartPagerAdapter extends FragmentStatePagerAdapter {

    private ResultAuditActivity mResultAuditActivity;
    private HandicapAggregateUiModel mHandicapAggregator;
    private AccessibilityStatsUiModel customStat = null;

    public ChartPagerAdapter(final ResultAuditActivity resultAuditActivity, final HandicapAggregateUiModel handicapAggregator) {
        super(resultAuditActivity.getSupportFragmentManager());
        mResultAuditActivity = resultAuditActivity;
        mHandicapAggregator = handicapAggregator;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return customStat != null ? 1 : mHandicapAggregator.getHandicapRef().size() + 1;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        AccessibilityStatsUiModel stat = new AccessibilityStatsUiModel();

        if (customStat != null) {
            stat.plus(customStat);
        } else {
            if (position == 0) {
                mHandicapAggregator.computeTotal();
                stat.plus(mHandicapAggregator.getTotalStat());
            } else {
                stat.plus(mHandicapAggregator.getStat(position - 1));
            }
        }

        int notOkScore = 0;
        for (int score : stat.getMImpactRefAndScoreMap().values()) {
            notOkScore += score;
        }

        Timber.i(
                "Message=Get HandicapPieChartFragment;WithCustomStat=%b;Position=%d;OK=%d;NOK=%d;Doubt=%d;NoAnswer=%d",
                customStat != null, position, stat.getMControleOkScore(), notOkScore, stat.getMControleDoubtScore(), stat.getMNoAnswerScore());
        return HandicapPieChartFragment_
                .builder()
                .ok(stat.getMControleOkScore())
                .nok(notOkScore)
                .doubt(stat.getMControleDoubtScore())
                .noAnswer(stat.getMNoAnswerScore())
                .build();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (customStat != null) {
            return mResultAuditActivity.getString(com.orange.ocara.R.string.audit_results_custom_handicap);
        } else {
            if (position == 0) {
                return mResultAuditActivity.getString(com.orange.ocara.R.string.audit_results_table_all_handicap);
            }

            final ProfileTypeEntity handicap = mHandicapAggregator.getHandicap(position - 1);
            return handicap.getName();
        }
    }

    public void setCustomStat(AccessibilityStatsUiModel custom) {
        this.customStat = custom;

        notifyDataSetChanged();
    }
}
