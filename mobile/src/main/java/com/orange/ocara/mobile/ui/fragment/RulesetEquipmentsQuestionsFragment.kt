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
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.orange.ocara.domain.models.QuestionModel
import com.orange.ocara.domain.models.RuleModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentReferentialEquipmentsQuestionsBindingImpl
import com.orange.ocara.mobile.ui.adapters.EquipmentQuestionsAdapter
import com.orange.ocara.mobile.ui.dialogs.RuleDialog
import com.orange.ocara.mobile.ui.viewmodel.EquipmentsQuestionsViewModel
import com.orange.ocara.mobile.ui.viewmodel.QuestionsViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class RulesetEquipmentsQuestionsFragment : OcaraFragment(){
    lateinit var binding: FragmentReferentialEquipmentsQuestionsBindingImpl
    val args: RulesetEquipmentsQuestionsFragmentArgs by navArgs()
    lateinit var viewModel: EquipmentsQuestionsViewModel
    lateinit var questionsAdapter: EquipmentQuestionsAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_referential_equipments_questions, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(EquipmentsQuestionsViewModel::class.java)
        viewModel.context = requireContext()
        binding.refrentialName.text=args.rulesetName
        binding.objectDescriptionTitle.text = args.equipmentType


        val path = requireContext().externalCacheDir.toString() + File.separator + args.equipmentIcon
        val icon = File(path)
        Picasso
                .with(context)
                .load(icon)
                .error(android.R.color.black)
                .into(binding.objectDescriptionImage)
        setHasOptionsMenu(true)
        initDataInViewModel()
        addObserversForLiveData()
        return binding.root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.view_refrential_menu, menu)

        menu.findItem(R.id.search_rules).isVisible =false

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun addObserversForLiveData() {
        viewModel.addObserverForDisplayedQuestions(QuestionsViewModel.ObserverData(viewLifecycleOwner) {
            displayedQuestionObserver(it)
        })
    }

    private fun displayedQuestionObserver(it: List<QuestionModel>) {
        questionsAdapter = EquipmentQuestionsAdapter(requireContext(), it, this::onClickForRule , this::navigateToIllustrations)
        binding.expandableList.setAdapter(questionsAdapter)
    }

    private fun navigateToIllustrations(ruleRef: String) {
        NavHostFragment.findNavController(this)
                .navigate(RulesetEquipmentsQuestionsFragmentDirections.actionEquipmentQuestionsFragmentToExplanationsFragment(ruleRef , args.rulesetRef , args.rulesetVer.toInt()))
    }

    private fun onClickForRule(rule: RuleModel) {
        val dialog = RuleDialog(rule)
        dialog.show(childFragmentManager, RuleDialog.TAG)
    }

    private fun initDataInViewModel() {
//        viewModel.loadData(args.auditEquipmentId)
        viewModel.loadQuestions(args.rulesetRef , args.equipmentRef , args.rulesetVer )
    }


}