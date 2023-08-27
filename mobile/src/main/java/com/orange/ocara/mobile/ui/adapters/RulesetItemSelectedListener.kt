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

package com.orange.ocara.mobile.ui.adapters

import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import com.orange.ocara.mobile.R
import timber.log.Timber

abstract class RulesetItemSelectedListener : AdapterView.OnItemSelectedListener {


    public abstract fun onRulesetSelected(position: Int)

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        Timber.d("selected item pos : $position")
        parent?.findViewById<ImageView>(R.id.dowloadedStatus)?.visibility = View.GONE
        parent?.findViewById<ImageView>(R.id.spinner_item_divider)?.visibility = View.GONE

        onRulesetSelected(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}