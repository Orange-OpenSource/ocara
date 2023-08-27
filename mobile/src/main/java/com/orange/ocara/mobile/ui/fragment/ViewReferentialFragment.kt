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
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs

import com.orange.ocara.domain.models.RulesetModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentViewRefrentialBinding
import com.orange.ocara.mobile.ui.adapters.RulesetDropListAdapter
import com.orange.ocara.mobile.ui.adapters.RulesetItemSelectedListener
import com.orange.ocara.mobile.ui.viewmodel.ViewRefrentialViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewReferentialFragment : OcaraFragment() {

    private val RULESET = "RULESET"

    lateinit var binding: FragmentViewRefrentialBinding
    lateinit var viewModel: ViewRefrentialViewModel

    //    var rulsetId: Long = 0
    lateinit var ruleSet: RulesetModel
    var selectedRuleSetId: Long = -1
    lateinit var rulesetAdapter: RulesetDropListAdapter
    var searchModeOn = false
//    lateinit var equipmentsAdapter: RulesetEquipmentsAdapter

    private val args: ViewReferentialFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedRuleSetId = args.defaultRuleset.toLong()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_view_refrential, container, false
        )
        viewModel = ViewModelProvider(this).get(ViewRefrentialViewModel::class.java)



        setHasOptionsMenu(true)

        initRulesets()
        viewModel.getDownloadedRulesets()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.view_refrential_menu, menu)

        menu.findItem(R.id.search_rules).isVisible = !searchModeOn

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (searchModeOn) {
                searchModeOn = !searchModeOn
                requireActivity().invalidateOptionsMenu()
                showEquipments(ruleSet)
                return true
            }
        } else if (item.itemId == R.id.help) {
        } else if (item.itemId == R.id.search_rules) {
            searchModeOn = !searchModeOn;
            requireActivity().invalidateOptionsMenu()
            showRules(ruleSet);
        }

        return super.onOptionsItemSelected(item)
    }

    var livedataObserver: Observer<List<RulesetModel>> =
        Observer<List<RulesetModel>> { rulesetModels ->
            rulesetAdapter = RulesetDropListAdapter(requireContext(), rulesetModels)

            binding.spinner.adapter = rulesetAdapter
            setDefaultSelection(rulesetModels)
        }

    private fun setDefaultSelection(rulesetModels: List<RulesetModel>) {
        var position = -1
        rulesetModels.forEachIndexed { index, it ->
            if (it.id == selectedRuleSetId) position = index
        }
        if (position != -1)
            binding.spinner.setSelection(position)
    }

    private fun initRulesets() {
        binding.spinner.onItemSelectedListener = object : RulesetItemSelectedListener() {
            override fun onRulesetSelected(position: Int) {
                selectedRuleSetId = rulesetAdapter.getItem(position).id
                if (searchModeOn) {
                    showRules(rulesetAdapter.getItem(position))
                } else showEquipments(rulesetAdapter.getItem(position))
            }
        }
        viewModel.rulesetsLiveData.observe(viewLifecycleOwner, livedataObserver)

    }


    private fun showRules(selectedruleSet: RulesetModel) {
        ruleSet = selectedruleSet
        if (childFragmentManager.findFragmentById(binding.searchLayout.id) == null) {
            createRulesFragment(ruleSet)
        } else {
            if (childFragmentManager.findFragmentById(binding.searchLayout.id) is RulesetRulesFragment) {
                (childFragmentManager.findFragmentById(binding.searchLayout.id) as RulesetRulesFragment)?.showRules(
                    ruleSet.reference,
                    ruleSet.version
                )
            } else {
                createRulesFragment(ruleSet)
            }
        }
    }

    private fun createRulesFragment(ruleSet: RulesetModel) {
        val fragment = RulesetRulesFragment()

        val i = Bundle()
        i.putString("RULESET_REF", ruleSet.reference);
        i.putString("RULESET_VER", ruleSet.version);
//        i.putLong("rulesetId", id)
//        i.putString("rulesetVer", ruleset.getVersion())
        fragment.setArguments(i)
        childFragmentManager
            .beginTransaction()
            .replace(binding.searchLayout.id, fragment, RULESET)
            .commit()
        return
    }

    private fun showEquipments(selectedruleSet: RulesetModel) {
        ruleSet = selectedruleSet
        if (childFragmentManager.findFragmentById(binding.searchLayout.id) == null) {
            createEquipmentsFragment(ruleSet.id)
        } else {
            if (childFragmentManager.findFragmentById(binding.searchLayout.id) is RulesetEquipmentsFragment) {
                (childFragmentManager.findFragmentById(binding.searchLayout.id) as RulesetEquipmentsFragment)?.showEquipments(
                    ruleSet.id
                )
            } else {
                createEquipmentsFragment(ruleSet.id)
            }
        }
    }

    private fun createEquipmentsFragment(id: Long) {
        val fragment = RulesetEquipmentsFragment { equipment ->
//            Toast.makeText(context, equipment.name, Toast.LENGTH_LONG).show()
//            NavHostFragment.findNavController(this).navigate(R.id.action_equip_questions_Fragment)

            NavHostFragment.findNavController(this).navigate(
                ViewReferentialFragmentDirections
                    .actionEquipQuestionsFragment(
                        equipment.reference,
                        equipment.icon,
                        equipment.name,
                        ruleSet.reference,
                        ruleSet.type,
                        ruleSet.version
                    )
            )
        }
        val i = Bundle()
        i.putLong("rulesetId", id)
        fragment.setArguments(i)
        childFragmentManager
            .beginTransaction()
            .replace(binding.searchLayout.id, fragment, RULESET)
            .commit()
        return
    }
}