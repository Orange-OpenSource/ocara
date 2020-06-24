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

package com.orange.ocara.ui.fragment;

import androidx.core.content.ContextCompat;

import com.orange.ocara.R;
import com.orange.ocara.ui.view.piechart.PieGraph;
import com.orange.ocara.ui.view.piechart.PieSlice;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

@EFragment(resName = "fragment_handicap_pie_chart")
public class HandicapPieChartFragment extends BaseFragment {

    @FragmentArg("accepted")
    Integer ok;
    @FragmentArg("nok")
    Integer nok;
    @FragmentArg("doubt")
    Integer doubt;
    @FragmentArg("noAnswer")
    Integer noAnswer;

    @ViewById(resName = "pie_chart")
    PieGraph pieChart;

    @AfterViews
    void updateChart() {
        pieChart.removeSlices();

        if (nok != null && nok > 0) {
            addSlice(ContextCompat.getColor(this.getContext(), R.color.red), nok, getString(R.string.audit_results_anomaly_rules_title));
        }

        if (ok != null && ok > 0) {
            addSlice(ContextCompat.getColor(this.getContext(), R.color.green), ok, getString(R.string.audit_results_table_no_impact_title));
        }

        if (doubt != null && doubt > 0) {
            addSlice(ContextCompat.getColor(this.getContext(), R.color.yellow), doubt, getString(R.string.audit_results_table_doubt_title));
        }

        if (noAnswer != null && noAnswer > 0) {
            addSlice(ContextCompat.getColor(this.getContext(), R.color.grey), noAnswer, getString(R.string.audit_results_not_answered));
        }
    }

    private void addSlice(int color, float value, String title) {
        PieSlice slice = new PieSlice();
        slice.setColor(color);
        slice.setValue(value);
        slice.setTitle(title);
        pieChart.addSlice(slice);
    }
}
