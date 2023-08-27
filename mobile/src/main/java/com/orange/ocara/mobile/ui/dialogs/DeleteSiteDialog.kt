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
import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.FragmentManager
import com.orange.ocara.domain.models.SiteModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.DialogDeleteRulesetBinding
import com.orange.ocara.mobile.databinding.DialogGenericConfirmationBinding
import com.orange.ocara.mobile.ui.showProgressDialog
import com.orange.ocara.mobile.ui.viewmodel.AuditInfoParentViewModel
import com.orange.ocara.mobile.ui.viewmodel.CreateAuditViewModel
import timber.log.Timber

class DeleteSiteDialog(val viewModel: AuditInfoParentViewModel, val site: SiteModel) :
    OcaraDialog<DialogGenericConfirmationBinding>(R.layout.dialog_generic_confirmation) {

    companion object {
        const val TAG = "DeleteSiteDialog"
    }

    var progressDialog: ProgressDialog? = null

    override fun initClickListeners() {
        binding.msg.text = String.format(getString(R.string.delete_site_dialog_title),site.name)
        binding.msg.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_trash,
            0,
            0,
            0
        )
        binding.close.setOnClickListener {
            dismiss()
        }
        binding.confirm.setOnClickListener {
            progressDialog = showProgressDialog(requireActivity(), "deleting")
            viewModel.deleteSite(site.id)
        }
        binding.cancel.setOnClickListener {
            dismiss()
        }
        viewModel.siteDeleted.observe(viewLifecycleOwner) {
            if (it) {
                if (progressDialog != null)
                    progressDialog?.dismiss()
                dismiss()
            }
        }
    }

}