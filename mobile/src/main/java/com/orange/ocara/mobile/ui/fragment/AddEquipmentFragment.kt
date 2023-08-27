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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.orange.ocara.domain.models.EquipmentForAddEquipmentModel
import com.orange.ocara.domain.models.EquipmentModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.ui.adapters.AddEquipmentAdapter
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.mobile.ui.viewmodel.AddEquipmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEquipmentFragment : PathChoicesParentFragment() {

    private val args: CurrentRouteFragmentArgs by navArgs()
    lateinit var viewModel: AddEquipmentViewModel
    lateinit var addEquipmentAdapter: AddEquipmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddEquipmentViewModel::class.java)
        changeButton()
        loadAudit()
        binding.objectsLabel.visibility = View.GONE
        loadAuditEquipments()
        return view
    }

    private fun changeButton() {
        binding.floatingAddButton.visibility = View.GONE
        binding.report.visibility = View.VISIBLE
        binding.report.text = getString(R.string.add_to_path)
        binding.report.setOnClickListener {
            val selectedEquipments = getSelectedEquipments()
            if (selectedEquipments.isEmpty()) {
                requireActivity().onBackPressed()
            } else {
                viewModel.addEquipmentsToAudit(selectedEquipments)
                        .subscribeAndObserve {
                            requireActivity().onBackPressed()
                        }
            }
        }
    }

    private fun getSelectedEquipments(): List<EquipmentModel> {
        val result = ArrayList<EquipmentModel>()
        for (eq in addEquipmentAdapter.data) {
            if (eq.isSelected) result.add(eq)
        }
        return result
    }

    private fun loadAudit(){
        viewModel.loadAuditById(args.auditId)
    }

    private fun loadAuditEquipments() {
        viewModel.getRulesetId(args.auditId)
                .subscribeAndObserve {
                    loadEquipments(it)
                }
    }

    private fun loadEquipments(rulesetId: Int) {
        viewModel.loadEquipments(rulesetId)
                .subscribeAndObserve {
                    initAdapter(it)
                }
    }

    private fun initAdapter(equipments: List<EquipmentForAddEquipmentModel>) {
        addEquipmentAdapter = AddEquipmentAdapter(requireContext())
        addEquipmentAdapter.data = equipments
        binding.recyclerView.adapter = addEquipmentAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
    }


    override fun handleEquipmentClick(id: Int) {
        Toast.makeText(requireContext(), "$id", Toast.LENGTH_LONG).show()
    }

    override fun navigateToTermsOfUse() {
        NavHostFragment.findNavController(this).navigate(R.id.action_addEquipmentFragment_to_termsOfUseContentFragment)
    }

    override fun getOptionsMenu() = R.menu.default_options_menu
    override fun getTitle() = R.string.path_choices
}