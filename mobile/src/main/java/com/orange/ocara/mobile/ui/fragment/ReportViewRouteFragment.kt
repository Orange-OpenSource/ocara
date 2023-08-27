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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.orange.ocara.domain.models.AuditEquipmentModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentReportViewRouteBinding
import com.orange.ocara.mobile.ui.adapters.ReportViewRouteAdapter
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.mobile.ui.viewmodel.ReportViewRouteViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReportViewRouteFragment : OcaraFragment() {
    lateinit var binding: FragmentReportViewRouteBinding

    @Inject
    lateinit var reportViewRouteAdapter: ReportViewRouteAdapter
    private val viewModel: ReportViewRouteViewModel by viewModels()
    private val args: ReportViewRouteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_report_view_route, container, false
        )
        setHasOptionsMenu(true)
        loadEquipments()
        return binding.root
    }

    private fun loadEquipments() {
        viewModel.loadAuditEquipments(args.auditId)
            .subscribeAndObserve {
                initAdapter(it)
            }
    }

    private fun initAdapter(equipments: ArrayList<AuditEquipmentModel>) {
        reportViewRouteAdapter.update(equipments)
        reportViewRouteAdapter.setOnClickListener {
            navigateToAuditEquipmentDetails(it.id)
        }
        binding.recyclerView.adapter = reportViewRouteAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.default_options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun navigateToAuditEquipmentDetails(id: Int) {
        NavHostFragment.findNavController(this)
            .navigate(
                ReportViewRouteFragmentDirections
                    .actionReportViewRouteFragmentToReportEquipmentsFragment(id , args.isEditable)
            )
    }
}