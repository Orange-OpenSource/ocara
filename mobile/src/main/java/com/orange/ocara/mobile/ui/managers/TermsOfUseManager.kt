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

package com.orange.ocara.mobile.ui.managers

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.orange.ocara.mobile.R

class TermsOfUseManager(private val titleTV:TextView,private val contentTV:TextView
        ,private val superManager: TermsOfUseSuperManager) {

    init {
        initClickListeners()
    }

    var isShown = false
    private fun initClickListeners() {
        titleTV.setOnClickListener {
            toggleContentState()
        }
    }

    private fun toggleContentState() {
        if(isShown){
            hideContent()
        }else{
            showContent()
        }
    }

    fun showContent() {
        superManager.closeAll()
        isShown = true
        contentTV.visibility = View.VISIBLE
//        titleTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0)
    }

    fun hideContent() {
        isShown = false
        contentTV.visibility = View.GONE
//        titleTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_right_24, 0)
    }
}