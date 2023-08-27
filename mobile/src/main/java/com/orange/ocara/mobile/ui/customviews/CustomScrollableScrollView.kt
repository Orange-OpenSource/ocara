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

package com.orange.ocara.mobile.ui.customviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView


class CustomScrollableScrollView(context: Context?, attrs: AttributeSet?) : ScrollView(context, attrs)  {

    private var enableScrolling = true

    fun isEnableScrolling(): Boolean {
        return enableScrolling
    }

    fun setEnableScrolling(enableScrolling: Boolean) {
        this.enableScrolling = enableScrolling
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (isEnableScrolling()) {
            super.onInterceptTouchEvent(ev)
        } else {
            false
        }
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return if (isEnableScrolling()) {
            super.onTouchEvent(ev)
        } else {
            false
        }
    }
}