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

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orange.ocara.data.cache.database.NonTables.RuleAnswerAndLabel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.ui.getIconFromAnswer
import com.orange.ocara.utils.enums.Answer

class AnomaliesAdapter(data: ArrayList<RuleAnswerAndLabel>) :
        ItemRVAdapter<RuleAnswerAndLabel, AnomaliesAdapter.ViewHolder>(data) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val label: TextView = view.findViewById(R.id.text)
        val icon: ImageView = view.findViewById(R.id.icon)
        val yesBtn: Button = view.findViewById(R.id.yes_btn)
        val doubtBtn: Button = view.findViewById(R.id.doubt_btn)
        val noBtn: Button = view.findViewById(R.id.no_btn)
        val nABtn: Button = view.findViewById(R.id.n_a_btn)
    }

    override fun getViewHolder(view: View) = ViewHolder(view)

    override fun getLayout() = R.layout.anomalies_item_adapter

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.label.text = item.label
//        holder.icon.visibility = View.GONE

        setAnswerBtn(holder, item)
    }

    private fun setAnswerBtn(holder: ViewHolder, item: RuleAnswerAndLabel) {
        holder.yesBtn.isSelected = false
        holder.doubtBtn.isSelected = false
        holder.noBtn.isSelected = false
        holder.nABtn.isSelected = false
        when (item.answer) {
            Answer.OK -> {
                holder.yesBtn.isSelected = true
                holder.icon.setImageResource(R.drawable.ic_modifier_done_yes)
            }
            Answer.DOUBT -> {
                holder.doubtBtn.isSelected = true
                holder.icon.setImageResource(R.drawable.ic_modifier_done_doubt)
            }
            Answer.NOK -> {
                holder.noBtn.isSelected = true
                holder.icon.setImageResource(R.drawable.ic_modifier_done_no)
            }
            else -> {
                holder.nABtn.isSelected = true
                holder.icon.setImageResource(R.drawable.ic_modifier_done_na)
            }
        }
    }

}