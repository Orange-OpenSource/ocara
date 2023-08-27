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
import android.util.DisplayMetrics
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.orange.ocara.domain.models.RulesetModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentAboutNewdesignBinding
import com.orange.ocara.mobile.databinding.FragmentSignBinding
import com.orange.ocara.mobile.ui.dialogs.GenericConfirmationDialog
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.mobile.ui.managers.ShowAndCollapseViewWithAnimationManager
import com.orange.ocara.mobile.ui.viewmodel.ReportViewModel
import com.orange.ocara.mobile.ui.viewmodel.SignReportViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class SignReportFragment : OcaraFragment() {

    private lateinit var binding: FragmentSignBinding

    private val args: SignReportFragmentArgs by navArgs()

    lateinit var viewModel: SignReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sign, container, false
        )
        viewModel = ViewModelProvider(requireActivity()).get(SignReportViewModel::class.java)

        initViews()
        setHasOptionsMenu(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPaint()
    }

    private fun initPaint() {
        val metrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(metrics)
        binding.paintView.init(metrics)
        binding.paintView.setOnDraw {
            if (!binding.paintView.isBlank()) {
                binding.fillImgErrTxt.visibility = View.INVISIBLE

                binding.contentCard.strokeColor = getResources().getColor(R.color.grey)
            }
        }
        binding.paintView.setOnEndDraw { binding.scrollCont.setEnableScrolling(true) }
        binding.paintView.setOnStartDraw { binding.scrollCont.setEnableScrolling(false) }

    }

    private fun initViews() {
        (activity as AppCompatActivity).supportActionBar?.title =
            resources.getString(R.string.sign_screen_title)
        (requireActivity() as AppCompatActivity).supportActionBar?.setLogo(null)

        binding.save.setOnClickListener {
            onSave()
        }
        binding.cancelBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.undoBtn.setOnClickListener {
            binding.paintView.clear()
        }

    }

//    private fun saveImage() {
//        if (binding.paintView.isBlank()) {
//            binding.fillImgErrTxt.visibility = View.VISIBLE
//            return
//        }
//        binding.paintView.saveImage(args.auditId)
//    }

    fun onSave() {

        if (binding.paintView.isBlank()) {
            binding.fillImgErrTxt.visibility = View.VISIBLE
            binding.contentCard.strokeColor = getResources().getColor(R.color.red)
            return
        }
        binding.paintView.saveImage(args.auditId)
        performLock()
//        val confirmDownloadDialog =
//            GenericConfirmationDialog(getString(R.string.sign_conf_title),
//                getString(R.string.sign_conf_content), null, null,
//                onConfirmed = {
//                    saveImage()
//                    performLock() },
//                {})
//        confirmDownloadDialog.show(parentFragmentManager, "confirmDownloadDialog")
    }

    private fun performLock() {
        viewModel.lockAudit(args.auditId).subscribeAndObserve {
            gotoListAudits()
        }
    }

    private fun gotoListAudits() {

        findNavController().apply {
            popBackStack(R.id.homeFragment, false)
            navigate(R.id.listAuditsFragment)
        }
//        NavHostFragment.findNavController(this)
//            .navigate(SignReportFragmentDirections.actionSignReportFragmentToAllAudits())
    }
}