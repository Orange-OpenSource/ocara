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


private val SiteCodePattern = Pattern.compile("^[0-9]{5,}+\$")

fun validateSiteName(et: EditText): Boolean {
    return et.text.trim()?.isNotEmpty() == true
}

fun validateSiteAddress(et: EditText): Boolean {
    return et.text.trim()?.isNotEmpty() == true
}

fun validateSitePostalCode(et: EditText): Boolean {
    val matcher = SiteCodePattern.matcher(et.text.toString())
    return et.text?.isNotEmpty() == true && matcher.matches()
}

fun validateSiteCity(et: EditText): Boolean {
    return et.text.trim()?.isNotEmpty() == true
}

private fun validatePostalCodeIsFiveDigits(et: EditText): Boolean {
    val minimumFiveDigitsNumber = 10000
    val maximumFiveDigitsNumber = 99999
    return et.text?.toString()?.toInt() in minimumFiveDigitsNumber..maximumFiveDigitsNumber
}

