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

package com.orange.ocara.mobile.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity


val OCARA_MOB_TERMS_PREFS = "OCARA_MOB_TERMS_PREFS"
val OCARA_MOB_TERMS_PREFS_ACCEPT_KEY = "OCARA_MOB_TERMS_PREFS_ACCEPT_KEY"

fun writeAcceptTerms(appContext: Context) {
    val sharedPreferences = appContext.getSharedPreferences(
        OCARA_MOB_TERMS_PREFS,
        AppCompatActivity.MODE_PRIVATE
    )
    val myEdit: SharedPreferences.Editor = sharedPreferences.edit()
    myEdit.putBoolean(OCARA_MOB_TERMS_PREFS_ACCEPT_KEY, true)
    myEdit.commit()
}


fun isTermsAccepted(appContext: Context): Boolean {
    val sharedPreferences = appContext.getSharedPreferences(
        OCARA_MOB_TERMS_PREFS,
        AppCompatActivity.MODE_PRIVATE
    )
    return sharedPreferences.getBoolean(OCARA_MOB_TERMS_PREFS_ACCEPT_KEY, false)
}