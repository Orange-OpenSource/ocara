/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2020 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.ui.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.preference.ListPreference;

import com.orange.ocara.R;
import com.orange.ocara.ui.dialog.OcaraDialogBuilder;

import java.lang.reflect.Method;

public class OcaraListPreference extends ListPreference {

    private Context mContext;
    private AppCompatDialog mDialog;


    public OcaraListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }


    public AppCompatDialog getDialog() {
        return mDialog;
    }


    protected void showDialog(Bundle state) {
        if (getEntries() == null || getEntryValues() == null) {
            return;
        }

        final int preselect = findIndexOfValue(getValue());
        AlertDialog.Builder builder = new OcaraDialogBuilder(mContext)
                .setTitle(getDialogTitle())
                .setIcon(getDialogIcon())
                .setNegativeButton(R.string.setting_test_button_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String value = getEntryValues()[preselect].toString();
                                setValue(value);
                            }
                        }
                )
                .setPositiveButton(R.string.setting_test_button_validate, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                )
                .setSingleChoiceItems(getEntries(), preselect, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which >= 0 && getEntryValues() != null) {
                            String value = getEntryValues()[which].toString();
                            if (callChangeListener(value) && isPersistent())
                                setValue(value);
                        }
                    }

                });


        androidx.preference.PreferenceManager pm = getPreferenceManager();
        try {
            Method method = pm.getClass().getDeclaredMethod(
                    "registerOnActivityDestroyListener",
                    PreferenceManager.OnActivityDestroyListener.class);
            method.setAccessible(true);
            method.invoke(pm, this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mDialog = builder.create();
        if (state != null) {
            mDialog.onRestoreInstanceState(state);
        }
        mDialog.show();
    }


}

