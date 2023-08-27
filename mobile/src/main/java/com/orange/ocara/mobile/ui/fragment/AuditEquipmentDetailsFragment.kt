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
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.mobile.ui.viewmodel.AuditEquipmentDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuditEquipmentDetailsFragment : AuditEquipmentDetailsParentFragment() {
    val args: AuditEquipmentDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadNumberOfSubObjects()
    }
    private fun loadNumberOfSubObjects() {
        (viewModel as AuditEquipmentDetailsViewModel).getNumberOfSubObjects(args.auditEquipmentId)
                .subscribeAndObserve {
                    if (it > 0){
                        goToDetailsWithCharacterstics()
                    }
                }
    }

    private fun goToDetailsWithCharacterstics() {
        NavHostFragment.findNavController(this)
                .navigate(AuditEquipmentDetailsFragmentDirections
                        .actionEquipmentDetailsFragmentToAuditEquipmentWithCharacteristicsFragment(args.auditEquipmentId))
    }

    override fun initClickListeners() {
        binding.auditTV.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(
                    AuditEquipmentDetailsFragmentDirections
                            .actionEquipmentDetailsFragmentToQuestionsFragment(args.auditEquipmentId))
        }
    }

    override fun getViewModelObject(): AuditEquipmentDetailsViewModel {
        return ViewModelProvider(requireActivity()).get(AuditEquipmentDetailsViewModel::class.java)
    }

    override fun getAuditEquipmentId(): Int = args.auditEquipmentId

}