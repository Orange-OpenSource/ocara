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

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.orange.ocara.domain.models.AuditEquipmentWithNumberOfCommentAndOrderModel
import com.orange.ocara.mobile.AuditEquipmentDetailsNavigationDirections
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentEquipmentDetailsBinding
import com.orange.ocara.mobile.showSoftKeyboard
import com.orange.ocara.mobile.ui.dialogs.GenericDeleteConfirmationDialog
import com.orange.ocara.mobile.ui.extensionFunctions.afterTextChanged
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.mobile.ui.viewmodel.AuditEquipmentDetailsParentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
abstract class AuditEquipmentDetailsParentFragment : OcaraFragment() {
    lateinit var viewModel: AuditEquipmentDetailsParentViewModel
    lateinit var binding: FragmentEquipmentDetailsBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_equipment_details, container, false
        )
        viewModel = getViewModelObject()
        setHasOptionsMenu(true)
        initActionBar()
        addObserversForLiveData()
        initUpdateObjectName()
        initGoToComments()
        viewModel.getAuditEquipment(getAuditEquipmentId())
        initClickListeners()
        return binding.root
    }

    private fun initGoToComments() {
        binding.commentsCount.setOnClickListener {
            NavHostFragment.findNavController(this)
                    .navigate(
                            AuditEquipmentDetailsNavigationDirections
                                    .actionAuditEquipmentDetailsListCommentsFragment(getAuditEquipmentId())
                    )
        }
        binding.commentsIcon.setOnClickListener {
            NavHostFragment.findNavController(this)
                    .navigate(
                            AuditEquipmentDetailsNavigationDirections
                                    .actionAuditEquipmentDetailsListCommentsFragment(getAuditEquipmentId())
                    )
        }
    }


    private fun initUpdateObjectName() {
        binding.editIcon.setOnClickListener {
            enterEditMode()
        }
        binding.checkIcon.setOnClickListener {
            onEditObjNameEnd()
        }

        binding.editTextObjectName.afterTextChanged {
            if (binding.fillNameTxt.visibility == View.VISIBLE) {
                if (!binding.editTextObjectName.text.isEmpty()) {
                    binding.fillNameTxt.visibility = View.GONE
                }
            }
        }
        binding.editTextObjectName.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onEditObjNameEnd()
            }
            false
        }
    }

    private fun onEditObjNameEnd() {
        if (binding.editTextObjectName.text.isEmpty()) {
//                showErrorDialogForTxt()
            binding.editTextObjectName.requestFocus()
            binding.editTextObjectName.backgroundTintList =
                    getResources().getColorStateList(R.color.red)
//                binding.editTextObjectName.error =resources.getString(R.string.required_field)
            binding.fillNameTxt.visibility = View.VISIBLE
        } else {
            updateAuditObjectName()
            binding.fillNameTxt.visibility = View.GONE
        }
    }

    private fun exitEditMode() {
        hideKeyboard()
        binding.fillNameTxt.visibility = View.GONE
        binding.objNameTv.visibility = View.VISIBLE
        binding.editTextObjectName.visibility = View.GONE
        binding.editIcon.visibility = View.VISIBLE
        binding.checkIcon.visibility = View.GONE
        viewModel.loadAuditObjectName(getAuditEquipmentId())
                .subscribeAndObserve {
                    binding.objNameTv.text = it
                }

    }

    private fun enterEditMode() {
        binding.objNameTv.visibility = View.INVISIBLE
        binding.editTextObjectName.visibility = View.VISIBLE
        binding.editIcon.visibility = View.INVISIBLE
        binding.checkIcon.visibility = View.VISIBLE
        viewModel.loadAuditObjectName(getAuditEquipmentId())
                .subscribeAndObserve {
                    binding.editTextObjectName.setText(it)
                    binding.editTextObjectName.requestFocus()
                    binding.editTextObjectName.showSoftKeyboard()
                    binding.editTextObjectName.setSelection(binding.editTextObjectName.length())
                }
    }

    private fun updateAuditObjectName() {
        viewModel.updateAuditEquipmentName(getAuditEquipmentId(), getNewObjectName())
                .subscribeAndObserve {
//                    (activity as AppCompatActivity).supportActionBar?.setTitle(getNewObjectName())
                    exitEditMode()
                }
    }

    private fun getNewObjectName() = binding.editTextObjectName.text.toString()


    private fun initActionBar() {
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.object_tile)
//        viewModel.loadAuditObjectName(getAuditEquipmentId())
//                .subscribeAndObserve {
//                    (activity as AppCompatActivity).supportActionBar?.setTitle(it)
//                }
//        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.`object`)
        (activity as AppCompatActivity).supportActionBar?.setLogo(null)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.audit_equipment_details_menu, menu)
         menu.findItem(R.id.delete).setContentDescription(getString(R.string.content_desc_delete_obj))
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> {
                deleteDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun deleteDialog() {
        val deleteDialog = GenericDeleteConfirmationDialog(
                title = getString(R.string.delete_obj),
                msg = getString(R.string.delete_audit_obj_confirmation_msg),
                onDeleteConfirmed = this::deleteAuditEquipment
        )
        deleteDialog.show(childFragmentManager, "DeleteCommentDialog")
    }

    private fun deleteAuditEquipment() {
        viewModel.deleteAuditEquipment(getAuditEquipmentId())
                .subscribeAndObserve {
                    activity?.onBackPressed()
                }
    }

    open fun updateUI(auditEquipmentModel: AuditEquipmentWithNumberOfCommentAndOrderModel) {
        binding.objNameTv.text = auditEquipmentModel.name
        binding.commentsCount.text =
                getString(R.string.comments, auditEquipmentModel.numberOfComments)
    }

    private fun addObserversForLiveData() {
        viewModel.auditEquipmentLiveData.observe(viewLifecycleOwner) {
            updateUI(it)
        }
    }

    override fun onBack(): Boolean {
        Timber.d("in onBack")
        if (checkIfEditMode()) {
            exitEditMode()
            return true
        }
        return false
    }

    private fun checkIfEditMode(): Boolean {
        return binding.checkIcon.visibility == View.VISIBLE &&
                binding.editIcon.visibility == View.INVISIBLE &&
                binding.editTextObjectName.visibility == View.VISIBLE &&
                binding.objNameTv.visibility == View.INVISIBLE
    }

    abstract fun initClickListeners()

    abstract fun getViewModelObject(): AuditEquipmentDetailsParentViewModel

    abstract fun getAuditEquipmentId(): Int


}