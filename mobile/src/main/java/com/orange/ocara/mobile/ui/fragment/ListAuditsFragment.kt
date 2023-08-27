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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orange.ocara.domain.models.AuditForAuditListModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentListAuditsBinding
import com.orange.ocara.mobile.ui.adapters.AuditsAdapter
import com.orange.ocara.mobile.ui.dialogs.GenericConfirmationDialog
import com.orange.ocara.mobile.ui.dialogs.GenericDeleteConfirmationDialog
import com.orange.ocara.mobile.ui.dialogs.GenericDeleteNavGraphConfirmationDialog
import com.orange.ocara.mobile.ui.dialogs.OcaraDialog
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.mobile.ui.viewmodel.ListAuditsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListAuditsFragment : OcaraFragment(), AuditsAdapter.ClickListeners {
    lateinit var binding: FragmentListAuditsBinding
    lateinit var adapter: AuditsAdapter
    lateinit var viewModel: ListAuditsViewModel
    lateinit var menu: Menu
    lateinit var dialog: DialogFragment
    var deleteDialog: GenericDeleteConfirmationDialog? = null

    override fun onResume() {
        super.onResume()
        changeToolbarTitle()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_list_audits, container, false
        )
        viewModel = ViewModelProvider(this).get(ListAuditsViewModel::class.java)
        addLiveDataListeners()
        loadData()
        initClickListeners()
        setHasOptionsMenu(true)

        return binding.root
    }

    private fun changeToolbarTitle() {
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
                getString(R.string.all_audits)
        (requireActivity() as AppCompatActivity).supportActionBar?.setLogo(null)
    }

    private fun getFilterQuery(): String {
        if (this::menu.isInitialized) {
            val searchMenuItem = this.menu.findItem(R.id.search)
            val searchView = searchMenuItem?.actionView as SearchView?
            if (searchView != null && searchView.query.isNotEmpty()) {
                return searchView.query.toString()

            }
        }
        return ""
    }

    private fun loadData() {
        viewModel.loadAudits(getFilterQuery())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_audits_menu, menu)
        this.menu = menu
        menu.findItem(R.id.sort).setContentDescription(getString(R.string.content_desc_sort))
        addSearchView()
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun addSearchView() {

        val searchMenuItem = this.menu.findItem(R.id.search)
        val searchView = searchMenuItem.actionView as SearchView
        searchView.queryHint = getString(R.string.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                loadData()
                return false
            }
        })
        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                (activity as AppCompatActivity).supportActionBar?.setHomeActionContentDescription(getString(R.string.home_btn_content_desc));
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                // Write your code here
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort_by_date -> viewModel.sortByDate()
            R.id.sort_by_site -> viewModel.sortBySite()
            R.id.sort_by_status -> viewModel.sortByStatus()
            R.id.reverse -> viewModel.reverse()
        }
        item.setChecked(true)
        return super.onOptionsItemSelected(item)
    }


    private fun initClickListeners() {
        binding.addAudit.setOnClickListener {
            navigateToCreateAudit()
        }
        binding.noAuditsFoundButton.setOnClickListener {
            navigateToCreateAudit()
        }
    }

    private fun navigateToCreateAudit() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_listAuditsFragment_to_createAuditFragment)
    }

    private fun <T : RecyclerView.ViewHolder> setAdapterForRecyclerView(adapter: RecyclerView.Adapter<T>) {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        binding.recyclerView.addItemDecoration(
//            DividerItemDecoration(
//                context,
//                DividerItemDecoration.VERTICAL
//            )
//        )
    }

    private fun addLiveDataListeners() {
        viewModel.addObserverForAudits(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                auditsFound(it)
            } else {
                noAuditsFound()
            }
            if (getFilterQuery().isNotEmpty()) {
                announceItemsFound(it.size)
            }
        }
    }

    private fun announceItemsFound(itemsCount: Int) {

        if (this::menu.isInitialized) {
            val searchMenuItem = this.menu.findItem(R.id.search)
            val searchView = searchMenuItem?.actionView as SearchView?
            if (searchView != null) {
                searchView.announceForAccessibility(String.format(getString(R.string.content_desc_list_audit_items_found), itemsCount))
                searchView.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
                searchView.requestFocus()
            }
        }
    }

    private fun noAuditsFound() {
        setNoAuditsViewsVisibility(View.VISIBLE)
        setAuditsViewsVisibility(View.GONE)
        if (getFilterQuery().isNotEmpty()) {
            // if there is a filter applied and the result is empty , we don't want to show the add audit button
            binding.noAuditsFoundButton.visibility = View.GONE
        }
    }

    private fun auditsFound(it: ArrayList<AuditForAuditListModel>) {
        setNoAuditsViewsVisibility(View.GONE)
        setAuditsViewsVisibility(View.VISIBLE)
        setupAdapter(it)
    }

    private fun setAuditsViewsVisibility(visibility: Int) {
        binding.recyclerView.visibility = visibility
//        binding.addAudit.visibility = visibility
    }

    private fun setNoAuditsViewsVisibility(visibility: Int) {
//        binding.noAuditsFoundButton.visibility = visibility
        binding.noAuditsFoundView.visibility = visibility
    }

    private fun setupAdapter(audits: ArrayList<AuditForAuditListModel>) {
        adapter = AuditsAdapter(audits, requireContext(), this)
        adapter.setOnClickListener {
            if (it.isInProgress) {
                navigateToCurrentRoute(it.id)
            } else {
                navigateToReport(it.id)
            }
        }
        setAdapterForRecyclerView(adapter)
    }

    private fun navigateToReport(id: Int) {
        NavHostFragment.findNavController(this)
                .navigate(ListAuditsFragmentDirections.actionListAuditsFragmentToReportFragment(id))
    }

    private fun navigateToCurrentRoute(id: Int) {
        NavHostFragment.findNavController(this)
                .navigate(ListAuditsFragmentDirections.actionListAuditsFragmentToCurrentRouteFragment(id))
    }

    override fun onEditClicked(auditId: AuditForAuditListModel) {
        NavHostFragment.findNavController(this)
                .navigate(
                        ListAuditsFragmentDirections.actionListAuditsFragmentToEditAuditFragment(
                                auditId = auditId.id
                        )
                )
    }

    override fun onDeleteClicked(auditId: AuditForAuditListModel) {
        deleteDialog = GenericDeleteConfirmationDialog(
                getString(R.string.delete_audit),
                getString(
                        R.string.delete_audit_confirmation_msg,
                        viewModel.getAuditNameById(auditId.id)
                )
        ) {
            deleteAudit(auditId.id)
        }
        deleteDialog?.let {
            it.show(childFragmentManager, "DeleteCommentDialog")
        }
    }

//    override fun onDeleteClicked(auditId: AuditForAuditListModel) {
//        NavHostFragment.findNavController(this)
//                .navigate(ListAuditsFragmentDirections.actionListAuditsFragmentToGenericDeleteNavGraphConfirmationDialog(getString(R.string.delete_audit)
//                ,getString(
//                        R.string.delete_audit_confirmation_msg,
//                        viewModel.getAuditNameById(auditId.id)
//                )
//                ))
//        parentFragmentManager.setFragmentResultListener(GenericDeleteNavGraphConfirmationDialog.GENERIC_DELET_DIALOG_RES_DELETE_CONF_KEY , this) { _, bundle ->
//            deleteAudit(auditId.id)
//        }
//
//    }

    fun deleteAudit(id: Int) {
        viewModel.deleteAudit(id).subscribeAndObserve {
            loadData()
        }
    }

    override fun onCompleteClicked(auditId: AuditForAuditListModel) {
        navigateToCurrentRoute(auditId.id)
    }

    override fun onCopyClicked(audit: AuditForAuditListModel) {
        if (audit.isInProgress) {
            onCopyInProgressAudit(audit)
        } else {
            onCopyClosedAudit(audit)
        }
    }

    fun onCopyInProgressAudit(audit: AuditForAuditListModel) {


        dialog = GenericConfirmationDialog(
                title = null,
                msg = getString(R.string.retest_aud_keep_ans_msg, audit.name),
                confirmString = getString(R.string.retest_aud_keep_ans_msg_yes),
                cancelString = getString(R.string.retest_aud_keep_ans_msg_no),
                onConfirmed = { performCopy(audit, true) },
                onCancel = { performCopy(audit, false) }
        )
        dialog.show(childFragmentManager, "ConfirmAnswersDialog")
    }

    fun onCopyClosedAudit(audit: AuditForAuditListModel) {


        dialog = GenericConfirmationDialog(
                title = null,
                msg = getString(R.string.retest_aud_msg, audit.name),
                confirmString = getString(R.string.confirm),
                cancelString = getString(R.string.cancel),
                onConfirmed = { performCopy(audit, false) },
                onCancel = { }
        )
        dialog.show(childFragmentManager, "ConfirmAnswersDialog")
    }

    fun performCopy(audit: AuditForAuditListModel, copyAnswers: Boolean) {
        viewModel.copyAudit(audit.id, copyAnswers).subscribeAndObserve {
//            adapter.addCopiedItem(audit.updateAuditVersion())
            loadData()
        }
    }

    override fun onPause() {
//        deleteDialog?.let {
//            it.dismiss()
//        }
        super.onPause()
    }
}