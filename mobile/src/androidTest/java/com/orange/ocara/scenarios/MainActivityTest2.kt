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
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.ui.MainActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest2 : ParentUITest() {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest2() {
        val appCompatTextView = onView(
                allOf(withId(R.id.create_audit), withText("Create New Audit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                1),
                        isDisplayed()))
        appCompatTextView.perform(click())

        val appCompatEditText = onView(
                allOf(withId(R.id.auditNameET),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.ScrollView")),
                                        0),
                                1)))
        appCompatEditText.perform(scrollTo(), replaceText("audit"), closeSoftKeyboard())

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

        val appCompatEditText2 = onView(
                allOf(withId(R.id.siteNameET),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.ScrollView")),
                                        0),
                                3)))
        appCompatEditText2.perform(scrollTo(), replaceText("site1"), closeSoftKeyboard())

        val appCompatButton = onView(
                allOf(withId(R.id.createSiteBtn), withText("Create site"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.ScrollView")),
                                        0),
                                10)))
        appCompatButton.perform(scrollTo(), click())

        val appCompatImageView3 = onView(
                allOf(withId(R.id.selectAuditor),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.ScrollView")),
                                        0),
                                11)))
        appCompatImageView3.perform(scrollTo(), click())

        val appCompatImageView4 = onView(
                allOf(withId(R.id.addAuditor),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()))
        appCompatImageView4.perform(click())

        val appCompatEditText3 = onView(
                allOf(withId(R.id.auditorNameET),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.ScrollView")),
                                        0),
                                3)))
        appCompatEditText3.perform(scrollTo(), replaceText("auditor"), closeSoftKeyboard())

        val appCompatEditText4 = onView(
                allOf(withId(R.id.auditorEmailET),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.ScrollView")),
                                        0),
                                7)))
        appCompatEditText4.perform(scrollTo(), replaceText("email@fasd"), closeSoftKeyboard())

        val appCompatButton2 = onView(
                allOf(withId(R.id.add_auditor_button), withText("Add auditor"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.ScrollView")),
                                        0),
                                8)))
        appCompatButton2.perform(scrollTo(), click())

        val constraintLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.referentialSpinner),
                        childAtPosition(
                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                7)))
                .atPosition(0)
        constraintLayout.perform(scrollTo(), click())

        val appCompatButton3 = onView(
                allOf(withId(R.id.download_button), withText("Download"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                11),
                        isDisplayed()))
        appCompatButton3.perform(click())

        val button = onView(
                allOf(withId(R.id.create_audit_button), withText("CREATE NEW AUDIT"),
                        withParent(withParent(IsInstanceOf.instanceOf(android.widget.ScrollView::class.java))),
                        isDisplayed()))
        button.check(matches(isDisplayed()))
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
