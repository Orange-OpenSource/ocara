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
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.RootUtils
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    lateinit var disp: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setSupportActionBar(null)

        if (RootUtils().isDeviceRooted()) {
            finish()
        }

        disp = Single.timer(3, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { i ->
                onfinish()
                disp.dispose()
            }


    }

    fun onfinish() {
//        val mainIntent = Intent(this, MainActivity::class.java)
//        startActivity(mainIntent)
        if (isTermsAccepted(applicationContext)) {
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        } else {
            val mainIntent = Intent(this, TermsActivity::class.java)
            startActivity(mainIntent)
        }
        finish()

    }

}