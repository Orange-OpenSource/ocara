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
import com.orange.ocara.domain.interactors.GetAuditScoresTask
import com.orange.ocara.domain.models.ProfileAnswersUIModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.ui.loadImageIntoView
import timber.log.Timber

class ProfilesListAdapter(
    private val rv: RecyclerView,
    private val profiles: ArrayList<ProfileAnswersUIModel>,
    private val onItemSelectedCallback: (List<ProfileAnswersUIModel>) -> Unit
) : ItemRVAdapter<ProfileAnswersUIModel, ProfilesListAdapter.ViewHolder>(profiles) {

    var selectedProfiles = mutableListOf<ProfileAnswersUIModel>()
    val viewHolders = mutableListOf<ProfilesListAdapter.ViewHolder>()

    override fun getViewHolder(view: View): ViewHolder = ViewHolder(view)

    override fun getLayout(): Int = R.layout.disabilties_list_item
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
        viewHolders.add(holder)
    }


    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val name: TextView = view.findViewById(R.id.disability_name)
        val icon: ImageView = view.findViewById(R.id.disability_icon)
        lateinit var ref: String
        lateinit var totalProfile: ProfileAnswersUIModel

        fun bind(position: Int) {
            name.text = profiles[position].profileTypeModel.name
            ref = profiles[position].profileTypeModel.reference

            setProfileIcon(view.context, profiles[position].profileTypeModel.icon, icon)
            totalProfile = profiles.filter {
                it.profileTypeModel.reference == GetAuditScoresTask.ALL_DISABILITIES
            }.first()

            view.setOnClickListener {
                onViewClickHandler(it, position)
                Timber.d("Adapter position = $bindingAdapterPosition")
            }
            if (ref == GetAuditScoresTask.ALL_DISABILITIES) {
                onViewClickHandler(view, position)
            }

            view.contentDescription = profiles[position].profileTypeModel.name + " "+ String.format(view.context.getString(R.string.content_desc_item_list_size) , itemCount) + " "+
                    String.format(view.context.getString(R.string.content_desc_item_list_pos) , position+1)
        }

        fun onViewClickHandler(view: View?, position: Int) {

            val isAllDisabilities = isAllDisabilities(position)


            if (view?.isSelected!!) {
                deselect(position)
                view.announceForAccessibility(profiles[position].profileTypeModel.name + " "+view.context.getString(R.string.content_desc_deselected)  )

            }
            else {
                if (isAllDisabilities) {
                    deselectAllProfiles()
                }
                select(position, isAllDisabilities)
            }

            onItemSelectedCallback(selectedProfiles)
        }

        fun select(position: Int, isAllDisabilities: Boolean) {

            if (!isAllDisabilities) {
                viewHolders.forEach {
                    if (it.ref == GetAuditScoresTask.ALL_DISABILITIES)
                        if (it.view.isSelected) it.view.isSelected = false
                }
                if (selectedProfiles.contains(totalProfile))
                    selectedProfiles.remove(totalProfile)
            }
            selectedProfiles.add(profiles[position])
            view.isSelected = true

        }

        private fun deselect(position: Int) {
            if (selectedProfiles.contains(profiles[position]))
                selectedProfiles.remove(profiles[position])
            view.isSelected = false
            if (selectedProfiles.isEmpty()) {
                selectAllDisabilities()
            }
        }

        private fun deselectAllProfiles() {
            selectedProfiles = mutableListOf()
            viewHolders.forEach {
                it.view.isSelected = false
            }
            selectAllDisabilities()
        }

        private fun setProfileIcon(context: Context, icon: String, icView: ImageView) {
            loadImageIntoView(icon, icView, R.drawable.ic_handicap_all)
//            Picasso.with(context)
//                    .load(icon)
//                    .placeholder(AppCompatResources.getDrawable(context, R.drawable.placeholder))
//                    .into(icView)
        }
    }

    private fun isAllDisabilities(position: Int): Boolean {
        return profiles[position].profileTypeModel.reference == GetAuditScoresTask.ALL_DISABILITIES
    }

    private fun selectAllDisabilities() {
        viewHolders.forEach {
            if (it.ref == GetAuditScoresTask.ALL_DISABILITIES) {
                it.select(it.absoluteAdapterPosition, true)
            }
        }
    }
}

