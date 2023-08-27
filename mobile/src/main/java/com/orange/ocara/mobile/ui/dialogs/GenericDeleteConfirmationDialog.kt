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
import com.orange.ocara.mobile.databinding.DialogGenericDeleteConfirmationBinding

class GenericDeleteConfirmationDialog(
        private val title: String, private val msg: String, private val onDeleteConfirmed: () -> Unit)
    : OcaraDialog<DialogGenericDeleteConfirmationBinding>(R.layout.dialog_generic_delete_confirmation) {
    override fun initClickListeners() {
        setTitleAndMsg()
        binding.cancel.setOnClickListener {
            dismiss()
        }
        binding.close.setOnClickListener {
            dismiss()
        }
        binding.confirm.setOnClickListener {
            onDeleteConfirmed()
            dismiss()
        }
    }

    private fun setTitleAndMsg() {
        binding.title.text = title
        binding.msg.text = msg;
    }
}