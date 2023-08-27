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

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.R.layout
import com.orange.ocara.mobile.ui.helpers.OnBackPressedListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
//        supportActionBar?.setLogo(R.drawable.ic_logo_ocara_topbar)
        val toolBar : Toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolBar.disableTitleSingleLine()
        supportActionBar?.setLogo(null)
        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.toolbar_rectangle))
        supportActionBar?.setHomeActionContentDescription(getString(R.string.home_btn_content_desc));

        val navController = this.findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

    }
    fun Toolbar.disableTitleSingleLine() {
        for (i in 0..childCount) {
            val child = getChildAt(i)
            if (child is TextView) {
                child.isSingleLine = false
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        val navHostFragment: NavHostFragment? = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val changedBehavior = (navHostFragment!!.childFragmentManager.fragments[0] as OnBackPressedListener).onBack()
        Timber.d("onBackPressed")
        if(!changedBehavior){
            Timber.d("onBackPressed behavior not changed")
            super.onBackPressed()
        }
    }

}