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

package com.orange.ocara.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.activeandroid.Model;
import com.orange.ocara.R;
import com.orange.ocara.data.cache.db.ModelManager;
import com.orange.ocara.data.cache.db.ModelManagerImpl;
import com.orange.ocara.data.cache.model.CommentEntity;
import com.orange.ocara.tools.FileUtils;
import com.orange.ocara.tools.StringUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.view.View.GONE;

@EActivity
public abstract class EditCommentActivity extends BaseActivity {

    @Bean(ModelManagerImpl.class)
    ModelManager modelManager;

    @ViewById(R.id.comment)
    EditText commentContent;

    @Extra("commentId")
    Long commentId;
    @Extra("commentType")
    CommentEntity.Type commentType;
    @Extra("attachmentDirectory")
    String attachmentDirectory;
    @Extra
    String imageName;
    @Extra("name")
    String name;
    @Extra("auditId")
    Long auditId;
    @Extra("title")
    String title;
    @Extra("subTitle")
    String subTitle;

    CommentEntity comment;

    @ViewById(R.id.title)
    TextView customTitle;
    @ViewById(R.id.subtitle)
    TextView customSubtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isNewComment()) {
            this.comment = new CommentEntity(commentType, "");
        } else {
            this.comment = Model.load(CommentEntity.class, commentId);
        }
    }

    @Override
    void setUpToolbar() {
        super.setUpToolbar();

        final View responseButtonBar = LayoutInflater.from(this).inflate(R.layout.audit_object_toolbar, null);
        responseButtonBar.findViewById(R.id.response_ok_button).setVisibility(View.GONE);
        responseButtonBar.findViewById(R.id.response_no_answer_button).setVisibility(GONE);
        responseButtonBar.findViewById(R.id.response_nok_button).setVisibility(View.GONE);

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
        updateLogo(imageName);

    }

    @AfterViews
    void setUpComment() {

        if (StringUtils.isNotBlank(title) && StringUtils.isNotBlank(imageName)) {
            updateActionBar();
        } else {
            if (auditId != null) {
                loadAuditBackground();
            }
        }

        commentContent.setText(comment.getContent());
        commentContent.requestFocus();

        if (isNewComment()) {
            createAttachment();
        } else {
            handleAttachment();
        }
    }

    @Background
    void loadAuditBackground() {
        initTitle(modelManager.getAudit(auditId).getName());
    }

    @UiThread
    void initTitle(String auditName) {
        String titleAudit = getString(com.orange.ocara.R.string.edit_comment_title, auditName);
        setTitle(titleAudit);
    }

    @Click(R.id.validate_comment)
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
