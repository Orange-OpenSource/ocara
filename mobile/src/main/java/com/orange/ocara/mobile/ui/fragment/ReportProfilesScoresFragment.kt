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

package com.orange.ocara.mobile.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.orange.ocara.domain.models.ProfileAnswersUIModel

import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentReportProfileScoresBinding
import com.orange.ocara.mobile.ui.adapters.*
import com.orange.ocara.mobile.ui.dialogs.ReportProfileScoresDialog
import com.orange.ocara.mobile.ui.dialogs.SelectCharacteristicsDialog
import com.orange.ocara.mobile.ui.managers.ReportTableManager
import com.orange.ocara.mobile.ui.viewmodel.AuditEquipmentWithCharacteristicsViewModel
import com.orange.ocara.mobile.ui.viewmodel.ReportProfileScoresViewModel
import dagger.hilt.android.AndroidEntryPoint
// the profiles list with scores lower part of the report summary fragment
@Deprecated("this on is removed from the xd")
@AndroidEntryPoint
class ReportProfilesScoresFragment : Fragment() {

    companion object {
        const val TAG = "ReportProfilesScoresFragment"
        const val AUD_ID_ARG = "audit_id"
    }

    lateinit var binding: FragmentReportProfileScoresBinding
    lateinit var viewModel: ReportProfileScoresViewModel
    lateinit var reportProfilesScoresAdapter: ReportProfilesScoresAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_report_profile_scores, container, false)
        viewModel = ViewModelProvider(requireParentFragment()).get(ReportProfileScoresViewModel::class.java)

        addObservers()
        viewModel.getAuditScores(requireArguments().getInt(AUD_ID_ARG))
        return binding.root
    }

    private fun initProfilesAdapter(profiles: ArrayList<ProfileAnswersUIModel>) {

        reportProfilesScoresAdapter = ReportProfilesScoresAdapter(profiles)

        reportProfilesScoresAdapter.setOnClickListener {
            onProfileSelected(it)
        }
        binding.profilesList.adapter = reportProfilesScoresAdapter
        binding.profilesList.layoutManager = LinearLayoutManager(requireContext())
        binding.profilesList.addItemDecoration(DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL))
        binding.profilesList.layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false)
    }

    private fun addObservers() {
        viewModel.profilesLiveData.observe(viewLifecycleOwner) {
            initProfilesAdapter(it)
        }
    }

    private fun onProfileSelected(profile : ProfileAnswersUIModel) {
        val reportProfileScoresDialog = ReportProfileScoresDialog(viewModel , profile)
        reportProfileScoresDialog.show(childFragmentManager, ReportProfileScoresDialog.TAG)

    }


}