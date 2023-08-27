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
import com.orange.ocara.domain.models.EquipmentModel
import com.orange.ocara.mobile.R
import com.orange.ocara.utils.ListUtils
import com.squareup.picasso.Picasso
import dagger.hilt.android.qualifiers.ActivityContext
import java.io.File
import java.io.Serializable
import java.util.*
import javax.inject.Inject


class EquipmentsAdapter @Inject constructor
    (@ActivityContext private val context: Context) : ItemListAdapter<EquipmentModel>() {


    override fun update(objects: List<EquipmentModel>) {
        val items: List<EquipmentModel> = ListUtils.newArrayList(objects)
        Collections.sort(items, ObjectDescriptionComparator())
        super.update(items)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var view = convertView
        val viewHolder: ObjectViewHolder
        if (view == null) {
            view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.ruleset_equipment_grid_item, parent, false)
            viewHolder = ObjectViewHolder(view)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ObjectViewHolder
        }
        val objectDescription = getItem(position)
        // Trigger the download of the URL asynchronously into the image view.
        val path = context.externalCacheDir.toString() + File.separator + objectDescription.icon
        val icon = File(path)
        Picasso
                .with(context)
                .load(icon)
                .error(android.R.color.black)
                .into(viewHolder.image)
        viewHolder.bind(objectDescription)
        return view
    }


    /**
     * an implementation of [Comparator] that helps on sorting elements in alphabetical order
     */
    private class ObjectDescriptionComparator : Comparator<EquipmentModel?>, Serializable {
        override fun compare(o1: EquipmentModel?, o2: EquipmentModel?): Int {
            if (o1 == null) {
                return -1
            }
            if (o2 == null) {
                return 1
            }
            return if (o1.name == o2.name) {
                0
            } else o1.name.compareTo(o2.name)
        }

        companion object {
            private const val serialVersionUID: Long = 1
        }
    }

    private class ObjectViewHolder(convertView: View) {
        val image: ImageView = convertView.findViewById(R.id.object_description_image)
        val title: TextView = convertView.findViewById(R.id.object_description_title)
        fun bind(content: EquipmentModel) {
            title.text = content.name
        }
    }
}