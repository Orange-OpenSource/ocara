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

package com.orange.ocara.mobile.ui.dialogs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.orange.ocara.domain.models.ProfileAnswersUIModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.DialogSelectProfilesBinding
import com.orange.ocara.mobile.ui.adapters.ChartChoiceAdapterRefactored
import com.orange.ocara.mobile.ui.viewmodel.ReportViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SelectProfilesDialog(private val viewModel: ReportViewModel) :
        OcaraDialog<DialogSelectProfilesBinding>(R.layout.dialog_select_profiles), ChartChoiceAdapterRefactored.OnProfileClickedListener {

    var currentSelectedProfiles:ArrayList<ProfileAnswersUIModel> = ArrayList(viewModel.selectedProfilesLiveData.value!!)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProfilesList(viewModel.allProfiles)
    }

    @Inject
    lateinit var chartChoiceAdapterRefactored: ChartChoiceAdapterRefactored

    private fun initProfilesList(list: List<ProfileAnswersUIModel>) {
        chartChoiceAdapterRefactored.setProfileAnswers(list, currentSelectedProfiles)
        chartChoiceAdapterRefactored.onProfileClickedListener = this
        binding.chartChoices.adapter = chartChoiceAdapterRefactored
        binding.chartChoices.layoutManager = GridLayoutManager(requireContext(), 5)
        binding.selectAllProfiles.isChecked = areAllProfilesSelected()
    }

    private fun areAllProfilesSelected() = viewModel.allProfiles.size == currentSelectedProfiles.size

    override fun initClickListeners() {
        binding.cancel.setOnClickListener {
            dismiss()
        }
        binding.close.setOnClickListener {
            dismiss()
        }
        binding.validate.setOnClickListener {
            viewModel.selectedProfilesLiveData.postValue(currentSelectedProfiles)
            dismiss()
        }
        binding.selectAllProfiles.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectAllProfiles()
            } else {
                unSelectAllProfiles()
            }
        }
    }

    private fun selectAllProfiles() {
        currentSelectedProfiles = ArrayList(viewModel.allProfiles)
        chartChoiceAdapterRefactored.setProfileAnswers(ArrayList(viewModel.allProfiles), currentSelectedProfiles)
    }

    private fun unSelectAllProfiles() {
        currentSelectedProfiles = ArrayList()
        chartChoiceAdapterRefactored.setProfileAnswers(viewModel.allProfiles, currentSelectedProfiles)
    }

    override fun onSelected(profileAnswersUIModel: ProfileAnswersUIModel) {
        currentSelectedProfiles.add(profileAnswersUIModel)
    }

    override fun onUnSelected(profileAnswersUIModel: ProfileAnswersUIModel) {
        currentSelectedProfiles.remove(profileAnswersUIModel)
    }


}