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
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orange.ocara.data.cache.database.NonTables.AuditEquipmentForReport
import com.orange.ocara.data.source.ImageSource
import com.orange.ocara.mobile.R
import com.squareup.picasso.Picasso
import timber.log.Timber
import javax.inject.Inject

class ReportPathElementRVAdapter @Inject constructor(val imageCache: ImageSource.ImageCache)
    : ItemRVAdapter<AuditEquipmentForReport, ReportPathElementRVAdapter.ViewHolder>() {


    override fun getLayout(): Int = R.layout.current_route_not_tested_list_item

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.objectIcon)

        val name: TextView = view.findViewById(R.id.objectName)
    }

    override fun getViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name.text = data[position].name
        setEquipmentIcon(holder, data[position].icon)

    }

    private fun setEquipmentIcon(holder: ViewHolder, icon: String) {

        if (imageCache.fileExists(icon)) {

            val iconFile = imageCache.get(icon)
            Timber.d("Message=Trying to load image;Icon= ${iconFile.path}")

            Picasso.with(holder.itemView.context)
                    .load(iconFile)
                    .error(android.R.color.darker_gray)
                    .fit()
                    .into(holder.icon)
        }

    }
}