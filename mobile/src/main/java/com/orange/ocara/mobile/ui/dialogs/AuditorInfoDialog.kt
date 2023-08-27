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
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import com.orange.ocara.domain.models.AuditorModel
import com.orange.ocara.domain.models.SiteModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.DialogAuditorInfoBinding
import com.orange.ocara.mobile.databinding.DialogSiteInfoBinding
import com.orange.ocara.mobile.showSoftKeyboard
import com.orange.ocara.mobile.ui.extensionFunctions.afterTextChanged
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.mobile.ui.models.LightAuditor
import com.orange.ocara.mobile.ui.setFocusBackgroundForTextField
import com.orange.ocara.mobile.ui.validation.getAuditorNameFilter
import com.orange.ocara.mobile.ui.validation.validateAuditorEmail
import com.orange.ocara.mobile.ui.validation.validateAuditorName
import com.orange.ocara.mobile.ui.viewmodel.AuditInfoParentViewModel
import com.orange.ocara.mobile.ui.viewmodel.CreateAuditViewModel
import timber.log.Timber

class AuditorInfoDialog(
        val mFragmentManager: FragmentManager,
        val viewModel: AuditInfoParentViewModel,
        val onAuditorUpdated: (AuditorModel) -> Unit
) :
        OcaraBottomSheetDialog<DialogAuditorInfoBinding>(R.layout.dialog_auditor_info) {

    var editMode: Boolean = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        setFocusColorForAllEditText()

        binding.firstNameInfoET.setFilters(getAuditorNameFilter())
        binding.lastNameInfoTV.setFilters(getAuditorNameFilter())

        checkIfCanBeDeleted(viewModel.currentAuditor.value!!.id)

    }

    fun setFocusColorForAllEditText() {
        setFocusBackgroundForTextField(binding.firstNameInfoET, requireActivity())
        setFocusBackgroundForTextField(binding.lastNameInfoTV, requireActivity())
        setFocusBackgroundForTextField(binding.emailInfoET, requireActivity())
    }


    override fun initClickListeners() {
        binding.deleteButton.setOnClickListener {
            val deleteAuditorDialog =
                    DeleteAuditorDialog(viewModel, viewModel.currentAuditor.value!!)
            Timber.d("show delete dialog")
            deleteAuditorDialog?.show(mFragmentManager, DeleteAuditorDialog.TAG)
        }
        binding.modifyButton.setOnClickListener {
//            if (editMode) {
//                //perform edit
//                validateAndUpdateAuditor()
//            } else {
            binding.firstNameInfoET.isEnabled = true
            binding.firstNameInfoET.requestFocus()
            binding.firstNameInfoET.setSelection(binding.firstNameInfoET.length())
            binding.firstNameInfoET.showSoftKeyboard()
//                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

            binding.lastNameInfoTV.isEnabled = true
            binding.emailInfoET.isEnabled = true
//                binding.modifyButton.text = getString(R.string.update)
//                binding.modifyButton.icon = AppCompatResources.getDrawable(requireContext(),R.drawable.form_tick_1)
            binding.modifyButton.visibility = View.GONE
            binding.cancel.visibility = View.VISIBLE
            binding.confirm.visibility = View.VISIBLE
            binding.deleteButton.visibility = View.GONE

            editMode = !editMode
//            }
        }
        binding.close.setOnClickListener {
            dismiss()
        }

        binding.cancel.setOnClickListener {
            binding.cancel.visibility = View.GONE
            binding.confirm.visibility = View.GONE
            binding.modifyButton.visibility = View.VISIBLE
            checkIfCanBeDeleted(viewModel.currentAuditor.value!!.id)

            binding.firstNameInfoET.setText(viewModel.currentAuditor.value!!.firstName)
            binding.lastNameInfoTV.setText(viewModel.currentAuditor.value!!.lastName)
            binding.emailInfoET.setText(viewModel.currentAuditor.value!!.email)

            binding.firstNameInfoET.isEnabled = false
            binding.lastNameInfoTV.isEnabled = false
            binding.emailInfoET.isEnabled = false

            validateFieldsWithErrMsgs()
            editMode = !editMode
        }

        binding.confirm.setOnClickListener {
            validateAndUpdateAuditor()
        }
        binding.firstNameInfoET.afterTextChanged {
            afterEditTextChanged()
            if (binding.fillNameErrTxt.visibility == View.VISIBLE) {
                if (validateAuditorName(binding.firstNameInfoET)) {
                    binding.fillNameErrTxt.visibility = View.GONE
                    ViewCompat.setBackground(
                            binding.firstNameInfoET,
                            ResourcesCompat.getDrawable(
                                    requireContext().resources,
                                    R.drawable.edit_text_bg_focus,
                                    null
                            )
                    )

                }
            }

        }
        binding.lastNameInfoTV.afterTextChanged {
            afterEditTextChanged()

            if (binding.fillLastNameErrTxt.visibility == View.VISIBLE) {
                if (validateAuditorName(binding.lastNameInfoTV)) {
                    binding.fillLastNameErrTxt.visibility = View.GONE
                    ViewCompat.setBackground(
                            binding.lastNameInfoTV,
                            ResourcesCompat.getDrawable(
                                    requireContext().resources,
                                    R.drawable.edit_text_bg_focus,
                                    null
                            )
                    )

                }
            }
        }
        binding.emailInfoET.afterTextChanged {
            afterEditTextChanged()
            if (binding.fillMailErrTxt.visibility == View.VISIBLE)
                if (validateAuditorEmail(binding.emailInfoET)) {
                    binding.fillMailErrTxt.visibility = View.GONE
                    ViewCompat.setBackground(
                            binding.emailInfoET,
                            ResourcesCompat.getDrawable(
                                    requireContext().resources,
                                    R.drawable.edit_text_bg_focus,
                                    null
                            )
                    )
                }
        }


        viewModel.auditorDeleted.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.auditorDeleted.postValue(false)
                dismiss()
            }
        }
    }

    private fun afterEditTextChanged() {
        if (isAuditorModified()) {
            binding.confirm.setBackgroundColor(requireContext().getColor(R.color.orange))
            binding.confirm.isClickable = true
            binding.confirm.isEnabled = true
            binding.confirm.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
        } else {
            binding.confirm.setBackgroundColor(requireContext().getColor(R.color.grey))
            binding.confirm.isClickable = false
            binding.confirm.isEnabled = false
            binding.confirm.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
        }
    }

    private fun isAuditorModified(): Boolean {
        if (!binding.firstNameInfoET.text.toString()
                        .equals(viewModel.currentAuditor.value!!.firstName)
        )
            return true
        if (!binding.lastNameInfoTV.text.toString()
                        .equals(viewModel.currentAuditor.value!!.lastName)
        )
            return true
        if (!binding.emailInfoET.text.toString().equals(viewModel.currentAuditor.value!!.email))
            return true
        return false
    }

    private fun validateFieldsWithErrMsgs(): Boolean {
        if (!validateAuditorName(binding.firstNameInfoET)) {
            binding.firstNameInfoET.requestFocus()
            binding.lastNameInfoTV.clearFocus()
            binding.emailInfoET.clearFocus()

            binding.fillNameErrTxt.visibility = View.VISIBLE
            ViewCompat.setBackground(
                    binding.firstNameInfoET,
                    ResourcesCompat.getDrawable(resources, R.drawable.edit_text_bg_error, null)
            )
            binding.fillLastNameErrTxt.visibility = View.GONE
            binding.fillMailErrTxt.visibility = View.GONE

            return false
        }
        if (!validateAuditorName(binding.lastNameInfoTV)) {
            binding.firstNameInfoET.clearFocus()
            binding.lastNameInfoTV.requestFocus()
            binding.emailInfoET.clearFocus()

            binding.fillNameErrTxt.visibility = View.GONE
            binding.fillLastNameErrTxt.visibility = View.VISIBLE
            ViewCompat.setBackground(
                    binding.lastNameInfoTV,
                    ResourcesCompat.getDrawable(resources, R.drawable.edit_text_bg_error, null)
            )
            binding.fillMailErrTxt.visibility = View.GONE

            return false
        }
        if (!validateAuditorEmail(binding.emailInfoET)) {
            binding.firstNameInfoET.clearFocus()
            binding.lastNameInfoTV.clearFocus()
            binding.emailInfoET.requestFocus()
            ViewCompat.setBackground(
                    binding.emailInfoET,
                    ResourcesCompat.getDrawable(resources, R.drawable.edit_text_bg_error, null)
            )
            binding.fillNameErrTxt.visibility = View.GONE
            binding.fillLastNameErrTxt.visibility = View.GONE
            binding.fillMailErrTxt.visibility = View.VISIBLE

            return false
        }
        return true
    }

    private fun validateAndUpdateAuditor() {
        Timber.d("Create Auditor Btn is clicked !!")

//        if (!validateAuditorName(binding.firstNameInfoET)) {
//            binding.firstNameInfoET.error = resources.getString(R.string.required_field)
//            binding.firstNameInfoET.requestFocus()
//            return
//        } else if (!validateAuditorEmail(binding.emailInfoET)) {
//            binding.emailInfoET.error = "Input a valid email"
//            binding.emailInfoET.requestFocus()
//            return
//        }
        if (!validateFieldsWithErrMsgs()) return

        val auditorModel = AuditorModel.builder()


        if (validateAuditorName(binding.firstNameInfoET))
            auditorModel.firstName(binding.firstNameInfoET.text.toString())

        if (validateAuditorName(binding.lastNameInfoTV))
            auditorModel.lastName(binding.lastNameInfoTV.text.toString())

        if (validateAuditorEmail(binding.emailInfoET))
            auditorModel.email(binding.emailInfoET.text.toString())

        auditorModel.isSubscribed(binding.sendEmailCheck.isChecked)

        auditorModel.id(viewModel.currentAuditor.value!!.id)
        val updatedAuditor = auditorModel.build()
        binding.modifyButton.icon =
                AppCompatResources.getDrawable(requireContext(), R.drawable.pencil_1)
        editMode = !editMode
        viewModel.updateAuditor(updatedAuditor).subscribeAndObserve {
            onAuditorUpdated(updatedAuditor)
            dismiss()
        }
    }

    fun checkIfCanBeDeleted(id: Int) {
        viewModel.getNumberOfRelatedAuditsToAuditor(id)
                .subscribeAndObserve { cnt ->
                    if (cnt > 0) {
                        binding.deleteButton.visibility = View.GONE
                    }
                }
    }
    companion object {
        const val TAG = "AuditorInfoDialog"
    }
}