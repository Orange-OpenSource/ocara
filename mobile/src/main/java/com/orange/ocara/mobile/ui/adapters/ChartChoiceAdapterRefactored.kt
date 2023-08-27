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
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orange.ocara.data.source.ImageSource
import com.orange.ocara.domain.models.ProfileAnswersUIModel
import com.orange.ocara.domain.models.ProfileTypeModel
import com.orange.ocara.mobile.R
import com.squareup.picasso.Picasso
import dagger.hilt.android.qualifiers.ActivityContext
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class ChartChoiceAdapterRefactored @Inject constructor(@ActivityContext private val context: Context) :
        RecyclerView.Adapter<ChartChoiceAdapterRefactored.ViewHolder>() {

    @Inject
    lateinit var imageCache: ImageSource.ImageCache
    private var profileAnswersUIModel: List<ProfileAnswersUIModel> = emptyList()
    private var selectedProfiles: List<ProfileAnswersUIModel> = emptyList()

    var onProfileClickedListener: OnProfileClickedListener? = null

    fun setProfileAnswers(profiles: List<ProfileAnswersUIModel>, selectedProfiles: List<ProfileAnswersUIModel>) {
        this.profileAnswersUIModel = profiles
        this.selectedProfiles = selectedProfiles
        notifyDataSetChanged()
    }

    interface OnProfileClickedListener {
        fun onSelected(profileAnswersUIModel: ProfileAnswersUIModel)
        fun onUnSelected(profileAnswersUIModel: ProfileAnswersUIModel)
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val icon = view.findViewById<View>(R.id.handicap_type_icon) as ImageView
        val title = view.findViewById<View>(R.id.handicap_type_title) as TextView
        val frame = view.findViewById<View>(R.id.frameLayout) as FrameLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.handicap_type_choice, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val handicap: ProfileAnswersUIModel = profileAnswersUIModel[position]
        setProfileInfo(handicap, holder)
        holder.view.setOnClickListener {
            onProfileClickedListener(holder, position)
        }
    }

    private fun setProfileInfo(handicap: ProfileAnswersUIModel, holder: ViewHolder) {
        if (imageCache.fileExists(handicap.profileTypeModel.icon)) {
            setIconInImageView(handicap.profileTypeModel.icon, holder)
            holder.title.text = handicap.profileTypeModel.name
        }
        holder.frame.isSelected = selectedProfiles.contains(handicap)
    }

    private fun setIconInImageView(icon: String, holder: ViewHolder) {
        val iconFile: File = imageCache.get(icon)
        Picasso.with(context).load(iconFile).placeholder(android.R.color.black).into(holder.icon)
    }

    private fun onProfileClickedListener(holder: ViewHolder, position: Int) {
        if (holder.frame.isSelected) {
            holder.frame.isSelected = false
            onProfileClickedListener?.onUnSelected(profileAnswersUIModel[position])
        } else {
            holder.frame.isSelected = true
            onProfileClickedListener?.onSelected(profileAnswersUIModel[position])
        }
    }

    override fun getItemCount() = profileAnswersUIModel.size
}