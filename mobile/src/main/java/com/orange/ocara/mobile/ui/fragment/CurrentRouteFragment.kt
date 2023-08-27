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
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.orange.ocara.domain.models.AuditEquipmentModel
import com.orange.ocara.domain.models.AuditModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.ui.adapters.CurrentRouteRVAdapter
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.mobile.ui.viewmodel.CurrentRouteViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@AndroidEntryPoint
class CurrentRouteFragment : PathChoicesParentFragment() {


    lateinit var viewModel: CurrentRouteViewModel

    @Inject
    lateinit var currentRouteAdapter: CurrentRouteRVAdapter
    private val args: CurrentRouteFragmentArgs by navArgs()

    lateinit var deleteMenuItem: MenuItem
    lateinit var editMenuItem: MenuItem

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CurrentRouteViewModel::class.java)
        initClickListeners()
        addObserverForEquipmentsOrderChanged()
        return view
    }

    override fun onResume() {
        super.onResume()
        loadAuditEquipments()
    }

    private fun addObserverForEquipmentsOrderChanged() {
        viewModel.addObserverForEquipmentsOrderChanged(viewLifecycleOwner) {
            if (it) {
                currentRouteAdapter.notifyDataSetChanged()

                binding.objectsLabel.announceForAccessibility(getString(R.string.content_desc_order_chaged))
                binding.objectsLabel.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
                binding.objectsLabel.requestFocus()

                if (currentRouteAdapter.itemCount <= 0) {
                    binding.noObjFoundView.visibility = View.VISIBLE
                }else{
                    binding.noObjFoundView.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        deleteMenuItem = menu.findItem(R.id.delete_mode)
        editMenuItem = menu.findItem(R.id.edit_mode)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_mode -> {
                deleteMenuItem = item
                if (!currentRouteAdapter.isDeleteMode()) {
                    openDeleteMode()
                }
            }
            R.id.edit_mode -> {
                editMenuItem = item
                if (!currentRouteAdapter.isEditMode()) {
                    openEditMode()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initClickListeners() {
        binding.floatingAddButton.setOnClickListener {
            if (currentRouteAdapter.isEditMode()) {
                performEdit()
            } else if (currentRouteAdapter.isDeleteMode()) {
                performDelete()
            } else
                addButtonClickListener()
        }
        binding.report.setOnClickListener {
            navigateToReport()
        }
    }

    private fun addButtonClickListener() {
        NavHostFragment.findNavController(this)
                .navigate(
                        CurrentRouteFragmentDirections.actionCurrentRouteFragmentToAddEquipmentFragment(
                                args.auditId
                        )
                )
    }

    private fun loadAuditEquipments() {
        viewModel.getAudit(args.auditId).subscribeAndObserve {
            if (it != null) {

                initAdapter(it.objects as ArrayList<AuditEquipmentModel>, it.name)
                checkReportButton(it)
            }
        }

    }

    fun initAdapter(equipments: List<AuditEquipmentModel>, audtName: String) {
        binding.objectsLabel.visibility = View.VISIBLE
        binding.objectsLabel.setText(audtName)

        currentRouteAdapter.data = equipments
        currentRouteAdapter.auditName = audtName
        currentRouteAdapter.clickListener = this::handleEquipmentClick
        currentRouteAdapter.onItemUp = this::onItemUpClick
        currentRouteAdapter.onItemDown = this::onItemDownClick
        binding.recyclerView.adapter = currentRouteAdapter
//        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        if (equipments.isEmpty()) {
            binding.noObjFoundView.visibility = View.VISIBLE
        }else{
            binding.noObjFoundView.visibility = View.GONE
        }
    }

    private fun onItemUpClick(pos: Int) {
        viewModel.onUpClick(pos)
    }

    private fun onItemDownClick(pos: Int) {
        viewModel.onDownClick(pos)
    }

    // report button should only appear if any equipment is tested
    private fun checkReportButton(it: AuditModel) {
        var isAnyTested = false
        for (eq in it.objects) {
            if (eq.isTested) {
                isAnyTested = true
                break
            }
        }
        if (isAnyTested) {
            binding.report.visibility = View.VISIBLE
        } else {
            binding.report.visibility = View.GONE
        }
    }

    override fun handleEquipmentClick(id: Int) {
        if (currentRouteAdapter.isDeleteMode() || currentRouteAdapter.isEditMode())
            return
        NavHostFragment.findNavController(this)
                .navigate(
                        CurrentRouteFragmentDirections.actionCurrentRouteFragmentToEquipmentDetailsFragment(
                                id
                        )
                )
    }

    override fun navigateToTermsOfUse() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_currentRouteFragment_to_termsOfUseContentFragment)
    }

    private fun navigateToReport() {
        closeEditMode()
        closeDeleteMode()
        NavHostFragment.findNavController(this)
                .navigate(CurrentRouteFragmentDirections.actionCurrentRouteFragmentToReportFragment(args.auditId))
    }

    private fun performEdit() {
        viewModel.performUpdateOrderInDB().subscribeAndObserve {
            closeEditMode()

        }
    }

    private fun openEditMode() {
//        binding.recyclerView.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
        deleteMenuItem.isVisible = false
        editMenuItem.isVisible = false
        currentRouteAdapter.openEditMode()
        binding.floatingAddButton.setImageResource(R.drawable.form_tick_1)
        binding.floatingAddButton.contentDescription = getString(R.string.content_desc_modify_route)

    }

    private fun closeEditMode() {
        if (currentRouteAdapter.isEditMode()) {
            currentRouteAdapter.exitEditMode()
        }
        binding.floatingAddButton.setImageResource(R.drawable.ic_baseline_add_24)
        binding.floatingAddButton.contentDescription = getString(R.string.content_desc_add_obj_to_route)
        deleteMenuItem.isVisible = true
        editMenuItem.isVisible = true
        loadAuditEquipments()
    }

    private fun performDelete() {
        viewModel.deleteAuditEquipments(currentRouteAdapter.data)
                .subscribeAndObserve {
                    closeDeleteMode()
                    publishDeleteDone()
                    loadAuditEquipments()
                }
    }

    private fun publishDeleteDone() {
//        binding.objectsLabel.setContentDescription(binding.objectsLabel.text.toString() + " Item deleted")
        binding.objectsLabel.announceForAccessibility(getString(R.string.content_desc_item_delete))
        binding.objectsLabel.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
        binding.objectsLabel.requestFocus()
    }

    private fun openDeleteMode() {
        deleteMenuItem.isVisible = false
        editMenuItem.isVisible = false
        currentRouteAdapter.openDeleteMode()
        binding.floatingAddButton.setImageResource(R.drawable.ic_trash)
        binding.floatingAddButton.contentDescription = getString(R.string.content_desc_del_obj_from_route)

    }

    private fun closeDeleteMode() {
        if (currentRouteAdapter.isDeleteMode()) {

            currentRouteAdapter.exitDeleteMode()
        }
        binding.floatingAddButton.setImageResource(R.drawable.ic_baseline_add_24)
        binding.floatingAddButton.contentDescription = getString(R.string.content_desc_add_obj_to_route)
        deleteMenuItem.isVisible = true
        editMenuItem.isVisible = true
    }

    override fun getOptionsMenu() = R.menu.current_route_options_menu

    override fun onBack(): Boolean {
        if (currentRouteAdapter.isEditMode() || currentRouteAdapter.isDeleteMode()) {
            closeDeleteMode()
            closeEditMode()
            return true
        }
        return false
    }

    override fun getTitle() = R.string.current_rout_title

}