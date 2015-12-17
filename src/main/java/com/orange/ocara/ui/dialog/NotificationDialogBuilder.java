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