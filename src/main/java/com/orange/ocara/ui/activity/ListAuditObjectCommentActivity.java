/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.activity;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.orange.ocara.model.AuditObject;
import com.orange.ocara.ui.dialog.OcaraDialogBuilder;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

@EActivity(resName="activity_list_comment")
public class ListAuditObjectCommentActivity extends ListCommentActivity<AuditObject> {

    @Extra("auditObjectId")
    Long auditObjectId;

    @Inject
    Picasso picasso;


    @ViewById(resName="title")
    TextView customTitle;
    @ViewById(resName="subtitle")
    TextView customSubtitle;

    String title;
    String subTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.entity = modelManager.getAuditObject(auditObjectId);
    }


    @Override
    void setUpToolbar() {
        super.setUpToolbar();

        final View responseButtonBar = LayoutInflater.from(this).inflate(com.orange.ocara.R.layout.audit_object_toolbar, null);
        responseButtonBar.findViewById(com.orange.ocara.R.id.response_ok_button).setVisibility(View.GONE);

        responseButtonBar.findViewById(com.orange.ocara.R.id.response_nok_button).setVisibility(View.GONE);

        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Gravity.LEFT);
        responseButtonBar.setLayoutParams(lp);

        toolbar.addView(responseButtonBar);
    }

    @Override
    public void setTitle(int id) {
        setTitle(getString(id));
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle("");
        customTitle.setText(title);
    }

    void setSubtitle(String subtitle) {
        if (!TextUtils.isEmpty(subtitle)) {
            customSubtitle.setVisibility(View.VISIBLE);
            customSubtitle.setText(subtitle);
        }
    }



    private String getAuditObjectCharacteristicsTitle(AuditObject auditObject) {
        String result = "";
        for(AuditObject characteristics : auditObject.getChildren()) {
            if (!result.isEmpty()) {
                result += " ";
            }
            result += "+"+"\u00A0" + characteristics.getName();
        }
        return result;
    }


    private void updateActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayShowTitleEnabled(false);
        supportActionBar.setDisplayUseLogoEnabled(true);
        updateTitle();
        updateLogo(Uri.parse(entity.getObjectDescription().getIcon().toString()));


    }

    private void updateTitle() {
        title = modelManager.getAuditObject(auditObjectId).getName();
        subTitle = getAuditObjectCharacteristicsTitle(entity);
        setTitle(title);
        setSubtitle(subTitle);
    }


    @AfterViews
    void setUpView() {
        updateActionBar();
    }


    @OptionsItem(resName="delete_all_comments")
    void onDeleteAllComments() {
        AlertDialog confirmDialog = new OcaraDialogBuilder(ListAuditObjectCommentActivity.this)
                .setTitle(com.orange.ocara.R.string.comment_list_delete_all_comment_message_title) // title
                .setMessage(getString(com.orange.ocara.R.string.comment_list_delete_all_comment_object_message)) // message
                .setPositiveButton(com.orange.ocara.R.string.action_remove, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAllComments(entity.getComments());                    }
                })
                .setNegativeButton(com.orange.ocara.R.string.action_cancel, null)
                .create();
        confirmDialog.show();
    }


    @Override
    protected EditCommentTextActivity_.IntentBuilder_ createEditCommentTextActivity(Long commentId) {
        return super.createEditCommentTextActivity(commentId)
                .title(title)
                .subTitle(subTitle)
                .imageUri(entity.getObjectDescription().getIcon().toString());
    }

    @Override
    protected EditCommentAudioActivity_.IntentBuilder_ createEditCommentAudioActivity(Long commentId) {
        return super.createEditCommentAudioActivity(commentId)
                .title(title)
                .subTitle(subTitle)
                .imageUri(entity.getObjectDescription().getIcon().toString());
    }

    @Override
    protected EditCommentPhotoActivity_.IntentBuilder_ createEditCommentPhotoActivity(Long commentId) {
        return super.createEditCommentPhotoActivity(commentId)
                .title(title)
                .subTitle(subTitle)
                .imageUri(entity.getObjectDescription().getIcon().toString());
    }
}
