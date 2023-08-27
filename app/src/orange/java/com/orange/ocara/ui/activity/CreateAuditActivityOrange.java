/*
 * Copyright (C) 2015 Orange
 * Authors: IDBA6391
 *
 * This software is the confidential and proprietary information of Orange.
 * You shall not disclose such confidential information and shall use it only
 * in accordance with the terms of the license agreement you entered into
 * with Orange.
 */
package com.orange.ocara.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;

import com.orange.ocara.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

/**
 * activity dedicated to the creation of audits
 */
@EActivity(resName = "activity_create_audit")
public class CreateAuditActivityOrange extends CreateAuditActivity {

    public static final String MY_PREFERENCES = "MyPrefs";

    private static final String LAST_NAME_PREFS_KEY = "lastname";

    private static final String FIRST_NAME_PREFS_KEY = "firstname";

    private static final String DISCONNECT_EXTRA_KEY = "disconnect";

    private static final String FROM_APP_EXTRA_KEY = "fromApp";

    @Extra("lastname")
    String lastname;

    @Extra("firstname")
    String firstname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        OcaraConfiguration.findAll().setAboutXmlId(R.layout.activity_about_orange);
//        OcaraConfiguration.findAll().setMenuGlobalXmlId(R.menu.global_orange);

        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("clear", false))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask();
            } else {
                finish();
            }
    }

    @AfterViews
    void SetUpAuthor() {
        SharedPreferences sharedPreferences = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if ((lastname != null) && (firstname != null)) {
            editor.putString(LAST_NAME_PREFS_KEY, lastname);
            editor.putString(FIRST_NAME_PREFS_KEY, firstname);
            editor.commit();
        } else {
            String lastnamePref = sharedPreferences.getString(LAST_NAME_PREFS_KEY, null);
            String firstnamePref = sharedPreferences.getString(FIRST_NAME_PREFS_KEY, null);

            if (lastnamePref != null) lastname = lastnamePref;
            if (firstnamePref != null) firstname = firstnamePref;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {

        if (intent.hasExtra(DISCONNECT_EXTRA_KEY)) {
            disconnectApp();
            return;
        }

        super.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
        disconnectApp();
    }

    private void disconnectApp() {
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.orangebusiness.portailsmtk");
        if (intent != null) {
            intent.putExtra(FROM_APP_EXTRA_KEY, true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            intent.putExtra(DISCONNECT_EXTRA_KEY, true);
            startActivity(intent);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }

}