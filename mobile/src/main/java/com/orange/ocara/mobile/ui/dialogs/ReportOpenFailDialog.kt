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
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.orange.ocara.domain.models.AuditEquipmentModel
import com.orange.ocara.domain.models.EquipmentModel
import com.orange.ocara.domain.models.ProfileAnswersUIModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.DialogReportProfileScoresBinding
import com.orange.ocara.mobile.databinding.DialogRuleBinding
import com.orange.ocara.mobile.databinding.DialogSelectCharacteristicsBinding
import com.orange.ocara.mobile.ui.adapters.CharacteristicsDialogAdapter
import com.orange.ocara.mobile.ui.convertLongToDateTime
import com.orange.ocara.mobile.ui.managers.ReportProfileScoresTableManager
import com.orange.ocara.mobile.ui.managers.ReportTableManager
import com.orange.ocara.mobile.ui.viewmodel.AuditEquipmentWithCharacteristicsViewModel
import com.orange.ocara.mobile.ui.viewmodel.ReportProfileScoresViewModel

class ReportOpenFailDialog(val path: String ) : OcaraDialog<DialogRuleBinding>(R.layout.dialog_rule) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ruleLabel.text = context?.getString(R.string.export_docx_fail_app_error, path)
    }

    override fun initClickListeners() {
        binding.close.setOnClickListener { dismiss() }
    }

    companion object {
        const val TAG = "ReportOpenFailDialog"
    }
}