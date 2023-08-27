/*
 *
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 *
 */

package com.orange.ocara.mobile.ui.managers

import android.app.Activity
import androidx.core.content.ContextCompat
import com.orange.ocara.domain.models.ProfileAnswersUIModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.ui.view.piechart.PieGraph
import com.orange.ocara.mobile.ui.view.piechart.PieSlice
import timber.log.Timber

class PieChartManager(private val pieGraph: PieGraph,
                      private val activity: Activity) {
    var nok: Int = 0
    var ok: Int = 0
    var doubt: Int = 0
    var noAnswer: Int = 0

    fun updateEmptyPieChart() {
        resetCounters()
        pieGraph.removeSlices()
        addSlice(ContextCompat.getColor(activity, R.color.grey), 1f, activity.getString(R.string.audit_results_not_answered))
    }

    fun updatePieChart(it: ArrayList<ProfileAnswersUIModel>) {
        resetCounters()
        it.forEach { profile ->
            updateCountersWithThisProfile(profile)
        }
        updateChart()
        Timber.d("updating graph")
    }

    private fun updateCountersWithThisProfile(profile: ProfileAnswersUIModel) {
        nok += profile.totalNumberOfNo
        ok += profile.numberOfOk
        doubt += profile.numberOfDoubt
        noAnswer += profile.numberOfNA
    }

    private fun resetCounters() {
        nok = 0
        ok = 0
        doubt = 0
        noAnswer = 0
    }

    private fun updateChart() {
        pieGraph.removeSlices()
        if (nok <= 0 && ok <= 0 && doubt <= 0 && noAnswer <= 0) {
            addSlice(ContextCompat.getColor(activity, R.color.grey), 1f, activity.getString(R.string.audit_results_not_answered))
        }
        if (nok > 0) {
            addSlice(ContextCompat.getColor(activity, R.color.piechart_red), nok.toFloat(), activity.getString(R.string.audit_results_anomaly_rules_title))
        }
        if (ok > 0) {
            addSlice(ContextCompat.getColor(activity, R.color.piechart_green), ok.toFloat(), activity.getString(R.string.audit_results_table_no_impact_title))
        }
        if (doubt > 0) {
            addSlice(ContextCompat.getColor(activity, R.color.piechart_yellow), doubt.toFloat(), activity.getString(R.string.audit_results_table_doubt_title))
        }
        if (noAnswer > 0) {
            addSlice(ContextCompat.getColor(activity, R.color.dark_grey), noAnswer.toFloat(), activity.getString(R.string.audit_results_not_answered))
        }

    }

    private fun addSlice(color: Int, value: Float, title: String) {
        val slice = PieSlice()
        slice.color = color
        slice.value = value
        slice.title = title
        pieGraph.addSlice(slice)
    }
}