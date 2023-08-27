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

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.orange.ocara.domain.models.AuditModel
import com.orange.ocara.domain.models.AuditorModel
import com.orange.ocara.domain.models.RulesetModel
import com.orange.ocara.domain.models.SiteModel
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.mobile.ui.viewmodel.CreateAuditViewModel
import com.orange.ocara.utils.enums.AuditLevel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CreateAuditFragment : AuditInfoParentFragment() {
    override fun getViewModelInstance() =
        ViewModelProvider(requireActivity()).get(CreateAuditViewModel::class.java)

    private val args: CreateAuditFragmentArgs by navArgs()

    override fun loadData() {
        loadSites()
        viewModel.getRulesets()
        loadAuditors()
    }

    private fun loadSites() {
        viewModel.loadAllSite(args.siteId)
    }

    private fun loadAuditors() {
        viewModel.loadAuditors(args.auditorId)
    }

    override fun onAddAuditorAction() {
        NavHostFragment.findNavController(this)
            .navigate(CreateAuditFragmentDirections.actionCreateAuditFragmentToAddAuditorFragment())
    }

    override fun onAddSiteAction() {
        NavHostFragment.findNavController(this)
            .navigate(CreateAuditFragmentDirections.actionCreateAuditFragmentToAddSiteFragment())
    }

    override fun onSiteUpdated(site: SiteModel) {
        viewModel.updateSelectedSite(site)
        loadSites()
    }

    override fun onAuditorUpdated(auditor: AuditorModel) {
        viewModel.updateSelectedAuditor(auditor)
        loadAuditors()
    }

    override fun navigateToViewReferential(ruleset: RulesetModel) {
        NavHostFragment.findNavController(this)
            .navigate(
                CreateAuditFragmentDirections.actionCreateAuditFragmentToViewReferentialsFragment(
                    ruleset.id.toInt()
                )
            )
    }

    override fun initSeperateViews() {
        showMainBtn()
//        disableMainBtn()
        initRulesetAdapter()
        initSwitchButton()
        onRulesetDeleted()
        addObserverForRulesetDownloaded()
        binding.createAuditButton.setOnClickListener {
            onCreateAuditButtonClickedHandler()
        }
    }

    fun initSwitchButton() {
        selectRadioButton(binding.noviceSwitchButton)
        unSelectRadioButton(binding.expertSwitchButton)
    }

    private fun onRulesetDeleted() {
        (viewModel as CreateAuditViewModel).rulesetDeleted.observe(viewLifecycleOwner) {
            Timber.d("livedata =  $it")
            if (it) {
                Timber.d("onRulesetDeleted: is Complete and deleted from DB ")
                // after a ruleset is deleted the set of rulesets is changed so I need
                // to reload them again
                viewModel.getRulesets()
            }
        }
    }

    private fun onCreateAuditButtonClickedHandler() {
//        if (validateAudit()) {
//            createAuditAndInsertItInDB()
//        }
        if (validateAuditWithErrorMessage()) {
            createAuditAndInsertItInDB()
        }
    }

    private fun createAuditAndInsertItInDB() {
        val auditModel = AuditModel.builder()
            .site(viewModel.currentSite.value)
            .ruleset(getSelectedRuleset())
            .level(if (viewModel.expertModeLiveData.value!!) AuditLevel.EXPERT else AuditLevel.BEGINNER)
            .name(binding.auditNameET.text.toString())
            .auditorId(viewModel.currentAuditor.value!!.id)
            .date(System.currentTimeMillis())
            .version(1)
            .build()
        insertAudit(auditModel)
    }

    private fun addObserverForRulesetDownloaded() {
        viewModel.rulesetDownloadCompleteLiveData.observe(
            viewLifecycleOwner,
            { checkIfRulesetIsNotNull(it) })
    }

    private fun checkIfRulesetIsNotNull(ruleset: RulesetModel?) {
        if (ruleset != null) {
            savePreferedRulesetAndReloadRulesets(ruleset)
        }
    }

    private fun savePreferedRulesetAndReloadRulesets(ruleset: RulesetModel) {
        viewModel.savePreferedRuleset(ruleset)
        viewModel.getRulesets()
    }

    private fun showMainBtn() {
        binding.createAuditButton.visibility = View.VISIBLE
        binding.updateAuditButton.visibility = View.GONE
    }

    private fun insertAudit(audit: AuditModel) {
        (viewModel as CreateAuditViewModel).insertAudit(audit)
            .subscribeAndObserve { id ->
                NavHostFragment.findNavController(this).navigate(
                    CreateAuditFragmentDirections.actionCreateAuditFragmentToCurrentRouteFragment(id!!.toInt())
                )
                NavHostFragment.findNavController(this)
                        .navigate(CurrentRouteFragmentDirections.actionCurrentRouteFragmentToAddEquipmentFragment(id.toInt()))
                viewModel.clearAudit()
            }
    }

//    override fun enableMainBtn() {
//        binding.createAuditButton.isEnabled = true
//    }
//
//    override fun disableMainBtn() {
//        binding.createAuditButton.isEnabled = false
//    }
}

