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

import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.DialogGenericDeleteConfirmationBinding
import com.orange.ocara.mobile.ui.fragment.IllustrationsFragmentArgs
import androidx.fragment.app.Fragment


class GenericDeleteNavGraphConfirmationDialog()
    : OcaraDialog<DialogGenericDeleteConfirmationBinding>(R.layout.dialog_generic_delete_confirmation) {
    private val args: GenericDeleteNavGraphConfirmationDialogArgs by navArgs()

    override fun initClickListeners() {
        setTitleAndMsg()
        binding.cancel.setOnClickListener {
            dismiss()
        }
        binding.close.setOnClickListener {
            dismiss()
        }
        binding.confirm.setOnClickListener {
//            onDeleteConfirmed()
            onDelete()
            dismiss()
        }
    }

    private fun onDelete() {
        parentFragmentManager.setFragmentResult(GENERIC_DELET_DIALOG_RES_DELETE_CONF_KEY, bundleOf(GENERIC_DELET_DIALOG_RES_DELETE_CONF_KEY to GENERIC_DELET_DIALOG_RES_DELETE_CONF_VAL))
    }

    private fun setTitleAndMsg() {
        binding.title.text = args.title
        binding.msg.text = args.msg
    }

    companion object {
        val GENERIC_DELET_DIALOG_RES_DELETE_CONF_KEY = "GENERIC_DELET_DIALOG_RES_DELETE_CONF_KEY"
        val GENERIC_DELET_DIALOG_RES_DELETE_CONF_VAL = 1
    }

}