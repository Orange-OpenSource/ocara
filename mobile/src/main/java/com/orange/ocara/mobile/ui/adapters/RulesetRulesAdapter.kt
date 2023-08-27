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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orange.ocara.domain.models.RuleWithEquipmentModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.ui.models.LightEquipment
import com.orange.ocara.mobile.ui.models.LightRule
import java.util.ArrayList

class RulesetRulesAdapter(rules: ArrayList<RuleWithEquipmentModel>, val onIllustrationsClicked: (ruleRef: String) -> Unit) :
        ItemRVAdapter<RuleWithEquipmentModel, RulesetRulesAdapter.ViewHolder>(rules) {


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val info: ImageView = view.findViewById(R.id.info)
        val objName: TextView = view.findViewById(R.id.objectTV)
        val ruleLabel: TextView = view.findViewById(R.id.ruleTV)

    }

    override fun getLayout() = R.layout.ruleset_rule_list_item

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.view.setOnClickListener {
            onClick?.invoke(data[position])
        }
        viewHolder.objName.text = data[position].equipmentNamesString
        viewHolder.ruleLabel.text = data[position].rule.label
        viewHolder.view.setContentDescription(data[position].equipmentNamesString + " " + data[position].rule.label + " " + viewHolder.view.context.getString(R.string.content_desc_in_list))

        if (data[position].rule.isIllustrated) {
            viewHolder.info.visibility = View.VISIBLE
            viewHolder.info.setOnClickListener {
                onIllustrationsClicked(data[position].rule.ref)
            }
        } else {
            viewHolder.info.visibility = View.GONE
        }


    }

    override fun getViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }


}