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
import com.orange.ocara.mobile.databinding.DialogSelectDocFormatBinding
import com.orange.ocara.mobile.ui.viewmodel.DOCFormat
import com.orange.ocara.mobile.ui.viewmodel.ReportViewModel


class ChooseDocFormatDialog(val viewModel: ReportViewModel) :OcaraDialog<DialogSelectDocFormatBinding>(R.layout.dialog_select_doc_format) {



    override fun initClickListeners() {

        binding.cancelBtn.setOnClickListener { dismiss() }
        binding.pdfItemLayout.setOnClickListener { viewModel.onDocumentFormatSelected(DOCFormat.PDF) ; dismiss() }
        binding.wordItemLayout.setOnClickListener { viewModel.onDocumentFormatSelected(DOCFormat.WORD) ; dismiss()  }
        binding.oofItemLayout.setOnClickListener { viewModel.onDocumentFormatSelected(DOCFormat.OOF)  ; dismiss() }
    }
}