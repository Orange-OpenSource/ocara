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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.orange.ocara.data.cache.database.NonTables.AuditEquipmentForReport
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentReportSummaryResultsBinding
import com.orange.ocara.mobile.ui.adapters.ReportEquipmentsAdapter
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.mobile.ui.viewmodel.ReportProfileScoresViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@Deprecated("this on is removed from the xd")
@AndroidEntryPoint
abstract class ReportResultsSummaryFragment : OcaraFragment() {
    lateinit var binding: FragmentReportSummaryResultsBinding
    lateinit var viewModel: ReportProfileScoresViewModel

    lateinit var equipmentsAdapter: ReportEquipmentsAdapter



    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_report_summary_results, container, false)
        viewModel = ViewModelProvider(this).get(ReportProfileScoresViewModel::class.java)
        loadAuditObjects()
        showResultsFragment()
        initNextAndPrevButtonListsners()
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.default_options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initNextAndPrevButtonListsners() {

        binding.reportPreviousButton.setOnClickListener {
            val pos = (binding.equipmentsList.getLayoutManager() as LinearLayoutManager).findFirstVisibleItemPosition()
            Timber.d("REPORT POS " + pos)
            System.err.println("REPORT POS " + pos)
            binding.equipmentsList.scrollToPosition(pos - 2)
        }
        binding.reportNextButton.setOnClickListener {
            val pos = (binding.equipmentsList.getLayoutManager() as LinearLayoutManager).findFirstVisibleItemPosition()
            Timber.d("REPORT POS " + pos)
            System.err.println("REPORT POS " + pos)
            binding.equipmentsList.scrollToPosition(pos + 2)
        }
    }

    private fun loadAuditObjects() {
        System.err.println("REPORT load obj")

        viewModel.getEquipments(getAuditId()).subscribeAndObserve {
//            System.err.println("REPORT setEquipmentsForReport " + args.auditId + " " + it.size)

            setEquipmentsForReport(it)
        }

    }

    private fun setEquipmentsForReport(equipments: List<AuditEquipmentForReport>) {
//        System.err.println("REPORT setEquipmentsForReport "+ equipments.size)
        equipmentsAdapter = ReportEquipmentsAdapter(equipments as ArrayList<AuditEquipmentForReport>)
        equipmentsAdapter.setOnClickListener {
            onEquipmentClick(it)
        }
        binding.equipmentsList.adapter = equipmentsAdapter
        binding.equipmentsList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        //        ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition()


    }

//    private fun onEquipmentClick(it: AuditEquipmentForReport) {
////        Toast.makeText(requireContext(), "EQ !! ", Toast.LENGTH_LONG).show()
//        NavHostFragment.findNavController(this)
//                .navigate(ReportResultsSummaryFragmentDirections.actionSummaryFragmentToReportEquipmentsFragment(it.id))
//    }

    abstract fun showResultsFragment()
    abstract fun onEquipmentClick(it: AuditEquipmentForReport)
    abstract fun getAuditId() : Int

}