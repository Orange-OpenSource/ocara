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
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orange.ocara.domain.models.EquipmentForAddEquipmentModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.ui.loadImageIntoView

class AddEquipmentAdapter (val context: Context) : RecyclerView.Adapter<AddEquipmentAdapter.ViewHolder>() {

    val viewTypeSelected : Int = 1
    val viewTypeNotSelected : Int = 2

    lateinit var data: List<EquipmentForAddEquipmentModel>

    class ViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
        val icon: ImageView = v.findViewById(R.id.objectIcon)
        val name: TextView = v.findViewById(R.id.objectName)
    }

    /*
        this method returns 2 if object not tested and 1 if tested
     */
    override fun getItemViewType(position: Int): Int {
        if (data[position].isSelected) return viewTypeSelected
        return viewTypeNotSelected
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddEquipmentAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(getLayout(viewType), parent, false)
        return ViewHolder(view)
    }

    private fun getLayout(viewType: Int): Int {
        return if (viewType == viewTypeSelected) R.layout.add_equipment_selected_list_item
        else R.layout.current_route_not_tested_list_item
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val equipment = data[position]
        holder.name.text = equipment.name
        loadImageIntoView(equipment.icon, holder.icon)
        holder.itemView.setOnClickListener {
            equipment.isSelected = !equipment.isSelected
            notifyItemChanged(position)
        }
        if (equipment.isSelected)
            holder.itemView.contentDescription = equipment.name+" "+context.getString(R.string.content_desc_obj_selected)
        else
            holder.itemView.contentDescription = equipment.name+" "+context.getString(R.string.content_desc_obj_not_selected)
    }

    override fun getItemCount() = data.size
}