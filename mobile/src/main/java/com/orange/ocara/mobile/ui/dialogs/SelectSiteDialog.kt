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

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.orange.ocara.domain.models.AuditorModel
import com.orange.ocara.domain.models.CommentModel
import com.orange.ocara.domain.models.SiteModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.DialogSelectSiteBinding
import com.orange.ocara.mobile.ui.adapters.AuditorsSearchAdapter
import com.orange.ocara.mobile.ui.adapters.SitesAdapter
import com.orange.ocara.mobile.ui.adapters.SitesSearchAdapter
import com.orange.ocara.mobile.ui.viewmodel.AuditInfoParentViewModel
import com.orange.ocara.mobile.ui.viewmodel.CreateAuditViewModel

class SelectSiteDialog(val viewModel: AuditInfoParentViewModel, val onSiteSelected: (SiteModel) -> Unit, val onCreateSiteClicked: () -> Unit)
    : SearchBottomSheetDialog<SiteModel>() {

    lateinit var sitesAdapter: SitesSearchAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()

    }

    override fun initAdapter() {

        viewModel.allSitesLive.observe(viewLifecycleOwner,
                { sites ->
                    sitesAdapter = SitesSearchAdapter(sites as ArrayList<SiteModel>)
                    binding.objectsList.adapter = sitesAdapter
                    sitesAdapter.update(sites)
                    sitesAdapter.setOnClickListener { item ->
                        onItemClicked(item)
                    }
                })
    }

    fun onItemClicked(item: SiteModel) {
        onSiteSelected(item)
        dismiss()
    }

    override fun onAddClicked() {
        onCreateSiteClicked()
        dismiss()
    }


    override fun onSearchTextChange(newText: String?) {
        sitesAdapter.update(viewModel.filterSites(newText!!))
    }

    override fun getTitle(): String {
        return getString(R.string.choose_site_title)
    }

    override fun getButtonText(): String {
        return getString(R.string.add_site)
    }
    companion object {
        const val TAG = "SelectSiteDialog"
    }

}