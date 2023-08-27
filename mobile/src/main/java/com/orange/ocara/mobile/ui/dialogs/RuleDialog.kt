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
import com.orange.ocara.domain.models.RuleModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.DialogRuleBinding

class RuleDialog(private val rule:RuleModel) : OcaraDialog<DialogRuleBinding>(R.layout.dialog_rule) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.ruleLabel.text = rule.label
    }

    override fun initClickListeners() {
        binding.close.setOnClickListener {
            dismiss()
        }
    }
    companion object {
        const val TAG = "RuleDialog"
    }
}