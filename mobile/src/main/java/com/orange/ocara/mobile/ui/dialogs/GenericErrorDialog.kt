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

import android.view.View
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.DialogGenericConfirmationBinding
import com.orange.ocara.mobile.databinding.DialogGenericDeleteConfirmationBinding
import com.orange.ocara.mobile.setWidthPercent

class GenericErrorDialog(
    private val title: String,
    private val msg: String,
    private val confirmString: String,
    private val cancelString: String,
    private val onConfirmed: () -> Unit,
) : GenericConfirmationDialog(title , msg , confirmString , cancelString , onConfirmed , {}) {
    override fun initClickListeners() {
        setTitleAndMsg()
        binding.cancel.visibility = View.GONE
        binding.close.setOnClickListener {
            dismiss()
        }
        binding.confirm.setOnClickListener {
            onConfirmed()
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        setWidthPercent(90)
    }
    private fun setTitleAndMsg() {
        if (title != null)
            binding.title.text =  title
        if (msg != null)
            binding.msg.text = msg
        if (confirmString != null)
            binding.confirm.text = confirmString

    }
}