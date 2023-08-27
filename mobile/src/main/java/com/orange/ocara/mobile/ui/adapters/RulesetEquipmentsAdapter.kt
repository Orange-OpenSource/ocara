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
import com.orange.ocara.data.source.ImageSource
import com.orange.ocara.domain.models.EquipmentModel
import com.orange.ocara.mobile.R
import com.squareup.picasso.Picasso
import dagger.hilt.android.qualifiers.ActivityContext
import timber.log.Timber
import java.io.File
import java.util.*
import javax.inject.Inject

class RulesetEquipmentsAdapter @Inject constructor(val imageCache: ImageSource.ImageCache,
                                                  @ActivityContext val context:Context) :
        ItemRVAdapter<EquipmentModel, RulesetEquipmentsAdapter.ViewHolder>() {


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val objName: TextView = view.findViewById(R.id.object_description_title)
        val icon: ImageView = view.findViewById(R.id.object_description_image)

    }

    override fun getLayout() = R.layout.ruleset_equipment_grid_item

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.view.setOnClickListener {
            onClick?.invoke(data[position])
        }
        viewHolder.objName.text = data[position].name
        updateEquipmentIcon(viewHolder.icon ,data[position].icon)

    }
    fun updateEquipmentIcon(imageView: ImageView, icon: String) {
        if (imageCache!!.fileExists(icon)) {
            val iconFile: File = imageCache!!.get(icon)
            Timber.v("Message=Trying to load image;Icon=%s", icon)
            Picasso
                    .with(context)
                    .load(iconFile)
                    .error(android.R.color.darker_gray)
                    .fit()
                    .into(imageView)
        }
    }

    override fun getViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }


}