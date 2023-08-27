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

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.orange.ocara.mobile.R

/*
    this handles the clicking on a floating button by showing or hiding other floating buttons
 */
class ShowFloatingActionButtonsManager private constructor(builder: Builder) {
    private val addButton: FloatingActionButton = builder.addButton // this is button that shows and hides other buttons when clicked
    private val cancelButton: FloatingActionButton = builder.cancelButton // this is button that shows and hides other buttons when clicked
    private val otherButtons: View = builder.otherButtons// this is the parent view that holds the other buttons that will be handled by the mainButton
    private val context: Context = addButton.context
    private val showLayoutAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.show_layout_down_up)
    private val hideLayoutAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.hide_layout_up_down)

    init {
        initClickListeners()
    }

    private fun initClickListeners() {
        addButton.setOnClickListener {
            showOtherButtons()
        }
        cancelButton.setOnClickListener {
            hideOtherButtons()
        }
    }

    private fun showOtherButtons() {
        addButton.visibility = View.GONE
        otherButtons.visibility = View.VISIBLE
        otherButtons.startAnimation(showLayoutAnimation)
    }

    fun hideOtherButtons() {
        addButton.visibility = View.VISIBLE
        if (otherButtons.visibility == View.VISIBLE) {
            otherButtons.visibility = View.GONE
            otherButtons.startAnimation(hideLayoutAnimation)
        }
    }

    fun builder(): Builder {
        return Builder()
    }

    class Builder {
        lateinit var otherButtons: View
        lateinit var addButton: FloatingActionButton
        lateinit var cancelButton: FloatingActionButton

        fun otherButtons(otherButtons: View): Builder {
            this.otherButtons = otherButtons
            return this
        }

        fun cancelButton(cancelButton: FloatingActionButton): Builder {
            this.cancelButton = cancelButton
            return this
        }

        fun addButton(addButton: FloatingActionButton): Builder {
            this.addButton = addButton
            return this
        }

        fun build(): ShowFloatingActionButtonsManager {
            return ShowFloatingActionButtonsManager(this)
        }
    }
}