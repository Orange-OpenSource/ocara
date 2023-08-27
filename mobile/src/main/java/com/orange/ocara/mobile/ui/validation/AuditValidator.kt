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

import android.widget.EditText
import java.util.regex.Pattern


private val AuditPattern = Pattern.compile("[a-zA-z]{3,}+\$")

fun validateAuditName(et: EditText): Boolean {
    val matcher = AuditPattern.matcher(et.text)
    return et.text?.isEmpty() == true && matcher.matches()
}

