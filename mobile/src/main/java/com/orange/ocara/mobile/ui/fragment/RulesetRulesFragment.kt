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
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orange.ocara.domain.models.RuleWithEquipmentModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentRefrentialRulesBinding
import com.orange.ocara.mobile.ui.adapters.RulesetRulesAdapter
import com.orange.ocara.mobile.ui.dialogs.RuleAnswerDialog
import com.orange.ocara.mobile.ui.dialogs.RuleDialog
import com.orange.ocara.mobile.ui.viewmodel.RulsetRulesViewModel
import timber.log.Timber

class RulesetRulesFragment : Fragment() {
    lateinit var binding: FragmentRefrentialRulesBinding
    lateinit var viewModel: RulsetRulesViewModel
    lateinit var rulesAdapter: RulesetRulesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_refrential_rules, container, false
        )
        viewModel = ViewModelProvider(requireParentFragment()).get(RulsetRulesViewModel::class.java)


        initRulesAdapter()
//        initRulesets()
        initLiveDataListener()
        initObjectSearch()

        val rulesetRef = requireArguments().getString("RULESET_REF")
        val rulesetVer = requireArguments().getString("RULESET_VER")
        showRules(rulesetRef!!, rulesetVer!!)
        return binding.root
    }

    private fun initLiveDataListener() {
        viewModel.rulesLiveData.observe(viewLifecycleOwner) { rules ->
            Timber.d("${rules?.size}")
            rulesAdapter.update(rules?.filterNotNull()!!)
        }
    }

    private fun initObjectSearch() {
        binding.ruleSearchEt.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filterRules(newText!!.lowercase())
                return false
            }

        })
        binding.ruleSearchEt.setOnClickListener { view -> binding.ruleSearchEt.setIconified(false) }

    }

    private fun initRulesAdapter() {
        rulesAdapter = RulesetRulesAdapter(ArrayList(), this::navigateToIllustrations)
        rulesAdapter.setOnClickListener { rule ->
//            Toast.makeText(requireContext(), "clicked" + rule.rule.label, Toast.LENGTH_LONG).show()
            onRuleClick(rule)
        }
        binding.objectsGrid.adapter = rulesAdapter
        LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false).apply {
            binding.objectsGrid.layoutManager = this
        }

//        GridLayoutManager(
//                requireContext(), 4, RecyclerView.VERTICAL, false).apply {
//            binding.objectsGrid.layoutManager = this
//        }
    }

    private fun navigateToIllustrations(ruleRef: String) {
        NavHostFragment.findNavController(requireParentFragment())
            .navigate(
                ViewReferentialFragmentDirections.actionViewRefFragmentToExplanationsFragment(
                    ruleRef,
                    requireArguments().getString("RULESET_REF")!!,
                    requireArguments().getString("RULESET_VER")!!.toInt()
                )
            )
    }

    fun showRules(ruleSetRef: String, ruleSetVer: String) {
        viewModel.getRules(ruleSetRef, ruleSetVer)

    }

    private fun onRuleClick(rule: RuleWithEquipmentModel) {
        val dialog = RuleDialog(rule.rule)
        dialog.show(parentFragmentManager, RuleAnswerDialog.TAG)
    }

}

