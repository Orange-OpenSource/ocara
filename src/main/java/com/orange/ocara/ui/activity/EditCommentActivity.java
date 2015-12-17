/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.activeandroid.Model;
import com.orange.ocara.model.Comment;
import com.orange.ocara.model.ModelManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

@EActivity
public abstract class EditCommentActivity extends BaseActivity {



    @Inject
    ModelManager modelManager;



    @ViewById(resName="comment")
    EditText commentContent;

    @Extra("commentId")
    Long commentId;
    @Extra("commentType")
    Comment.Type commentType;
    @Extra("attachmentDirectory")
    String attachmentDirectory;
    @Extra("imageUri")
    String imageUri;
    @Extra("name")
    String name;
    @Extra("auditId")
    Long auditId;
    @Extra("title")
    String title;
    @Extra("subTitle")
    String subTitle;

    Comment comment;

    @ViewById(resName="title")
    TextView customTitle;
    @ViewById(resName="subtitle")
    TextView customSubtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isNewComment()) {
            this.comment = new Comment(commentType, "");
        } else {
            this.comment = Model.load(Comment.class, commentId);
        }

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
    public void setTitle(CharSequence title) {
        super.setTitle("");
        customTitle.setText(title);
    }

    void setSubtitle(String subTitle) {
        if (!TextUtils.isEmpty(subTitle)) {
            customSubtitle.setVisibility(View.VISIBLE);
            customSubtitle.setText(subTitle);
        }
    }


    private void updateActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayShowTitleEnabled(false);
        supportActionBar.setDisplayUseLogoEnabled(true);
        setTitle(title);
        setSubtitle(subTitle);
        updateLogo(Uri.parse(imageUri));

    }

    @AfterViews
    void setUpComment() {

        if (StringUtils.isNoneBlank(title, imageUri) ) {
           updateActionBar();
        } else {
            String auditName = modelManager.getAudit(auditId).getName();
            String titleAudit = getString(com.orange.ocara.R.string.edit_comment_title,auditName);
            setTitle(titleAudit);
        }

        commentContent.setText(comment.getContent());
        commentContent.requestFocus();

        if (isNewComment()) {
            createAttachment();
        } else {
            handleAttachment();
        }
    }

    @Click(resName="validate_comment")
    void onValidateComment() {
        comment.setContent(commentContent.getText().toString());
        comment.save();

        Intent intent = new Intent();
        intent.putExtra("commentId", comment.getId());

        setResult(RESULT_OK, intent);
        finish();
    }

    protected abstract void createAttachment();

    protected abstract void handleAttachment();

    protected String makeTimestampedFilename(String prefix) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        return prefix + "_" + timeStamp + "_";
    }


    protected String copyAsAttachment(File tempAttachment) throws IOException {
        return copyAsAttachment(tempAttachment, null);
    }

    protected String copyAsAttachment(File tempAttachment, String attachmentName) throws IOException {
        if (StringUtils.isBlank(attachmentName)) {
            attachmentName = tempAttachment.getName();
        }
        File targetAttachment = new File(attachmentDirectory, attachmentName);

        FileUtils.copyFile(tempAttachment, targetAttachment);
        return targetAttachment.getAbsolutePath();
    }

    /**
     * @return true if we are creating a new comment, false in case of editing an existing one.
     */
    protected boolean isNewComment() {
        return commentId == null;
    }

}
