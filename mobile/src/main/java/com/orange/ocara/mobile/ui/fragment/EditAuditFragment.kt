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
import com.orange.ocara.mobile.ui.extensionFunctions.afterTextChanged
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.mobile.ui.viewmodel.EditAuditViewModel
import com.orange.ocara.utils.enums.AuditLevel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class EditAuditFragment : AuditInfoParentFragment() {
    private val args: EditAuditFragmentArgs by navArgs()
    private lateinit var audit: AuditModel

    override fun getViewModelInstance() =
        ViewModelProvider(requireActivity()).get(EditAuditViewModel::class.java)

    override fun initSeperateViews() {
        binding.updateAuditButton.visibility = View.VISIBLE
        binding.createAuditButton.visibility = View.GONE
        addClickListeners()
        addObserversForLiveData()
        binding.referentialSpinner.isEnabled = false
//        binding.referentialInfo.visibility = View.GONE
    }

    private fun addClickListeners() {
        binding.updateAuditButton.setOnClickListener {
            onUpdateAuditButtonClickedHandler()
        }
    }

    private fun addObserversForLiveData() {
        (viewModel as EditAuditViewModel).auditLiveData.observe(viewLifecycleOwner) {
            Timber.d("EditAuditFragment: addObserversForLiveData")
            audit = it
            loadAuditors(it)
            loadSites(it)
            setExpertiseLevel(it)
            setAuditName(it)
            viewModel.currentAuditor.observe(viewLifecycleOwner) {
                handleUpdateBtn()
            }
            viewModel.currentSite.observe(viewLifecycleOwner) {
                handleUpdateBtn()
            }
            viewModel.expertModeLiveData.observe(viewLifecycleOwner) {
                handleUpdateBtn()
            }
        }


    }

    private fun setAuditName(audit: AuditModel) {
        binding.auditNameET.setText(audit.name)
        binding.auditNameET.afterTextChanged {
            handleUpdateBtn()
        }

    }

    private fun handleUpdateBtn() {
        binding.updateAuditButton.isEnabled = checkIsAuditChanged()
    }

    private fun checkIsAuditChanged(): Boolean {
        if ((viewModel as EditAuditViewModel).getCurrentAudit() == null)
            return false
        val isNameChanged =
            !(binding.auditNameET.text!!.toString()
                .equals((viewModel as EditAuditViewModel).getCurrentAudit().name))
        var isSiteChanged = false
        if ((viewModel as EditAuditViewModel).hasSelectedSite()) {
            if ((viewModel as EditAuditViewModel).currentSite.value!!.id != (viewModel as EditAuditViewModel).getCurrentAudit().site.id) {
                isSiteChanged = true
            }
        }
        var isAuditorChanged = false
        if ((viewModel as EditAuditViewModel).hasSelectedAuditor()) {
            if ((viewModel as EditAuditViewModel).currentAuditor.value!!.id != (viewModel as EditAuditViewModel).getCurrentAudit().auditor.id) {
                isAuditorChanged = true
            }
        }
        var isExpertiseChanged = false
        if ((viewModel as EditAuditViewModel).getCurrentAudit().level == AuditLevel.BEGINNER && binding.expertSwitchButton.isChecked) {
            isExpertiseChanged = true
        }
        if ((viewModel as EditAuditViewModel).getCurrentAudit().level == AuditLevel.EXPERT && binding.noviceSwitchButton.isChecked) {
            isExpertiseChanged = true
        }
        return (isNameChanged || isSiteChanged || isAuditorChanged || isExpertiseChanged)
    }

    private fun setExpertiseLevel(it: AuditModel) {
        if (it.level == AuditLevel.BEGINNER) {
            unSelectRadioButton(binding.expertSwitchButton)
            selectRadioButton(binding.noviceSwitchButton)
            viewModel.expertMode = false
        } else {
            selectRadioButton(binding.expertSwitchButton)
            unSelectRadioButton(binding.noviceSwitchButton)
            viewModel.expertMode = true
        }
    }

    private fun loadSites(it: AuditModel) {
        if (args.siteId == -1) {
            viewModel.loadAllSite(it.site.id)
        } else {
            viewModel.loadAllSite(args.siteId)
        }
    }

    private fun loadAuditors(it: AuditModel) {
        if (args.auditorId == -1) {
            viewModel.loadAuditors(it.auditor.id)
        } else {
            viewModel.loadAuditors(args.auditorId)
        }
    }

    private fun onUpdateAuditButtonClickedHandler() {
//        if (validateAudit()) {
//            updateAuditInDB()
//        } else {
//            Toast.makeText(requireContext(),"there is an error in some input field",Toast.LENGTH_LONG).show()
//        }
        if (validateAuditWithErrorMessage()) {
            updateAuditInDB()
        }
    }

    override fun onSiteUpdated(site: SiteModel) {
        viewModel.updateSelectedSite(site)
        loadSites(audit)
        handleUpdateBtn()
    }

    override fun onAuditorUpdated(auditor: AuditorModel) {
        viewModel.updateSelectedAuditor(auditor)
        loadAuditors(audit)
        handleUpdateBtn()
    }

    private fun updateAuditInDB() {
        val auditModel = AuditModel.builder()
            .id(args.auditId)
            .site(viewModel.currentSite.value)
            .level(if (viewModel.expertMode) AuditLevel.EXPERT else AuditLevel.BEGINNER)
            .name(binding.auditNameET.text.toString())
            .auditorId(viewModel.currentAuditor.value!!.id)
            .date(System.currentTimeMillis())
            .build()
        updateAudit(auditModel)
    }

    private fun updateAudit(auditModel: AuditModel) {
        (viewModel as EditAuditViewModel).updateAudit(auditModel)
            .subscribeAndObserve {
                navigateToCurrentRoute()
                viewModel.clearAudit()
            }
    }

    private fun navigateToCurrentRoute() {
        NavHostFragment.findNavController(this)
            .navigate(
                EditAuditFragmentDirections.actionEditAuditFragmentToCurrentRouteFragment(
                    auditId = args.auditId
                )
            )

    }

//    override fun enableMainBtn() {
//        binding.updateAuditButton.isEnabled = true
//    }
//
//    override fun disableMainBtn() {
//        binding.updateAuditButton.isEnabled = false
//    }

    override fun loadData() {
        (viewModel as EditAuditViewModel).loadAudit(args.auditId)
    }

    override fun onAddAuditorAction() {
        NavHostFragment.findNavController(this)
            .navigate(
                EditAuditFragmentDirections.actionEditAuditFragmentToAddAuditorFragment(
                    auditId = args.auditId
                )
            )
    }

    override fun onAddSiteAction() {
        NavHostFragment.findNavController(this)
            .navigate(EditAuditFragmentDirections.actionEditAuditFragmentToAddSiteFragment(auditId = args.auditId))
    }

    override fun navigateToViewReferential(ruleset: RulesetModel) {
        NavHostFragment.findNavController(this)
            .navigate(
                EditAuditFragmentDirections.actionEditAuditFragmentToViewReferentialsFragment(
                    ruleset.id.toInt()
                )
            )
    }
}