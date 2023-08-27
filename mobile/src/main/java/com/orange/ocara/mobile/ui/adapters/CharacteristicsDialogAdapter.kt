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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.orange.ocara.domain.models.AuditEquipmentModel
import com.orange.ocara.domain.models.EquipmentModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.ui.loadImageIntoView
import com.squareup.picasso.Picasso

class CharacteristicsDialogAdapter(
        private val context: Context, private val auditEquipment: AuditEquipmentModel,
        private val itemStateChanged: ItemStateChanged
) :
        ItemRVAdapter<EquipmentModel, CharacteristicsDialogAdapter.ViewHolder>(auditEquipment.equipment.children as ArrayList<EquipmentModel>) {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val objectName: TextView = view.findViewById(R.id.nameTv)
        val icon: ImageView = view.findViewById(R.id.icon)
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)
        val container: ConstraintLayout = view.findViewById(R.id.item_cont)
    }

    interface ItemStateChanged {
        fun onItemSelected(equipment: EquipmentModel)
        fun onItemUnSelected(equipment: EquipmentModel)
    }

    override fun getLayout() = R.layout.select_characteristics_item

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.objectName.text = data[position].name
        loadImageIntoView(data[position].icon, holder.icon)
        if (checkIfCharacteristicIsAddedToAudit(data[position].reference)) {
            holder.checkBox.isChecked = true
            holder.view.contentDescription = getContentDesc(position, holder.checkBox.isChecked)
        }
        addClickListenersForCheckbox(holder.checkBox, data[position])
        holder.container.setOnClickListener {
            holder.checkBox.isChecked = !holder.checkBox.isChecked
            holder.view.contentDescription = getContentDesc(position, holder.checkBox.isChecked)
        }

        holder.view.contentDescription = getContentDesc(position, holder.checkBox.isChecked)
    }

    private fun getContentDesc(position: Int, ischeck: Boolean): String {
        return data[position].name + " " + String.format(context.getString(R.string.content_desc_item_list_size), itemCount) + " " +
                String.format(context.getString(R.string.content_desc_item_list_pos), position + 1) + " " +
                if (ischeck) context.getString(R.string.content_desc_activated) else context.getString(R.string.content_desc_deactivated)
    }

    private fun addClickListenersForCheckbox(checkBox: CheckBox, equipment: EquipmentModel) {
        checkBox.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                itemStateChanged.onItemSelected(equipment)
            else
                itemStateChanged.onItemUnSelected(equipment)
        }
    }

    private fun checkIfCharacteristicIsAddedToAudit(characteristicRef: String): Boolean {
        return auditEquipment.childrenReferences.contains(characteristicRef)
    }

    override fun getViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }


}