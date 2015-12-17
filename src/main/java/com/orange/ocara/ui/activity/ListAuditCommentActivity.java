/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.orange.ocara.model.Audit;
import com.orange.ocara.ui.dialog.OcaraDialogBuilder;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;

@EActivity(resName="activity_list_comment")
public class ListAuditCommentActivity extends ListCommentActivity<Audit> {

    @Extra("auditId")
    Long auditId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.entity = modelManager.getAudit(auditId);
        String auditName = this.entity.getName();
        String title = getString(com.orange.ocara.R.string.list_comment_title,auditName);

        setTitle(title);

    }

    @OptionsItem(resName="delete_all_comments")
    void onDeleteAllComments() {
        AlertDialog confirmDialog = new OcaraDialogBuilder(ListAuditCommentActivity.this)
                .setTitle(com.orange.ocara.R.string.comment_list_delete_all_comment_message_title) // title
                .setMessage(getString(com.orange.ocara.R.string.comment_list_delete_all_comment_audit_message)) // message
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
                .auditId(auditId);
    }

    @Override
    protected EditCommentAudioActivity_.IntentBuilder_ createEditCommentAudioActivity(Long commentId) {
        return super.createEditCommentAudioActivity(commentId)
                .auditId(auditId);
    }

    @Override
    protected EditCommentPhotoActivity_.IntentBuilder_ createEditCommentPhotoActivity(Long commentId) {
        return super.createEditCommentPhotoActivity(commentId)
                .auditId(auditId);
    }
}
