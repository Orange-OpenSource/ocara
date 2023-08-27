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

package com.orange.ocara.scenarios.ViewAssertions

import android.view.View
import android.widget.ExpandableListView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import org.junit.Assert

class CheckExpandableListViewItems(private val expectedItems: List<String>) : ViewAssertion {
    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }
        val listView: ExpandableListView = view as ExpandableListView
        val adapter = listView.adapter as CurrentRouteAdapter
        val dataInAdapter = adapter.data
        Assert.assertEquals(expectedItems.size, dataInAdapter.size)
        for (i in expectedItems.indices) {
            Assert.assertEquals(expectedItems[i], dataInAdapter[i].name)
        }
    }
}