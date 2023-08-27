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

import android.app.ProgressDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import com.orange.ocara.domain.models.RulesetModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.DialogRulesetInfoBinding
import com.orange.ocara.mobile.setFullScreen
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.mobile.ui.showProgressDialog
import com.orange.ocara.mobile.ui.testing.EspressoIdlingResource
import com.orange.ocara.mobile.ui.viewmodel.AuditInfoParentViewModel
import com.orange.ocara.mobile.ui.viewmodel.CreateAuditViewModel
import com.orange.ocara.utils.enums.RuleSetStat
import timber.log.Timber
import java.util.*

class RulesetInfoDialog(
    val mFragmentManager: FragmentManager,
    val ruleset: RulesetModel,
    val viewModel: AuditInfoParentViewModel,
    val viewRulesAction: (RulesetModel) -> Unit
) : OcaraDialog<DialogRulesetInfoBinding>(R.layout.dialog_ruleset_info) {

    companion object {

        const val TAG = "RulesetInfoDialog"
    }

    var deleteRulesetDialog: DeleteRulesetDialog? = null
    var mDownloadRuleSetProgressDialog: ProgressDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInfos()
        if (ruleset.id != null) {
            checkIfCanBeDeleted(ruleset.id.toInt())
        }
    }

    override fun onResume() {
        super.onResume()
        // setWidthPercent(95)
        setFullScreen()
    }

    fun checkIfCanBeDeleted(id: Int) {
        viewModel.getNumberOfRelatedAuditsToRuleset(id)
            .subscribeAndObserve { cnt ->
                if (cnt > 0) {
                    binding.deleteIcon.visibility = View.GONE
                }
            }
    }

    override fun initClickListeners() {
        binding.close.setOnClickListener {
            dismiss()
        }
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
        binding.viewRules.setOnClickListener {
            dismiss()
            viewRulesAction(ruleset)
        }
        binding.deleteIcon.setOnClickListener {
            deleteRulesetDialog =
                DeleteRulesetDialog(viewModel as CreateAuditViewModel, ruleset.id.toInt())
            Timber.d("show delete dialog")
            deleteRulesetDialog?.show(mFragmentManager, DeleteRulesetDialog.TAG)
        }
        binding.downloadButton.setOnClickListener {
            Log.d(TAG, "initClickListeners: download Ruleset button is clicked")

            // for Testing
            EspressoIdlingResource.increment()

            mDownloadRuleSetProgressDialog =
                showProgressDialog(activity!!, getString(R.string.downloading))
            viewModel.downloadRuleset(ruleset)
        }
        viewModel.rulesetDownloadCompleteLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                // this is essential to so this function doesn't get called again with true
                // when this dialog is created again
                viewModel.rulesetDownloadCompleteLiveData.postValue(null)
                hideProgressDialog()
                dismiss()

                //for testing.
                EspressoIdlingResource.decrement()

            }
        }
        //todo when uninstall refrential
        viewModel.rulesetDeleted.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.rulesetDeleted.postValue(false)
                dismiss()
            }
        }
    }

    private fun initInfos() {
        binding.name.text = ruleset.type
        binding.authorTV.text = ruleset.author
        binding.commentTV.text = ruleset.comment
        binding.ruleCategoryTV.text = ruleset.ruleCategoryName



        binding.dateTV.text = DateFormat.format("dd/MM/yyyy", ruleset.date.toLong())
        binding.versionTV.text = ruleset.version
        if (ruleset.stat == RuleSetStat.ONLINE) {
            binding.deleteIcon.visibility = View.INVISIBLE
            binding.viewRulesButton.visibility = View.INVISIBLE
            binding.downloadButton.visibility = View.VISIBLE
            binding.viewRules.visibility = View.INVISIBLE
        } else {
            binding.deleteIcon.visibility = View.VISIBLE
            binding.viewRulesButton.visibility = View.INVISIBLE
            binding.downloadButton.visibility = View.INVISIBLE
            binding.cancelButton.visibility = View.INVISIBLE
        }
    }

    fun hideProgressDialog() {
        if (mDownloadRuleSetProgressDialog != null) {
            mDownloadRuleSetProgressDialog?.dismiss()
        }
    }


}