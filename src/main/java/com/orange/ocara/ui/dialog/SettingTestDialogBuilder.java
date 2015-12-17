/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import lombok.Getter;
import timber.log.Timber;


public class SettingTestDialogBuilder extends OcaraDialogBuilder {

    @Getter
    Boolean userAuditObjectNowSetting;
    RadioGroup settingTestRadioGroup;

    public SettingTestDialogBuilder(Context context, final Boolean userAuditObjectNowPreference) {
        super(context);


        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View settingTestView = inflater.inflate(com.orange.ocara.R.layout.dialog_setting_test, null);
        setView(settingTestView);

        settingTestRadioGroup = (RadioGroup) settingTestView.findViewById(com.orange.ocara.R.id.setting_test);

        if (userAuditObjectNowPreference == null) {
            settingTestRadioGroup.check(com.orange.ocara.R.id.setting_test_ask_every_time);
        } else if (userAuditObjectNowPreference == true) {
            settingTestRadioGroup.check(com.orange.ocara.R.id.setting_test_now);
        } else {
            settingTestRadioGroup.check(com.orange.ocara.R.id.setting_test_later);
        }

        settingTestRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == com.orange.ocara.R.id.setting_test_ask_every_time) {
                    userAuditObjectNowSetting = null;
                    Timber.v("setting_test_ask_every_time");
                } else if (checkedId == com.orange.ocara.R.id.setting_test_later){
                    userAuditObjectNowSetting = false;
                    Timber.v("setting_test_later");
                } else if (checkedId == com.orange.ocara.R.id.setting_test_now) {
                    Timber.v("setting_test_now");
                    userAuditObjectNowSetting = true;

                }

            }
        });
    }

}