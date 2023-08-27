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
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.DialogDeleteRulesetBinding
import com.orange.ocara.mobile.ui.helpers.DeleteCommentInterface

class DeleteCommentDialog(val deleteComment: DeleteCommentInterface, val commentId: Int) : OcaraDialog<DialogDeleteRulesetBinding>(R.layout.dialog_delete_ruleset) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textView.text = requireContext().resources.getString(R.string.delete_comment)
    }

    override fun initClickListeners() {
        binding.close.setOnClickListener {
            dismiss()
        }
        binding.confirm.setOnClickListener {
            deleteComment.deleteComment(commentId)
        }
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }
}

