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

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.orange.ocara.domain.models.ProfileAnswersUIModel
import com.orange.ocara.mobile.R
import timber.log.Timber
import java.util.*

class ReportTableManager(private val resumeTable: TableLayout,private val resumeTitle: TableRow,
                private val layoutInflater: LayoutInflater) {
    val context: Context = resumeTable.context

    fun updateScoresTable(profileAnswersUIModels: List<ProfileAnswersUIModel>) {
        removeTableRows()
        val impactTitles: MutableSet<String> = HashSet()
        var rowIndex = 0
        for (model in profileAnswersUIModels) {
            val keys = model.numberOfNo.keys
            if (!impactTitles.containsAll(keys )) impactTitles.addAll(model.numberOfNo.keys)
            addTableRowValues(model, rowIndex++)
        }
        addTableHeader(R.string.audit_results_table_no_impact_title)
        addTableHeader(R.string.audit_results_table_doubt_title)
        for (name in impactTitles) {
            addTableHeader(name)
        }
    }

    private fun removeTableRows() {
        resumeTable.removeViews(1, resumeTable.childCount - 1)
        resumeTitle.removeViews(1, resumeTitle.childCount - 1)
    }

    private fun addTableHeader(textId: Int) {
        val headerTextView = TextView(context, null, R.style.ResumeCellHeader)
        headerTextView.setText(textId)
        headerTextView.gravity = Gravity.CENTER
        addMarginToTextview(headerTextView)
        setTextAppearance(headerTextView, R.style.ResumeCellHeader)
        resumeTitle.addView(headerTextView)
    }

    private fun addTableHeader(text: String) {
        val headerTextView = TextView(context, null, R.style.ResumeCellHeader)
        headerTextView.text = text
        headerTextView.gravity = Gravity.CENTER
        setTextAppearance(headerTextView, R.style.ResumeCellHeader)
        addMarginToTextview(headerTextView)
        resumeTitle.addView(headerTextView)
    }

    private fun addMarginToTextview(headerTextView: TextView) {
        val layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(10, 0, 10, 0)
        headerTextView.layoutParams = layoutParams
    }

    private fun addTableRowValues(profileAnswersUIModel: ProfileAnswersUIModel, rowIndex: Int) {
        Timber.i(
                "Message=Adding a new row in the table;handicapId=%s;OK=%d;Doubt=%d;",
                profileAnswersUIModel.profileTypeModel.reference,
                profileAnswersUIModel.numberOfOk,
                profileAnswersUIModel.numberOfDoubt)
        resumeTable.addView(buildScoreTableRow(profileAnswersUIModel, rowIndex))
    }

    private fun buildScoreTableRow(profileAnswersUIModel: ProfileAnswersUIModel, rowIndex: Int): View? {
        val row = layoutInflater.inflate(R.layout.result_resume_item, null) as TableRow
        val handicapType = row.findViewById<TextView>(R.id.resume_handicap_type)
        handicapType.text = profileAnswersUIModel.profileTypeModel.name
        val okScoreTextView = TextView(context, null, R.style.ResumeCellHeader)
        okScoreTextView.text = profileAnswersUIModel.numberOfOk.toString()
        okScoreTextView.gravity = Gravity.CENTER
        row.addView(okScoreTextView)
        val doubtScoreTextView = TextView(context, null, R.style.ResumeCellHeader)
        doubtScoreTextView.text = profileAnswersUIModel.numberOfDoubt.toString()
        doubtScoreTextView.gravity = Gravity.CENTER
        row.addView(doubtScoreTextView)

        for (impactValue in profileAnswersUIModel.numberOfNo.keys) {
            val textView = TextView(context, null, R.style.ResumeCellHeader)
            val counter = profileAnswersUIModel.numberOfNo[impactValue].toString()
            textView.text = counter
            textView.gravity = Gravity.CENTER
            row.addView(textView)
        }
        val backGroundColor = if (rowIndex % 2 == 0) R.color.resultTableColorEven else R.color.resultTableColorOdd
        row.setBackgroundResource(backGroundColor)
        return row
    }

    private fun setTextAppearance(headerTextView: TextView, styleId: Int) {
        headerTextView.setTextAppearance(styleId)
    }
}