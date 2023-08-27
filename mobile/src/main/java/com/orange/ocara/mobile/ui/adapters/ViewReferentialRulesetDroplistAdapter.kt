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
import com.orange.ocara.domain.models.RulesetModel
import com.orange.ocara.mobile.R
import com.orange.ocara.utils.enums.RuleSetStat

class ViewReferentialRulesetDroplistAdapter(val context: Context, var rulesets: List<RulesetModel>)
    : BaseAdapter() {

    val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int = rulesets.size

    override fun getItem(position: Int): RulesetModel = rulesets.get(position)

    override fun getItemId(position: Int): Long = 0L

    override fun getView(position: Int, viewParent: View?, parent: ViewGroup?): View {
        val ruleset = rulesets[position]
        val view = inflater.inflate(R.layout.ruleset_view_ref_droplist_item, null)
//        val icon = view.findViewById(R.id.dowloadedStatus) as ImageView
        val names = view.findViewById(R.id.rulesetName) as TextView
        names.text = context.getString(R.string.rulesetInDroplistFormat, ruleset.type, ruleset.version.toInt())
//        icon.visibility = View.GONE
        return view

    }

}