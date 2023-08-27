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

package com.orange.ocara.mobile.ui.validation

import android.text.InputFilter
import android.text.SpannableString
import android.text.Spanned
import android.widget.EditText


fun validateAuditorName(et: EditText): Boolean {
    return et.text.trim()?.isNotEmpty() == true
}

fun validateAuditorEmail(et: EditText): Boolean {
    val matcher = android.util.Patterns.EMAIL_ADDRESS.matcher(et.text)
    return et.text.trim()?.isNotEmpty() == true && matcher.matches()
}

fun getAuditorNameFilter(): Array<InputFilter?> {
    val pattern = "[\\p{Alpha}\\s]*"

    val filters = arrayOfNulls<InputFilter>(1)

    filters[0] = InputFilter { source, start, end, dest, dstart, dend ->
        if (end > start) {
            val destTxt = dest.toString()
            val resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend)
            if (!resultingTxt.matches(pattern.toRegex())) {
                if (source is Spanned) {
                    return@InputFilter SpannableString(resultingTxt.dropLast(1))
                } else {
                    return@InputFilter resultingTxt.dropLast(1)
                }
            }
        }
        null
    }
    return filters
}


