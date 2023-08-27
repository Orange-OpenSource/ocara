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
import androidx.recyclerview.widget.RecyclerView
import com.orange.ocara.data.source.ImageSource
import com.orange.ocara.mobile.R
import com.squareup.picasso.Picasso
import dagger.hilt.android.qualifiers.ActivityContext
import java.io.File
import javax.inject.Inject

class ProfilesIconAdapter @Inject constructor(@ActivityContext val context: Context, val imageCache: ImageSource.ImageCache)
    : ItemRVAdapter<String, ProfilesIconAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.icon)
    }

    override fun getLayout() = R.layout.profiles_icon_list_item

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (imageCache.fileExists(data[position])) {
            setIconInImageView(data[position], holder)
        }
    }

    private fun setIconInImageView(icon: String, holder: ViewHolder) {
        val iconFile: File = imageCache.get(icon)
        Picasso.with(context).load(iconFile).placeholder(android.R.color.black).into(holder.icon)
    }

    override fun getViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }
}