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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.orange.ocara.domain.models.SiteModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentAddSiteBinding
import com.orange.ocara.mobile.ui.extensionFunctions.afterTextChanged
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.mobile.ui.setFocusBackgroundForTextField
import com.orange.ocara.mobile.ui.validation.*
import com.orange.ocara.mobile.ui.viewmodel.AddSiteViewModel
import com.orange.ocara.mobile.ui.viewmodel.CreateAuditViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSiteFragment : OcaraFragment() {

    lateinit var binding: FragmentAddSiteBinding
    lateinit var viewModel: AddSiteViewModel
    private val args: AddSiteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_site, container, false
        )
        viewModel = ViewModelProvider(requireActivity()).get(AddSiteViewModel::class.java)
        binding.lifecycleOwner = this
        binding.createSiteButton.setOnClickListener { validateSite() }

        binding.siteNameET.afterTextChanged {

            binding.siteNameET.contentDescription = ""
            binding.siteAddressET.contentDescription = ""
            binding.sitePostalCodeET.contentDescription = ""
            binding.siteCityET.contentDescription = ""

            if (binding.fillNameErrTxt.visibility == View.VISIBLE) {
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
        }


        binding.siteAddressET.afterTextChanged {

            binding.siteNameET.contentDescription = ""
            binding.siteAddressET.contentDescription = ""
            binding.sitePostalCodeET.contentDescription = ""
            binding.siteCityET.contentDescription = ""
            if (binding.fillAddressErrTxt.visibility == View.VISIBLE) {
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
        }

        binding.sitePostalCodeET.afterTextChanged {

            binding.siteNameET.contentDescription = ""
            binding.siteAddressET.contentDescription = ""
            binding.sitePostalCodeET.contentDescription = ""
            binding.siteCityET.contentDescription = ""
            if (binding.fillCodeErrTxt.visibility == View.VISIBLE) {
                if (validateSitePostalCode(binding.sitePostalCodeET)) {
                    binding.fillCodeErrTxt.visibility = View.GONE
                    ViewCompat.setBackground(
                        binding.sitePostalCodeET,
                        ResourcesCompat.getDrawable(
                            requireContext().resources,
                            R.drawable.edit_text_bg_focus,
                            null
                        )
                    )
                }
            }
        }

        binding.siteCityET.afterTextChanged {

            binding.siteNameET.contentDescription = ""
            binding.siteAddressET.contentDescription = ""
            binding.sitePostalCodeET.contentDescription = ""
            binding.siteCityET.contentDescription = ""
            if (binding.fillCityErrTxt.visibility == View.VISIBLE) {
                if (validateSiteCity(binding.siteCityET)) {
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
        }
        setFocusColorForAllEditText()
        return binding.root
    }

    fun setFocusColorForAllEditText() {
        setFocusBackgroundForTextField(binding.siteNameET, requireActivity())
        setFocusBackgroundForTextField(binding.siteAddressET, requireActivity())
        setFocusBackgroundForTextField(binding.sitePostalCodeET, requireActivity())
        setFocusBackgroundForTextField(binding.siteCityET, requireActivity())
    }

    private fun validateFieldsWithErrMsgs(): Boolean {
        if (!validateSiteName(binding.siteNameET)) {
            binding.siteNameET.requestFocus()
            ViewCompat.setBackground(
                binding.siteNameET,
                ResourcesCompat.getDrawable(resources, R.drawable.edit_text_bg_error, null)
            )
            binding.siteAddressET.clearFocus()
            binding.sitePostalCodeET.clearFocus()
            binding.siteCityET.clearFocus()

            binding.siteNameET.contentDescription = getString(R.string.fill_field_error_title)+ " " + getString(R.string.mobile_create_site_name)
            binding.siteAddressET.contentDescription = ""
            binding.sitePostalCodeET.contentDescription = ""
            binding.siteCityET.contentDescription = ""

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
            binding.sitePostalCodeET.clearFocus()
            binding.siteCityET.clearFocus()


            binding.siteNameET.contentDescription = ""
            binding.siteAddressET.contentDescription = getString(R.string.fill_field_error_title)+ " " + getString(R.string.mobile_create_site_address)
            binding.sitePostalCodeET.contentDescription = ""
            binding.siteCityET.contentDescription = ""


            binding.fillNameErrTxt.visibility = View.GONE
            binding.fillAddressErrTxt.visibility = View.VISIBLE
            binding.fillCodeErrTxt.visibility = View.GONE
            binding.fillCityErrTxt.visibility = View.GONE
            return false
        }
        if (!validateSitePostalCode(binding.sitePostalCodeET)) {
            binding.siteNameET.clearFocus()
            binding.siteAddressET.clearFocus()
            binding.sitePostalCodeET.requestFocus()

            binding.siteNameET.contentDescription = ""
            binding.siteAddressET.contentDescription = ""
            binding.sitePostalCodeET.contentDescription = getString(R.string.fill_field_error_title)+ " " + getString(R.string.mobile_create_site_code_hint)
            binding.siteCityET.contentDescription = ""

            ViewCompat.setBackground(
                binding.sitePostalCodeET,
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
            binding.sitePostalCodeET.clearFocus()
            binding.siteCityET.requestFocus()

            binding.siteNameET.contentDescription = ""
            binding.siteAddressET.contentDescription = ""
            binding.sitePostalCodeET.contentDescription = ""
            binding.siteCityET.contentDescription = getString(R.string.fill_field_error_title)+ " " + getString(R.string.create_site_town)

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

    private fun validateSite() {
//        if (!validateSiteName(binding.siteNameET)) {
//            showSiteNameMissingError()
//            return
//        }
//        if (!validateSitePostalCode(binding.sitePostalCodeET)) {
//            showSiteCodeError()
//            return
//        }
        if (!validateFieldsWithErrMsgs())
            return

        val siteModel = SiteModel.builder()

        if (validateSiteName(binding.siteNameET))
            siteModel.name(binding.siteNameET.text.toString())

        if (validateSiteCity(binding.siteCityET))
            siteModel.addressCity(binding.siteCityET.text.toString())

        if (validateSiteAddress(binding.siteAddressET))
            siteModel.addressStreet(binding.siteAddressET.text.toString())

        // siteCodeET
        if (validateSitePostalCode(binding.sitePostalCodeET))
            siteModel.addressCode(binding.sitePostalCodeET.text.toString().toInt())

        viewModel.insertSite(siteModel.build()).subscribeAndObserve {
            navigateToBackScreen(it.toInt())
        }
    }

    private fun showSiteCodeError() {
        binding.sitePostalCodeET.error = "should be 5 digits"
        binding.sitePostalCodeET.requestFocus()
    }

    private fun showSiteNameMissingError() {
        binding.siteNameET.error = resources.getString(R.string.required_field)
        binding.siteNameET.requestFocus()
    }


    private fun validatePostalCodeIsFiveDigits(): Boolean {
        val minimumFiveDigitsNumber = 10000
        val maximumFiveDigitsNumber = 99999
        return binding.siteCodeET.text.toString()
            .toInt() in minimumFiveDigitsNumber..maximumFiveDigitsNumber
    }

    private fun navigateToBackScreen(siteId: Int) {
        if (args.auditId == -1) {
            navigateToCreateAudit(siteId)
        } else {
            navigateToEditAudit(siteId)
        }
    }

    private fun navigateToEditAudit(siteId: Int) {
        NavHostFragment.findNavController(this).navigate(
            AddSiteFragmentDirections
                .actionAddSiteFragmentToEditAuditFragment(auditId = args.auditId, siteId = siteId)
        )
    }

    private fun navigateToCreateAudit(siteId: Int) {
        val action =
            AddSiteFragmentDirections.actionAddSiteFragmentToCreateAuditFragment(siteId = siteId)
        NavHostFragment.findNavController(this)
            .navigate(action)
    }
}