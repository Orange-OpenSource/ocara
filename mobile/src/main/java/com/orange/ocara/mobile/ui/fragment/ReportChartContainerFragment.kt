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
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.orange.ocara.data.cache.database.NonTables.AuditEquipmentForReport
@Deprecated("Report chart fragment is the used one")
class ReportChartContainerFragment : ReportResultsSummaryFragment() {

    val args: ReportChartContainerFragmentArgs by navArgs()

    override fun showResultsFragment() {
        if (childFragmentManager.findFragmentById(binding.resultsLayout.id) == null) {
            createResultsFragment()
        } else {
            if (childFragmentManager.findFragmentById(binding.resultsLayout.id) is ReportChartFragment) {
            } else {
                createResultsFragment()
            }
        }
    }

    private fun createResultsFragment() {
        val fragment = ReportChartFragment()
        val i = Bundle()
        i.putInt(ReportChartFragment.AUD_ID_ARG, args.auditId)
        fragment.setArguments(i)

        childFragmentManager
                .beginTransaction()
                .replace(binding.resultsLayout.id, fragment, ReportChartFragment.TAG)
                .commit()
    }

    override fun onEquipmentClick(it: AuditEquipmentForReport) {
        NavHostFragment.findNavController(this).navigate(ReportChartContainerFragmentDirections.actionChartFragmentToReportEquipmentsFragment(it.id , true))
    }

    override fun getAuditId() : Int {
        return args.auditId
    }

}