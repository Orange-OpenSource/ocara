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

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.orange.ocara.mobile.R
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable


@AndroidEntryPoint
class TermsActivity : AppCompatActivity() {

    lateinit var disp: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_of_use)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setLogo(R.drawable.ic_logo_ocara_topbar)
        supportActionBar?.setTitle(R.string.terms_of_use)
        findViewById<Button>(R.id.confirm).setOnClickListener {
            writeAcceptTerms(applicationContext)
            onfinish()
            finish()
        }
        findViewById<Button>(R.id.cancel).setOnClickListener {
            finish()
        }
    }

    fun onfinish() {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }


}