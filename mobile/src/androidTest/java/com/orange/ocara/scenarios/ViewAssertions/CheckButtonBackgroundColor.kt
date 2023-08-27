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

package com.orange.ocara.mobile.scenarios.ViewAssertions

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Button
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import com.orange.ocara.mobile.R
import org.junit.Assert

class CheckButtonBackgroundColor(private val color:Int) :ViewAssertion{
    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        if(noViewFoundException != null){
            throw noViewFoundException
        }
        val button = view as Button
        Assert.assertEquals((button.background as ColorDrawable).color, button.resources.getColor(color))
    }
}