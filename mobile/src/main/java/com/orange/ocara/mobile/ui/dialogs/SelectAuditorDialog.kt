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

package com.orange.ocara.mobile.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.orange.ocara.domain.models.AuditorModel
import com.orange.ocara.domain.models.SiteModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.ui.adapters.AuditorsSearchAdapter
import com.orange.ocara.mobile.ui.adapters.BottomSheetSearchAdapter
import com.orange.ocara.mobile.ui.viewmodel.AuditInfoParentViewModel
import com.orange.ocara.mobile.ui.viewmodel.CreateAuditViewModel

class SelectAuditorDialog(val viewModel: AuditInfoParentViewModel, val onAuditorSelected: (AuditorModel) -> Unit, val onCreateAuditorClicked: () -> Unit)
    : SearchBottomSheetDialog<AuditorModel>() {

    lateinit var auditorsAdapter: AuditorsSearchAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        binding.infoTextview.visibility=View.VISIBLE
    }

    override fun initAdapter() {

        viewModel.allAuditorsLive.observe(viewLifecycleOwner,
                { auditors ->
                    auditorsAdapter = AuditorsSearchAdapter(auditors as ArrayList<AuditorModel>)
//                    bindAdapter(auditorsAdapter)
                    binding.objectsList.adapter = auditorsAdapter
                    auditorsAdapter.update(auditors)
                    auditorsAdapter.setOnClickListener { item ->
                        onItemClicked(item)
                    }
                })
    }

    fun onItemClicked(item: AuditorModel) {
        onAuditorSelected(item)
        dismiss()
    }

    override fun onAddClicked() {
        dismiss()
        onCreateAuditorClicked()
    }


    override fun onSearchTextChange(newText: String?) {

        var auditors: List<AuditorModel> = viewModel.filterAuditors(newText!!)
        auditorsAdapter.update(auditors)
    }

    override fun getTitle(): String {
        return getString(R.string.choose_auditor_title)
    }

    override fun getButtonText(): String {
        return getString(R.string.create_auditor_title)
    }
    companion object {
        const val TAG = "SelectAuditorDialog"
    }

}