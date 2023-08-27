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

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.orange.ocara.domain.models.AuditEquipmentWithNumberOfCommentAndOrderModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.ui.dialogs.SelectCharacteristicsDialog
import com.orange.ocara.mobile.ui.viewmodel.AuditEquipmentDetailsParentViewModel
import com.orange.ocara.mobile.ui.viewmodel.AuditEquipmentWithCharacteristicsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuditEquipmentWithCharacteristicsFragment : AuditEquipmentDetailsParentFragment() {

    val args: AuditEquipmentWithCharacteristicsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showViews()
    }

    override fun initClickListeners() {
        binding.auditTV.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(
                    AuditEquipmentWithCharacteristicsFragmentDirections
                            .actionAuditEquipmentWithCharacteristicsFragmentToQuestionsFragment(args.auditEquipmentId))
        }
        binding.subObjectsTv.setOnClickListener {
            val selectCharacteristicsDialog = SelectCharacteristicsDialog(
                    viewModel as AuditEquipmentWithCharacteristicsViewModel)
            selectCharacteristicsDialog.show(childFragmentManager, SelectCharacteristicsDialog.TAG)
        }
    }

    override fun getViewModelObject(): AuditEquipmentDetailsParentViewModel {
        return ViewModelProvider(requireActivity()).get(AuditEquipmentWithCharacteristicsViewModel::class.java)
    }

    override fun getAuditEquipmentId(): Int = args.auditEquipmentId

    fun showViews() {
        binding.view2.visibility = View.VISIBLE
        binding.subObjectsTv.visibility = View.VISIBLE
        binding.subObjectsIcon.visibility = View.VISIBLE
    }

    override fun updateUI(auditEquipmentModel: AuditEquipmentWithNumberOfCommentAndOrderModel) {
        super.updateUI(auditEquipmentModel)
        binding.subObjectsTv.text = getString(R.string.properties, auditEquipmentModel.children.size)
    }
}