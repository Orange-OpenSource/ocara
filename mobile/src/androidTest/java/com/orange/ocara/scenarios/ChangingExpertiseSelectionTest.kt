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

package com.orange.ocara.scenarios


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.scenarios.ParentUITest
import com.orange.ocara.mobile.scenarios.ViewAssertions.CheckButtonBackgroundColor
import com.orange.ocara.mobile.ui.MainActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ChangingExpertiseSelectionTest : ParentUITest(){

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun changingExpertiseSelectionTest() {
        val appCompatTextView = onView(
                allOf(withId(R.id.create_audit), withText("Create New Audit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                1),
                        isDisplayed()))
        appCompatTextView.perform(click())

        val appCompatButton = onView(
                allOf(withId(R.id.expert_switch_button), withText("EXPERT"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.ScrollView")),
                                        0),
                                14)))
        appCompatButton.perform(scrollTo(), click())


        val button2 = onView(
                allOf(withId(R.id.expert_switch_button), withText("EXPERT"),
                        withParent(withParent(IsInstanceOf.instanceOf(android.widget.ScrollView::class.java))),
                        isDisplayed()))
        button2.check(CheckButtonBackgroundColor(R.color.orange))

        val appCompatButton2 = onView(
                allOf(withId(R.id.novice_switch_button), withText("NOVICE"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.ScrollView")),
                                        0),
                                13)))
        appCompatButton2.perform(scrollTo(), click())

        val button3 = onView(
                allOf(withId(R.id.novice_switch_button), withText("NOVICE"),
                        withParent(withParent(IsInstanceOf.instanceOf(android.widget.ScrollView::class.java))),
                        isDisplayed()))
        button3.check(CheckButtonBackgroundColor(R.color.orange))
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
