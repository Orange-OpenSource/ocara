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

import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.DialogGenericConfirmationBinding
import com.orange.ocara.mobile.databinding.DialogGenericDeleteConfirmationBinding

open class GenericConfirmationDialog(
    private val title: String?,
    private val msg: String?,
    private val confirmString : String?,
    private val cancelString: String?,
    private val onConfirmed: () -> Unit,
    private val onCancel: () -> Unit
) : OcaraDialog<DialogGenericConfirmationBinding>(R.layout.dialog_generic_confirmation) {
    override fun initClickListeners() {
        setTitleAndMsg()
        binding.cancel.setOnClickListener {
            onCancel()
            dismiss()
        }
        binding.close.setOnClickListener {
            dismiss()
        }
        binding.confirm.setOnClickListener {
            onConfirmed()
            dismiss()
        }
    }

    private fun setTitleAndMsg() {
        if (title != null)
            binding.title.text = title
        if (msg != null)
            binding.msg.text = msg
        if (cancelString != null)
            binding.cancel.text = cancelString
        if (confirmString != null)
            binding.confirm.text = confirmString

    }
}