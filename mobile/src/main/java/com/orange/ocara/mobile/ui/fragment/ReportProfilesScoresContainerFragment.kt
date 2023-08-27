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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.orange.ocara.data.cache.database.NonTables.AuditEquipmentForReport
import com.orange.ocara.mobile.R
@Deprecated("this on is removed from the xd")
class ReportProfilesScoresContainerFragment : ReportResultsSummaryFragment() {

    val args: ReportProfilesScoresContainerFragmentArgs by navArgs()

    override fun showResultsFragment() {
        if (childFragmentManager.findFragmentById(binding.resultsLayout.id) == null) {
            createResultsFragment()
        } else {
            if (childFragmentManager.findFragmentById(binding.resultsLayout.id) is ReportProfilesScoresFragment) {
            } else {
                createResultsFragment()
            }
        }
    }

    private fun createResultsFragment() {
        val fragment = ReportProfilesScoresFragment()
        val i = Bundle()
        i.putInt(ReportProfilesScoresFragment.AUD_ID_ARG, args.auditId)
        fragment.setArguments(i)

        childFragmentManager
                .beginTransaction()
                .replace(binding.resultsLayout.id, fragment, ReportProfilesScoresFragment.TAG)
                .commit()
    }
    override fun onEquipmentClick(it: AuditEquipmentForReport) {
        NavHostFragment.findNavController(this).navigate(ReportProfilesScoresContainerFragmentDirections.actionSummaryFragmentToReportEquipmentsFragment(it.id , true))
    }

    override fun getAuditId() : Int {
        return args.auditId
    }


}