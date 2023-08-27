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

package com.orange.ocara.mobile.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.orange.ocara.domain.models.RulesetModel
import com.orange.ocara.mobile.R
import com.orange.ocara.utils.enums.RuleSetStat

class RulesetDropListAdapter(
    val context: Context, var rulesets: List<RulesetModel>,
//    var onRuleDownloadClicked: (rulesetPos: Int) -> Unit
) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private lateinit var viewHolder: ViewHolder

    override fun getCount(): Int = rulesets.size

    override fun getItem(position: Int): RulesetModel = rulesets.get(position)

    override fun getItemId(position: Int): Long = 0L

    override fun getView(position: Int, viewParent: View?, parent: ViewGroup?): View {
        val view: View
        if (viewParent == null) {

            view = inflater.inflate(R.layout.ruleset_droplist_item, parent, false)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder

        } else {
            view = viewParent
            viewHolder = view.tag as ViewHolder

        }
        viewHolder.updateItemDate(position, context, rulesets /*, onRuleDownloadClicked*/)
        if (position == 0) {
            view.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
        }
        setRulesetContentDescription(rulesets[position] , view)
        return view
    }
    private fun setRulesetContentDescription(ruleSet: RulesetModel , view: View) {
        var contentDesc = context.getString(
                R.string.rulesetInDroplistFormat, ruleSet.type, ruleSet.version.toInt())
        if (ruleSet.isLocallyAvailable) {
            contentDesc = contentDesc + " " + context.getString(R.string.rulesetInDroplist_content_desc_downloaded)
        } else {
            contentDesc = contentDesc + " " + context.getString(R.string.rulesetInDroplist_content_desc_not_downloaded)
        }
        view.setContentDescription(contentDesc)

    }

    class ViewHolder(view: View) {
        private val icon: ImageView = view.findViewById(R.id.dowloadedStatus)
        private val names: TextView = view.findViewById(R.id.rulesetName)
        private val rulesetContainer: ConstraintLayout = view.findViewById(R.id.rulesetContainer)

        fun updateItemDate(
            position: Int,
            context: Context,
            rulesets: List<RulesetModel>,
//            onRuleDownloadClicked: (rulesetPos: Int) -> Unit
        ) {
            val ruleset = rulesets[position]
            names.text = context.getString(
                R.string.rulesetInDroplistFormat,
                ruleset.type,
                ruleset.version.toInt()
            )
            icon.setImageResource(
                if (ruleset.stat == RuleSetStat.OFFLINE)
                    R.drawable.modifier_done_19371_1
                else
                    R.drawable.modifier_download
            )
//            if (ruleset.stat != RuleSetStat.OFFLINE) {
//                icon.isClickable = true
//                icon.setOnClickListener { onRuleDownloadClicked(position) }
//                rulesetContainer.setOnClickListener { onRuleDownloadClicked(position) }
//            }else{
//                icon.isClickable=false
//                rulesetContainer.setOnClickListener {  }
//            }
        }
    }

}