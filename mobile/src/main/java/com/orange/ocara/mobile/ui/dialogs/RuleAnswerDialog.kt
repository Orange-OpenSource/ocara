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
import com.orange.ocara.domain.models.RuleAnswerModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.DialogRulesAnswerBinding
import com.orange.ocara.mobile.ui.helpers.OnAnswerSelected
import com.orange.ocara.utils.enums.Answer

class RuleAnswerDialog(private val ruleAnswer:RuleAnswerModel,private val onAnswerSelected: OnAnswerSelected) : OcaraDialog<DialogRulesAnswerBinding>(R.layout.dialog_rules_answer) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.ruleLabel.text = ruleAnswer.rule.label
    }

    override fun initClickListeners() {
        binding.close.setOnClickListener {
            dismiss()
        }
        binding.okButton.setOnClickListener {
            onAnswerSelected.onAnswerSelected(ruleAnswer,Answer.OK)
            dismiss()
        }
        binding.doubtButton.setOnClickListener {
            onAnswerSelected.onAnswerSelected(ruleAnswer,Answer.DOUBT)
            dismiss()
        }
        binding.noAnswerButton.setOnClickListener {
            onAnswerSelected.onAnswerSelected(ruleAnswer,Answer.NO_ANSWER)
            dismiss()
        }
        binding.nokButton.setOnClickListener {
            onAnswerSelected.onAnswerSelected(ruleAnswer,Answer.NOK)
            dismiss()
        }
    }
    companion object {
        const val TAG = "RuleAnswerDialog"
    }
}