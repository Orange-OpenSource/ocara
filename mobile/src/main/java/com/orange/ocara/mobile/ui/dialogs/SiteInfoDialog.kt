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
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentManager
import com.orange.ocara.domain.models.SiteModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.DialogSiteInfoBinding
import com.orange.ocara.mobile.showSoftKeyboard
import com.orange.ocara.mobile.ui.extensionFunctions.afterTextChanged
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.mobile.ui.setFocusBackgroundForTextField
import com.orange.ocara.mobile.ui.validation.validateSiteAddress
import com.orange.ocara.mobile.ui.validation.validateSiteCity
import com.orange.ocara.mobile.ui.validation.validateSiteName
import com.orange.ocara.mobile.ui.validation.validateSitePostalCode
import com.orange.ocara.mobile.ui.viewmodel.AuditInfoParentViewModel
import com.orange.ocara.mobile.ui.viewmodel.CreateAuditViewModel
import timber.log.Timber


class SiteInfoDialog(
        val mFragmentManager: FragmentManager,
        val viewModel: AuditInfoParentViewModel,
        val onSiteUpdated: (SiteModel) -> Unit
) :
        OcaraBottomSheetDialog<DialogSiteInfoBinding>(R.layout.dialog_site_info) {


    var editMode: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        setFocusColorForAllEditText()
        checkIfCanBeDeleted(viewModel.currentSite.value!!.id)
    }

    fun setFocusColorForAllEditText() {
        setFocusBackgroundForTextField(binding.siteNameET, requireActivity())
        setFocusBackgroundForTextField(binding.siteAddressET, requireActivity())
        setFocusBackgroundForTextField(binding.siteEditPostalCodeTxt, requireActivity())
        setFocusBackgroundForTextField(binding.siteCityET, requireActivity())

    }

    override fun initClickListeners() {
        binding.deleteButton.setOnClickListener {
            val deleteSiteDialog =
                    DeleteSiteDialog(viewModel, viewModel.currentSite.value!!)
            Timber.d("show delete dialog")
            deleteSiteDialog?.show(mFragmentManager, DeleteSiteDialog.TAG)
        }
        binding.modifySiteButton.setOnClickListener {
//            if (editMode) {
//                //perform edit
//                validateAndUpdateSite()
//            } else {
            binding.siteNameET.isEnabled = true
            binding.siteNameET.requestFocus()
            binding.siteNameET.setSelection(binding.siteNameET.length())
            binding.siteNameET.showSoftKeyboard()

            binding.siteCityET.isEnabled = true
            binding.siteAddressET.isEnabled = true
            binding.siteEditPostalCodeTxt.isEnabled = true
//            binding.modifySiteButton.icon =
//                AppCompatResources.getDrawable(requireContext(), R.drawable.form_tick_1)
//                binding.modifySiteButton.text = getString(R.string.update)
            binding.modifySiteButton.visibility = View.GONE
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

            checkIfCanBeDeleted(viewModel.currentSite.value!!.id)

            binding.modifySiteButton.visibility = View.VISIBLE

            binding.siteNameET.setText(viewModel.currentSite.value!!.name)
            binding.siteAddressET.setText(viewModel.currentSite.value!!.addressStreet)
            binding.siteEditPostalCodeTxt.setText(viewModel.currentSite.value!!.addressCode.toString())
            binding.siteCityET.setText(viewModel.currentSite.value!!.addressCity)


            binding.siteNameET.isEnabled = false
            binding.siteAddressET.isEnabled = false
            binding.siteEditPostalCodeTxt.isEnabled = false
            binding.siteCityET.isEnabled = false

            validateFieldsWithErrMsgs()
            editMode = !editMode
        }
        binding.confirm.setOnClickListener {
            validateAndUpdateSite()
        }

        binding.siteNameET.afterTextChanged {
            afterEditTxtChanged()
            if (binding.fillNameErrTxt.visibility == View.VISIBLE)
                if (validateSiteName(binding.siteNameET)) {
                    binding.fillNameErrTxt.visibility = View.GONE
                    ViewCompat.setBackground(
                            binding.siteNameET,
                            ResourcesCompat.getDrawable(
                                    requireContext().resources,
                                    R.drawable.edit_text_bg_focus,
                                    null
                            )
                    )
                }
        }
        binding.siteAddressET.afterTextChanged {
            afterEditTxtChanged()
            if (binding.fillAddressErrTxt.visibility == View.VISIBLE)
                if (validateSiteAddress(binding.siteAddressET)) {
                    binding.fillAddressErrTxt.visibility = View.GONE
                    ViewCompat.setBackground(
                            binding.siteAddressET,
                            ResourcesCompat.getDrawable(
                                    requireContext().resources,
                                    R.drawable.edit_text_bg_focus,
                                    null
                            )
                    )
                }
        }
        binding.siteEditPostalCodeTxt.afterTextChanged {
            afterEditTxtChanged()
            if (binding.fillCodeErrTxt.visibility == View.VISIBLE)
                if (validateSiteAddress(binding.siteEditPostalCodeTxt)) {
                    binding.fillCodeErrTxt.visibility = View.GONE
                    ViewCompat.setBackground(
                            binding.siteEditPostalCodeTxt,
                            ResourcesCompat.getDrawable(
                                    requireContext().resources,
                                    R.drawable.edit_text_bg_focus,
                                    null
                            )
                    )
                }
        }
        binding.siteCityET.afterTextChanged {
            afterEditTxtChanged()
            if (binding.fillCityErrTxt.visibility == View.VISIBLE)
                if (validateSiteAddress(binding.siteCityET)) {
                    binding.fillCityErrTxt.visibility = View.GONE
                    ViewCompat.setBackground(
                            binding.siteCityET,
                            ResourcesCompat.getDrawable(
                                    requireContext().resources,
                                    R.drawable.edit_text_bg_focus,
                                    null
                            )
                    )
                }
        }

        viewModel.siteDeleted.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.siteDeleted.postValue(false)
                dismiss()
            }
        }
    }

    private fun afterEditTxtChanged() {
        if (isSiteModified()) {
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

    private fun isSiteModified(): Boolean {
        if (!binding.siteNameET.text.toString()
                        .equals(viewModel.currentSite.value!!.name)
        )
            return true
        if (!binding.siteAddressET.text.toString()
                        .equals(viewModel.currentSite.value!!.addressStreet)
        )
            return true
        if (!binding.siteEditPostalCodeTxt.text.toString()
                        .equals(viewModel.currentSite.value!!.addressCode.toString())
        )
            return true

        if (!binding.siteCityET.text.toString().equals(viewModel.currentSite.value!!.addressCity))
            return true
        return false
    }

    private fun validateAndUpdateSite() {
//        if (!validateSiteName(binding.siteNameET)) {
//            showSiteNameMissingError()
//            return
//        }
//        if (!validateSitePostalCode(binding.siteEditPostalCodeTxt)) {
//            showSiteCodeError()
//            return
//        }
        if (!validateFieldsWithErrMsgs()) return

        val siteModel = SiteModel.builder()

        if (validateSiteName(binding.siteNameET))
            siteModel.name(binding.siteNameET.text.toString())

        if (validateSiteCity(binding.siteCityET))
            siteModel.addressCity(binding.siteCityET.text.toString())

        if (validateSiteAddress(binding.siteAddressET))
            siteModel.addressStreet(binding.siteAddressET.text.toString())

        // siteEditPostalCodeTxt
        if (validateSitePostalCode(binding.siteEditPostalCodeTxt))
            siteModel.addressCode(binding.siteEditPostalCodeTxt.text.toString().toInt())

        siteModel.noImmo(viewModel.currentSite.value!!.noImmo)
        siteModel.id(viewModel.currentSite.value!!.id)
        val updatedSite = siteModel.build()
        binding.modifySiteButton.icon =
                AppCompatResources.getDrawable(requireContext(), R.drawable.pencil_1)
        editMode = !editMode
        viewModel.updateSite(updatedSite).subscribeAndObserve {
            onSiteUpdated(updatedSite)
            dismiss()
        }
    }

    private fun showSiteCodeError() {
        binding.siteEditPostalCodeTxt.error = "should be 5 digits"
        binding.siteEditPostalCodeTxt.requestFocus()
    }

    private fun showSiteNameMissingError() {
        binding.siteNameET.error = resources.getString(R.string.required_field)
        binding.siteNameET.requestFocus()
    }


    private fun validatePostalCodeIsFiveDigits(): Boolean {
        val minimumFiveDigitsNumber = 10000
        val maximumFiveDigitsNumber = 99999
        return binding.siteEditPostalCodeTxt.text.toString()
                .toInt() in minimumFiveDigitsNumber..maximumFiveDigitsNumber
    }

    private fun validateFieldsWithErrMsgs(): Boolean {
        if (!validateSiteName(binding.siteNameET)) {
            binding.siteNameET.requestFocus()
            ViewCompat.setBackground(
                    binding.siteNameET,
                    ResourcesCompat.getDrawable(resources, R.drawable.edit_text_bg_error, null)
            )
            binding.siteAddressET.clearFocus()
            binding.siteEditPostalCodeTxt.clearFocus()
            binding.siteCityET.clearFocus()

            binding.fillNameErrTxt.visibility = View.VISIBLE
            binding.fillAddressErrTxt.visibility = View.GONE
            binding.fillCodeErrTxt.visibility = View.GONE
            binding.fillCityErrTxt.visibility = View.GONE
            return false
        }
        if (!validateSiteAddress(binding.siteAddressET)) {
            binding.siteNameET.clearFocus()
            binding.siteAddressET.requestFocus()
            ViewCompat.setBackground(
                    binding.siteAddressET,
                    ResourcesCompat.getDrawable(resources, R.drawable.edit_text_bg_error, null)
            )
            binding.siteEditPostalCodeTxt.clearFocus()
            binding.siteCityET.clearFocus()

            binding.fillNameErrTxt.visibility = View.GONE
            binding.fillAddressErrTxt.visibility = View.VISIBLE
            binding.fillCodeErrTxt.visibility = View.GONE
            binding.fillCityErrTxt.visibility = View.GONE
            return false
        }
        if (!validateSitePostalCode(binding.siteEditPostalCodeTxt)) {
            binding.siteNameET.clearFocus()
            binding.siteAddressET.clearFocus()
            binding.siteEditPostalCodeTxt.requestFocus()
            ViewCompat.setBackground(
                    binding.siteEditPostalCodeTxt,
                    ResourcesCompat.getDrawable(resources, R.drawable.edit_text_bg_error, null)
            )
            binding.siteCityET.clearFocus()

            binding.fillNameErrTxt.visibility = View.GONE
            binding.fillAddressErrTxt.visibility = View.GONE
            binding.fillCodeErrTxt.visibility = View.VISIBLE
            binding.fillCityErrTxt.visibility = View.GONE
            return false
        }
        if (!validateSiteCity(binding.siteCityET)) {
            binding.siteNameET.clearFocus()
            binding.siteAddressET.clearFocus()
            binding.siteEditPostalCodeTxt.clearFocus()
            binding.siteCityET.requestFocus()
            ViewCompat.setBackground(
                    binding.siteCityET,
                    ResourcesCompat.getDrawable(resources, R.drawable.edit_text_bg_error, null)
            )

            binding.fillNameErrTxt.visibility = View.GONE
            binding.fillAddressErrTxt.visibility = View.GONE
            binding.fillCodeErrTxt.visibility = View.GONE
            binding.fillCityErrTxt.visibility = View.VISIBLE
            return false
        }
        return true
    }

    fun checkIfCanBeDeleted(id: Int) {
        viewModel.getNumberOfRelatedAuditsToSite(id)
                .subscribeAndObserve { cnt ->
                    if (cnt > 0) {
                        binding.deleteButton.visibility = View.GONE
                    }
                }
    }

    companion object {
        const val TAG = "SiteInfoDialog"
    }

}