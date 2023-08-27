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
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orange.ocara.data.cache.database.NonTables.AuditEquipmentForReport
import com.orange.ocara.mobile.R
import com.orange.ocara.utils.enums.Answer
import com.squareup.picasso.Picasso
import java.io.File

class ReportEquipmentsAdapter(data: ArrayList<AuditEquipmentForReport>)
    : ItemRVAdapter<AuditEquipmentForReport,
        ReportEquipmentsAdapter.ViewHolder>(data) {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val equipmentName = view.findViewById<TextView>(R.id.path_name)
        val equipmentIcon = view.findViewById<ImageView>(R.id.path_icon)
//        val answerIcon = view.findViewById<ImageView>(R.id.answerIcon)
    }

    override fun getLayout() = R.layout.report_path_elements_item

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val equipment = data[position]
        holder.equipmentName.text = equipment.name
//        holder.answerIcon.setImageResource(getDrawableForAnswer(equipment.answer))
        setEquipmentIcon(holder, equipment)
        holder.view.setOnClickListener {
            onClick?.invoke(equipment)
        }
    }

    private fun setEquipmentIcon(holder: ViewHolder, equipment: AuditEquipmentForReport) {
        val context = holder.itemView.context
        val path = context.externalCacheDir.toString() + File.separator + equipment.icon
        val icon = File(path)
        Picasso
                .with(context)
                .load(icon)
                .error(android.R.color.black)
                .into(holder.equipmentIcon)
    }

    private fun getDrawableForAnswer(answer: Answer?): Int {
        return when (answer){
            Answer.NO_ANSWER -> R.drawable.na_answer
            Answer.OK -> R.drawable.ok_answer
            Answer.DOUBT -> R.drawable.doubt_answer
            Answer.NOK -> R.drawable.no_answer
            else -> R.drawable.na_answer
        }
    }

    override fun getViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }

}