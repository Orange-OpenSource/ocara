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

package com.orange.ocara.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class NotificationDialogBuilder extends OcaraDialogBuilder {


    protected final View notificationView;
    protected CheckBox optionCheckBox;

    public NotificationDialogBuilder(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        notificationView = inflater.inflate(com.orange.ocara.R.layout.dialog_notification, null);
        setView(notificationView);

        optionCheckBox = (CheckBox) notificationView.findViewById(com.orange.ocara.R.id.dialog_notification_option);
    }

    public NotificationDialogBuilder setInfo(String info) {
        TextView infoView = (TextView) notificationView.findViewById(com.orange.ocara.R.id.dialog_notification_info);
        infoView.setText(info);
        return this;
    }

    public NotificationDialogBuilder setInfo(CharSequence info) {
        TextView infoView = (TextView) notificationView.findViewById(com.orange.ocara.R.id.dialog_notification_info);
        infoView.setText(info);
        return this;
    }

    public NotificationDialogBuilder setOption(String option) {
        optionCheckBox.setVisibility(View.VISIBLE);
        optionCheckBox.setText(option);
        return this;
    }

    public boolean getOptionValue() {
        return optionCheckBox.isChecked();
    }


}