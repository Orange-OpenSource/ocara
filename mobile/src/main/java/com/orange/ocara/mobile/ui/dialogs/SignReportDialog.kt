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
import android.util.DisplayMetrics
import android.view.View
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.DialogSignBinding

class SignReportDialog() :
    OcaraDialog<DialogSignBinding>(R.layout.dialog_sign) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPaint()
    }

    override fun initClickListeners() {
        binding.save.setOnClickListener {
            saveImage()
//            dismiss()
        }
        binding.close.setOnClickListener {
            dismiss()
        }
    }

    private fun initPaint() {
        val metrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(metrics)
        binding.paintView.init(metrics)
    }

    private fun saveImage() {
        binding.paintView.saveImage(0)
    }
}

