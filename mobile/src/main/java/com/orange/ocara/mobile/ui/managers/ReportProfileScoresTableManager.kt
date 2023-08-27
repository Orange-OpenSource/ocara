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
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
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
import kotlin.collections.ArrayList

class ReportProfileScoresTableManager(private val resumeTable: TableLayout, private val resumeTitle: TableRow,
                                      private val layoutInflater: LayoutInflater) {
    val context: Context = resumeTable.context

    //    lateinit var noKeys: ArrayList<String>
    fun updateScoresTable(profileAnswersUIModel: ProfileAnswersUIModel) {
        var noKeys = ArrayList<String>()
        val impactTitles: MutableSet<String> = HashSet()
//        var rowIndex = 0
        val keys = profileAnswersUIModel.numberOfNo.keys
        if (!impactTitles.containsAll(keys)) impactTitles.addAll(profileAnswersUIModel.numberOfNo.keys)
        addTableHeader(R.string.audit_results_table_no_impact_title)
        addTableHeader(R.string.audit_results_table_doubt_title)
        for (name in impactTitles) {
            noKeys.add(name)
            addTableHeader(name)
        }
        addTableRowValues(profileAnswersUIModel, noKeys)

    }


    private fun addTableHeader(textId: Int) {
        val headerTextView = TextView(context, null, R.style.ResumeCellHeader)
        headerTextView.setText(textId)
        headerTextView.gravity = Gravity.CENTER
        formatText(headerTextView)
        setPadingToHeaderTextview(headerTextView)
        resumeTitle.addView(headerTextView)
    }

    private fun addTableHeader(text: String) {
        val headerTextView = TextView(context, null, R.style.ResumeCellHeader)
        headerTextView.text = text
        headerTextView.gravity = Gravity.CENTER
        formatText(headerTextView)
        setPadingToHeaderTextview(headerTextView)
        resumeTitle.addView(headerTextView)
    }

    private fun setPadingToHeaderTextview(headerTextView: TextView) {
        headerTextView.setPadding(10, 50, 10, 50)
    }

    private fun addTableRowValues(profileAnswersUIModel: ProfileAnswersUIModel, noKeys: ArrayList<String>) {
        Timber.i(
                "Message=Adding a new row in the table;handicapId=%s;OK=%d;Doubt=%d;",
                profileAnswersUIModel.profileTypeModel.reference,
                profileAnswersUIModel.numberOfOk,
                profileAnswersUIModel.numberOfDoubt)
        resumeTable.addView(buildScoreTableRow(profileAnswersUIModel, noKeys))
    }

    private fun buildScoreTableRow(profileAnswersUIModel: ProfileAnswersUIModel, noKeys: ArrayList<String>): View? {

        val row = layoutInflater.inflate(R.layout.result_resume_item, null) as TableRow
        val okScoreTextView = row.findViewById<TextView>(R.id.resume_handicap_type)
        okScoreTextView.text = profileAnswersUIModel.numberOfOk.toString()
        okScoreTextView.gravity = Gravity.CENTER
        formatText(okScoreTextView)
//        setPadingToHeaderTextview(okScoreTextView)

        val doubtScoreTextView = TextView(context, null, R.style.ResumeCellHeader)
        doubtScoreTextView.text = profileAnswersUIModel.numberOfDoubt.toString()
        doubtScoreTextView.gravity = Gravity.CENTER
        formatText(doubtScoreTextView)
        row.addView(doubtScoreTextView)

//        profileAnswersUIModel.numberOfNo.keys
        for (impactValue in noKeys) {
            val textView = TextView(context, null, R.style.ResumeCellHeader)
            val counter = profileAnswersUIModel.numberOfNo[impactValue].toString()
            textView.text = counter
            textView.gravity = Gravity.CENTER
            formatText(textView)
            row.addView(textView)
        }

        return row
    }

    private fun formatText(textView: TextView) {
        textView.setBackgroundResource(R.drawable.grey_sharp_border_bg_text)
        textView.setTextColor(Color.BLACK)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        textView.setTypeface(null, Typeface.BOLD);
        textView.setPadding(0, 50, 0, 50)
    }


    private fun setTextAppearance(headerTextView: TextView, styleId: Int) {
        headerTextView.setTextAppearance(styleId)
    }
}