/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.fragment;

import android.content.res.Resources;

import com.orange.ocara.ui.view.piechart.PieGraph;
import com.orange.ocara.ui.view.piechart.PieSlice;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

@EFragment(resName="fragment_handicap_pie_chart")
public class HandicapPieChartFragment extends BaseFragment {

    @FragmentArg("nok")
    Integer nok;
    @FragmentArg("ok")
    Integer ok;
    @FragmentArg("doute")
    Integer doute;
    @FragmentArg("noAnswer")
    Integer noAnswer;

    @ViewById(resName="pie_chart")
    PieGraph pieChart;

    @AfterViews
    void updateChart() {
        pieChart.removeSlices();

        final Resources resources = getResources();

        if (nok != null && nok > 0) {
            addSlice(resources.getColor(com.orange.ocara.R.color.red), nok, "Non accessible");
        }

        if (ok != null && ok > 0) {
            addSlice(resources.getColor(com.orange.ocara.R.color.green), ok, "Accessible");
        }

        if (doute != null && doute > 0) {
            addSlice(resources.getColor(com.orange.ocara.R.color.yellow), doute, "Doute");
        }

        if (noAnswer != null && noAnswer > 0) {
            addSlice(resources.getColor(com.orange.ocara.R.color.grey), noAnswer, "No Answer");
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
