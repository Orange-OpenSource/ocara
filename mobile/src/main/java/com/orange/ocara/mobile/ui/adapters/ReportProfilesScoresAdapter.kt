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
import com.orange.ocara.domain.models.ProfileAnswersUIModel
import com.orange.ocara.mobile.R
import com.orange.ocara.utils.enums.Answer
import com.squareup.picasso.Picasso
import java.io.File

class ReportProfilesScoresAdapter(data: ArrayList<ProfileAnswersUIModel >)
    : ItemRVAdapter<ProfileAnswersUIModel,
        ReportProfilesScoresAdapter.ViewHolder>(data) {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val profileName = view.findViewById<TextView>(R.id.profileName)
    }

    override fun getLayout() = R.layout.profiles_score_list_item

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profile = data[position]
        holder.profileName.text = profile.profileTypeModel.name
        holder.view.setOnClickListener {
            onClick?.invoke(profile)
        }
    }

    override fun getViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }

}