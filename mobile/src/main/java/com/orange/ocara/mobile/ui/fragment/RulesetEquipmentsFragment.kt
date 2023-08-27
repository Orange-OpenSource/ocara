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
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.orange.ocara.domain.models.EquipmentModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentReferentialEquipmentsWithGridviewBinding
import com.orange.ocara.mobile.ui.adapters.EquipmentsAdapter
import com.orange.ocara.mobile.ui.viewmodel.RulsetEquipmentsViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class RulesetEquipmentsFragment(val onClickListener: (EquipmentModel) -> Unit) : Fragment() {

    lateinit var binding: FragmentReferentialEquipmentsWithGridviewBinding
    lateinit var viewModel: RulsetEquipmentsViewModel

    @Inject
    lateinit var equipmentsAdapter: EquipmentsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_referential_equipments_with_gridview, container, false)
        viewModel = ViewModelProvider(this).get(RulsetEquipmentsViewModel::class.java)

        initEquipmentsAdapter()
        initObjectSearch()
        initLiveDataListeners()
        val rulesetId = requireArguments().getLong("rulesetId")
        showEquipments(rulesetId)
        return binding.root
    }

    private fun initLiveDataListeners() {
        viewModel.equipmentsLiveData.observe(viewLifecycleOwner) { equipments ->
            Timber.d("${equipments?.size}")
            equipmentsAdapter.update(equipments?.filterNotNull()!!)
        }
    }

    private fun initObjectSearch() {
        binding.objNameEt.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filterEquipments(newText!!.lowercase())
                return false
            }

        })
        binding.objNameEt.setOnClickListener { view->binding.objNameEt.setIconified(false) }
    }
    private fun initEquipmentsAdapter() {
        binding.objectsGrid.adapter = equipmentsAdapter
        binding.objectsGrid.setOnItemClickListener { parent, view, position, id ->
            onClickListener(parent.adapter.getItem(position) as EquipmentModel)
            binding.objNameEt.setQuery("", false);
            binding.objNameEt.clearFocus();
        }
    }

    fun showEquipments(rulesetId: Long) {
        viewModel.getEquipments(rulesetId)
    }

}

