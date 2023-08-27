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
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.orange.ocara.domain.models.AuditorModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentAddAuditorBinding
import com.orange.ocara.mobile.ui.extensionFunctions.afterTextChanged
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.mobile.ui.setFocusBackgroundForTextField
import com.orange.ocara.mobile.ui.validation.*
import com.orange.ocara.mobile.ui.viewmodel.AddAuditorViewModel
import com.orange.ocara.mobile.ui.viewmodel.CreateAuditViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AddAuditorFragment : OcaraFragment() {

    lateinit var binding: FragmentAddAuditorBinding
    lateinit var viewModel: AddAuditorViewModel
    private val args: AddAuditorFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_auditor, container, false
        )
        viewModel = ViewModelProvider(requireActivity()).get(AddAuditorViewModel::class.java)

        binding.lifecycleOwner = this

        setFocusColorForAllEditText()

        binding.createAuditorButton.setOnClickListener { validateAuditor() }

        binding.auditorNameET.setFilters(getAuditorNameFilter())
        binding.auditorLastnameET.setFilters(getAuditorNameFilter())

        binding.auditorNameET.afterTextChanged {

            binding.auditorNameET.contentDescription = ""
            binding.auditorLastnameET.contentDescription = ""
            binding.auditorEmailET.contentDescription = ""

            if (binding.fillNameErrTxt.visibility == View.VISIBLE) {
                if (validateAuditorName(binding.auditorNameET)) {
                    binding.fillNameErrTxt.visibility = View.GONE
                    ViewCompat.setBackground(
                        binding.auditorNameET,
                        ResourcesCompat.getDrawable(
                            requireContext().resources,
                            R.drawable.edit_text_bg_focus,
                            null
                        )
                    )
                }
            }
        }

        binding.auditorLastnameET.afterTextChanged {
            binding.auditorNameET.contentDescription = ""
            binding.auditorLastnameET.contentDescription = ""
            binding.auditorEmailET.contentDescription = ""


            if (binding.fillLastNameErrTxt.visibility == View.VISIBLE) {
                if (validateAuditorName(binding.auditorLastnameET)) {
                    binding.fillLastNameErrTxt.visibility = View.GONE
                    ViewCompat.setBackground(
                        binding.auditorLastnameET,
                        ResourcesCompat.getDrawable(
                            requireContext().resources,
                            R.drawable.edit_text_bg_focus,
                            null
                        )
                    )
                }
            }
        }
        binding.auditorEmailET.afterTextChanged {
            binding.auditorNameET.contentDescription = ""
            binding.auditorLastnameET.contentDescription = ""
            binding.auditorEmailET.contentDescription = ""


            if (binding.fillMailErrTxt.visibility == View.VISIBLE) {
                if (validateAuditorEmail(binding.auditorEmailET)) {
                    binding.fillMailErrTxt.visibility = View.GONE
                    ViewCompat.setBackground(
                        binding.auditorEmailET,
                        ResourcesCompat.getDrawable(
                            requireContext().resources,
                            R.drawable.edit_text_bg_focus,
                            null
                        )
                    )
                }
            }
        }

        return binding.root

    }

    fun setFocusColorForAllEditText() {
        setFocusBackgroundForTextField(binding.auditorNameET, requireActivity())
        setFocusBackgroundForTextField(binding.auditorLastnameET, requireActivity())
        setFocusBackgroundForTextField(binding.auditorEmailET, requireActivity())
    }

    private fun validateFieldsWithErrMsgs(): Boolean {
        if (!validateAuditorName(binding.auditorNameET)) {
            binding.auditorNameET.requestFocus()
            binding.auditorNameET.contentDescription = getString(R.string.fill_field_error_title) + " " + getString(R.string.create_auditor_name)
            binding.auditorLastnameET.contentDescription = ""
            binding.auditorEmailET.contentDescription = ""


            ViewCompat.setBackground(
                binding.auditorNameET,
                ResourcesCompat.getDrawable(resources, R.drawable.edit_text_bg_error, null)
            )
            binding.auditorLastnameET.clearFocus()
            binding.auditorEmailET.clearFocus()

            binding.fillNameErrTxt.visibility = View.VISIBLE
            binding.fillLastNameErrTxt.visibility = View.GONE
            binding.fillMailErrTxt.visibility = View.GONE

            return false
        }
        if (!validateAuditorName(binding.auditorLastnameET)) {
            binding.auditorNameET.clearFocus()
            binding.auditorLastnameET.requestFocus()

            binding.auditorNameET.contentDescription = ""
            binding.auditorLastnameET.contentDescription = getString(R.string.fill_field_error_title)+ " " + getString(R.string.create_auditor_lastname)
            binding.auditorEmailET.contentDescription = ""

            ViewCompat.setBackground(
                binding.auditorLastnameET,
                ResourcesCompat.getDrawable(resources, R.drawable.edit_text_bg_error, null)
            )
            binding.auditorEmailET.clearFocus()

            binding.fillNameErrTxt.visibility = View.GONE
            binding.fillLastNameErrTxt.visibility = View.VISIBLE
            binding.fillMailErrTxt.visibility = View.GONE

            return false
        }
        if (!validateAuditorEmail(binding.auditorEmailET)) {
            binding.auditorNameET.clearFocus()
            binding.auditorLastnameET.clearFocus()
            binding.auditorEmailET.requestFocus()

            binding.auditorNameET.contentDescription = ""
            binding.auditorLastnameET.contentDescription =""
            binding.auditorEmailET.contentDescription = getString(R.string.fill_field_error_title)+ " " + getString(R.string.create_auditor_email)

            ViewCompat.setBackground(
                binding.auditorEmailET,
                ResourcesCompat.getDrawable(resources, R.drawable.edit_text_bg_error, null)
            )

            binding.fillNameErrTxt.visibility = View.GONE
            binding.fillLastNameErrTxt.visibility = View.GONE
            binding.fillMailErrTxt.visibility = View.VISIBLE

            return false
        }
        return true
    }

    private fun validateAuditor() {
        Timber.d("Create Auditor Btn is clicked !!")

//        if (!validateAuditorName(binding.auditorNameET)) {
//            binding.auditorNameET.error = resources.getString(R.string.required_field)
//            binding.auditorNameET.requestFocus()
//            return
//        } else if (!validateAuditorEmail(binding.auditorEmailET)) {
//            binding.auditorEmailET.error = "Input a valid email"
//            binding.auditorEmailET.requestFocus()
//            return
//        }
        if (!validateFieldsWithErrMsgs())
            return

        val auditorModel = AuditorModel.builder()

        if (validateAuditorName(binding.auditorNameET))
            auditorModel.firstName(binding.auditorNameET.text.toString())

        if (validateAuditorName(binding.auditorLastnameET))
            auditorModel.lastName(binding.auditorLastnameET.text.toString())

        if (validateAuditorEmail(binding.auditorEmailET))
            auditorModel.email(binding.auditorEmailET.text.toString())

        auditorModel.isSubscribed(binding.auditorCheck.isChecked)

        viewModel.insertAuditor(auditorModel = auditorModel.build())
            .subscribeAndObserve {
                navigateToBackScreen(it.toInt())
            }
    }

    private fun navigateToBackScreen(auditorId: Int) {
        if (args.auditId == -1) {
            navigateToCreateAudit(auditorId)
        } else {
            navigateToEditAudit(auditorId)
        }
    }

    private fun navigateToEditAudit(auditorId: Int) {
        NavHostFragment.findNavController(this)
            .navigate(
                AddAuditorFragmentDirections.actionAddAuditorFragmentToEditAuditFragment(
                    auditId = args.auditId,
                    auditorId = auditorId
                )
            )
    }

    private fun navigateToCreateAudit(auditorId: Int) {
        val action =
            AddAuditorFragmentDirections.actionAddAuditorFragmentToCreateAuditFragment(auditorId = auditorId)
        NavHostFragment.findNavController(this)
            .navigate(action)
    }
}