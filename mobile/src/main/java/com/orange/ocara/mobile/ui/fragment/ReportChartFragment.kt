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
import android.text.Html
import android.text.SpannableStringBuilder
import android.view.*
import androidx.core.text.bold
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.orange.ocara.data.cache.database.NonTables.AuditEquipmentForReport
import com.orange.ocara.domain.models.ProfileAnswersUIModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentReportResultBinding
import com.orange.ocara.mobile.ui.adapters.ProfilesListAdapter
import com.orange.ocara.mobile.ui.adapters.ReportPathElementRVAdapter
import com.orange.ocara.mobile.ui.managers.PieChartManager
import com.orange.ocara.mobile.ui.viewmodel.ReportViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

// the lower part of the report chart fragment

@AndroidEntryPoint
class ReportChartFragment : OcaraFragment() {

    companion object {
        const val TAG = "ReportResultFragment"
        const val AUD_ID_ARG = "audit_id"
    }

    val args: ReportChartFragmentArgs by navArgs()

    private lateinit var binding: FragmentReportResultBinding
    private lateinit var viewModel: ReportViewModel

    @Inject
    lateinit var adapter: ReportPathElementRVAdapter
//    private val args: ReportResultFragmentArgs by navArgs()

    private lateinit var pieChartManager: PieChartManager

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_report_result, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(ReportViewModel::class.java)
        setHasOptionsMenu(true)

        initViews()
        getEquipmentsData()
        return binding.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.default_options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getEquipmentsData() {
//        viewModel.getEquipments(requireArguments().getInt(AUD_ID_ARG))
        viewModel.getEquipments(args.auditId)

        viewModel.auditEquipmentsInfoLiveData.observe(viewLifecycleOwner, {

            initPathAdapter(it as ArrayList<AuditEquipmentForReport>)
        })
        //    setEquipmentsForReport(it)
        // }

//            private fun setEquipmentsForReport(equipments: List<AuditEquipmentForReport>) {
//        equipmentsAdapter = ReportEquipmentsAdapter(equipments as ArrayList<AuditEquipmentForReport>)
//        equipmentsAdapter.setOnClickListener {
//            navigateToReportEquipments(it)
        //  }
//        binding.equipmentsList.adapter = equipmentsAdapter
//        binding.equipmentsList.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initPathAdapter(data: ArrayList<AuditEquipmentForReport>) {
        adapter.data = data
        adapter.notifyDataSetChanged()
//        binding.reportAuditPath.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        binding.reportAuditPath.adapter = adapter
    }


    private fun initViews() {

        //init Pie chart
        pieChartManager = PieChartManager(binding.pieChart, requireActivity())

//        viewModel.getAuditScores(requireArguments().getInt(AUD_ID_ARG))
        viewModel.getAuditScores(args.auditId)

        viewModel.selectedProfilesLiveData.observe(viewLifecycleOwner) {

//            pieChartManager.updatePieChart(it)
//            updateChartPercentages(it)
//            initProfilesList(it as ArrayList<ProfileAnswersUIModel>)
            onEmptyPieChart()
            initProfilesList(it as ArrayList<ProfileAnswersUIModel>)


        }
    }

    private fun updateChartPercentages(list: java.util.ArrayList<ProfileAnswersUIModel>?) {

        var nok: Int = 0
        var ok: Int = 0
        var doubt: Int = 0
        var noAnswer: Int = 0

        list?.forEach {
            ok += it.numberOfOk
            nok += it.totalNumberOfNo
            doubt += it.numberOfDoubt
            noAnswer += it.numberOfNA

        }
        val total = ok + nok + doubt +
                noAnswer

        var okPerc: Float = 0.0f
        var nokPerc: Float = 0.0f
        var doubtPerc: Float = 0.0f
        var noAPerc: Float = 0.0f

        if (total != 0) {
            okPerc = (ok.div(total.toFloat())).times(100f)
            nokPerc = (nok.div(total.toFloat())).times(100f)
            doubtPerc = (doubt.div(total.toFloat())).times(100f)
            noAPerc = (noAnswer.div(total.toFloat())).times(100f)
        }

        val okString = SpannableStringBuilder()
                .append(resources.getString(R.string.control_ok))
                .bold { append(" (" + ok.toString() + ")") }
        binding.chartOkPercentage.text = format(okPerc)
        binding.chartOkTxt.text = okString

        val nokString = SpannableStringBuilder()
                .append(resources.getString(R.string.control_no))
                .bold { append(" (" + nok.toString() + ")") }
        binding.chartNoPercentage.text = format(nokPerc)
        binding.chartNoTxt.text = nokString


        val doubtString = SpannableStringBuilder()
                .append(resources.getString(R.string.control_doubt))
                .bold { append(" (" + doubt.toString() + ")") }
        binding.chartDoubtPercentage.text = format(doubtPerc)
        binding.chartDoubtTxt.text = doubtString

        binding.chartOkPercentage.announceForAccessibility(okString.toString() + " " + doubtString + " " +nokString)

    }

    private fun format(f: Float): String {
        return "%.1f".format(f) + "%"
//        return SpannableStringBuilder().bold { append("%.1f".format(f) + "%" )}
    }


    private fun initProfilesList(profiles: ArrayList<ProfileAnswersUIModel>) {
        val adapter = ProfilesListAdapter(binding.disabilitiesRV, profiles, ::onProfileSelect)
        binding.disabilitiesRV.adapter = adapter
        binding.disabilitiesRV.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }


    private fun onProfileSelect(selectedProfiles: List<ProfileAnswersUIModel>) {

        selectedProfiles.forEach {
            Timber.d("selected list : " + it.profileTypeModel.name)
        }

        if (!selectedProfiles.isNullOrEmpty()) {
            pieChartManager.updatePieChart(selectedProfiles as ArrayList<ProfileAnswersUIModel>)
            updateChartPercentages(selectedProfiles)
        } else {
            onEmptyPieChart()
        }
    }

    private fun onEmptyPieChart() {
        binding.chartOkPercentage.text = format(0.0f)
        binding.chartNoPercentage.text = format(0.0f)
        binding.chartDoubtPercentage.text = format(0.0f)
        pieChartManager.updateEmptyPieChart()


        val okString = SpannableStringBuilder()
                .append(resources.getString(R.string.control_ok))
                .bold { append(" (0)") }
        binding.chartOkTxt.text = okString

        val nokString = SpannableStringBuilder()
                .append(resources.getString(R.string.control_no))
                .bold { append(" (0)") }
        binding.chartNoTxt.text = nokString


        val doubtString = SpannableStringBuilder()
                .append(resources.getString(R.string.control_doubt))
                .bold { append(" (0)") }
        binding.chartDoubtTxt.text = doubtString
    }
}