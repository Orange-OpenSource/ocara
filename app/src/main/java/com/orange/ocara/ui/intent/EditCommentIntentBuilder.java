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

package com.orange.ocara.ui.intent;

import android.content.Context;

import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.cache.model.CommentEntity;
import com.orange.ocara.ui.activity.EditCommentAudioActivity_;
import com.orange.ocara.ui.activity.EditCommentFileActivity_;
import com.orange.ocara.ui.activity.EditCommentPhotoActivity_;
import com.orange.ocara.ui.activity.EditCommentTextActivity_;

import org.androidannotations.api.builder.ActivityIntentBuilder;

/**
 * Builder for the display of an {@link android.content.Intent} that helps on editing a {@link CommentEntity}
 */
public class EditCommentIntentBuilder {

    /**
     * a context
     */
    private final Context context;

    /**
     * a {@link CommentEntity.Type}
     */
    private final CommentEntity.Type commentType;

    /**
     * a location for the file linked to the comment
     */
    private final String attachmentDirectory;

    /**
     * an identifier for a {@link CommentEntity}
     */
    private Long commentId = null;

    /**
     * an identifier for an {@link AuditEntity}
     */
    private Long auditId = null;

    /**
     * a {@link String}
     */
    private String title = null;

    /**
     * a {@link String}
     */
    private String subtitle = null;

    /**
     * Instantiates
     *
     * @param context a {@link Context}
     * @param type a {@link CommentEntity.Type}
     * @param directory a location for files
     */
    public EditCommentIntentBuilder(Context context, CommentEntity.Type type, String directory) {
        this.context = context;
        this.commentType = type;
        this.attachmentDirectory = directory;
    }

    /**
     *
     * @param commentId a {@link Long}
     * @return an instance of {@link EditCommentIntentBuilder}
     */
    public EditCommentIntentBuilder commentId(Long commentId) {
        this.commentId = commentId;
        return this;
    }

    /**
     *
     * @param auditId a {@link Long}
     * @return an instance of {@link EditCommentIntentBuilder}
     */
    public EditCommentIntentBuilder auditId(Long auditId) {
        this.auditId = auditId;
        return this;
    }

    /**
     *
     * @param title a {@link String} that replaces the default title of the view
     * @return an instance of {@link EditCommentIntentBuilder}
     */
    public EditCommentIntentBuilder title(String title) {
        this.title = title;
        return this;
    }

    /**
     *
     * @param subtitle a {@link String} that replaces the default subtitle of the view
     * @return an instance of {@link EditCommentIntentBuilder}
     */
    public EditCommentIntentBuilder subtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    /**
     * Builds and displays the {@link android.content.Intent}
     *
     * @param resultCode an {@link Integer}
     */
    public void startIntent(int resultCode) {

        ActivityIntentBuilder builder;

        switch (commentType) {
            case TEXT:
                EditCommentTextActivity_.IntentBuilder_ txtTmpBuilder = EditCommentTextActivity_
                        .intent(context)
                        .commentId(commentId)
                        .commentType(CommentEntity.Type.TEXT)
                        .attachmentDirectory(attachmentDirectory)
                        .auditId(auditId);

                if (title != null) {
                    txtTmpBuilder.title(title);
                }

                if (subtitle != null) {
                    txtTmpBuilder.subTitle(subtitle);
                }

                builder = txtTmpBuilder;
                break;
            case PHOTO:
                EditCommentPhotoActivity_.IntentBuilder_ photoTmpBuilder = EditCommentPhotoActivity_
                        .intent(context)
                        .commentId(commentId)
                        .commentType(CommentEntity.Type.PHOTO)
                        .attachmentDirectory(attachmentDirectory)
                        .auditId(auditId)
                        .title(title)
                        .subTitle(subtitle);

                if (title != null) {
                    photoTmpBuilder.title(title);
                }

                if (subtitle != null) {
                    photoTmpBuilder.subTitle(subtitle);
                }

                builder = photoTmpBuilder;
                break;
            case AUDIO:
                EditCommentAudioActivity_.IntentBuilder_ audioTmpBuilder = EditCommentAudioActivity_
                        .intent(context)
                        .commentId(commentId)
                        .commentType(CommentEntity.Type.AUDIO)
                        .attachmentDirectory(attachmentDirectory)
                        .auditId(auditId)
                        .title(title)
                        .subTitle(subtitle);

                if (title != null) {
                    audioTmpBuilder.title(title);
                }

                if (subtitle != null) {
                    audioTmpBuilder.subTitle(subtitle);
                }

                builder = audioTmpBuilder;
                break;
            case FILE:
                EditCommentFileActivity_.IntentBuilder_ fileTmpBuilder = EditCommentFileActivity_
                        .intent(context)
                        .commentId(commentId)
                        .commentType(CommentEntity.Type.FILE)
                        .attachmentDirectory(attachmentDirectory)
                        .auditId(auditId)
                        .title(title)
                        .subTitle(subtitle);

                if (title != null) {
                    fileTmpBuilder.title(title);
                }

                if (subtitle != null) {
                    fileTmpBuilder.subTitle(subtitle);
                }

                builder = fileTmpBuilder;
                break;
            default:
                throw new RuntimeException("Error to be defined");
        }

        if (builder != null) {
            builder.startForResult(resultCode);
        }
    }
}
