/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class OcaraDialogBuilder extends AlertDialog.Builder {



    private  View notificationView;
    private View titleHeaderView;
    private AlertDialog alertDialog;

    public OcaraDialogBuilder(Context context) {
        super(context, com.orange.ocara.R.style.AlertDialog);
        customize(context);
        customizeMessage(context);

    }


    protected void customizeMessage(Context context) {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    notificationView = inflater.inflate(com.orange.ocara.R.layout.dialog_notification_message, null);
    setView(notificationView);
    }

    @NonNull
    @Override
    public AlertDialog.Builder setMessage(CharSequence message) {
        TextView infoView = (TextView) notificationView.findViewById(com.orange.ocara.R.id.dialog_notification_message);
        infoView.setText(message);
        return this;
    }

    @NonNull
    @Override
    public AlertDialog.Builder setMessage(int messageId) {
        TextView infoView = (TextView) notificationView.findViewById(com.orange.ocara.R.id.dialog_notification_message);
        infoView.setText(messageId);
        return this;
    }


    protected void customize(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        titleHeaderView = inflater.inflate(com.orange.ocara.R.layout.dialog_title, null);

        ImageView backImage = (ImageView) titleHeaderView.findViewById(com.orange.ocara.R.id.alert_dialog_back);
        backImage.setOnClickListener( new OnBackImageClickListener() );


        setCustomTitle(titleHeaderView);
    }


    @NonNull
    @Override
    public AlertDialog create() {
        alertDialog = super.create();
        return alertDialog;
    }



    @NonNull
    @Override
    public AlertDialog.Builder setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
        ImageView backImage = (ImageView) titleHeaderView.findViewById(com.orange.ocara.R.id.alert_dialog_back);
        backImage.setEnabled(cancelable );
        if (cancelable) {
            // non modal  ==> show back image
            backImage.setVisibility(View.VISIBLE);
        } else {
            // modal ==> hide back image
            backImage.setVisibility(View.GONE);
        }
        return this;
    }

    @NonNull
    @Override
    public AlertDialog.Builder setTitle(CharSequence title) {
        TextView tt = (TextView) titleHeaderView.findViewById(com.orange.ocara.R.id.alert_dialog_title);
        tt.setText(title);
        return this;
    }

    @NonNull
    @Override
    public AlertDialog.Builder setTitle(int titleId) {
        TextView tt = (TextView) titleHeaderView.findViewById(com.orange.ocara.R.id.alert_dialog_title);
        tt.setText(titleId);
        return this;
    }

    public class OnBackImageClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
        }
    }


}