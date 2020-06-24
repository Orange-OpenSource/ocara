/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.orange.ocara.ui.activity;

import android.Manifest;
import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.orange.ocara.R;
import com.xamarin.testcloud.espresso.Factory;
import com.xamarin.testcloud.espresso.ReportHelper;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.doubleClick;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasType;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CreateAuditActivityFT {

    @Rule
    public ActivityTestRule<CreateAuditActivity_> mActivityTestRule = new ActivityTestRule<>(CreateAuditActivity_.class);

    @Rule
    public ReportHelper reportHelper = Factory.getReportHelper();

    @Rule
    public GrantPermissionRule mGrantPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO);

    @After
    public void tearDown() {
        reportHelper.label("Stopping App");
    }

    /**
     * Simple instrumentated test that creates an audit and generates an export:
     *
     * - Fills the audit creation form
     * - Adds an item to audit
     * - Review the item to be audited
     * - Finish the audit
     * - Generates a report as docx
     *
     * This a happy path and we could add more assertions to make this test robuster.
     */
    @Test
    public void shouldGenerateAReportWithoutCrashWhenCreatingANewAudit() {
        Intents.init();

        reportHelper.label("Accueil");

        onView(withId(R.id.audit_name))
                .perform(scrollTo(), typeText("Test"), closeSoftKeyboard());

        onView(withId(R.id.site))
                .perform(scrollTo(), typeText("TEST"));

        onView(withText("Immeuble OCARA - TEST"))
                .inRoot(isPlatformPopup())
                .perform(click());

        onView(withId(R.id.author_complete_view))
                .perform(scrollTo(), typeText("John Doe"), closeSoftKeyboard());

        onView(withId(R.id.ruleset_type))
                .perform(doubleClick());

        onView(withText("Accessibilité des Circulations"))
                .inRoot(isPlatformPopup())
                .perform(click());

        waitFor(60 * 1000);

        reportHelper.label("Filled form");

        onView(allOf(withId(R.id.start_audit_button), withText("New")))
                .perform(scrollTo(), click());

        reportHelper.label("Audit");

        onView(allOf(withText("Ascenseur"), isDisplayed()))
                .perform(click());

        onView(allOf(withText("Remember my choice"), isDisplayed()))
                .inRoot(isDialog())
                .perform(click());

        onView(allOf(withText("Test it now"), isDisplayed()))
                .perform(scrollTo(), click());

        onView(allOf(withText("Finish"), isDisplayed()))
                .perform(click());

        onView(allOf(withText("Close"), isDisplayed()))
                .inRoot(isDialog())
                .perform(click());

        onView(allOf(withText("Ascenseur"), isDescendantOfA(withId(R.id.audited_objects))))
                .check(matches(isDisplayed()));

        onView(allOf(withText("Report"), isDisplayed()))
                .perform(click());

        onView(allOf(withText("Ascenseur"), isDescendantOfA(withId(R.id.audited_objects))))
                .check(matches(isDisplayed()));

        reportHelper.label("Export");

        onView(allOf(withText("Generate Report"), isDisplayed()))
                .perform(click());

        onView(allOf(withText("Share"), isDisplayed()))
                .inRoot(isDialog())
                .perform(click());

        waitFor(30 * 1000);

        intended(allOf(hasAction(Intent.ACTION_CHOOSER), hasExtra(is(Intent.EXTRA_INTENT),
                allOf(hasAction(Intent.ACTION_SEND), hasType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")))));
    }

    private static void waitFor(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
