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
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.orange.ocara.data.source.ImageSource
import com.orange.ocara.domain.models.AuditEquipmentModel
import com.orange.ocara.mobile.R
import com.orange.ocara.utils.enums.Answer
import com.squareup.picasso.Picasso
import dagger.hilt.android.qualifiers.ActivityContext
import java.io.File
import javax.inject.Inject

class ReportViewRouteAdapter @Inject constructor(
    private val imageCache: ImageSource.ImageCache,
    @ActivityContext private val context: Context
) :
    ItemRVAdapter<AuditEquipmentModel, ReportViewRouteAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val containerCard: MaterialCardView = view.findViewById(R.id.container_card)
        val containerConst: ConstraintLayout = view.findViewById(R.id.container_const)
        val number: TextView = view.findViewById(R.id.equipmentNumberTv)
        val name: TextView = view.findViewById(R.id.equipmentName)
        val icon: ImageView = view.findViewById(R.id.equipmentIcon)
        val ansIcon: ImageView = view.findViewById(R.id.ansIcon)
    }

    override fun getViewHolder(view: View) = ViewHolder(view)

    override fun getLayout() = R.layout.report_view_route_list_item

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val auditEquipment = data[position]
        holder.name.text = auditEquipment.name
        holder.number.text = "${position + 1}" + "."
        setIconAns(auditEquipment.answer, holder)
        setIconInImageView(auditEquipment.equipment.icon, holder.icon)
        holder.itemView.setOnClickListener {
            onClick?.invoke(auditEquipment)
        }
    }

    private fun setIconAns(answer: Answer?, holder: ViewHolder) {
        when (answer) {
            Answer.OK -> {
                holder.containerCard.setStrokeColor(Color.parseColor("#32c832"))
                holder.containerConst.setBackgroundColor(Color.parseColor("#1932c832"))
                setIconInAnsImage(R.drawable.ic_ans_ok, holder.ansIcon)
            }
            Answer.NOK -> {
                holder.containerCard.setStrokeColor(Color.parseColor("#e21b1b"))
                holder.containerConst.setBackgroundColor(Color.parseColor("#19e21b1b"))
                setIconInAnsImage(R.drawable.no_answer, holder.ansIcon)
            }
            Answer.DOUBT -> {
                holder.containerCard.setStrokeColor(Color.parseColor("#ffcc00"))
                holder.containerConst.setBackgroundColor(Color.parseColor("#19ffcc00"))
                setIconInAnsImage(R.drawable.ic_ans_doubt, holder.ansIcon)
            }
            else -> {
                setIconInAnsImage(R.drawable.na_answer, holder.ansIcon)
            }
//            Answer.OK -> setIconInAnsImage(R.drawable.ok_answer, ansIcon)
//            Answer.NOK -> setIconInAnsImage(R.drawable.no_answer, ansIcon)
//            Answer.DOUBT -> setIconInAnsImage(R.drawable.doubt_answer, ansIcon)
//            else -> setIconInAnsImage(R.drawable.na_answer, ansIcon)
        }
    }

    private fun setIconInAnsImage(id: Int, ansIcon: ImageView) {
        ansIcon.setImageDrawable(
            ResourcesCompat.getDrawable(
                context.resources,
                id,
                context.theme
            )
        )
    }

    private fun setIconInImageView(icon: String, imageView: ImageView) {
        val iconFile: File? = imageCache.get(icon)
        Picasso.with(context).load(iconFile).placeholder(android.R.color.black).into(imageView)
    }
}