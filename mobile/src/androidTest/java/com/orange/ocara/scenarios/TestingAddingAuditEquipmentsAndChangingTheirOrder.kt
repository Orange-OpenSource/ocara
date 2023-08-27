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
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.scenarios.ParentUITest
import com.orange.ocara.mobile.ui.MainActivity
import com.orange.ocara.scenarios.ViewAssertions.CheckExpandableListViewItems
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class TestingAddingAuditEquipmentsAndChangingTheirOrder : ParentUITest() {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    val auditName = "audit"

    @Test
    fun testingAddingAuditEquipmentsAndChangingTheirOrder() {
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
        appCompatEditText.perform(scrollTo(), replaceText(auditName), closeSoftKeyboard())

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
        appCompatEditText2.perform(scrollTo(), click())

        val appCompatEditText3 = onView(
                allOf(withId(R.id.siteNameET),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.ScrollView")),
                                        0),
                                3)))
        appCompatEditText3.perform(scrollTo(), replaceText("site1"), closeSoftKeyboard())

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

        val appCompatEditText4 = onView(
                allOf(withId(R.id.auditorNameET),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.ScrollView")),
                                        0),
                                3)))
        appCompatEditText4.perform(scrollTo(), replaceText("auditor"), closeSoftKeyboard())

        val appCompatEditText5 = onView(
                allOf(withId(R.id.auditorEmailET),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.ScrollView")),
                                        0),
                                7)))
        appCompatEditText5.perform(scrollTo(), replaceText("ss@"), closeSoftKeyboard())

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

        val appCompatButton4 = onView(
                allOf(withId(R.id.expert_switch_button), withText("EXPERT"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.ScrollView")),
                                        0),
                                14)))
        appCompatButton4.perform(scrollTo(), click())

        val appCompatButton5 = onView(
                allOf(withId(R.id.create_audit_button), withText("Create New Audit"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.ScrollView")),
                                        0),
                                15)))
        appCompatButton5.perform(scrollTo(), click())

        val floatingActionButton = onView(
                allOf(withId(R.id.floatingAddButton), withContentDescription("Edit path"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                1),
                        isDisplayed()))
        floatingActionButton.perform(click())

        val cardView = onData(anything())
                .inAdapterView(allOf(withId(R.id.objects_grid),
                        childAtPosition(
                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                2)))
                .atPosition(0)
        cardView.perform(click())

        val textView = onView(
                allOf(withId(R.id.obj_name_tv), withText("Escalier"),
                        withParent(withParent(withId(R.id.nav_host_fragment))),
                        isDisplayed()))
        textView.check(matches(withText("Escalier")))

        val textView2 = onView(
                allOf(withId(R.id.obj_name_tv), withText("Escalier"),
                        withParent(withParent(withId(R.id.nav_host_fragment))),
                        isDisplayed()))
        textView2.check(matches(withText("Escalier")))

        val appCompatTextView2 = onView(
                allOf(withId(R.id.auditTV), withText("Audit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                13),
                        isDisplayed()))
        appCompatTextView2.perform(click())

        val appCompatTextView3 = onView(
                allOf(withId(R.id.nextTV), withText("validate"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                1),
                        isDisplayed()))
        appCompatTextView3.perform(click())

        expandList()

        val textView3 = onView(
                allOf(withId(R.id.equipmentName), withText("Escalier"),
                        withParent(withParent(withId(R.id.expandable_list))),
                        isDisplayed()))
        textView3.check(matches(withText("Escalier")))

        val floatingActionButton2 = onView(
                allOf(withId(R.id.floatingAddButton), withContentDescription("Edit path"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                1),
                        isDisplayed()))
        floatingActionButton2.perform(click())

        val cardView2 = onData(anything())
                .inAdapterView(allOf(withId(R.id.objects_grid),
                        childAtPosition(
                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                2)))
                .atPosition(1)
        cardView2.perform(click())

        val textView4 = onView(
                allOf(withId(R.id.obj_name_tv), withText("Handrail"),
                        withParent(withParent(withId(R.id.nav_host_fragment))),
                        isDisplayed()))
        textView4.check(matches(withText("Handrail")))

        val appCompatTextView4 = onView(
                allOf(withId(R.id.auditTV), withText("Audit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                13),
                        isDisplayed()))
        appCompatTextView4.perform(click())

        val appCompatTextView5 = onView(
                allOf(withId(R.id.nextTV), withText("validate"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                1),
                        isDisplayed()))
        appCompatTextView5.perform(click())
        expandList()
        val actionMenuItemView = onView(
                allOf(withId(R.id.edit_path), withContentDescription("Edit path"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        3),
                                0),
                        isDisplayed()))
        actionMenuItemView.perform(click())

        val appCompatImageView5 = onView(
                allOf(withId(R.id.upArrow),
                        childAtPosition(
                                withParent(withId(R.id.expandable_list)),
                                4),
                        isDisplayed()))
        appCompatImageView5.perform(click())

        val textView5 = onView(
                allOf(withId(R.id.equipmentName), withText("Handrail"),
                        withParent(withParent(withId(R.id.expandable_list))),
                        isDisplayed()))
        textView5.check(matches(withText("Handrail")))

        val textView6 = onView(
                allOf(withId(R.id.equipmentName), withText("Escalier"),
                        withParent(withParent(withId(R.id.expandable_list))),
                        isDisplayed()))
        textView6.check(matches(withText("Escalier")))

        val appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(`is`("android.widget.LinearLayout")),
                                                0)),
                                4),
                        isDisplayed()))
        appCompatImageButton.perform(click())

        val textView7 = onView(
                allOf(withId(R.id.equipmentName), withText("Handrail"),
                        withParent(withParent(withId(R.id.expandable_list))),
                        isDisplayed()))
        textView7.check(matches(withText("Handrail")))

        val textView8 = onView(
                allOf(withId(R.id.equipmentName), withText("Escalier"),
                        withParent(withParent(withId(R.id.expandable_list))),
                        isDisplayed()))
        textView8.check(matches(withText("Escalier")))

        val textView9 = onView(
                allOf(withId(R.id.commentsCount), withText("0"),
                        withParent(withParent(withId(R.id.expandable_list))),
                        isDisplayed()))
        textView9.check(matches(withText("0")))

        val textView10 = onView(
                allOf(withId(R.id.commentsCount), withText("0"),
                        withParent(withParent(withId(R.id.expandable_list))),
                        isDisplayed()))
        textView10.check(matches(withText("0")))

        val floatingActionButton3 = onView(
                allOf(withId(R.id.floatingAddButton), withContentDescription("Edit path"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                1),
                        isDisplayed()))
        floatingActionButton3.perform(click())

        val cardView3 = onData(anything())
                .inAdapterView(allOf(withId(R.id.objects_grid),
                        childAtPosition(
                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                2)))
                .atPosition(3)
        cardView3.perform(click())

        val textView11 = onView(
                allOf(withId(R.id.obj_name_tv), withText("Signalétique"),
                        withParent(withParent(withId(R.id.nav_host_fragment))),
                        isDisplayed()))
        textView11.check(matches(withText("Signalétique")))

        val appCompatTextView6 = onView(
                allOf(withId(R.id.auditTV), withText("Audit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                13),
                        isDisplayed()))
        appCompatTextView6.perform(click())

        val appCompatTextView7 = onView(
                allOf(withId(R.id.nextTV), withText("validate"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                1),
                        isDisplayed()))
        appCompatTextView7.perform(click())
        expandList()
        onView(withId(R.id.expandable_list)).check(CheckExpandableListViewItems(listOf("Escalier", "Handrail", "Signalétique")))
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

    private fun expandList() {
        val textView = onView(
                allOf(withId(R.id.listTitle), withText(auditName),
                        withParent(withParent(withId(R.id.expandable_list))),
                        isDisplayed()))
        textView.perform(click())
    }
}
