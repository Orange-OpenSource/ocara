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
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.view.isVisible
import com.orange.ocara.mobile.R

// view is the view that gets shown and collapsed based on a click on controller
class ShowAndCollapseViewWithAnimationManager(private val view: View,
                                              private val controller: View,
                                              private val arrowIcon: ImageView = controller as ImageView,
                                              private var isShown:Boolean = false) {

    init {
        controller.setOnClickListener {
            changeStat()
        }
    }

    private fun changeStat() {
        if (isShown) {
            hideView()
        } else {
            showView()
        }
    }

    private fun showView() {
        isShown = true
        arrowIcon.setImageResource(R.drawable.ic_arrow_drop_up)
        view.visibility = View.VISIBLE
      //  view.startAnimation(AnimationUtils.loadAnimation(view.context,R.anim.show_layout_up_down))
    }

    private fun hideView() {
        isShown = false
        arrowIcon.setImageResource(R.drawable.ic_arrow_drop_down)
        view.visibility = View.GONE
      //  view.startAnimation(AnimationUtils.loadAnimation(view.context,R.anim.hide_layout_down_up))
    }
}