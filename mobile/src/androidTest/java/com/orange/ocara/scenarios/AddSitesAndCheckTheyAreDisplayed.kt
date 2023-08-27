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

package com.orange.ocara.mobile.scenarios


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.ui.MainActivity
import com.orange.ocara.mobile.scenarios.ViewAssertions.RecyclerViewItemCountAssertion
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AddSitesAndCheckTheyAreDisplayed : ParentUITest(){

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest4() {
        val appCompatTextView = onView(
                allOf(withId(R.id.create_audit), withText("Create New Audit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                1),
                        isDisplayed()))
        appCompatTextView.perform(click())

        val appCompatImageView = onView(
                allOf(withId(R.id.selectSite),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.ScrollView")),
                                        0),
                                5)))
        appCompatImageView.perform(scrollTo(), click())

        val appCompatImageView2 = onView(
                allOf(withId(R.id.addSite),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()))
        appCompatImageView2.perform(click())

        val appCompatEditText = onView(
                allOf(withId(R.id.siteNameET),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.ScrollView")),
                                        0),
                                3)))
        appCompatEditText.perform(scrollTo(), replaceText("site1"), closeSoftKeyboard())

        val appCompatButton = onView(
                allOf(withId(R.id.createSiteBtn), withText("Create site"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.ScrollView")),
                                        0),
                                10)))
        appCompatButton.perform(scrollTo(), click())

        val appCompatImageView3 = onView(
                allOf(withId(R.id.selectSite),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.ScrollView")),
                                        0),
                                5)))
        appCompatImageView3.perform(scrollTo(), click())

        val appCompatImageView4 = onView(
                allOf(withId(R.id.addSite),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()))
        appCompatImageView4.perform(click())

        val appCompatEditText2 = onView(
                allOf(withId(R.id.siteNameET),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.ScrollView")),
                                        0),
                                3)))
        appCompatEditText2.perform(scrollTo(), replaceText("site2"), closeSoftKeyboard())

        val appCompatButton2 = onView(
                allOf(withId(R.id.createSiteBtn), withText("Create site"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.ScrollView")),
                                        0),
                                10)))
        appCompatButton2.perform(scrollTo(), click())

        val appCompatImageView5 = onView(
                allOf(withId(R.id.selectSite),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.ScrollView")),
                                        0),
                                5)))
        appCompatImageView5.perform(scrollTo(), click())

        val textView = onView(
                allOf(withId(R.id.siteName), withText("site2"),
                        withParent(withParent(withId(R.id.sites_list))),
                        isDisplayed()))
        textView.check(matches(withText("site2")))

        val textView2 = onView(
                allOf(withId(R.id.siteName), withText("site1"),
                        withParent(withParent(withId(R.id.sites_list))),
                        isDisplayed()))
        textView2.check(matches(withText("site1")))
        onView(withId(R.id.sites_list)).check(RecyclerViewItemCountAssertion(2))
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
